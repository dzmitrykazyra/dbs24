package org.dbs24.tik.mobile.test.functional;

import com.github.javafaker.Faker;
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
import org.dbs24.tik.mobile.entity.dto.order.OrderDto;
import org.dbs24.tik.mobile.entity.dto.order.OrderIdDto;
import org.dbs24.tik.mobile.entity.dto.payment.UserDepositDto;
import org.dbs24.tik.mobile.entity.dto.payment.UserDepositIncreaseDto;
import org.dbs24.tik.mobile.entity.dto.tiktok.TiktokPostIdentifierDto;
import org.dbs24.tik.mobile.entity.dto.tiktok.TiktokPostIdentifierListDto;
import org.dbs24.tik.mobile.entity.dto.tiktok.viewer.TiktokUserDto;
import org.dbs24.tik.mobile.entity.dto.user.TokenDto;
import org.dbs24.tik.mobile.entity.dto.user.UserDto;
import org.dbs24.tik.mobile.repo.UserRepo;
import org.dbs24.tik.mobile.service.TiktokAccountService;
import org.dbs24.tik.mobile.service.TokenHolder;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.stream.Collectors;

import static org.dbs24.consts.SysConst.ALL_PACKAGES;
import static org.dbs24.tik.mobile.constant.ApiPath.ORDERS_CREATE;
import static org.springframework.http.MediaType.APPLICATION_JSON;

@Log4j2
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = {TikMobile.class})
@Import({TikMobileConfig.class})
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@EntityScan(basePackages = {ALL_PACKAGES})
@TestPropertySource(properties = "config.security.profile.webfilter.chain=development")
public class StressTest extends AbstractWebTest {

    private final int maxUserPosts = 20;

    private TiktokAccountService tiktokAccountService;
    private UserRepo userRepo;
    private TokenHolder tokenHolder;
    private UserDao userDao;

    @Autowired
    public StressTest(TiktokAccountService tiktokAccountService, UserRepo userRepo, TokenHolder tokenHolder, UserDao userDao) {

        this.userRepo = userRepo;
        this.tiktokAccountService = tiktokAccountService;
        this.tokenHolder = tokenHolder;
        this.userDao = userDao;
    }

    @RepeatedTest(10000)
    @Order(100)
    public void createNewUserNewOrder() {

        runTest(() -> {
            String username = generateUniqueUsername();
            String password = StringFuncs.createRandomString(10);

            String jwt = createUserRecord(username, password);
            Integer userId = tokenHolder.getUserIdByToken(jwt);
            User user = userDao.findById(userId);
            increaseDeposit(jwt);

            List<String> userPostsWebLinks = getUserPostsWebLinks(user.getUsername());
            createOrders(userPostsWebLinks, jwt);
        });
    }

    private String generateUniqueUsername() {

        Faker faker = new Faker();
        String username = faker.name().firstName().concat(String.valueOf((int) (Math.random() * 100))).toLowerCase();
        log.info("Faker generated {} username", username);
        String realUsername = tiktokAccountService.searchTiktokUserByUsername(username).block().getLoginName();
        log.info("Find tiktok api username : {}", realUsername);

        //find equal tiktok user or most user with most common name
        return realUsername;
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
                .body(Mono.just(
                        StmtProcessor.create(
                                UserDto.class,
                                userDto -> {
                                    userDto.setTiktokAccountUsername(userName);
                                    userDto.setRawPassword(password);
                                }
                        )
                ), UserDto.class)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(TokenDto.class)
                .returnResult()
                .getResponseBody()
                .getJwt();
    }

    private void increaseDeposit(String jwt) {

        UserDepositDto responseUserBalanceDto = webTestClient
                .post()
                .uri(uriBuilder
                        -> uriBuilder
                        .path(ApiPath.DEPOSITS_INCREASE)
                        .build())
                .header(TikMobileRouter.AUTHORIZATION_HEADER_NAME, jwt)
                .body(Mono.just(
                        StmtProcessor.create(UserDepositIncreaseDto.class,
                                userDepositIncrease -> {
                                    userDepositIncrease.setAmountDepositsToIncrease(100000);
                                }
                        )
                ), UserDepositIncreaseDto.class)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(UserDepositDto.class)
                .returnResult()
                .getResponseBody();
    }

    private List<String> getUserPostsWebLinks(String username) {

        return tiktokAccountService.searchLatestUserPostsByQuantity(username, maxUserPosts)
                .map(TiktokPostIdentifierListDto::getTiktokPostIdentifierDtoList)
                .toProcessor()
                .block()
                .stream()
                .map(TiktokPostIdentifierDto::getVideoUrl)
                .collect(Collectors.toList());
    }

    private void createOrders(List<String> userPostsWebLinks, String jwt) {

        log.info("USER HAS {} POSTS", userPostsWebLinks.size());
        int ordersQuantity = (int) (Math.random() * maxUserPosts) + 1;

        for (int i = 0; i < ordersQuantity && i < userPostsWebLinks.size(); i++) {

            final Mono<OrderDto> monoOrder = Mono.just(
                    OrderDto.builder()
                            .orderDuration(3)
                            .actionsAmount((int) (Math.random() * 100 + 1))
                            .tiktokUri(userPostsWebLinks.get(i))
                            .orderTypeId(1)
                            .build()
            );

            OrderIdDto responseBody = webTestClient
                    .post()
                    .uri(uriBuilder
                            -> uriBuilder
                            .path(ORDERS_CREATE)
                            .build())
                    .accept(APPLICATION_JSON)
                    .header(TikMobileRouter.AUTHORIZATION_HEADER_NAME, jwt)
                    .body(monoOrder, OrderDto.class)
                    .exchange()
                    .expectStatus()
                    .isOk()
                    .expectBody(OrderIdDto.class)
                    .returnResult()
                    .getResponseBody();

            log.info("RESPONSE BODY{}", responseBody);
        }
    }
}
