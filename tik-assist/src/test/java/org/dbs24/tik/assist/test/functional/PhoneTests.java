/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.tik.assist.test.functional;

import java.time.LocalDateTime;
import lombok.Data;
import lombok.extern.log4j.Log4j2;
import org.dbs24.application.core.locale.NLS;
import org.dbs24.application.core.service.funcs.TestFuncs;
import static org.dbs24.consts.SysConst.ALL_PACKAGES;
import org.dbs24.stmt.StmtProcessor;
import org.dbs24.tik.assist.TikAssist;
import org.dbs24.tik.assist.config.TikAssistConfig;

import static org.dbs24.tik.assist.constant.ApiPath.URI_CREATE_OR_UPDATE_PHONE;

import org.dbs24.tik.assist.constant.ApiPath;
import org.dbs24.tik.assist.constant.RequestQueryParam;
import org.dbs24.tik.assist.entity.dto.phone.PhoneDto;
import org.dbs24.tik.assist.entity.dto.phone.CreatedPhoneDto;
import org.dbs24.tik.assist.entity.dto.phone.CreatedPhoneUsageDto;
import org.dbs24.tik.assist.entity.dto.phone.PhoneUsageDto;
import org.dbs24.tik.assist.test.AbstractTikAssistTest;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import reactor.core.publisher.Mono;

@Data
@Log4j2
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = {TikAssist.class})
@Import({TikAssistConfig.class})
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@EntityScan(basePackages = {ALL_PACKAGES})
public class PhoneTests extends AbstractTikAssistTest {

    private Integer createdPhoneId;
    private Long createdPhoneUsageId;

    //==========================================================================
    @Order(100)
    @Test
    @RepeatedTest(5)
    public void createPhone() {

        runTest(() -> {

            log.info("testing {}", ApiPath.URI_CREATE_OR_UPDATE_PHONE);

            final Mono<PhoneDto> monoPhonePhone = Mono.just(StmtProcessor.create(PhoneDto.class, phoneDto -> {
                //user.set
                phoneDto.setActualDate(NLS.localDateTime2long(LocalDateTime.now()));
                //phoneInfo.setPhoneStatusId(TestFuncs.selectFromCollection(PHONE_STATUSES_IDS));
                phoneDto.setApkAttrs(TestFuncs.generate100Bytes());
                phoneDto.setApkHashId(TestFuncs.generateTestString15());
                phoneDto.setDeviceId(TestFuncs.generateTestString15());
                phoneDto.setInstallId(TestFuncs.generateTestString15());

            }));

            final CreatedPhoneDto createdPhonePhoneDto
                    = webTestClient
                            .post()
                            .uri(uriBuilder
                                    -> uriBuilder
                                    .path(URI_CREATE_OR_UPDATE_PHONE)
                                    .build())
                            .accept(APPLICATION_JSON)
                            .body(monoPhonePhone, PhoneDto.class)
                            .exchange()
                            .expectStatus()
                            .isOk()
                            .expectBody(CreatedPhoneDto.class)
                            .returnResult()
                            .getResponseBody();

            log.info("{}: test result is '{}' ", URI_CREATE_OR_UPDATE_PHONE, createdPhonePhoneDto);

            createdPhoneId = createdPhonePhoneDto.getCreatedPhoneId();

        });
    }

    //==========================================================================
    @Order(200)
    @Test
    //@RepeatedTest(5)
    public void updatePhone() {

        runTest(() -> {

            log.info("testing update {}", URI_CREATE_OR_UPDATE_PHONE);

            final Mono<PhoneDto> monoPhonePhone = Mono.just(StmtProcessor.create(PhoneDto.class, agentInfo -> {
                //user.set
                agentInfo.setPhoneId(createdPhoneId);
                agentInfo.setActualDate(NLS.localDateTime2long(LocalDateTime.now().plusDays(1)));
                //agentInfo.setPhoneStatusId(TestFuncs.selectFromCollection(PHONE_STATUSES_IDS));
                agentInfo.setApkAttrs(TestFuncs.generate100Bytes());
                agentInfo.setApkHashId(TestFuncs.generateTestString15());
                agentInfo.setDeviceId(TestFuncs.generateTestString15());
                agentInfo.setInstallId(TestFuncs.generateTestString15());

            }));

            final CreatedPhoneDto createdPhonePhoneDto
                    = webTestClient
                            .post()
                            .uri(uriBuilder
                                    -> uriBuilder
                                    .path(URI_CREATE_OR_UPDATE_PHONE)
                                    .build())
                            .accept(APPLICATION_JSON)
                            .body(monoPhonePhone, PhoneDto.class)
                            .exchange()
                            .expectStatus()
                            .isOk()
                            .expectBody(CreatedPhoneDto.class)
                            .returnResult()
                            .getResponseBody();

            log.info("{}: test result is '{}' ", URI_CREATE_OR_UPDATE_PHONE, createdPhonePhoneDto);

            createdPhoneId = createdPhonePhoneDto.getCreatedPhoneId();

        });
    }

