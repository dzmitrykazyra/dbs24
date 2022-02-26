/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.test;

import lombok.extern.log4j.Log4j2;
import org.dbs24.test.core.AbstractWebTest;
import org.dbs24.tik.dev.rest.dto.contract.ContractInfo;
import org.dbs24.tik.dev.rest.dto.contract.CreateContractRequest;
import org.dbs24.tik.dev.rest.dto.contract.CreatedContractResponse;
import org.dbs24.tik.dev.rest.dto.developer.CreateDeveloperRequest;
import org.dbs24.tik.dev.rest.dto.developer.CreatedDeveloperResponse;
import org.dbs24.tik.dev.rest.dto.developer.DeveloperInfo;
import org.dbs24.tik.dev.rest.dto.device.CreateDeviceRequest;
import org.dbs24.tik.dev.rest.dto.device.CreatedDeviceResponse;
import org.dbs24.tik.dev.rest.dto.device.DeviceInfo;
import org.dbs24.tik.dev.rest.dto.endpoint.CreateEndpointActionRequest;
import org.dbs24.tik.dev.rest.dto.endpoint.CreatedEndpointActionResponse;
import org.dbs24.tik.dev.rest.dto.endpoint.EndpointActionInfo;
import org.dbs24.tik.dev.rest.dto.tariff.limit.CreateTariffLimitRequest;
import org.dbs24.tik.dev.rest.dto.tariff.limit.CreatedTariffLimitResponse;
import org.dbs24.tik.dev.rest.dto.tariff.limit.TariffLimitInfo;
import org.dbs24.tik.dev.rest.dto.tariff.plan.CreateTariffPlanRequest;
import org.dbs24.tik.dev.rest.dto.tariff.plan.CreatedTariffPlanResponse;
import org.dbs24.tik.dev.rest.dto.tariff.plan.TariffPlanInfo;
import org.dbs24.tik.dev.rest.dto.tariff.price.CreateTariffPlanPriceRequest;
import org.dbs24.tik.dev.rest.dto.tariff.price.CreatedTariffPlanPriceResponse;
import org.dbs24.tik.dev.rest.dto.tariff.price.TariffPlanPriceInfo;
import org.dbs24.tik.dev.rest.dto.tik.account.CreateTikAccountRequest;
import org.dbs24.tik.dev.rest.dto.tik.account.CreatedTikAccountResponse;
import org.dbs24.tik.dev.rest.dto.tik.account.TikAccountInfo;
import org.dbs24.tik.dev.rest.dto.tik.account.scope.CreateTikAccountScopeRequest;
import org.dbs24.tik.dev.rest.dto.tik.account.scope.CreatedTikAccountScopeResponse;
import org.dbs24.tik.dev.rest.dto.tik.account.scope.TikAccountScopeInfo;
import org.springframework.test.context.TestPropertySource;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

import static org.dbs24.application.core.service.funcs.TestFuncs.*;
import static org.dbs24.stmt.StmtProcessor.assertNotNull;
import static org.dbs24.stmt.StmtProcessor.create;
import static org.dbs24.tik.dev.consts.TikDevApiConsts.UriConsts.*;
import static org.dbs24.tik.dev.consts.enums.ContractStatusEnum.CONTRACTS_STATUSES_LIST_IDS;
import static org.dbs24.tik.dev.consts.enums.DeveloperStatusEnum.DEVELOPERS_STATUSES_LIST_IDS;
import static org.dbs24.tik.dev.consts.enums.DeviceStatusEnum.DEVICES_STATUSES_LIST_IDS;
import static org.dbs24.tik.dev.consts.enums.EndpointEnum.ENDPOINTS_LIST_IDS;
import static org.dbs24.tik.dev.consts.enums.EndpointResultEnum.ENDPOINTS_RESULTS_LIST_IDS;
import static org.dbs24.tik.dev.consts.enums.EndpointScopeEnum.ENDPOINTS_SCOPES_LIST_IDS;
import static org.dbs24.tik.dev.consts.enums.TariffPlanStatusEnum.TP_STATUSES_IDS;
import static org.dbs24.tik.dev.consts.enums.TariffPlanTypeEnum.TP_TYPES_LIST_IDS;
import static org.dbs24.tik.dev.consts.enums.TikAccountStatusEnum.TIK_ACCOUNT_STATUSES_IDS;
import static org.springframework.http.MediaType.APPLICATION_JSON;

