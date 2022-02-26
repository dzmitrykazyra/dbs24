package org.dbs24.test;

import lombok.Data;
import lombok.extern.log4j.Log4j2;
import org.dbs24.proxy.core.ProxyCoreApplication;
import org.dbs24.proxy.core.config.ProxyCoreConfig;
import org.dbs24.proxy.core.config.ProxyCoreRestConfig;
import org.dbs24.proxy.core.entity.dto.ApplicationNetworkDto;
import org.dbs24.proxy.core.entity.dto.request.CreateApplicationNetworkRequest;
import org.dbs24.proxy.core.entity.dto.response.CreateApplicationNetworkResponse;
import org.dbs24.rest.api.action.SimpleActionInfo;
import org.dbs24.stmt.StmtProcessor;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import reactor.core.publisher.Mono;

import static org.dbs24.consts.SysConst.ALL_PACKAGES;
import static org.dbs24.rest.api.action.RestAction.MODIFY_ENTITY;
import static org.springframework.http.MediaType.APPLICATION_JSON;

@Data
@Log4j2
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = {ProxyCoreApplication.class})
@Import({ProxyCoreConfig.class})
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@EntityScan(basePackages = {ALL_PACKAGES})
public class ApplicationNetworkTests extends AbstractProxyTest {

    @Order(100)
    @Test
    public void createApplicationNetwork() {

        runTest(() -> {

            final Mono<CreateApplicationNetworkRequest> mono = Mono.just(StmtProcessor.create(
                    CreateApplicationNetworkRequest.class,
                    applicationNetworkRequest -> {

                        applicationNetworkRequest.setEntityInfo(StmtProcessor.create(ApplicationNetworkDto.class,
                                applicationNetworkDto -> applicationNetworkDto.setName("tiktok")
                        ));

                        applicationNetworkRequest.setEntityAction(StmtProcessor.create(SimpleActionInfo.class,
                                sai -> sai.setActionCode(MODIFY_ENTITY)
                        ));
            }));

            CreateApplicationNetworkResponse responseBody = webTestClient
                    .post()
                    .uri(uriBuilder
                            -> uriBuilder
                            .path(ProxyCoreRestConfig.URI_CREATE_OR_UPDATE_APPLICATION_NETWORK)
                            .build())
                    .accept(APPLICATION_JSON)
                    .body(mono, CreateApplicationNetworkRequest.class)
                    .exchange()
                    .expectStatus()
                    .isOk()
                    .expectBody(CreateApplicationNetworkResponse.class)
                    .returnResult()
                    .getResponseBody();

            log.info("RESPONSE BODY MESSAGE: {}", responseBody.getMessage());
        });
    }
}
