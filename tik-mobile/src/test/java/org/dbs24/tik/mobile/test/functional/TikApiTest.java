package org.dbs24.tik.mobile.test.functional;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.deser.std.MapDeserializer;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.log4j.Log4j2;
import org.dbs24.stmt.StmtProcessor;
import org.dbs24.test.core.AbstractWebTest;
import org.dbs24.tik.mobile.TikMobile;
import org.dbs24.tik.mobile.config.TikMobileConfig;
import org.dbs24.tik.mobile.entity.dto.tiktok.viewer.SearchInfoBySecUserIdDto;
import org.dbs24.tik.mobile.entity.dto.tiktok.viewer.SearchSpecificPostDto;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;

import java.util.LinkedHashMap;
import java.util.Map;

import static org.dbs24.consts.SysConst.ALL_PACKAGES;

@Log4j2
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = {TikMobile.class})
@Import({TikMobileConfig.class})
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@EntityScan(basePackages = {ALL_PACKAGES})
@TestPropertySource(properties = "config.security.profile.webfilter.chain=development")
public class TikApiTest extends AbstractWebTest {

    public WebClient getWebClient() {
        return WebClient.builder()
                .clientConnector(new ReactorClientHttpConnector(HttpClient.newConnection().compress(true)))
                .baseUrl("http://195.2.80.143:5001")
                .exchangeStrategies(ExchangeStrategies.builder()
                        .codecs(configurer -> configurer
                                .defaultCodecs()
                                .maxInMemorySize(16 * 1024 * 1024))
                        .build())
                .build();
    }

    @RepeatedTest(1)
    @Order(100)
    public void postBuildRequestTest() {

        runTest(() -> {

            final Mono<SearchSpecificPostDto> requestMono = Mono.just(StmtProcessor.create(SearchSpecificPostDto.class, specificPost -> {
                specificPost.setWebLink("https://www.tiktok.com/@russiaeveryday/video/7024085153739803906?is_copy_url=1&is_from_webapp=v1");
            }));


            final BuildRequest responseBody = getWebClient()
                    .post()
                    .uri(uriBuilder
                            -> uriBuilder
                            .path("api/post_build_request")
                            .build())
                    .body(requestMono, SearchSpecificPostDto.class)
                    .retrieve()
                    .bodyToMono(BuildRequest.class)
                    .block();

            log.info("RESPONSE BODY: {}", responseBody);

        });
    }

    @RepeatedTest(1)
    @Order(200)
    public void postsBySidBuildRequestTest(){
        runTest(() -> {

            final Mono<SearchInfoBySecUserIdDto> requestMono = Mono.just(StmtProcessor.create(SearchInfoBySecUserIdDto.class, searchInfo -> {
                searchInfo.setSecUserId("");
                searchInfo.setPostsQuantity(5);
            }));


            final BuildRequest responseBody = getWebClient()
                    .post()
                    .uri(uriBuilder
                            -> uriBuilder
                            .path("api/posts_by_sid_build_request")
                            .build())
                    .body(requestMono, SearchSpecificPostDto.class)
                    .accept(MediaType.APPLICATION_JSON)
                    .retrieve()
                    .bodyToMono(BuildRequest.class)
                    .block();

            log.info("RESPONSE BODY: {}", responseBody);

        });
    }

    @RepeatedTest(1)
    @Order(300)
    public void searchBySidBuildRequest(){
        runTest(() -> {

            final Mono<SearchInfoBySecUserIdDto> requestMono = Mono.just(StmtProcessor.create(SearchInfoBySecUserIdDto.class, searchInfo -> {
                searchInfo.setSecUserId("");
                searchInfo.setPostsQuantity(5);
            }));


            final BuildRequest responseBody = getWebClient()
                    .post()
                    .uri(uriBuilder
                            -> uriBuilder
                            .path("api/search_by_sid_build_request")
                            .build())
                    .body(requestMono, SearchSpecificPostDto.class)
                    .accept(MediaType.APPLICATION_JSON)
                    .retrieve()
                    .bodyToMono(BuildRequest.class)
                    .block();

            log.info("RESPONSE BODY: {}", responseBody);

        });
    }

}

@Data
class BuildRequest {
    private RequestInfo request;
}

@Data
class RequestInfo {
    private String method;
    private String url;
    @JsonDeserialize(using = MapDeserializer.class)
    private Map<String, String> headers;
    private String body;
}