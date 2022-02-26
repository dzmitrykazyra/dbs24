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
import org.dbs24.stmt.StmtProcessor;
import org.dbs24.insta.fs.InstaFacesSearch;
import org.dbs24.insta.fs.config.InstafFsConfig;
import static org.dbs24.insta.fs.consts.IfsConst.UriConsts.*;
import static org.dbs24.insta.fs.consts.IfsConst.RestQueryParams.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import reactor.core.publisher.Mono;
import org.dbs24.insta.api.rest.AccountInfo;
import org.dbs24.insta.fs.rest.api.InstaAccountInfo;
import org.dbs24.insta.api.rest.CreatedAccount;
import static org.dbs24.insta.fs.consts.IfsConst.References.AccountStatuses.*;

@Data
@Log4j2
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = {InstaFacesSearch.class})
@Import({InstafFsConfig.class})
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@EntityScan(basePackages = {ALL_PACKAGES})
public class AccountsTests extends AbstractInstaFsTest {

    private Long createdAccountId;

    @Order(100)
    @Test
    @RepeatedTest(5)
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
    //@Transactional(readOnly = true)
    public void updateAccount() {

        runTest(() -> {

            log.info("testing {}", URI_CREATE_OR_UPDATE_ACCOUNT);

            final Mono<AccountInfo> monoAccount = Mono.just(StmtProcessor.create(AccountInfo.class, accountInfo -> {
                accountInfo.setAccountId(createdAccountId);
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

    @Order(300)
    @Test
    public void validateInstaAccount() {
        runTest(() -> {

            log.info("testing {}", URI_VALIDATE_ACCOUNT);

            final InstaAccountInfo instaAccountInfo = webTestClient
                    .get()
                    .uri(uriBuilder
                            -> uriBuilder
                            .path(URI_VALIDATE_ACCOUNT)
                            .queryParam(QP_INSTA_ID, createdAccountId)
                            .build())
                    .accept(APPLICATION_JSON)
                    .exchange()
                    .expectStatus()
                    .isOk()
                    .expectBody(InstaAccountInfo.class)
                    .returnResult()
                    .getResponseBody();

            log.info("receive instaAccountInfo = {} ", instaAccountInfo);

        });
    }
}
