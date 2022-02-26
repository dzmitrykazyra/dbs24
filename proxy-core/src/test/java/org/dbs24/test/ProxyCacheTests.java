/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.test;

import java.time.LocalDateTime;
import lombok.Data;
import lombok.extern.log4j.Log4j2;
import org.dbs24.application.core.locale.NLS;
import org.dbs24.application.core.service.funcs.TestFuncs;
import static org.dbs24.consts.SysConst.ALL_PACKAGES;
import org.dbs24.proxy.core.ProxyCoreApplication;
import org.dbs24.proxy.core.config.ProxyCoreConfig;
import static org.dbs24.proxy.core.consts.ProxyConsts.Countries.COUNTRIES_IDS;
import static org.dbs24.proxy.core.consts.ProxyConsts.ProxyProviderEnum.PROVIDERS_IDS;
import static org.dbs24.proxy.core.consts.ProxyConsts.ProxyStatusEnum.PROXY_STATUSES_IDS;
import static org.dbs24.proxy.core.consts.ProxyConsts.ProxyTypeEnum.PROXY_TYPES_IDS;
import static org.dbs24.proxy.core.rest.api.consts.ProxyHttpConsts.UriConsts.URI_CREATE_OR_UPDATE_PROXY;
import org.dbs24.proxy.core.component.ProxyService;
import org.dbs24.proxy.core.rest.api.proxy.CreatedProxy;
import org.dbs24.proxy.core.rest.api.proxy.ProxyInfo;
import org.dbs24.stmt.StmtProcessor;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import reactor.core.publisher.Mono;
import org.springframework.beans.factory.annotation.Autowired;

@Data
@Log4j2
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = {ProxyCoreApplication.class})
@Import({ProxyCoreConfig.class})
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@EntityScan(basePackages = {ALL_PACKAGES})
public class ProxyCacheTests extends AbstractProxyTest {

    private Integer createdProxyId;
    final ProxyService proxyService;

    @Autowired
    public ProxyCacheTests(ProxyService proxyService) {
        this.proxyService = proxyService;
    }

    @Order(100)
    @Test
    public void createProxy() {

        runTest(() -> {

            log.info("testing {}", URI_CREATE_OR_UPDATE_PROXY);

            final Mono<ProxyInfo> monoProxy = Mono.just(StmtProcessor.create(ProxyInfo.class, proxyInfo -> {
                proxyInfo.setActualDate(NLS.localDateTime2long(LocalDateTime.now()));
                proxyInfo.setCountryId(TestFuncs.<Integer>selectFromCollection(COUNTRIES_IDS));
                proxyInfo.setDateBegin(NLS.localDateTime2long(LocalDateTime.now()));
                proxyInfo.setLogin(TestFuncs.generateTestString15());
                proxyInfo.setPass(TestFuncs.generateTestString15());
                proxyInfo.setPort(TestFuncs.generateTestRangeInteger(100, 100000));
                proxyInfo.setProxyProviderId(TestFuncs.<Integer>selectFromCollection(PROVIDERS_IDS));
                proxyInfo.setProxyStatusId(TestFuncs.<Integer>selectFromCollection(PROXY_STATUSES_IDS));
                proxyInfo.setProxyTypeId(TestFuncs.<Integer>selectFromCollection(PROXY_TYPES_IDS));
                proxyInfo.setTraffic(TestFuncs.generateTestRangeInteger(100, 100000));
                proxyInfo.setUrl(TestFuncs.generateTestString15());
                proxyInfo.setUrlIpChange(TestFuncs.generateTestString15());
            }));

            final CreatedProxy createdProxy
                    = webTestClient
                            .post()
                            .uri(uriBuilder
                                    -> uriBuilder
                                    .path(URI_CREATE_OR_UPDATE_PROXY)
                                    .build())
                            .accept(APPLICATION_JSON)
                            .body(monoProxy, ProxyInfo.class)
                            .exchange()
                            .expectStatus()
                            .isOk()
                            .expectBody(CreatedProxy.class)
                            .returnResult()
                            .getResponseBody();

            log.info("{}: test result is '{}' ", URI_CREATE_OR_UPDATE_PROXY, createdProxy);

            createdProxyId = createdProxy.getCreatedProxyId();

            log.info("{}: cached proxy '{}' ", URI_CREATE_OR_UPDATE_PROXY, proxyService.findProxy(createdProxyId));
            log.info("{}: cached proxy '{}' ", URI_CREATE_OR_UPDATE_PROXY, proxyService.findProxy(createdProxyId));
            log.info("{}: cached proxy '{}' ", URI_CREATE_OR_UPDATE_PROXY, proxyService.findProxy(createdProxyId));

        });

    }

    @Order(200)
    @Test
    public void updateProxy() {

        runTest(() -> {

            log.info("testing {}", URI_CREATE_OR_UPDATE_PROXY);

            final Mono<ProxyInfo> monoProxy = Mono.just(StmtProcessor.create(ProxyInfo.class, proxyInfo -> {
                proxyInfo.setProxyId(createdProxyId);
                proxyInfo.setActualDate(NLS.localDateTime2long(LocalDateTime.now().plusMinutes(10)));
                proxyInfo.setCountryId(TestFuncs.<Integer>selectFromCollection(COUNTRIES_IDS));
                proxyInfo.setDateBegin(NLS.localDateTime2long(LocalDateTime.now()));
                proxyInfo.setDateEnd(NLS.localDateTime2long(LocalDateTime.now().plusDays(3)));
                proxyInfo.setLogin(TestFuncs.generateTestString15());
                proxyInfo.setPass(TestFuncs.generateTestString15());
                proxyInfo.setPort(TestFuncs.generateTestRangeInteger(100, 100000));
                proxyInfo.setProxyProviderId(TestFuncs.<Integer>selectFromCollection(PROVIDERS_IDS));
                proxyInfo.setProxyStatusId(TestFuncs.<Integer>selectFromCollection(PROXY_STATUSES_IDS));
                proxyInfo.setProxyTypeId(TestFuncs.<Integer>selectFromCollection(PROXY_TYPES_IDS));
                proxyInfo.setTraffic(TestFuncs.generateTestRangeInteger(100, 100000));
                proxyInfo.setUrl(TestFuncs.generateTestString15());
                proxyInfo.setUrlIpChange(TestFuncs.generateTestString15());
            }));

            final CreatedProxy createdProxy
                    = webTestClient
                            .post()
                            .uri(uriBuilder
                                    -> uriBuilder
                                    .path(URI_CREATE_OR_UPDATE_PROXY)
                                    .build())
                            .accept(APPLICATION_JSON)
                            .body(monoProxy, ProxyInfo.class)
                            .exchange()
                            .expectStatus()
                            .isOk()
                            .expectBody(CreatedProxy.class)
                            .returnResult()
                            .getResponseBody();

            log.info("{}: test result is '{}' ", URI_CREATE_OR_UPDATE_PROXY, createdProxy);

            createdProxyId = createdProxy.getCreatedProxyId();

            log.info("{}: cached proxy '{}' ", URI_CREATE_OR_UPDATE_PROXY, proxyService.findProxy(createdProxyId));
            log.info("{}: cached proxy '{}' ", URI_CREATE_OR_UPDATE_PROXY, proxyService.findProxy(createdProxyId));
            log.info("{}: cached proxy '{}' ", URI_CREATE_OR_UPDATE_PROXY, proxyService.findProxy(createdProxyId));
            
            StmtProcessor.sleep(1000);

        });

    }
}
