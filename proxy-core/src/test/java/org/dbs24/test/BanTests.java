package org.dbs24.test;

import lombok.extern.log4j.Log4j2;
import org.dbs24.proxy.core.ProxyCoreApplication;
import org.dbs24.proxy.core.config.ProxyCoreConfig;
import org.dbs24.proxy.core.config.ProxyCoreRestConfig;
import org.dbs24.proxy.core.entity.dto.ProxyUsageBanDto;
import org.dbs24.proxy.core.entity.dto.request.CreateProxyUsageBanRequest;
import org.dbs24.proxy.core.entity.dto.response.CreateProxyUsageBanResponse;
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
public class BanTests extends AbstractProxyTest {

    @Order(100)
    @Test
    //@RepeatedTest(5)
    public void createBan() {

        log.info("START");
        runTest(() -> {

            final Mono<CreateProxyUsageBanRequest> mono = Mono.just(StmtProcessor.create(CreateProxyUsageBanRequest.class, createProxyUsageBanRequest -> {

                createProxyUsageBanRequest.setEntityInfo(StmtProcessor.create(ProxyUsageBanDto.class, proxyUsageErrorDto -> {
                    proxyUsageErrorDto.setProxyUsageId(6);
                    proxyUsageErrorDto.setLog("BAN");
                    proxyUsageErrorDto.setApplicationNetworkName("instagram");
                    proxyUsageErrorDto.setBanTimeMillis(1234500);
                }));

                createProxyUsageBanRequest.setEntityAction(StmtProcessor.create(SimpleActionInfo.class,
                        sai -> sai.setActionCode(CREATE_ENTITY)
                ));
            }));

            CreateProxyUsageBanResponse createProxyUsageError = webTestClient
                    .post()
                    .uri(uriBuilder
                            -> uriBuilder
                            .path(ProxyCoreRestConfig.URI_CREATE_USAGE_BAN)
                            .build())
                    .accept(APPLICATION_JSON)
                    .body(mono, CreateProxyUsageBanRequest.class)
                    .exchange()
                    .expectStatus()
                    .isOk()
                    .expectBody(CreateProxyUsageBanResponse.class)
                    .returnResult()
                    .getResponseBody();

            log.info("GOT MESSAGE: {}", createProxyUsageError.getMessage());
            log.info("FINISHING TESTS");
        });
    }
}