/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.test;

import lombok.extern.log4j.Log4j2;
import org.dbs24.app.promo.rest.dto.action.CreateOrderActionRequest;
import org.dbs24.app.promo.rest.dto.action.CreatedOrderActionResponse;
import org.dbs24.app.promo.rest.dto.action.OrderActionInfo;
import org.dbs24.app.promo.rest.dto.apppackage.CreatePackageRequest;
import org.dbs24.app.promo.rest.dto.apppackage.CreatedPackageResponse;
import org.dbs24.app.promo.rest.dto.apppackage.PackageInfo;
import org.dbs24.app.promo.rest.dto.batchsetup.BatchSetupInfo;
import org.dbs24.app.promo.rest.dto.batchsetup.CreateBatchSetupRequest;
import org.dbs24.app.promo.rest.dto.batchsetup.CreatedBatchSetupResponse;
import org.dbs24.app.promo.rest.dto.batchtemplate.BatchTemplateInfo;
import org.dbs24.app.promo.rest.dto.batchtemplate.CreateBatchTemplateRequest;
import org.dbs24.app.promo.rest.dto.batchtemplate.CreatedBatchTemplateResponse;
import org.dbs24.app.promo.rest.dto.bot.BotInfo;
import org.dbs24.app.promo.rest.dto.bot.CreateBotRequest;
import org.dbs24.app.promo.rest.dto.bot.CreatedBotResponse;
import org.dbs24.app.promo.rest.dto.comment.CommentInfo;
import org.dbs24.app.promo.rest.dto.comment.CreateCommentRequest;
import org.dbs24.app.promo.rest.dto.comment.CreatedCommentResponse;
import org.dbs24.app.promo.rest.dto.order.CreateOrderRequest;
import org.dbs24.app.promo.rest.dto.order.CreatedOrderResponse;
import org.dbs24.app.promo.rest.dto.order.OrderInfo;
import org.dbs24.application.core.service.funcs.TestFuncs;
import org.dbs24.stmt.StmtProcessor;
import org.dbs24.test.core.AbstractWebTest;
import org.springframework.test.context.TestPropertySource;
import reactor.core.publisher.Mono;

import static org.dbs24.app.promo.consts.AppPromoutionConsts.ACTIONS_LIST_IDS;
import static org.dbs24.app.promo.consts.AppPromoutionConsts.ACTIONS_RESULTS_LIST_IDS;
import static org.dbs24.app.promo.consts.AppPromoutionConsts.BatchTypeEnum.BATCH_TYPES_LIST_IDS;
import static org.dbs24.app.promo.consts.AppPromoutionConsts.BotStatusEnum.BOT_STATUSES_LIST_IDS;
import static org.dbs24.app.promo.consts.AppPromoutionConsts.OrderStatusEnum.ORDER_STATUSES_LIST_IDS;
import static org.dbs24.app.promo.consts.AppPromoutionConsts.ProviderEnum.PROVIDERS_LIST_IDS;
import static org.dbs24.app.promo.consts.AppPromoutionConsts.UriConsts.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;

@Log4j2
@TestPropertySource(properties = {
        "config.security.profile.webfilter.chain=development",
        "springdoc.api-docs.enabled=false",
        "springdoc.swagger-ui.enabled=false"})
public abstract class AbstractAppPromoTest extends AbstractWebTest {

    final String fialedMsgTempl = "Endpoint '%s' execution is failed";
    private Integer createdBotId;
    private Integer createdCommentId;
    private Integer createdPackageId;
    private Integer createdBatchSetupId;
    private Integer createdOrderId;
    private Integer createdOrderActionId;
    private Integer createdBatchTemplateId;

