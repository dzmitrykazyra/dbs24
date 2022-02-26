/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.test;

import org.dbs24.proxy.core.rest.api.proxy.ProxyInfo;
import org.dbs24.proxy.core.rest.api.proxy.CreateProxyRequest;
import org.dbs24.proxy.core.rest.api.proxy.CreatedProxyResponse;
import java.time.LocalDateTime;
import lombok.Data;
import lombok.extern.log4j.Log4j2;
import org.dbs24.application.core.locale.NLS;
import org.dbs24.application.core.service.funcs.TestFuncs;
import static org.dbs24.consts.SysConst.ALL_PACKAGES;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.dbs24.proxy.core.config.ProxyCoreConfig;
import org.dbs24.proxy.core.ProxyCoreApplication;
import static org.dbs24.proxy.core.consts.ProxyConsts.Countries.COUNTRIES_IDS;
import static org.dbs24.proxy.core.consts.ProxyConsts.ProxyProviderEnum.PROVIDERS_IDS;
import static org.dbs24.proxy.core.consts.ProxyConsts.ProxyStatusEnum.PROXY_STATUSES_IDS;
import static org.dbs24.proxy.core.consts.ProxyConsts.ProxyTypeEnum.PROXY_TYPES_IDS;
import static org.dbs24.proxy.core.consts.ProxyConsts.SocksAuthMethods.SOCKS_AUTH_METHOD_IDS;
import static org.dbs24.proxy.core.rest.api.consts.ProxyHttpConsts.UriConsts.*;
import org.dbs24.rest.api.action.SimpleActionInfo;
import org.dbs24.stmt.StmtProcessor;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import reactor.core.publisher.Mono;
import org.dbs24.proxy.core.rest.api.proxy.ProxyInfoResponse;
import static org.dbs24.proxy.core.rest.api.consts.ProxyHttpConsts.RestQueryParams.QP_PROXY_ID;
import static org.dbs24.rest.api.action.RestAction.MODIFY_ENTITY;

@Data
@Log4j2
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = {ProxyCoreApplication.class})
@Import({ProxyCoreConfig.class})
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@EntityScan(basePackages = {ALL_PACKAGES})
public class ProxiesTests extends AbstractProxyTest {

    private Integer createdProxyId;

    @Order(100)
    @Test
    @RepeatedTest(5)
    public void createProxy() {

        runTest(() -> {

            log.info("testing {}", URI_CREATE_OR_UPDATE_PROXY);

            final Mono<CreateProxyRequest> mono = Mono.just(StmtProcessor.create(CreateProxyRequest.class, proxyMessageBody -> {

                // Entity Body
                proxyMessageBody.setEntityInfo(StmtProcessor.create(ProxyInfo.class, proxyInfo -> {

                    proxyInfo.setActualDate(NLS.localDateTime2long(LocalDateTime.now()));
                    proxyInfo.setCountryId(TestFuncs.<Integer>selectFromCollection(COUNTRIES_IDS));
                    proxyInfo.setDateBegin(NLS.localDateTime2long(LocalDateTime.now()));
                    proxyInfo.setLogin(TestFuncs.generateTestString15());
                    proxyInfo.setPass(TestFuncs.generateTestString15());
                    proxyInfo.setPort(TestFuncs.generateTestRangeInteger(100, 100000));
                    proxyInfo.setSocksAuthData(TestFuncs.generate1000Bytes());
                    proxyInfo.setSocksClientData(TestFuncs.generate1000Bytes());
                    proxyInfo.setSocksAuthMethodId(TestFuncs.<Integer>selectFromCollectionNullAllowed(SOCKS_AUTH_METHOD_IDS));
                    proxyInfo.setProxyProviderId(TestFuncs.<Integer>selectFromCollection(PROVIDERS_IDS));
                    proxyInfo.setProxyStatusId(TestFuncs.<Integer>selectFromCollection(PROXY_STATUSES_IDS));
                    proxyInfo.setProxyTypeId(TestFuncs.<Integer>selectFromCollection(PROXY_TYPES_IDS));
                    proxyInfo.setTraffic(TestFuncs.generateTestRangeInteger(100, 100000));
                    proxyInfo.setUrl(TestFuncs.generateTestString15());
                    proxyInfo.setUrlIpChange(TestFuncs.generateTestString15());

                }));

                // Action Info
                proxyMessageBody.setEntityAction(StmtProcessor.create(SimpleActionInfo.class,
                        sai -> {
                            //sai.setActionClass(ActCreateOrUpdateLoanContract.class);
                            sai.setActionCode(MODIFY_ENTITY);
                        }
                ));
            }));

            final CreatedProxyResponse result
                    = webTestClient
                            .post()
                            .uri(uriBuilder
                                    -> uriBuilder
                                    .path(URI_CREATE_OR_UPDATE_PROXY)
                                    .build())
                            .accept(APPLICATION_JSON)
                            .body(mono, CreateProxyRequest.class)
                            .exchange()
                            .expectStatus()
                            .isOk()
                            .expectBody(CreatedProxyResponse.class)
                            .returnResult()
                            .getResponseBody();

            log.info("{}: test result is '{}' ", URI_CREATE_OR_UPDATE_PROXY, result);

            createdProxyId = result.getCreatedEntity().getCreatedProxyId();

            log.info("{}: test result is '{}' ", URI_CREATE_OR_UPDATE_PROXY, createdProxyId);

        });
    }