@Log4j2
@TestPropertySource(properties = {
        "config.security.profile.webfilter.chain=development",
        "springdoc.api-docs.enabled=false",
        "springdoc.swagger-ui.enabled=false",
        "spring.datasource.url=jdbc:postgresql://193.178.170.145:5432/tda_tik_dev",
        "spring.datasource.username=tda_admin",
        "spring.datasource.password=$pr0m0$pwd76"})
public abstract class AbstractTikDevApiTest extends AbstractWebTest {

    final String fialedMsgTempl = "Endpoint '%s' execution is failed";
    private Long createdDeveloperId;
    private Long createdTikAccountId;
    private Long createdGrantId;
    private Long createdTariffLimitId;
    private Long createdTariffPlanId;
    private Long createdContractId;
    private Long createdTariffPlanPriceId;
    private Long createdDeviceId;
    private Long createdEndpointActionId;

    protected Long createTestDeveloper() {
        return createOrUpdateTestDeveloper(null);
    }

    protected Long createOrUpdateTestDeveloper(Long developerId) {

        runTest(() -> {

            final String testedRoute = URI_CREATE_OR_UPDATE_DEVELOPER;
            final String createEntityName = "createdDeveloperId";
            log.info("testing {}", testedRoute);

            final Mono<CreateDeveloperRequest> mono = Mono.just(create(CreateDeveloperRequest.class, createDeveloperRequest -> {

                // Entity Body
                createDeveloperRequest.setEntityInfo(create(DeveloperInfo.class, developerInfo -> {
                    developerInfo.setDeveloperId(developerId);
                    developerInfo.setDeveloperStatusId(selectFromCollection(DEVELOPERS_STATUSES_LIST_IDS));
                    developerInfo.setApiKey(generateTestString15());
                    developerInfo.setActualDate(generateUnsignedLong());
                    developerInfo.setEmail(generateTestString15());
                    developerInfo.setWebsite(generateTestString15());
                    developerInfo.setOauthClientId(generateTestString15());
                    developerInfo.setCountryCode(generateTestString(3));

                }));

                // Action Info
                //playerMessageBody.setEntityAction(SimpleActionInfo.createSimpleActionInfo(MODIFY_ENTITY));

            }));

            final CreatedDeveloperResponse result
                    = webTestClient
                    .post()
                    .uri(uriBuilder
                            -> uriBuilder
                            .path(testedRoute)
                            .build())
                    .accept(APPLICATION_JSON)
                    .body(mono, CreateDeveloperRequest.class)
                    .exchange()
                    .expectStatus()
                    .isOk()
                    .expectBody(CreatedDeveloperResponse.class)
                    .returnResult()
                    .getResponseBody();

            log.info("{}: test result is '{}' ", testedRoute, result);

            createdDeveloperId = result.getCreatedEntity().getCreatedDeveloperId();

            log.info("{}: test result is '{}' ", testedRoute, createdDeveloperId);

            checkTestResult(result, String.format(fialedMsgTempl, testedRoute.toUpperCase()));

            assertNotNull(Integer.class, createdDeveloperId, createEntityName);

        });

        return createdDeveloperId;
    }

    //==================================================================================================================
    protected Long createTestTikAccount() {
        return createOrUpdateTestTikAccount(null);
    }

