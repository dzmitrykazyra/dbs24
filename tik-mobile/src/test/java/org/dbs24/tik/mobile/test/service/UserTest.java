package org.dbs24.tik.mobile.test.service;

import lombok.extern.log4j.Log4j2;
import org.dbs24.application.core.service.funcs.StringFuncs;
import org.dbs24.stmt.StmtProcessor;
import org.dbs24.test.core.AbstractWebTest;
import org.dbs24.tik.mobile.TikMobile;
import org.dbs24.tik.mobile.config.TikMobileConfig;
import org.dbs24.tik.mobile.config.TikMobileRouter;
import org.dbs24.tik.mobile.constant.ApiPath;
import org.dbs24.tik.mobile.dao.UserDao;
import org.dbs24.tik.mobile.entity.domain.User;
import org.dbs24.tik.mobile.entity.dto.user.*;
import org.dbs24.tik.mobile.service.TokenHolder;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import static org.dbs24.consts.SysConst.ALL_PACKAGES;
import static org.springframework.http.MediaType.APPLICATION_JSON;

@Log4j2
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = {TikMobile.class})
@Import({TikMobileConfig.class})
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@EntityScan(basePackages = {ALL_PACKAGES})
public class UserTest extends AbstractWebTest {

    private TokenHolder tokenHolder;
    private UserDao userDao;

    @Autowired
    public UserTest(TokenHolder tokenHolder, UserDao userDao) {

        this.tokenHolder = tokenHolder;
        this.userDao = userDao;
    }

    @Order(100)
    @RepeatedTest(1)
    @Transactional
    public void registrationTest() {

        String username = StringFuncs.createRandomString(10);

        String jwt1 = createUserRecord(username, "password");

        Assertions.assertFalse(jwt1.isBlank());

        Assertions.assertEquals(
                HttpStatus.CONFLICT,
                webTestClient
                        .post()
                        .uri(uriBuilder
                                -> uriBuilder
                                .path(ApiPath.USER_REGISTER)
                                .build())
                        .contentType(APPLICATION_JSON)
                        .accept(APPLICATION_JSON)
                        .body(createUserRequest(username, "somenewpassword"), UserDto.class)
                        .exchange()
                        .expectStatus()
                        .is4xxClientError()
                        .expectBody(TokenDto.class)
                        .returnResult()
                        .getStatus()
        );
    }