    protected Integer createOrUpdateTestBot(Integer botId) {

        runTest(() -> {

            final String testedRoute = URI_CREATE_OR_UPDATE_BOT;
            final String createEntityName = "createdBotId";
            log.info("testing {}", testedRoute);

            final Mono<CreateBotRequest> mono = Mono.just(StmtProcessor.create(CreateBotRequest.class, createBotRequest -> {

                // Entity Body
                createBotRequest.setEntityInfo(StmtProcessor.create(BotInfo.class, botInfo -> {
                    botInfo.setBotId(botId);
                    botInfo.setBotNote(TestFuncs.generateTestString15());
                    botInfo.setBotStatusId(TestFuncs.selectFromCollection(BOT_STATUSES_LIST_IDS));
                    botInfo.setLogin(TestFuncs.generateTestString15());
                    botInfo.setPass(TestFuncs.generateTestString15());
                    botInfo.setProviderId(TestFuncs.selectFromCollection(PROVIDERS_LIST_IDS));
                    botInfo.setSessionId(TestFuncs.generateTestString15());
                }));

                // Action Info
                //playerMessageBody.setEntityAction(SimpleActionInfo.createSimpleActionInfo(MODIFY_ENTITY));

            }));

            final CreatedBotResponse result
                    = webTestClient
                    .post()
                    .uri(uriBuilder
                            -> uriBuilder
                            .path(testedRoute)
                            .build())
                    .accept(APPLICATION_JSON)
                    .body(mono, CreateBotRequest.class)
                    .exchange()
                    .expectStatus()
                    .isOk()
                    .expectBody(CreatedBotResponse.class)
                    .returnResult()
                    .getResponseBody();

            log.info("{}: test result is '{}' ", testedRoute, result);

            createdBotId = result.getCreatedEntity().getCreatedBotId();

            log.info("{}: test result is '{}' ", testedRoute, createdBotId);

            checkTestResult(result, String.format(fialedMsgTempl, testedRoute.toUpperCase()));

            StmtProcessor.assertNotNull(Integer.class, createdBotId, createEntityName);

        });

        return createdBotId;
    }

    //==================================================================================================================
    protected Integer createOrUpdateTestComment(Integer commentId) {

        runTest(() -> {

            final String testedRoute = URI_CREATE_OR_UPDATE_COMMENT;
            final String createEntityName = "createdCommentId";
            log.info("testing {}", testedRoute);

            final Mono<CreateCommentRequest> mono = Mono.just(StmtProcessor.create(CreateCommentRequest.class, createCommentRequest -> {

                // Entity Body
                createCommentRequest.setEntityInfo(StmtProcessor.create(CommentInfo.class, commentInfo -> {
                    commentInfo.setCommentId(commentId);
                    commentInfo.setCreateDate(TestFuncs.generateUnsignedLong());
                    commentInfo.setCommentSource(TestFuncs.generateTestString15());
                }));

                // Action Info
                //playerMessageBody.setEntityAction(SimpleActionInfo.createSimpleActionInfo(MODIFY_ENTITY));

            }));

            final CreatedCommentResponse result
                    = webTestClient
                    .post()
                    .uri(uriBuilder
                            -> uriBuilder
                            .path(testedRoute)
                            .build())
                    .accept(APPLICATION_JSON)
                    .body(mono, CreateCommentRequest.class)
                    .exchange()
                    .expectStatus()
                    .isOk()
                    .expectBody(CreatedCommentResponse.class)
                    .returnResult()
                    .getResponseBody();

            log.info("{}: test result is '{}' ", testedRoute, result);

            createdCommentId = result.getCreatedEntity().getCreatedCommentId();

            log.info("{}: test result is '{}' ", testedRoute, createdCommentId);

            checkTestResult(result, String.format(fialedMsgTempl, testedRoute.toUpperCase()));

            StmtProcessor.assertNotNull(Integer.class, createdCommentId, createEntityName);

        });

        return createdCommentId;
    }

