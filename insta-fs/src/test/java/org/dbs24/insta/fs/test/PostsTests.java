/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.insta.fs.test;

import java.time.LocalDateTime;
import lombok.Data;
import lombok.extern.log4j.Log4j2;
import org.dbs24.application.core.locale.NLS;
import org.dbs24.application.core.service.funcs.TestFuncs;
import static org.dbs24.consts.SysConst.ALL_PACKAGES;
import org.dbs24.insta.fs.InstaFacesSearch;
import org.dbs24.insta.fs.config.InstafFsConfig;
import static org.dbs24.insta.fs.consts.IfsConst.References.AccountStatuses.AS_ACTUAL;
import static org.dbs24.insta.fs.consts.IfsConst.References.PostStatuses.*;
import static org.dbs24.insta.fs.consts.IfsConst.References.PostTypes.*;
import static org.dbs24.insta.fs.consts.IfsConst.UriConsts.*;
import org.dbs24.insta.api.rest.AccountInfo;
import org.dbs24.insta.api.rest.CreatedAccount;
import org.dbs24.insta.fs.rest.api.PostInfo;
import org.dbs24.insta.fs.rest.api.CreatedPost;
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
public class PostsTests extends AbstractInstaFsTest {

    private Long createdPostId;
    private Long createdAccountId;

    @Order(100)
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
    
    
    @Order(200)
    @Test
    @RepeatedTest(5)
    //@Transactional(readOnly = true)
    public void createPost() {

        runTest(() -> {

            log.info("testing {}", URI_CREATE_OR_UPDATE_POST);

            final Mono<PostInfo> monoPost = Mono.just(StmtProcessor.create(PostInfo.class, postInfo -> {
                postInfo.setActualDate(NLS.localDateTime2long(LocalDateTime.now()));
                postInfo.setAccountId(createdAccountId);
                postInfo.setMediaId(TestFuncs.generateTestLong());
                postInfo.setPostStatusId(PS_ACTUAL);
                postInfo.setPostTypeId(PT_IMAGE);
                postInfo.setShortCode(TestFuncs.generateTestString15());
            }));

            final CreatedPost createdPost
                    = webTestClient
                            .post()
                            .uri(uriBuilder
                                    -> uriBuilder
                                    .path(URI_CREATE_OR_UPDATE_POST)
                                    .build())
                            .accept(APPLICATION_JSON)
                            .body(monoPost, PostInfo.class)
                            .exchange()
                            .expectStatus()
                            .isOk()
                            .expectBody(CreatedPost.class)
                            .returnResult()
                            .getResponseBody();

            log.info("{}: test result is '{}' ", URI_CREATE_OR_UPDATE_POST, createdPost);

            createdPostId = createdPost.getPostId();

        });
    }
    
    @Order(300)
    @Test
    //@Transactional(readOnly = true)
    public void updatePost() {

        runTest(() -> {

            log.info("testing {}", URI_CREATE_OR_UPDATE_POST);

            final Mono<PostInfo> monoPost = Mono.just(StmtProcessor.create(PostInfo.class, postInfo -> {
                postInfo.setPostId(createdPostId);
                postInfo.setActualDate(NLS.localDateTime2long(LocalDateTime.now()));
                postInfo.setAccountId(createdAccountId);
                postInfo.setMediaId(TestFuncs.generateTestLong());
                postInfo.setPostStatusId(PS_CLOSED);
                postInfo.setPostTypeId(PT_IMAGE);
                postInfo.setShortCode(TestFuncs.generateTestString15());
            }));

            final CreatedPost createdPost
                    = webTestClient
                            .post()
                            .uri(uriBuilder
                                    -> uriBuilder
                                    .path(URI_CREATE_OR_UPDATE_POST)
                                    .build())
                            .accept(APPLICATION_JSON)
                            .body(monoPost, PostInfo.class)
                            .exchange()
                            .expectStatus()
                            .isOk()
                            .expectBody(CreatedPost.class)
                            .returnResult()
                            .getResponseBody();

            log.info("{}: test result is '{}' ", URI_CREATE_OR_UPDATE_POST, createdPost);

            createdPostId = createdPost.getPostId();

        });
    }    
}

