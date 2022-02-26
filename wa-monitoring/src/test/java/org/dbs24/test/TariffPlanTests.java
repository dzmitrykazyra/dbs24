
package org.dbs24.test;

import lombok.extern.log4j.Log4j2;
import org.dbs24.WaMonitoring;
import org.dbs24.application.core.service.funcs.TestFuncs;
import org.dbs24.config.WaServerConfig;
import org.dbs24.rest.api.CreatedTariffPlan;
import org.dbs24.rest.api.TariffPlanInfo;
import org.dbs24.stmt.StmtProcessor;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

import static org.dbs24.consts.SysConst.*;
import static org.dbs24.consts.WaConsts.References.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;

import org.springframework.test.context.junit.jupiter.SpringExtension;
import reactor.core.publisher.Mono;

import static org.dbs24.consts.WaConsts.Uri.URI_CREATE_OR_UPDATE_TARIFF_PLAN;

@Log4j2
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = {WaMonitoring.class})
@Import({WaServerConfig.class})
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TariffPlanTests extends AbstractMonitoringTest {

    private Integer lastTariffPlanId;

    @Order(100)
    @Test
    @RepeatedTest(10)
    public void createTariffPlan() {

        final Mono<TariffPlanInfo> mono = Mono.just(StmtProcessor.create(TariffPlanInfo.class, tariffPlanInfo -> {

            tariffPlanInfo.setActualDate(LONG_NULL);
            tariffPlanInfo.setTariffPlanStatusId(TestFuncs.selectFromCollection(ALL_TARIFF_PLAN_STATUSES_IDS));
            tariffPlanInfo.setContractTypeId(TestFuncs.selectFromCollection(ALL_CONTRACT_TYPES_IDS));
            StmtProcessor.ifTrue(TestFuncs.generateBool(), () -> tariffPlanInfo.setDeviceTypeId(TestFuncs.selectFromCollection(ALL_DEVICE_TYPES_IDS)));
            tariffPlanInfo.setSku(TestFuncs.generateTestString15());
            tariffPlanInfo.setSubscriptionsAmount(TestFuncs.generateTestRangeInteger(1, 365));
            tariffPlanInfo.setDurationHours(TestFuncs.generateTestRangeInteger(1, 365));

        }));

        runTest(() -> {

            log.info("testing {}", URI_CREATE_OR_UPDATE_TARIFF_PLAN);

            lastTariffPlanId = webTestClient
                    .post()
                    .uri(uriBuilder
                            -> uriBuilder
                            .path(URI_CREATE_OR_UPDATE_TARIFF_PLAN)
                            .build())
                    .contentType(APPLICATION_JSON)
                    .accept(APPLICATION_JSON)
                    .body(mono, TariffPlanInfo.class)
                    .exchange()
                    .expectStatus()
                    .isOk()
                    .expectBody(CreatedTariffPlan.class)
                    .returnResult()
                    .getResponseBody()
                    .getTariffPlanId();

            StmtProcessor.assertNotNull(Integer.class, lastTariffPlanId, "lastTariffPlanId");

            log.info("{}: created tariff plan - {}", URI_CREATE_OR_UPDATE_TARIFF_PLAN, lastTariffPlanId);

        });
    }

    @Order(200)
    @Test
    public void updateTariffPlan() {

        final Mono<TariffPlanInfo> mono = Mono.just(StmtProcessor.create(TariffPlanInfo.class, tariffPlanInfo -> {

            tariffPlanInfo.setTariffPlanId(lastTariffPlanId);
            tariffPlanInfo.setActualDate(LONG_NULL);
            tariffPlanInfo.setTariffPlanStatusId(TestFuncs.selectFromCollection(ALL_TARIFF_PLAN_STATUSES_IDS));
            tariffPlanInfo.setContractTypeId(TestFuncs.selectFromCollection(ALL_CONTRACT_TYPES_IDS));
            tariffPlanInfo.setDeviceTypeId(TestFuncs.selectFromCollection(ALL_DEVICE_TYPES_IDS));
            tariffPlanInfo.setSku(TestFuncs.generateTestString15());
            tariffPlanInfo.setSubscriptionsAmount(TestFuncs.generateTestRangeInteger(1, 365));
            tariffPlanInfo.setDurationHours(TestFuncs.generateTestRangeInteger(1, 365));

        }));

        runTest(() -> {

            log.info("testing {}", URI_CREATE_OR_UPDATE_TARIFF_PLAN);

            lastTariffPlanId = webTestClient
                    .post()
                    .uri(uriBuilder
                            -> uriBuilder
                            .path(URI_CREATE_OR_UPDATE_TARIFF_PLAN)
                            .build())
                    .contentType(APPLICATION_JSON)
                    .accept(APPLICATION_JSON)
                    .body(mono, TariffPlanInfo.class)
                    .exchange()
                    .expectStatus()
                    .isOk()
                    .expectBody(CreatedTariffPlan.class)
                    .returnResult()
                    .getResponseBody()
                    .getTariffPlanId();

            StmtProcessor.assertNotNull(Integer.class, lastTariffPlanId, "lastTariffPlanId");

            log.info("{}: created tariff plan - {}", URI_CREATE_OR_UPDATE_TARIFF_PLAN, lastTariffPlanId);

        });
    }


    @Order(300)
    @Test
    public void createFailTariffPlan() {

        final Mono<TariffPlanInfo> mono = Mono.just(StmtProcessor.create(TariffPlanInfo.class, tariffPlanInfo -> {

            tariffPlanInfo.setTariffPlanId(INTEGER_NULL);
            tariffPlanInfo.setActualDate(LONG_NULL);
            tariffPlanInfo.setTariffPlanStatusId(TestFuncs.selectFromCollection(ALL_TARIFF_PLAN_STATUSES_IDS));
            tariffPlanInfo.setContractTypeId(TestFuncs.selectFromCollection(ALL_CONTRACT_TYPES_IDS));
            tariffPlanInfo.setDeviceTypeId(TestFuncs.selectFromCollection(ALL_DEVICE_TYPES_IDS));
            tariffPlanInfo.setSku(STRING_NULL);
            tariffPlanInfo.setSubscriptionsAmount(TestFuncs.generateTestRangeInteger(1, 365));
            tariffPlanInfo.setDurationHours(TestFuncs.generateTestRangeInteger(1, 365));

        }));

        runTest(() -> {

            log.info("testing {}", URI_CREATE_OR_UPDATE_TARIFF_PLAN);

            final CreatedTariffPlan createdTariffPlan = webTestClient
                    .post()
                    .uri(uriBuilder
                            -> uriBuilder
                            .path(URI_CREATE_OR_UPDATE_TARIFF_PLAN)
                            .build())
                    .contentType(APPLICATION_JSON)
                    .accept(APPLICATION_JSON)
                    .body(mono, TariffPlanInfo.class)
                    .exchange()
                    .expectStatus()
                    .isOk()
                    .expectBody(CreatedTariffPlan.class)
                    .returnResult()
                    .getResponseBody();

            //StmtProcessor.assertNotNull(Integer.class, lastTariffPlanId, "lastTariffPlanId");

            log.info("{}: answer {}", URI_CREATE_OR_UPDATE_TARIFF_PLAN, createdTariffPlan);

        });
    }
//
//    //==========================================================================
//    @Order(300)
//    @Test
//    public void getAllFirebaseApplications() {
//
//        runTest(() -> {
//
//            log.info("testing {}", URI_ALL_FIREBASE_APPLICATION);
//
//            final AllFirebaseApplications allFirebaseApplications
//                    = webTestClient
//                    .get()
//                    .uri(uriBuilder
//                            -> uriBuilder
//                            .path(URI_ALL_FIREBASE_APPLICATION)
//                            .build())
//                    .accept(APPLICATION_JSON)
//                    .exchange()
//                    .expectStatus()
//                    .isOk()
//                    .expectBody(AllFirebaseApplications.class)
//                    .returnResult()
//                    .getResponseBody();
//
//            log.info("{}: {}", URI_ALL_FIREBASE_APPLICATION, allFirebaseApplications);
//
//        });
//    }
}