    //==================================================================================================================
    protected Integer createOrUpdateTestPackage(Integer packageId) {

        runTest(() -> {

            final String testedRoute = URI_CREATE_OR_UPDATE_PACKAGE;
            final String createEntityName = "createdPackageId";
            log.info("testing {}", testedRoute);

            final Mono<CreatePackageRequest> mono = Mono.just(StmtProcessor.create(CreatePackageRequest.class, createPackageRequest -> {

                // Entity Body
                createPackageRequest.setEntityInfo(StmtProcessor.create(PackageInfo.class, packageInfo -> {
                    packageInfo.setPackageId(packageId);
                    packageInfo.setPackageName(TestFuncs.generateTestString15());
                    packageInfo.setActualDate(TestFuncs.generateUnsignedLong());
                    packageInfo.setIsActual(TestFuncs.generateBool());
                    packageInfo.setPackageNote(TestFuncs.generateTestString15());
                    packageInfo.setProviderId(TestFuncs.selectFromCollection(PROVIDERS_LIST_IDS));
                }));

                // Action Info
                //playerMessageBody.setEntityAction(SimpleActionInfo.createSimpleActionInfo(MODIFY_ENTITY));

            }));

            final CreatedPackageResponse result
                    = webTestClient
                    .post()
                    .uri(uriBuilder
                            -> uriBuilder
                            .path(testedRoute)
                            .build())
                    .accept(APPLICATION_JSON)
                    .body(mono, CreatePackageRequest.class)
                    .exchange()
                    .expectStatus()
                    .isOk()
                    .expectBody(CreatedPackageResponse.class)
                    .returnResult()
                    .getResponseBody();

            log.info("{}: test result is '{}' ", testedRoute, result);

            createdPackageId = result.getCreatedEntity().getCreatedPackageId();

            log.info("{}: test result is '{}' ", testedRoute, createdPackageId);

            checkTestResult(result, String.format(fialedMsgTempl, testedRoute.toUpperCase()));

            StmtProcessor.assertNotNull(Integer.class, createdPackageId, createEntityName);

        });

        return createdPackageId;
    }

    //==================================================================================================================
    protected Integer createOrUpdateTestBatchSetup(Integer batchSetupId) {
        return createOrUpdateTestBatchSetup(null, batchSetupId);
    }

    protected Integer createOrUpdateTestBatchSetup(Integer bachTemplateId, Integer batchSetupId) {

        runTest(() -> {

            final String testedRoute = URI_CREATE_OR_UPDATE_BATCH_SETUP;
            final String createEntityName = "createdBatchSetupId";
            log.info("testing {}", testedRoute);

            final Mono<CreateBatchSetupRequest> mono = Mono.just(StmtProcessor.create(CreateBatchSetupRequest.class, createBatchSetupRequest -> {

                // Entity Body
                createBatchSetupRequest.setEntityInfo(StmtProcessor.create(BatchSetupInfo.class, batchSetupInfo -> {
                    batchSetupInfo.setBatchSetupId(batchSetupId);
                    batchSetupInfo.setBatchNote(TestFuncs.generateTestString15());
                    batchSetupInfo.setActualDate(TestFuncs.generateUnsignedLong());
                    batchSetupInfo.setIsActual(TestFuncs.generateBool());
                    batchSetupInfo.setExecutionOrder(TestFuncs.generateTestRangeInteger(1, 1000000));
                    batchSetupInfo.setMinDelay(TestFuncs.generateTestRangeInteger(1, 10000));
                    batchSetupInfo.setMaxDelay(TestFuncs.generateTestRangeInteger(1, 10000));
                    batchSetupInfo.setBatchTemplateId(createOrUpdateTestBatchTemplate(bachTemplateId));
                    batchSetupInfo.setActRefId(TestFuncs.selectFromCollection(ACTIONS_LIST_IDS));
                }));

                // Action Info
                //playerMessageBody.setEntityAction(SimpleActionInfo.createSimpleActionInfo(MODIFY_ENTITY));

            }));

            final CreatedBatchSetupResponse result
                    = webTestClient
                    .post()
                    .uri(uriBuilder
                            -> uriBuilder
                            .path(testedRoute)
                            .build())
                    .accept(APPLICATION_JSON)
                    .body(mono, CreateBatchSetupRequest.class)
                    .exchange()
                    .expectStatus()
                    .isOk()
                    .expectBody(CreatedBatchSetupResponse.class)
                    .returnResult()
                    .getResponseBody();

            log.info("{}: test result is '{}' ", testedRoute, result);

            createdBatchSetupId = result.getCreatedEntity().getCreatedBatchId();

            log.info("{}: test result is '{}' ", testedRoute, createdBatchSetupId);

            checkTestResult(result, String.format(fialedMsgTempl, testedRoute.toUpperCase()));

            StmtProcessor.assertNotNull(Integer.class, createdBatchSetupId, createEntityName);

        });

        return createdBatchSetupId;
    }

