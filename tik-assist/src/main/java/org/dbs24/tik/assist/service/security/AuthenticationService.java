package org.dbs24.tik.assist.service.security;

import lombok.extern.log4j.Log4j2;
import org.dbs24.component.JwtSecurityService;
import org.dbs24.stmt.StmtProcessor;
import org.dbs24.tik.assist.config.TikAssistRestConfig;
import org.dbs24.tik.assist.entity.domain.User;
import org.dbs24.tik.assist.entity.dto.oauth2.GoogleTikAssistUser;
import org.dbs24.tik.assist.entity.dto.oauth2.GoogleVerificationResponse;
import org.dbs24.tik.assist.entity.dto.user.GoogleLoginUserDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.server.ServerRequest;
import reactor.core.publisher.Mono;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.http.MediaType.APPLICATION_JSON;

@Log4j2
@Service
public class AuthenticationService {

    private final Map<Integer, String> userIdToToken;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    private final JwtSecurityService jwtUtils;

    @Value("${config.security.jwt.expiration}")
    private Long jwtLifeTime;

    private WebClient authServerWebClient;

    @Value("${config.auth.auth-server-uri}")
    private String authServerUri;
    @Value("${config.auth.oauth2.google-api}")
    private String googleApi;
    @Value("${config.auth.oauth2.facebook-api}")
    private String facebookApi;

    public AuthenticationService(BCryptPasswordEncoder bCryptPasswordEncoder, JwtSecurityService jwtUtils) {

        this.userIdToToken = new HashMap<>();

        this.jwtUtils = jwtUtils;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @PostConstruct
    public void webClientInit() {

        authServerWebClient = WebClient.builder()
                .baseUrl(authServerUri)
                .exchangeStrategies(ExchangeStrategies.builder()
                        .codecs(configurer -> configurer
                                .defaultCodecs()
                                .maxInMemorySize(16 * 1024 * 1024))
                        .build())
                .build();
    }

    public String encodePassword(String rawPassword) {

        return bCryptPasswordEncoder.encode(rawPassword);
    }

    public boolean isPasswordMatch(String rawPassword, String encodedPassword) {

        return bCryptPasswordEncoder.matches(rawPassword, encodedPassword);
    }

    public String generateJwtDefault(User user) {

        return generateJwt(user);
    }

    public Mono<GoogleVerificationResponse> generateJwtViaGoogle(GoogleLoginUserDto googleLoginUserDto) {

        GoogleVerificationResponse response = authServerWebClient
                .post()
                .uri(uriBuilder -> uriBuilder
                        .path(googleApi)
                        .build())
                .contentType(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .body(
                        Mono.just(
                                StmtProcessor.create(
                                        GoogleTikAssistUser.class,
                                        googleRequest -> googleRequest.setAccessToken(googleLoginUserDto.getAccessToken())
                                )
                        ),
                        GoogleTikAssistUser.class
                )
                .retrieve()
                .bodyToMono(GoogleVerificationResponse.class).toProcessor().block();

        log.info("auth server response {}", response);

        return Mono.just(response);
    }

    private String generateJwt(User user) {

        String subject = String.valueOf(user.getUserId());

        Map<String, String> claims = new HashMap<>();

        claims.put("email", user.getEmail());
        claims.put("google_user_id", String.valueOf(user.getGoogleUserId()));
        claims.put("facebook_user_id", String.valueOf(user.getFacebookUserId()));

        String token = jwtUtils.generateToken(subject, claims, jwtLifeTime);

        addTokenToPool(user.getUserId(), token);

        return token;
    }

    public String extractJwtFromServerRequest(ServerRequest request) {

        return request.headers().firstHeader(TikAssistRestConfig.AUTHORIZATION_HEADER_NAME/*HttpHeaders.AUTHORIZATION*/);
    }

    public Integer extractUserIdFromServerRequest(ServerRequest request) {

        return extractUserIdFromJwt(
                extractJwtFromServerRequest(request)
        );
    }

    public Integer extractUserIdFromJwt(String jwt) {

        return Integer.valueOf(jwtUtils.getUsernameFromToken(jwt));
    }

    public Integer removeToken(ServerRequest request) {

        Integer userId = extractUserIdFromServerRequest(request);

        removeTokenFromList(userId);

        return userId;
    }

    public boolean isTokenValid(String token) {

        return jwtUtils.validateToken(token) && userIdToToken.containsValue(token);
    }

    public void addTokenToPool(Integer userId, String token) {

        userIdToToken.put(userId, token);
    }

    public void removeTokenFromList(Integer userId) {

        userIdToToken.remove(userId);
    }
}