    protected Long createOrUpdateTestTikAccount(Long tikAccountId) {

        runTest(() -> {

            final String testedRoute = URI_CREATE_OR_UPDATE_TIK_ACCOUNT;
            final String createEntityName = "createdTikAccountId";
            log.info("testing {}", testedRoute);

            final Mono<CreateTikAccountRequest> mono = Mono.just(create(CreateTikAccountRequest.class, createTikAccountRequest -> {

                // Entity Body
                createTikAccountRequest.setEntityInfo(create(TikAccountInfo.class, tikAccountInfo -> {
                    tikAccountInfo.setDeveloperId(createTestDeveloper());
                    tikAccountInfo.setTikAccountStatusId(selectFromCollection(TIK_ACCOUNT_STATUSES_IDS));
                    tikAccountInfo.setTikAccountId(tikAccountId);
                    tikAccountInfo.setTikAuthKey(generateTestString15());
                    tikAccountInfo.setTikEmail(generateTestString15());
                    tikAccountInfo.setTikLogin(generateTestString15());
                    tikAccountInfo.setTikPwd(generateTestString20());

                }));

                // Action Info
                //playerMessageBody.setEntityAction(SimpleActionInfo.createSimpleActionInfo(MODIFY_ENTITY));

            }));

            final CreatedTikAccountResponse result
                    = webTestClient
                    .post()
                    .uri(uriBuilder
                            -> uriBuilder
                            .path(testedRoute)
                            .build())
                    .accept(APPLICATION_JSON)
                    .body(mono, CreateTikAccountRequest.class)
                    .exchange()
                    .expectStatus()
                    .isOk()
                    .expectBody(CreatedTikAccountResponse.class)
                    .returnResult()
                    .getResponseBody();

            log.info("{}: test result is '{}' ", testedRoute, result);

            createdTikAccountId = result.getCreatedEntity().getCreatedTikAccountId();

            log.info("{}: test result is '{}' ", testedRoute, createdTikAccountId);

            checkTestResult(result, String.format(fialedMsgTempl, testedRoute.toUpperCase()));

            assertNotNull(Integer.class, createdTikAccountId, createEntityName);

        });

        return createdTikAccountId;
    }

    //==================================================================================================================
    protected Long createTestTikAccountScope() {
        return createOrUpdateTestTikAccountScope(null);
    }

    protected Long createOrUpdateTestTikAccountScope(Long tikAccountScopeId) {

        runTest(() -> {

            final String testedRoute = URI_CREATE_OR_UPDATE_TIK_ACCOUNT_SCOPE;
            final String createEntityName = "createdGrantId";
            log.info("testing {}", testedRoute);

            final Mono<CreateTikAccountScopeRequest> mono = Mono.just(create(CreateTikAccountScopeRequest.class, createTikAccountScopeRequest -> {

                // Entity Body
                createTikAccountScopeRequest.setEntityInfo(create(TikAccountScopeInfo.class, tikAccountScopeInfo -> {
                    tikAccountScopeInfo.setGrantId(tikAccountScopeId);
                    tikAccountScopeInfo.setTikAccountId(createTestTikAccount());
                    tikAccountScopeInfo.setEndpointScopeId(selectFromCollection(ENDPOINTS_SCOPES_LIST_IDS));
                    tikAccountScopeInfo.setIsGranted(generateBool());

                }));

                // Action Info
                //playerMessageBody.setEntityAction(SimpleActionInfo.createSimpleActionInfo(MODIFY_ENTITY));

            }));

            final CreatedTikAccountScopeResponse result
                    = webTestClient
                    .post()
                    .uri(uriBuilder
                            -> uriBuilder
                            .path(testedRoute)
                            .build())
                    .accept(APPLICATION_JSON)
                    .body(mono, CreateTikAccountScopeRequest.class)
                    .exchange()
                    .expectStatus()
                    .isOk()
                    .expectBody(CreatedTikAccountScopeResponse.class)
                    .returnResult()
                    .getResponseBody();

            log.info("{}: test result is '{}' ", testedRoute, result);

            createdGrantId = result.getCreatedEntity().getCreatedGrantId();

            log.info("{}: test result is '{}' ", testedRoute, createdTikAccountId);

            checkTestResult(result, String.format(fialedMsgTempl, testedRoute.toUpperCase()));

            assertNotNull(Long.class, createdGrantId, createEntityName);

        });

        return createdGrantId;
    }

    //==================================================================================================================
    protected Long createTestTariffLimit() {
        return createOrUpdateTestTariffLimit(null);
    }