    //==================================================================================================================
    protected Integer createOrUpdateTestOrder(Integer orderId) {
        return createOrUpdateTestOrder(orderId, null);
    }

    protected Integer createOrUpdateTestOrder(Integer orderId, Integer batchTemplateId) {

        runTest(() -> {

            final String testedRoute = URI_CREATE_OR_UPDATE_ORDER;
            final String createEntityName = "createdOrderId";
            log.info("testing {}", testedRoute);

            final Mono<CreateOrderRequest> mono = Mono.just(StmtProcessor.create(CreateOrderRequest.class, createOrderRequest -> {

                // Entity Body
                createOrderRequest.setEntityInfo(StmtProcessor.create(OrderInfo.class, orderInfo -> {
                    orderInfo.setOrderId(orderId);
                    orderInfo.setActualDate(TestFuncs.generateUnsignedLong());
                    orderInfo.setBatchTemplateId(createOrUpdateTestBatchTemplate(batchTemplateId));
                    orderInfo.setOrderStatusId(TestFuncs.selectFromCollection(ORDER_STATUSES_LIST_IDS));
                    orderInfo.setExecFinishDate(TestFuncs.generateUnsignedLong());
                    orderInfo.setExecLastDate(TestFuncs.generateUnsignedLong());
                    orderInfo.setExecStartDate(TestFuncs.generateUnsignedLong());
                    orderInfo.setAppPackageId(createOrUpdateTestPackage(null));
                    orderInfo.setOrderNote(TestFuncs.generateTestString15());
                    orderInfo.setOrderBatchesAmount(TestFuncs.generateTestRangeInteger(2, 100));
                    orderInfo.setSuccessBatchesAmount(0);
                    orderInfo.setFailBatchesAmount(0);
                    orderInfo.setOrderName(TestFuncs.generateTestString15());

                }));

                // Action Info
                //playerMessageBody.setEntityAction(SimpleActionInfo.createSimpleActionInfo(MODIFY_ENTITY));

            }));

            final CreatedOrderResponse result
                    = webTestClient
                    .post()
                    .uri(uriBuilder
                            -> uriBuilder
                            .path(testedRoute)
                            .build())
                    .accept(APPLICATION_JSON)
                    .body(mono, CreateOrderRequest.class)
                    .exchange()
                    .expectStatus()
                    .isOk()
                    .expectBody(CreatedOrderResponse.class)
                    .returnResult()
                    .getResponseBody();

            log.info("{}: test result is '{}' ", testedRoute, result);

            createdOrderId = result.getCreatedEntity().getCreatedOrderId();

            log.info("{}: test result is '{}' ", testedRoute, createdOrderId);

            checkTestResult(result, String.format(fialedMsgTempl, testedRoute.toUpperCase()));

            StmtProcessor.assertNotNull(Integer.class, createdOrderId, createEntityName);

        });

        return createdOrderId;
    }

    protected Integer createOrUpdateTestOrderAction(Integer orderActionId) {
        return createOrUpdateTestOrderAction(null, orderActionId);
    }