    //==========================================================================
    @Order(210)
    @Test
    public void getPhone() {

        runTest(() -> {

            log.info("testing {}, get agentId = {} ", ApiPath.URI_GET_PHONE, createdPhoneId);

            runTest(() -> {

                log.info("testing {}", ApiPath.URI_GET_PHONE);

                final PhoneDto phoneDto
                        = webTestClient
                                .get()
                                .uri(uriBuilder
                                        -> uriBuilder
                                        .path(ApiPath.URI_GET_PHONE)
                                        .queryParam(RequestQueryParam.QP_PHONE_ID, createdPhoneId)
                                        .build())
                                .accept(APPLICATION_JSON)
                                .exchange()
                                .expectStatus()
                                .isOk()
                                .expectBody(PhoneDto.class)
                                .returnResult()
                                .getResponseBody();

                log.info("{}: {}", ApiPath.URI_GET_PHONE, phoneDto);

            });
        });
    }      
    
    //==========================================================================
    @Order(220)
    @Test
    public void getLeastUsedPhone() {

        runTest(() -> {

            log.info("testing {}, get getLeastUsedPhone ", ApiPath.URI_GET_AVAILABLE_PHONE);

            runTest(() -> {

                log.info("testing {}", ApiPath.URI_GET_AVAILABLE_PHONE);

                final PhoneDto phoneDto
                        = webTestClient
                                .get()
                                .uri(uriBuilder
                                        -> uriBuilder
                                        .path(ApiPath.URI_GET_AVAILABLE_PHONE)
                                        .build())
                                .accept(APPLICATION_JSON)
                                .exchange()
                                .expectStatus()
                                .isOk()
                                .expectBody(PhoneDto.class)
                                .returnResult()
                                .getResponseBody();

                log.info("{}: {}", ApiPath.URI_GET_AVAILABLE_PHONE, phoneDto);

            });
        });
    }
    
    //==========================================================================
    @Order(300)
    @Test
    @RepeatedTest(5)
    public void createPhoneUsage() {

        runTest(() -> {

            log.info("testing {}", ApiPath.URI_CREATE_OR_UPDATE_PHONE_USAGE);

            final Mono<PhoneUsageDto> mono = Mono.just(StmtProcessor.create(PhoneUsageDto.class, phoneUsageDto -> {
                //user.set
                phoneUsageDto.setActualDate(NLS.localDateTime2long(LocalDateTime.now()));
                phoneUsageDto.setPhoneId(createdPhoneId);
                phoneUsageDto.setIsSuccess(TestFuncs.generateBool());
                if (!phoneUsageDto.getIsSuccess()) {
                    phoneUsageDto.setErrMsg(TestFuncs.generateTestString20());
                }
            }));

            final CreatedPhoneUsageDto createdPhoneUsageDto
                    = webTestClient
                            .post()
                            .uri(uriBuilder
                                    -> uriBuilder
                                    .path(ApiPath.URI_CREATE_OR_UPDATE_PHONE_USAGE)
                                    .build())
                            .accept(APPLICATION_JSON)
                            .body(mono, PhoneUsageDto.class)
                            .exchange()
                            .expectStatus()
                            .isOk()
                            .expectBody(CreatedPhoneUsageDto.class)
                            .returnResult()
                            .getResponseBody();

            log.info("{}: test result is '{}' ", ApiPath.URI_CREATE_OR_UPDATE_PHONE_USAGE, createdPhoneUsageDto);

            //createdPhoneUsageId = createdPhoneUsage.getCreatedPhoneUsageId();

        });
    }

    //==========================================================================
    @Order(400)
    @Test
    public void updatePhoneUsage() {

        runTest(() -> {

            log.info("testing {}", ApiPath.URI_CREATE_OR_UPDATE_PHONE_USAGE);

            final Mono<PhoneUsageDto> mono = Mono.just(StmtProcessor.create(PhoneUsageDto.class, phoneUsageDto -> {
                //user.set
                //phoneUsageInfo.setPhoneUsageId(createdPhoneUsageId);
                phoneUsageDto.setActualDate(NLS.localDateTime2long(LocalDateTime.now().plusDays(10)));
                phoneUsageDto.setPhoneId(createdPhoneId);
                phoneUsageDto.setIsSuccess(TestFuncs.generateBool());
                if (!phoneUsageDto.getIsSuccess()) {
                    phoneUsageDto.setErrMsg(TestFuncs.generateTestString20());
                }
            }));

            final CreatedPhoneUsageDto createdPhoneUsageDto
                    = webTestClient
                            .post()
                            .uri(uriBuilder
                                    -> uriBuilder
                                    .path(ApiPath.URI_CREATE_OR_UPDATE_PHONE_USAGE)
                                    .build())
                            .accept(APPLICATION_JSON)
                            .body(mono, PhoneUsageDto.class)
                            .exchange()
                            .expectStatus()
                            .isOk()
                            .expectBody(CreatedPhoneUsageDto.class)
                            .returnResult()
                            .getResponseBody();

            log.info("{}: test result is '{}' ", ApiPath.URI_CREATE_OR_UPDATE_PHONE_USAGE, createdPhoneUsageDto);

            //createdPhoneUsageId = createdPhoneUsage.getCreatedPhoneUsageId();

        });
    }
}