    protected Long createOrUpdateTestTariffLimit(Long tariffLimitId) {

        runTest(() -> {

            final String testedRoute = URI_CREATE_OR_UPDATE_TARIFF_LIMIT;
            final String createEntityName = "createdTariffLimitId";
            log.info("testing {}", testedRoute);

            final Mono<CreateTariffLimitRequest> mono = Mono.just(create(CreateTariffLimitRequest.class, createTariffLimitRequest -> {

                // Entity Body
                createTariffLimitRequest.setEntityInfo(create(TariffLimitInfo.class, tariffLimitInfo -> {

                    tariffLimitInfo.setTariffLimitId(tariffLimitId);
                    tariffLimitInfo.setBandwidthMbLimit(generateTestRangeInteger(1000, 100000));
                    tariffLimitInfo.setOauthUsersLimit(generateTestRangeInteger(1000, 100000));
                    tariffLimitInfo.setDailyEndpointsLimit(generateTestRangeInteger(1000, 100000));
                    tariffLimitInfo.setUsePremiumPoints(generateBool());
                }));

                // Action Info
                //playerMessageBody.setEntityAction(SimpleActionInfo.createSimpleActionInfo(MODIFY_ENTITY));

            }));

            final CreatedTariffLimitResponse result
                    = webTestClient
                    .post()
                    .uri(uriBuilder
                            -> uriBuilder
                            .path(testedRoute)
                            .build())
                    .accept(APPLICATION_JSON)
                    .body(mono, CreateTariffLimitRequest.class)
                    .exchange()
                    .expectStatus()
                    .isOk()
                    .expectBody(CreatedTariffLimitResponse.class)
                    .returnResult()
                    .getResponseBody();

            log.info("{}: test result is '{}' ", testedRoute, result);

            createdTariffLimitId = result.getCreatedEntity().getCreatedLimitId();

            log.info("{}: test result is '{}' ", testedRoute, createdTariffLimitId);

            checkTestResult(result, String.format(fialedMsgTempl, testedRoute.toUpperCase()));

            assertNotNull(Long.class, createdTariffLimitId, createEntityName);

        });

        return createdTariffLimitId;
    }

    //==================================================================================================================
    protected Long createTestTariffPlan() {
        return createOrUpdateTestTariffPlan(null);
    }

    protected Long createOrUpdateTestTariffPlan(Long tariffPlanId) {

        runTest(() -> {

            final String testedRoute = URI_CREATE_OR_UPDATE_TARIFF_PLAN;
            final String createEntityName = "createdTariffPlanId";
            log.info("testing {}", testedRoute);

            final Mono<CreateTariffPlanRequest> mono = Mono.just(create(CreateTariffPlanRequest.class,
                    createTariffPlanRequest -> {

                        // Entity Body
                        createTariffPlanRequest.setEntityInfo(create(TariffPlanInfo.class, tariffPlanInfo -> {

                            tariffPlanInfo.setTariffPlanId(tariffPlanId);
                            tariffPlanInfo.setTariffPlanTypeId(selectFromCollection(TP_TYPES_LIST_IDS));
                            tariffPlanInfo.setTariffPlanStatusId(selectFromCollection(TP_STATUSES_IDS));
                            tariffPlanInfo.setTpName(generateTestString20());
                            tariffPlanInfo.setTpNote(generateTestString20());
                            tariffPlanInfo.setTariffLimitId(createOrUpdateTestTariffLimit(null));

                        }));

                        // Action Info
                        //playerMessageBody.setEntityAction(SimpleActionInfo.createSimpleActionInfo(MODIFY_ENTITY));

                    }));

            final CreatedTariffPlanResponse result
                    = webTestClient
                    .post()
                    .uri(uriBuilder
                            -> uriBuilder
                            .path(testedRoute)
                            .build())
                    .accept(APPLICATION_JSON)
                    .body(mono, CreateTariffPlanRequest.class)
                    .exchange()
                    .expectStatus()
                    .isOk()
                    .expectBody(CreatedTariffPlanResponse.class)
                    .returnResult()
                    .getResponseBody();

            log.info("{}: test result is '{}' ", testedRoute, result);

            createdTariffPlanId = result.getCreatedEntity().getCreatedTariffPlanId();

            log.info("{}: test result is '{}' ", testedRoute, createdTariffPlanId);

            checkTestResult(result, String.format(fialedMsgTempl, testedRoute.toUpperCase()));

            assertNotNull(Long.class, createdTariffPlanId, createEntityName);

        });

        return createdTariffPlanId;
    }

    //==================================================================================================================
    protected Long createTestContract() {
        return createOrUpdateTestContract(null);
    }

