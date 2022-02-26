/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.insta.tmp.test;

import java.time.LocalDateTime;
import lombok.Data;
import lombok.extern.log4j.Log4j2;
import org.dbs24.application.core.locale.NLS;
import org.dbs24.application.core.service.funcs.TestFuncs;
import static org.dbs24.consts.SysConst.ALL_PACKAGES;
import org.dbs24.insta.tmp.InstaFacesSearch;
import org.dbs24.insta.tmp.config.InstafFsConfig;
import static org.dbs24.insta.tmp.consts.IfsConst.References.AccountStatuses.AS_ACTUAL;
import static org.dbs24.insta.tmp.consts.IfsConst.References.BotStatuses.BS_ACTUAL;
import static org.dbs24.insta.tmp.consts.IfsConst.References.TaskTypes.TT_TYPE1;
import static org.dbs24.insta.tmp.consts.IfsConst.References.TaskResult.TR_RESULT1;
import static org.dbs24.insta.tmp.consts.IfsConst.UriConsts.URI_CREATE_OR_UPDATE_ACCOUNT;
import static org.dbs24.insta.tmp.consts.IfsConst.UriConsts.URI_CREATE_OR_UPDATE_TASK;
import static org.dbs24.insta.tmp.consts.IfsConst.UriConsts.URI_CREATE_OR_UPDATE_BOT;
import org.dbs24.insta.tmp.rest.api.AccountInfo;
import org.dbs24.insta.tmp.rest.api.BotInfo;
import org.dbs24.insta.tmp.rest.api.CreatedAccount;
import org.dbs24.insta.tmp.rest.api.CreatedBot;
import org.dbs24.insta.tmp.rest.api.TaskInfo;
import org.dbs24.insta.tmp.rest.api.CreatedTask;
import org.dbs24.stmt.StmtProcessor;
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
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = {InstaFacesSearch.class})
@Import({InstafFsConfig.class})
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@EntityScan(basePackages = {ALL_PACKAGES})
public class TasksTests extends AbstractInstaFsTest {
    
    private Long createdTaskId;    
    private Integer createdBotId;
    private Long createdAccountId;
    
    @Order(95)
    @Test
    //@Transactional(readOnly = true)
    public void createAccount() {
        
        runTest(() -> {
            
            log.info("testing {}", URI_CREATE_OR_UPDATE_ACCOUNT);
            
            final Mono<AccountInfo> monoAccount = Mono.just(StmtProcessor.create(AccountInfo.class, accountInfo -> {
                accountInfo.setActualDate(NLS.localDateTime2long(LocalDateTime.now()));
                accountInfo.setAccountStatusId(AS_ACTUAL);
                accountInfo.setBiography(TestFuncs.generateTestString15());
                accountInfo.setFollowees(TestFuncs.generateTestRangeInteger(1, 1000));
                accountInfo.setFollowers(TestFuncs.generateTestRangeInteger(1, 1000));
                accountInfo.setFullName(TestFuncs.generateTestString15());
                accountInfo.setInstaId(TestFuncs.generateTestLong());
                accountInfo.setIsPrivate(TestFuncs.generateBool());
                accountInfo.setIsVerified(TestFuncs.generateBool());
                accountInfo.setMediaCount(TestFuncs.generateTestRangeInteger(1, 1000));
                accountInfo.setProfilePicHdUrl(TestFuncs.generateTestString15());
                accountInfo.setProfilePicUrl(TestFuncs.generateTestString15());
                accountInfo.setUserName(TestFuncs.generateTestString15());
            }));
            
            final CreatedAccount createdAccount
                    = webTestClient
                            .post()
                            .uri(uriBuilder
                                    -> uriBuilder
                                    .path(URI_CREATE_OR_UPDATE_ACCOUNT)
                                    .build())
                            .accept(APPLICATION_JSON)
                            .body(monoAccount, AccountInfo.class)
                            .exchange()
                            .expectStatus()
                            .isOk()
                            .expectBody(CreatedAccount.class)
                            .returnResult()
                            .getResponseBody();
            
            log.info("{}: test result is '{}' ", URI_CREATE_OR_UPDATE_ACCOUNT, createdAccount);
            
            createdAccountId = createdAccount.getAccountId();
            
        });
    }    
    
    @Order(100)
    @Test
    public void createBot() {
        
        runTest(() -> {
            
            log.info("testing {}", URI_CREATE_OR_UPDATE_BOT);
            
            final Mono<BotInfo> monoBot = Mono.just(StmtProcessor.create(BotInfo.class, botInfo -> {
                botInfo.setActualDate(NLS.localDateTime2long(LocalDateTime.now()));
                botInfo.setBotStatusId(BS_ACTUAL);
                botInfo.setEmail(TestFuncs.generateTestString15());
                botInfo.setPassword(TestFuncs.generateTestString15());
                botInfo.setSessionId(TestFuncs.generateTestString15());
                botInfo.setUserAgent(TestFuncs.generateTestString15());
                botInfo.setUserName(TestFuncs.generateTestString15());
                
            }));
            
            final CreatedBot createdBot
                    = webTestClient
                            .post()
                            .uri(uriBuilder
                                    -> uriBuilder
                                    .path(URI_CREATE_OR_UPDATE_BOT)
                                    .build())
                            .accept(APPLICATION_JSON)
                            .body(monoBot, BotInfo.class)
                            .exchange()
                            .expectStatus()
                            .isOk()
                            .expectBody(CreatedBot.class)
                            .returnResult()
                            .getResponseBody();
            
            log.info("{}: test result is '{}' ", URI_CREATE_OR_UPDATE_BOT, createdBot);
            
            createdBotId = createdBot.getBotId();
            
        });
    }
    