    protected Integer createOrUpdateTestOrderAction(Integer orderId, Integer orderActionId) {

        runTest(() -> {

            final String testedRoute = URI_CREATE_OR_UPDATE_ORDER_ACTON;
            final String createEntityName = "createdOrderActionId";
            log.info("testing {}", testedRoute);

            final Mono<CreateOrderActionRequest> mono = Mono.just(StmtProcessor.create(CreateOrderActionRequest.class, createOrderActionRequest -> {

                // Entity Body
                createOrderActionRequest.setEntityInfo(StmtProcessor.create(OrderActionInfo.class, orderActionInfo -> {
                    orderActionInfo.setOrderId(createOrUpdateTestOrder(null));
                    orderActionInfo.setActionId(orderActionId);
                    orderActionInfo.setActRefId(TestFuncs.selectFromCollection(ACTIONS_LIST_IDS));
                    orderActionInfo.setBatchSetupId(createOrUpdateTestBatchSetup(null));
                    orderActionInfo.setActionResultId(TestFuncs.selectFromCollection(ACTIONS_RESULTS_LIST_IDS));
                    orderActionInfo.setActionFinishDate(TestFuncs.generateUnsignedLong());
                    orderActionInfo.setActionStartDate(TestFuncs.generateUnsignedLong());
                    orderActionInfo.setErrMsg(TestFuncs.generateTestString15());
                    orderActionInfo.setUsedIp(TestFuncs.generateTestString15());
                    orderActionInfo.setExecutionOrder(TestFuncs.generateTestRangeInteger(10, 1000));
                    orderActionInfo.setBotId(createOrUpdateTestBot(null));

                }));

                // Action Info
                //playerMessageBody.setEntityAction(SimpleActionInfo.createSimpleActionInfo(MODIFY_ENTITY));

            }));

            final CreatedOrderActionResponse result
                    = webTestClient
                    .post()
                    .uri(uriBuilder
                            -> uriBuilder
                            .path(testedRoute)
                            .build())
                    .accept(APPLICATION_JSON)
                    .body(mono, CreateOrderActionRequest.class)
                    .exchange()
                    .expectStatus()
                    .isOk()
                    .expectBody(CreatedOrderActionResponse.class)
                    .returnResult()
                    .getResponseBody();

            log.info("{}: test result is '{}' ", testedRoute, result);

            createdOrderActionId = result.getCreatedEntity().getCreatedActionId();

            log.info("{}: test result is '{}' ", testedRoute, createdOrderActionId);

            checkTestResult(result, String.format(fialedMsgTempl, testedRoute.toUpperCase()));

            StmtProcessor.assertNotNull(Integer.class, createdOrderActionId, createEntityName);

        });

        return createdOrderActionId;
    }

    //==================================================================================================================
    protected Integer createOrUpdateTestBatchTemplate(Integer batchTemplateId) {

        runTest(() -> {

            final String testedRoute = URI_CREATE_OR_UPDATE_BATCH_TEMPLATE;
            final String createEntityName = "createdBatchTemplateId";
            log.info("testing {}", testedRoute);

            final Mono<CreateBatchTemplateRequest> mono = Mono.just(StmtProcessor.create(CreateBatchTemplateRequest.class, createBatchTemplateRequest -> {

                // Entity Body
                createBatchTemplateRequest.setEntityInfo(StmtProcessor.create(BatchTemplateInfo.class, batchTemplateInfo -> {
                    batchTemplateInfo.setBatchTemplateId(batchTemplateId);
                    batchTemplateInfo.setActualDate(TestFuncs.generateUnsignedLong());
                    batchTemplateInfo.setIsActual(TestFuncs.generateBool());
                    batchTemplateInfo.setTemplateName(TestFuncs.generateTestString15());
                    batchTemplateInfo.setBatchTypeId(TestFuncs.selectFromCollection(BATCH_TYPES_LIST_IDS));
                    batchTemplateInfo.setProviderId(TestFuncs.selectFromCollection(PROVIDERS_LIST_IDS));
                }));

                // Action Info
                //playerMessageBody.setEntityAction(SimpleActionInfo.createSimpleActionInfo(MODIFY_ENTITY));

            }));

            final CreatedBatchTemplateResponse result
                    = webTestClient
                    .post()
                    .uri(uriBuilder
                            -> uriBuilder
                            .path(testedRoute)
                            .build())
                    .accept(APPLICATION_JSON)
                    .body(mono, CreateBatchTemplateRequest.class)
                    .exchange()
                    .expectStatus()
                    .isOk()
                    .expectBody(CreatedBatchTemplateResponse.class)
                    .returnResult()
                    .getResponseBody();

            log.info("{}: test result is '{}' ", testedRoute, result);

            createdBatchTemplateId = result.getCreatedEntity().getCreatedBatchId();

            log.info("{}: test result is '{}' ", testedRoute, createdBatchTemplateId);

            checkTestResult(result, String.format(fialedMsgTempl, testedRoute.toUpperCase()));

            StmtProcessor.assertNotNull(Integer.class, createdBatchTemplateId, createEntityName);

        });

        return createdBatchTemplateId;
    }
}
