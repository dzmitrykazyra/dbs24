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
import static org.dbs24.insta.tmp.consts.IfsConst.References.AccountStatuses.*;
import static org.dbs24.insta.tmp.consts.IfsConst.References.PostStatuses.*;
import static org.dbs24.insta.tmp.consts.IfsConst.References.PostTypes.*;
import static org.dbs24.insta.tmp.consts.IfsConst.References.SourceStatuses.*;
import static org.dbs24.insta.tmp.consts.IfsConst.UriConsts.*;
import org.dbs24.insta.tmp.rest.api.AccountInfo;
import org.dbs24.insta.tmp.rest.api.CreatedAccount;
import org.dbs24.insta.tmp.rest.api.CreatedPost;
import org.dbs24.insta.tmp.rest.api.CreatedSource;
import org.dbs24.insta.tmp.rest.api.CreatedFace;
import org.dbs24.insta.tmp.rest.api.PostInfo;
import org.dbs24.insta.tmp.rest.api.SourceInfo;
import org.dbs24.insta.tmp.rest.api.FaceInfo;
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
public class FacesTests extends AbstractInstaFsTest {

    private Long createdPostId;
    private Long createdAccountId;
    private Long createdSourceId;
    private Long createdFaceId;    

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
    @RepeatedTest(5)
    //@Transactional(readOnly = true)
    public void createSource() {

        runTest(() -> {

            log.info("testing {}", URI_CREATE_OR_UPDATE_SOURCE);

            final Mono<SourceInfo> monoSource = Mono.just(StmtProcessor.create(SourceInfo.class, sourceInfo -> {
                sourceInfo.setPostId(createdPostId);
                sourceInfo.setActualDate(NLS.localDateTime2long(LocalDateTime.now()));
                sourceInfo.setMainFaceBox(TestFuncs.generate1000Bytes());
                sourceInfo.setSourceHash(TestFuncs.generateTestString15());
                sourceInfo.setSourceStatusId(SS_ACTUAL);
                sourceInfo.setSourceUrl(TestFuncs.generateTestString15());
                
            }));

            final CreatedSource createdSource
                    = webTestClient
                            .post()
                            .uri(uriBuilder
                                    -> uriBuilder
                                    .path(URI_CREATE_OR_UPDATE_SOURCE)
                                    .build())
                            .accept(APPLICATION_JSON)
                            .body(monoSource, SourceInfo.class)
                            .exchange()
                            .expectStatus()
                            .isOk()
                            .expectBody(CreatedSource.class)
                            .returnResult()
                            .getResponseBody();

            log.info("{}: test result is '{}' ", URI_CREATE_OR_UPDATE_SOURCE, createdSource);

            createdSourceId = createdSource.getSourceId();

        });
    }    

    @Order(400)
    @Test
    @RepeatedTest(5)
    //@Transactional(readOnly = true)
    public void createFace() {

        runTest(() -> {

            log.info("testing {}", URI_CREATE_OR_UPDATE_FACE);

            final Mono<FaceInfo> monoFace = Mono.just(StmtProcessor.create(FaceInfo.class, faceInfo -> {
                faceInfo.setSourceId(createdSourceId);
                faceInfo.setActualDate(NLS.localDateTime2long(LocalDateTime.now()));
                faceInfo.setFaceVector(TestFuncs.generate1000Bytes());
                faceInfo.setFaceBox(TestFuncs.generate10Bytes());
            }));

            final CreatedFace createdFace
                    = webTestClient
                            .post()
                            .uri(uriBuilder
                                    -> uriBuilder
                                    .path(URI_CREATE_OR_UPDATE_FACE)
                                    .build())
                            .accept(APPLICATION_JSON)
                            .body(monoFace, FaceInfo.class)
                            .exchange()
                            .expectStatus()
                            .isOk()
                            .expectBody(CreatedFace.class)
                            .returnResult()
                            .getResponseBody();

            log.info("{}: test result is '{}' ", URI_CREATE_OR_UPDATE_FACE, createdFace);

            createdFaceId = createdFace.getFaceId();

        });
    }
    
    @Order(500)
    @Test
    //@Transactional(readOnly = true)
    public void updateFace() {

        runTest(() -> {

            log.info("testing {}", URI_CREATE_OR_UPDATE_FACE);

            final Mono<FaceInfo> monoFace = Mono.just(StmtProcessor.create(FaceInfo.class, faceInfo -> {
                faceInfo.setSourceFaceId(createdFaceId);
                faceInfo.setSourceId(createdSourceId);
                faceInfo.setActualDate(NLS.localDateTime2long(LocalDateTime.now()));
                faceInfo.setFaceVector(TestFuncs.generate1000Bytes());
                faceInfo.setFaceBox(TestFuncs.generate10Bytes());                
            }));

            final CreatedFace createdFace
                    = webTestClient
                            .post()
                            .uri(uriBuilder
                                    -> uriBuilder
                                    .path(URI_CREATE_OR_UPDATE_FACE)
                                    .build())
                            .accept(APPLICATION_JSON)
                            .body(monoFace, FaceInfo.class)
                            .exchange()
                            .expectStatus()
                            .isOk()
                            .expectBody(CreatedFace.class)
                            .returnResult()
                            .getResponseBody();

            log.info("{}: test result is '{}' ", URI_CREATE_OR_UPDATE_FACE, createdFace);

            createdFaceId = createdFace.getFaceId();

        });
    }    
}