    @Order(200)
    @Test
    @RepeatedTest(5)
    public void createTask() {
        
        runTest(() -> {
            
            log.info("testing {}", URI_CREATE_OR_UPDATE_TASK);
            
            final Mono<TaskInfo> monoTask = Mono.just(StmtProcessor.create(TaskInfo.class, taskInfo -> {
                taskInfo.setActualDate(NLS.localDateTime2long(LocalDateTime.now()));
                taskInfo.setBotId(createdBotId);
                taskInfo.setAccountId(createdAccountId);
                taskInfo.setError(TestFuncs.generateTestString15());
                taskInfo.setParentTaskId(null);
                taskInfo.setRequestPaginationId(TestFuncs.generateTestString15());
                taskInfo.setTaskFinishDate(NLS.localDateTime2long(LocalDateTime.now().plusDays(1)));
                taskInfo.setTaskResultId(TR_RESULT1);
                taskInfo.setTaskStartDate(NLS.localDateTime2long(LocalDateTime.now()));
                taskInfo.setTaskTypeId(TT_TYPE1);
            }));
            
            final CreatedTask createdTask
                    = webTestClient
                            .post()
                            .uri(uriBuilder
                                    -> uriBuilder
                                    .path(URI_CREATE_OR_UPDATE_TASK)
                                    .build())
                            .accept(APPLICATION_JSON)
                            .body(monoTask, TaskInfo.class)
                            .exchange()
                            .expectStatus()
                            .isOk()
                            .expectBody(CreatedTask.class)
                            .returnResult()
                            .getResponseBody();
            
            log.info("{}: test result is '{}' ", URI_CREATE_OR_UPDATE_TASK, createdTask);
            
            createdTaskId = createdTask.getTaskId();
            
        });
    }
    
    @Order(300)
    @Test
    public void updateTask() {
        
        runTest(() -> {
            
            log.info("testing {}", URI_CREATE_OR_UPDATE_TASK);
            
            final Mono<TaskInfo> monoTask = Mono.just(StmtProcessor.create(TaskInfo.class, taskInfo -> {
                taskInfo.setActualDate(NLS.localDateTime2long(LocalDateTime.now()));
                taskInfo.setBotId(createdBotId);
                taskInfo.setAccountId(createdAccountId);
                taskInfo.setError(TestFuncs.generateTestString15());
                taskInfo.setParentTaskId(createdTaskId);
                taskInfo.setRequestPaginationId(TestFuncs.generateTestString15());
                taskInfo.setTaskFinishDate(NLS.localDateTime2long(LocalDateTime.now().plusDays(1)));
                taskInfo.setTaskResultId(TR_RESULT1);
                taskInfo.setTaskStartDate(NLS.localDateTime2long(LocalDateTime.now()));
                taskInfo.setTaskTypeId(TT_TYPE1);
            }));
            
            final CreatedTask createdTask
                    = webTestClient
                            .post()
                            .uri(uriBuilder
                                    -> uriBuilder
                                    .path(URI_CREATE_OR_UPDATE_TASK)
                                    .build())
                            .accept(APPLICATION_JSON)
                            .body(monoTask, TaskInfo.class)
                            .exchange()
                            .expectStatus()
                            .isOk()
                            .expectBody(CreatedTask.class)
                            .returnResult()
                            .getResponseBody();
            
            log.info("{}: test result is '{}' ", URI_CREATE_OR_UPDATE_TASK, createdTask);
            
            createdTaskId = createdTask.getTaskId();
            
        });
    }

    @Order(400)
    @Test
    @RepeatedTest(5)
    public void createFutureAccount() {
        
        runTest(() -> {
            
            log.info("testing {}", URI_CREATE_OR_UPDATE_TASK);
            
            final Mono<TaskInfo> monoTask = Mono.just(StmtProcessor.create(TaskInfo.class, taskInfo -> {
                taskInfo.setActualDate(NLS.localDateTime2long(LocalDateTime.now()));
                taskInfo.setBotId(createdBotId);
                taskInfo.setInstaId(TestFuncs.generateUnsignedLong());
                taskInfo.setError(TestFuncs.generateTestString15());
                taskInfo.setParentTaskId(null);
                taskInfo.setRequestPaginationId(TestFuncs.generateTestString15());
                taskInfo.setTaskFinishDate(NLS.localDateTime2long(LocalDateTime.now().plusDays(1)));
                taskInfo.setTaskResultId(TR_RESULT1);
                taskInfo.setTaskStartDate(NLS.localDateTime2long(LocalDateTime.now()));
                taskInfo.setTaskTypeId(TT_TYPE1);
            }));
            
            final CreatedTask createdTask
                    = webTestClient
                            .post()
                            .uri(uriBuilder
                                    -> uriBuilder
                                    .path(URI_CREATE_OR_UPDATE_TASK)
                                    .build())
                            .accept(APPLICATION_JSON)
                            .body(monoTask, TaskInfo.class)
                            .exchange()
                            .expectStatus()
                            .isOk()
                            .expectBody(CreatedTask.class)
                            .returnResult()
                            .getResponseBody();
            
            log.info("{}: test result is '{}' ", URI_CREATE_OR_UPDATE_TASK, createdTask);
            
            createdTaskId = createdTask.getTaskId();
            
        });
    }
}