    protected Long createOrUpdateTestContract(Long contractId) {

        runTest(() -> {

            final String testedRoute = URI_CREATE_OR_UPDATE_CONTRACT;
            final String createEntityName = "createdContractId";
            log.info("testing {}", testedRoute);

            final Mono<CreateContractRequest> mono = Mono.just(create(CreateContractRequest.class,
                    createContractRequest -> {

                        // Entity Body
                        createContractRequest.setEntityInfo(create(ContractInfo.class, contractInfo -> {

                            contractInfo.setContractId(contractId);
                            contractInfo.setContractStatusId(selectFromCollection(CONTRACTS_STATUSES_LIST_IDS));
                            contractInfo.setTariffPlanId(createTestTariffPlan());
                            contractInfo.setBeginDate(generateUnsignedLong());
                            contractInfo.setEndDate(contractInfo.getBeginDate() + 100500);
                            contractInfo.setDeveloperId(createTestDeveloper());

                        }));

                        // Action Info
                        //playerMessageBody.setEntityAction(SimpleActionInfo.createSimpleActionInfo(MODIFY_ENTITY));

                    }));

            final CreatedContractResponse result
                    = webTestClient
                    .post()
                    .uri(uriBuilder
                            -> uriBuilder
                            .path(testedRoute)
                            .build())
                    .accept(APPLICATION_JSON)
                    .body(mono, CreateContractRequest.class)
                    .exchange()
                    .expectStatus()
                    .isOk()
                    .expectBody(CreatedContractResponse.class)
                    .returnResult()
                    .getResponseBody();

            log.info("{}: test result is '{}' ", testedRoute, result);

            createdContractId = result.getCreatedEntity().getCreatedContractId();

            log.info("{}: test result is '{}' ", testedRoute, createdContractId);

            checkTestResult(result, String.format(fialedMsgTempl, testedRoute.toUpperCase()));

            assertNotNull(Long.class, createdContractId, createEntityName);

        });

        return createdContractId;
    }

    //==================================================================================================================
    protected Long createTestTariffPlanPrice() {
        return createOrUpdateTestTariffPlanPrice(null);
    }

    protected Long createOrUpdateTestTariffPlanPrice(Long tariffPlanPriceId) {

        runTest(() -> {

            final String testedRoute = URI_CREATE_OR_UPDATE_TARIFF_PLAN_PRICE;
            final String createEntityName = "createdTariffPlanPriceId";
            log.info("testing {}", testedRoute);

            final Mono<CreateTariffPlanPriceRequest> mono = Mono.just(create(CreateTariffPlanPriceRequest.class,
                    createTariffPlanPriceRequest -> {

                        // Entity Body
                        createTariffPlanPriceRequest.setEntityInfo(create(TariffPlanPriceInfo.class, tariffPlanPriceInfo -> {

                            tariffPlanPriceInfo.setTariffPriceId(tariffPlanPriceId);
                            tariffPlanPriceInfo.setTariffPlanTypeId(selectFromCollection(TP_TYPES_LIST_IDS));
                            tariffPlanPriceInfo.setTariffBeginDate(generateUnsignedLong());
                            tariffPlanPriceInfo.setCountryCode(generateTestString(2));
                            tariffPlanPriceInfo.setCurrencyIso(generateTestString(3));
                            tariffPlanPriceInfo.setSumm(generateBigDecimal(new BigDecimal("1000000")));

                        }));

                        // Action Info
                        //playerMessageBody.setEntityAction(SimpleActionInfo.createSimpleActionInfo(MODIFY_ENTITY));

                    }));

            final CreatedTariffPlanPriceResponse result
                    = webTestClient
                    .post()
                    .uri(uriBuilder
                            -> uriBuilder
                            .path(testedRoute)
                            .build())
                    .accept(APPLICATION_JSON)
                    .body(mono, CreateTariffPlanPriceRequest.class)
                    .exchange()
                    .expectStatus()
                    .isOk()
                    .expectBody(CreatedTariffPlanPriceResponse.class)
                    .returnResult()
                    .getResponseBody();

            log.info("{}: test result is '{}' ", testedRoute, result);

            createdTariffPlanPriceId = result.getCreatedEntity().getCreatedTariffPlanPriceId();

            log.info("{}: test result is '{}' ", testedRoute, createdTariffPlanPriceId);

            checkTestResult(result, String.format(fialedMsgTempl, testedRoute.toUpperCase()));

            assertNotNull(Long.class, createdTariffPlanPriceId, createEntityName);

        });

        return createdTariffPlanPriceId;
    }

    //===========================================================
    protected Long createTestDevice() {
        return createTestDevice(null);
    }

