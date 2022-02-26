package org.dbs24.test;

import lombok.extern.log4j.Log4j2;
import org.dbs24.proxy.core.ProxyCoreApplication;
import org.dbs24.proxy.core.config.ProxyCoreConfig;
import org.dbs24.proxy.core.config.ProxyCoreRestConfig;
import org.dbs24.proxy.core.entity.dto.ProxyUsageErrorDto;
import org.dbs24.proxy.core.entity.dto.request.CreateProxyUsageErrorRequest;
import org.dbs24.proxy.core.entity.dto.response.CreateProxyUsageErrorResponse;
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
import static org.dbs24.rest.api.action.RestAction.CREATE_ENTITY;
import static org.springframework.http.MediaType.APPLICATION_JSON;

@Log4j2
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = {ProxyCoreApplication.class})
@Import({ProxyCoreConfig.class})
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@EntityScan(basePackages = {ALL_PACKAGES})
public class ErrorTests extends AbstractProxyTest {

    @Order(100)
    @Test
    //@RepeatedTest(5)
    public void createError() {

        log.info("START");
        runTest(() -> {

            final Mono<CreateProxyUsageErrorRequest> mono = Mono.just(StmtProcessor.create(CreateProxyUsageErrorRequest.class, createProxyUsageErrorRequest -> {

                createProxyUsageErrorRequest.setEntityInfo(StmtProcessor.create(ProxyUsageErrorDto.class, proxyUsageErrorDto -> {
                    proxyUsageErrorDto.setProxyUsageId(5);
                    proxyUsageErrorDto.setErrorTypeName("undefined");
                    proxyUsageErrorDto.setLog("some stacktrace.... bla bla bla....");
                }));

                createProxyUsageErrorRequest.setEntityAction(StmtProcessor.create(SimpleActionInfo.class,
                        sai -> sai.setActionCode(CREATE_ENTITY)
                ));
            }));

            CreateProxyUsageErrorResponse createProxyUsageError = webTestClient
                    .post()
                    .uri(uriBuilder
                            -> uriBuilder
                            .path(ProxyCoreRestConfig.URI_CREATE_USAGE_ERROR)
                            .build())
                    .accept(APPLICATION_JSON)
                    .body(mono, CreateProxyUsageErrorRequest.class)
                    .exchange()
                    .expectStatus()
                    .isOk()
                    .expectBody(CreateProxyUsageErrorResponse.class)
                    .returnResult()
                    .getResponseBody();

            log.info("GOT MESSAGE: {}", createProxyUsageError.getMessage());
            log.info("FINISHING TESTS");
        });
    }
}