    @Order(200)
    @Test
    public void updateProxy() {

        runTest(() -> {

            log.info("testing {}", URI_CREATE_OR_UPDATE_PROXY);

            final Mono<CreateProxyRequest> mono = Mono.just(StmtProcessor.create(CreateProxyRequest.class, proxyMessageBody -> {

                // Entity Body
                proxyMessageBody.setEntityInfo(StmtProcessor.create(ProxyInfo.class, proxyInfo -> {

                    proxyInfo.setProxyId(createdProxyId);
                    proxyInfo.setActualDate(NLS.localDateTime2long(LocalDateTime.now()));
                    proxyInfo.setCountryId(TestFuncs.<Integer>selectFromCollection(COUNTRIES_IDS));
                    proxyInfo.setDateBegin(NLS.localDateTime2long(LocalDateTime.now()));
                    proxyInfo.setLogin(TestFuncs.generateTestString15());
                    proxyInfo.setSocksAuthData(TestFuncs.generate1000Bytes());
                    proxyInfo.setSocksClientData(TestFuncs.generate1000Bytes());
                    proxyInfo.setSocksAuthMethodId(TestFuncs.<Integer>selectFromCollectionNullAllowed(SOCKS_AUTH_METHOD_IDS));
                    proxyInfo.setPass(TestFuncs.generateTestString15());
                    proxyInfo.setPort(TestFuncs.generateTestRangeInteger(100, 100000));
                    proxyInfo.setProxyProviderId(TestFuncs.<Integer>selectFromCollection(PROVIDERS_IDS));
                    proxyInfo.setProxyStatusId(TestFuncs.<Integer>selectFromCollection(PROXY_STATUSES_IDS));
                    proxyInfo.setProxyTypeId(TestFuncs.<Integer>selectFromCollection(PROXY_TYPES_IDS));
                    proxyInfo.setTraffic(TestFuncs.generateTestRangeInteger(100, 100000));
                    proxyInfo.setUrl(TestFuncs.generateTestString15());
                    proxyInfo.setUrlIpChange(TestFuncs.generateTestString15());

                }));

                // Action Info
                proxyMessageBody.setEntityAction(StmtProcessor.create(SimpleActionInfo.class,
                        sai -> {
                            //sai.setActionClass(ActCreateOrUpdateLoanContract.class);
                            sai.setActionCode(MODIFY_ENTITY);
                        }
                ));

            }));

            final CreatedProxyResponse createdProxyResult
                    = webTestClient
                            .post()
                            .uri(uriBuilder
                                    -> uriBuilder
                                    .path(URI_CREATE_OR_UPDATE_PROXY)
                                    .build())
                            .accept(APPLICATION_JSON)
                            .body(mono, CreateProxyRequest.class)
                            .exchange()
                            .expectStatus()
                            .isOk()
                            .expectBody(CreatedProxyResponse.class)
                            .returnResult()
                            .getResponseBody();

            log.info("{}: test result is '{}' ", URI_CREATE_OR_UPDATE_PROXY, createdProxyResult);

        });
    }

    @Order(300)
    @Test
    public void getProxy() {

        runTest(() -> {

            log.info("testing {}, get proxyId: {} ", URI_GET_PROXY, createdProxyId);

            final ProxyInfoResponse proxyInfoResponse
                    = webTestClient
                            .get()
                            .uri(uriBuilder
                                    -> uriBuilder
                                    .path(URI_GET_PROXY)
                                    .queryParam(QP_PROXY_ID, createdProxyId)
                                    .build())
                            .accept(APPLICATION_JSON)
                            .exchange()
                            .expectStatus()
                            .isOk()
                            .expectBody(ProxyInfoResponse.class)
                            .returnResult()
                            .getResponseBody();

            log.info("{}: proxy details: {}", URI_GET_PROXY, proxyInfoResponse);

        });
    }
}