    protected Long createTestDevice(Long contractId) {

        runTest(() -> {

            final String testedRoute = URI_CREATE_OR_UPDATE_DEVICE;
            final String createEntityName = "createdDeviceId";
            log.info("testing {}", testedRoute);

            final Mono<CreateDeviceRequest> mono = Mono.just(create(CreateDeviceRequest.class,
                    createDeviceRequest -> {

                        // Entity Body
                        createDeviceRequest.setEntityInfo(create(DeviceInfo.class, contractInfo -> {

                            contractInfo.setDeviceId(contractId);
                            contractInfo.setDeviceStatusId(selectFromCollection(DEVICES_STATUSES_LIST_IDS));
                            contractInfo.setApkAttrs(generate100Bytes());
                            contractInfo.setDeviceIdStr(generateTestString15());
                            contractInfo.setApkHashId(generateTestString15());
                            contractInfo.setInstallId(generateTestString15());
                        }));

                        // Action Info
                        //playerMessageBody.setEntityAction(SimpleActionInfo.createSimpleActionInfo(MODIFY_ENTITY));

                    }));

            final CreatedDeviceResponse result
                    = webTestClient
                    .post()
                    .uri(uriBuilder
                            -> uriBuilder
                            .path(testedRoute)
                            .build())
                    .accept(APPLICATION_JSON)
                    .body(mono, CreateDeviceRequest.class)
                    .exchange()
                    .expectStatus()
                    .isOk()
                    .expectBody(CreatedDeviceResponse.class)
                    .returnResult()
                    .getResponseBody();

            log.info("{}: test result is '{}' ", testedRoute, result);

            createdDeviceId = result.getCreatedEntity().getCreatedDeviceId();

            log.info("{}: test result is '{}' ", testedRoute, createdDeviceId);

            checkTestResult(result, String.format(fialedMsgTempl, testedRoute.toUpperCase()));

            assertNotNull(Long.class, createdDeviceId, createEntityName);

        });

        return createdDeviceId;
    }

    //===========================================================
    protected Long createTestEndpointAction() {

        runTest(() -> {

            final String testedRoute = URI_CREATE_ENDPOINT_ACTION;
            final String createEntityName = "createdEndpointActionId";
            log.info("testing {}", testedRoute);

            final Mono<CreateEndpointActionRequest> mono = Mono.just(create(CreateEndpointActionRequest.class,
                    createEndpointActionRequest -> {

                        // Entity Body
                        createEndpointActionRequest.setEntityInfo(create(EndpointActionInfo.class, endpointActionInfo -> {

                            //endpointActionInfo.setExecutionDate(TestFuncs.generateUnsignedLong());
                            endpointActionInfo.setEndpointActionId(null);
                            endpointActionInfo.setContractId(createTestContract());
                            endpointActionInfo.setEndpointId(selectFromCollection(ENDPOINTS_LIST_IDS));
                            endpointActionInfo.setEndpointResponse(generateTestInteger(200, 401, 403, 500));
                            endpointActionInfo.setBody(generateTestString20());
                            endpointActionInfo.setDeviceId(createTestDevice());
                            endpointActionInfo.setEndpointResultId(selectFromCollection(ENDPOINTS_RESULTS_LIST_IDS));
                            endpointActionInfo.setTikAccountId(createTestTikAccount());
                            endpointActionInfo.setErrLog(generateTestString20());
                            endpointActionInfo.setHeaders(generateTestString20());
                            endpointActionInfo.setIpAddress(generateTestString(10));
                            endpointActionInfo.setQueryParams(generateTestString20());
                            endpointActionInfo.setUsedBytes(generateTestRangeInteger(100, 100500));

                        }));

                        // Action Info
                        //playerMessageBody.setEntityAction(SimpleActionInfo.createSimpleActionInfo(MODIFY_ENTITY));

                    }));

            final CreatedEndpointActionResponse result
                    = webTestClient
                    .post()
                    .uri(uriBuilder
                            -> uriBuilder
                            .path(testedRoute)
                            .build())
                    .accept(APPLICATION_JSON)
                    .body(mono, CreateEndpointActionRequest.class)
                    .exchange()
                    .expectStatus()
                    .isOk()
                    .expectBody(CreatedEndpointActionResponse.class)
                    .returnResult()
                    .getResponseBody();

            log.info("{}: test result is '{}' ", testedRoute, result);

            createdEndpointActionId = result.getCreatedEntity().getCreatedEndpointActionId();

            log.info("{}: test result is '{}' ", testedRoute, createdEndpointActionId);

            checkTestResult(result, String.format(fialedMsgTempl, testedRoute.toUpperCase()));

            assertNotNull(Long.class, createdEndpointActionId, createEntityName);

        });

        return createdEndpointActionId;
    }
}
