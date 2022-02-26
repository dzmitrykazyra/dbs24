/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.test;

import lombok.extern.log4j.Log4j2;
import org.dbs24.WaMonitoring;
import org.dbs24.application.core.service.funcs.TestFuncs;
import org.dbs24.config.WaServerConfig;
import static org.dbs24.consts.WaConsts.Uri.URI_CREATE_OR_UPDATE_PACKAGE_DETAILS;
import static org.dbs24.consts.WaConsts.Uri.URI_GET_LICENSE_AGREEMENT;
import static org.dbs24.consts.WaConsts.Uri.URI_ALL_PACKAGE_DETAILS;
import static org.dbs24.consts.WaConsts.RestQueryParams.QP_PACKAGE_NAME;
import org.dbs24.rest.api.AllPackageDetailsInfo;
import org.dbs24.rest.api.LicenseAgreementInfo;
import org.dbs24.rest.api.PackageDetailsInfo;
import org.dbs24.rest.api.CreatedPackageDetails;
import org.dbs24.stmt.StmtProcessor;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import reactor.core.publisher.Mono;

@Log4j2
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = {WaMonitoring.class})
@Import({WaServerConfig.class})
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AppSettingsTests extends AbstractMonitoringTest {

    private String lastPkgName;

    @Order(100)
    @Test
    @RepeatedTest(5)
    public void createPackageDetails() {

        final Mono<PackageDetailsInfo> mono = Mono.just(StmtProcessor.create(PackageDetailsInfo.class, pdi -> {

            pdi.setPackageName(TestFuncs.generateTestString15());
            pdi.setActualDate(null);
            pdi.setAppName(TestFuncs.generateTestString15());
            pdi.setCompanyName(TestFuncs.generateTestString15());
            pdi.setContactInfo(TestFuncs.generateTestString15());

        }));

        runTest(() -> {

            log.info("testing {}", URI_CREATE_OR_UPDATE_PACKAGE_DETAILS);

            lastPkgName = webTestClient
                    .post()
                    .uri(uriBuilder
                            -> uriBuilder
                            .path(URI_CREATE_OR_UPDATE_PACKAGE_DETAILS)
                            .build())
                    .contentType(APPLICATION_JSON)
                    .accept(APPLICATION_JSON)
                    .body(mono, PackageDetailsInfo.class)
                    .exchange()
                    .expectStatus()
                    .isOk()
                    .expectBody(CreatedPackageDetails.class)
                    .returnResult()
                    .getResponseBody()
                    .getPackageName();

            log.info("{}: created package details - '{}'", URI_CREATE_OR_UPDATE_PACKAGE_DETAILS, lastPkgName);

        });
    }

    @Order(110)
    @Test
    public void updtePackageDetails() {

        final Mono<PackageDetailsInfo> mono = Mono.just(StmtProcessor.create(PackageDetailsInfo.class, pdi -> {

            pdi.setPackageName(lastPkgName);
            pdi.setActualDate(null);
            pdi.setAppName("updated: " + TestFuncs.generateTestString15());
            pdi.setCompanyName("updated: " + TestFuncs.generateTestString15());
            pdi.setContactInfo("updated: " + TestFuncs.generateTestString15());

        }));

        runTest(() -> {

            log.info("testing {}", URI_CREATE_OR_UPDATE_PACKAGE_DETAILS);

            lastPkgName = webTestClient
                    .post()
                    .uri(uriBuilder
                            -> uriBuilder
                            .path(URI_CREATE_OR_UPDATE_PACKAGE_DETAILS)
                            .build())
                    .contentType(APPLICATION_JSON)
                    .accept(APPLICATION_JSON)
                    .body(mono, PackageDetailsInfo.class)
                    .exchange()
                    .expectStatus()
                    .isOk()
                    .expectBody(CreatedPackageDetails.class)
                    .returnResult()
                    .getResponseBody()
                    .getPackageName();

            log.info("{}: created package details - '{}'", URI_CREATE_OR_UPDATE_PACKAGE_DETAILS, lastPkgName);

        });
    }

    //==========================================================================
    @Order(200)
    @Test
    public void getPackageDetails() {

        runTest(() -> {

            log.info("testing {}", URI_ALL_PACKAGE_DETAILS);

            final AllPackageDetailsInfo allPackageDetailsInfo
                    = webTestClient
                            .get()
                            .uri(uriBuilder
                                    -> uriBuilder
                                    .path(URI_ALL_PACKAGE_DETAILS)
                                    .build())
                            .accept(APPLICATION_JSON)
                            .exchange()
                            .expectStatus()
                            .isOk()
                            .expectBody(AllPackageDetailsInfo.class)
                            .returnResult()
                            .getResponseBody();

            log.info("{}: {}", URI_ALL_PACKAGE_DETAILS, allPackageDetailsInfo);

        });
    }

    //==========================================================================
    @Order(300)
    @Test
    public void getLicanceAgreement() {

        runTest(() -> {

            log.info("testing {}, packagename = '{}'", URI_GET_LICENSE_AGREEMENT, lastPkgName);

            final LicenseAgreementInfo licenseAgreementInfo
                    = webTestClient
                            .get()
                            .uri(uriBuilder
                                    -> uriBuilder
                                    .path(URI_GET_LICENSE_AGREEMENT)
                                    .queryParam(QP_PACKAGE_NAME, lastPkgName)
                                    .build())
                            .accept(APPLICATION_JSON)
                            .exchange()
                            .expectStatus()
                            .isOk()
                            .expectBody(LicenseAgreementInfo.class)
                            .returnResult()
                            .getResponseBody();

            log.info("{}: {}", URI_GET_LICENSE_AGREEMENT, licenseAgreementInfo);

        });
    }
}
