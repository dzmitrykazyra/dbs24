package org.dbs24.auth.server.component;

import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;
import lombok.extern.log4j.Log4j2;
import org.dbs24.auth.server.entity.tik_assist.FacebookTikAssistUser;
import org.dbs24.auth.server.entity.tik_assist.FacebookVerificationResponse;
import org.dbs24.auth.server.entity.tik_assist.GoogleTikAssistUser;
import org.dbs24.auth.server.entity.tik_assist.GoogleVerificationResponse;
import org.dbs24.stmt.StmtProcessor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;

import javax.annotation.PostConstruct;
import javax.net.ssl.SSLException;

import java.util.concurrent.ExecutionException;
import java.util.function.Function;

import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Log4j2
@Component
public class OAuth2Service {

    private WebClient googleOAuth2WebClient;
    private WebClient facebookOAuth2WebClient;

    private final static String QP_FACEBOOK_INPUT_TOKEN = "input_token";
    private final static String QP_FACEBOOK_ACCESS_TOKEN = "access_token";
    private final static String QP_GOOGLE_ACCESS_TOKEN = "access_token";

    @Value("${config.oauth2.google.uri}")
    private String googleOAuth2Uri;
    @Value("${config.oauth2.google.api-verify-token}")
    private String googleOAuth2VerifyToken;

    @Value("${config.oauth2.facebook.uri}")
    private String facebookOAuth2Uri;
    @Value("${config.oauth2.facebook.api-verify-token}")
    private String facebookOAuth2VerifyToken;

    @PostConstruct
    public void initWebClients() throws SSLException {

        final SslContext sslContext = SslContextBuilder
                .forClient()
                .trustManager(InsecureTrustManagerFactory.INSTANCE)
                .build();

        final ReactorClientHttpConnector reactorClientHttpConnector = new ReactorClientHttpConnector(
                HttpClient
                        .create()
                        .secure(contextSpec -> contextSpec.sslContext(sslContext))
        );

        facebookOAuth2WebClient = WebClient.builder()
                .clientConnector(reactorClientHttpConnector)
                .baseUrl(facebookOAuth2Uri)
                .exchangeStrategies(ExchangeStrategies.builder()
                        .codecs(configurer -> configurer
                                .defaultCodecs()
                                .maxInMemorySize(16 * 1024 * 1024))
                        .build())
                .build();

        googleOAuth2WebClient = WebClient.builder()
                .clientConnector(reactorClientHttpConnector)
                .baseUrl(googleOAuth2Uri)
                .exchangeStrategies(ExchangeStrategies.builder()
                        .codecs(configurer -> configurer
                                .defaultCodecs()
                                .maxInMemorySize(16 * 1024 * 1024))
                        .build())
                .build();
    }

    public Mono<GoogleVerificationResponse> getGoogleVerificationResponse(GoogleTikAssistUser info) {

        return googleOAuth2WebClient
                .post()
                .uri(uriBuilder -> uriBuilder
                        .path(googleOAuth2VerifyToken)
                        .queryParam(QP_GOOGLE_ACCESS_TOKEN, info.getAccessToken())
                        .build())
                .header(CONTENT_TYPE, APPLICATION_JSON_VALUE)
                .accept(APPLICATION_JSON)
                .retrieve()
                .bodyToMono(GoogleVerificationResponse.class);
    }

    public Mono<FacebookVerificationResponse> getFacebookVerificationResponse(FacebookTikAssistUser facebookTikAssistUser) {

        return facebookOAuth2WebClient
                .post()
                .uri(uriBuilder -> uriBuilder
                        .path(facebookOAuth2VerifyToken)
                        .queryParam(QP_FACEBOOK_ACCESS_TOKEN, facebookTikAssistUser.getAccessToken())
                        .queryParam(QP_FACEBOOK_INPUT_TOKEN, facebookTikAssistUser.getInputToken())
                        .build())
                .header(CONTENT_TYPE, APPLICATION_JSON_VALUE)
                .accept(APPLICATION_JSON)
                .retrieve()
                .bodyToMono(FacebookVerificationResponse.class);
    }
}