    private String createUserRecord(String userName, String password) {

        return webTestClient
                .post()
                .uri(uriBuilder
                        -> uriBuilder
                        .path(ApiPath.USER_REGISTER)
                        .build())
                .contentType(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .body(createUserRequest(userName, password), UserDto.class)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(TokenDto.class)
                .returnResult()
                .getResponseBody()
                .getJwt();
    }

    private Mono<UserDto> createUserRequest(String userName, String password) {

        return Mono.just(
                        StmtProcessor.create(
                                UserDto.class,
                                userDto -> {
                                    userDto.setTiktokAccountUsername(userName);
                                    userDto.setRawPassword(password);
                                }
                        )
                );
    }

    @Order(200)
    @RepeatedTest(1)
    @Transactional
    public void logoutLoginTest() {

        String userName = StringFuncs.createRandomString(11);
        String password = "password";
        String jwt = createUserRecord(userName, "password");
        Assertions.assertFalse(jwt.isBlank());

        Assertions.assertTrue(tokenHolder.isTokenValid(jwt));

        Assertions.assertTrue(
                logout(jwt)
                .getIsVerified());

        Assertions.assertFalse(tokenHolder.isTokenValid(jwt));

        log.info("tryna login with incorrect password");
        Assertions.assertEquals(
                HttpStatus.FORBIDDEN,
                webTestClient
                    .post()
                    .uri(uriBuilder
                            -> uriBuilder
                            .path(ApiPath.USER_LOGIN)
                            .build())
                    .contentType(APPLICATION_JSON)
                    .accept(APPLICATION_JSON)
                    .body(createUserRequest(userName, password.concat("somecharstoinvelidpassword")), UserDto.class)
                    .exchange()
                    .expectStatus()
                    .is4xxClientError()
                    .expectBody(TokenDto.class)
                    .returnResult()
                    .getStatus()
        );

        log.info("tryna login with correct password");

        String reloginJwt =
                login(userName, password)
                .getJwt();

        Assertions.assertTrue(tokenHolder.isTokenValid(reloginJwt));
    }

    private TokenDto login(String username, String password) {

        return loginRequest(username, password)
                .expectStatus()
                .isOk()
                .expectBody(TokenDto.class)
                .returnResult()
                .getResponseBody();
    }

    private WebTestClient.ResponseSpec loginRequest(String username, String password) {

        return webTestClient
                .post()
                .uri(uriBuilder
                        -> uriBuilder
                        .path(ApiPath.USER_LOGIN)
                        .build())
                .contentType(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .body(createUserRequest(username, password), UserDto.class)
                .exchange();
    }

    private UserVerificationDto logout(String jwt) {

        return webTestClient
                .delete()
                .uri(uriBuilder
                        -> uriBuilder
                        .path(ApiPath.USER_LOGOUT)
                        .build())
                .accept(APPLICATION_JSON)
                .header(TikMobileRouter.AUTHORIZATION_HEADER_NAME, jwt)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(UserVerificationDto.class)
                .returnResult()
                .getResponseBody();
    }

    private Mono<TokenDto> createTokenDtoRequest(String token) {

        return Mono.just(TokenDto.of(token));
    }

    @Order(300)
    @RepeatedTest(1)
    @Transactional
    public void userEmailBoundTest() {

        String userName = StringFuncs.createRandomString(12);

        String jwt = createUserRecord(userName, "password");

        Assertions.assertEquals(
                HttpStatus.NO_CONTENT,
                webTestClient
                        .get()
                        .uri(uriBuilder
                                -> uriBuilder
                                .path(ApiPath.USER_IS_EMAIL_BOUNDED)
                                .build())
                        .accept(APPLICATION_JSON)
                        .header(TikMobileRouter.AUTHORIZATION_HEADER_NAME, jwt)
                        .exchange()
                        .expectStatus()
                        .isNoContent()
                        .expectBody(UserVerificationDto.class)
                        .returnResult()
                        .getStatus()

        );

        User userToSetEmail = userDao.findById(tokenHolder.getUserIdByToken(jwt));
        String userEmail = "someemail@gmail.com";
        userToSetEmail.setEmail(userEmail);
        userDao.update(userToSetEmail);

/*        Assertions.assertEquals(
                userEmail,
                webTestClient
                        .get()
                        .uri(uriBuilder
                            -> uriBuilder
                            .path(ApiPath.USER_IS_EMAIL_BOUNDED)
                            .build())
                        .accept(APPLICATION_JSON)
                        .header(TikMobileRouter.AUTHORIZATION_HEADER_NAME, jwt)
                        .exchange()
                        .expectStatus()
                        .isOk()
                        .expectBody(UserEmailDto.class)
                        .returnResult()
                        .getResponseBody()
                        .getUserEmail());*/
    }

    @Order(400)
    @RepeatedTest(1)
    @Transactional
    public void userForgotPasswordTest() {

        String username = "buzova86";
        String password = "password";
        String newPassword = "newpass";

        User user = userDao.findOptionalByUsername(username).orElseThrow();

        UserForgottenPasswordKeysetDto keyset = sendChangePasswordRequest(user.getEmail())
                .expectStatus()
                .isOk()
                .expectBody(UserForgottenPasswordKeysetDto.class)
                .returnResult()
                .getResponseBody();

        TokenDto jwt2 = sendChangePasswordKeyset(newPassword, keyset)
                .expectStatus()
                .isOk()
                .expectBody(TokenDto.class)
                .returnResult()
                .getResponseBody();


        logout(jwt2.getJwt());

        loginRequest(username, password)
                .expectStatus()
                .is4xxClientError();

        loginRequest(username, newPassword)
                .expectStatus()
                .isOk();
    }

    private WebTestClient.ResponseSpec sendChangePasswordRequest(String email) {

        Mono<UserEmailDto> userEmailDtoMono = Mono.just(
                StmtProcessor.create(
                        UserEmailDto.class,
                        userEmailDto -> userEmailDto.setUserEmail(email)
                )
        );

        return webTestClient
                .post()
                .uri(uriBuilder
                        -> uriBuilder
                        .path(ApiPath.USER_FORGOT_PASSWORD)
                        .build())
                .contentType(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .body(userEmailDtoMono, UserEmailDto.class)
                .exchange();
    }

    private WebTestClient.ResponseSpec sendChangePasswordKeyset(String rawNewPassword, UserForgottenPasswordKeysetDto keyset) {

        keyset.setRawNewPassword(rawNewPassword);

        return webTestClient
                .post()
                .uri(uriBuilder
                        -> uriBuilder
                        .path(ApiPath.USER_CHANGE_FORGOTTEN_PASSWORD)
                        .build())
                .contentType(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .body(Mono.just(keyset), UserForgottenPasswordKeysetDto.class)
                .exchange();
    }
}
