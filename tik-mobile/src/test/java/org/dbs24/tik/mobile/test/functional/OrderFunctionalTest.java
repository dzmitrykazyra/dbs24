package org.dbs24.tik.mobile.test.functional;

import lombok.extern.log4j.Log4j2;
import org.dbs24.stmt.StmtProcessor;
import org.dbs24.test.core.AbstractWebTest;
import org.dbs24.tik.mobile.TikMobile;
import org.dbs24.tik.mobile.config.TikMobileConfig;
import org.dbs24.tik.mobile.config.TikMobileRouter;
import org.dbs24.tik.mobile.dao.UserDao;
import org.dbs24.tik.mobile.dao.UserDepositDao;
import org.dbs24.tik.mobile.entity.dto.order.OrderDto;
import org.dbs24.tik.mobile.entity.dto.user.TokenDto;
import org.dbs24.tik.mobile.entity.dto.user.UserDto;
import org.dbs24.tik.mobile.service.TokenHolder;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import reactor.core.publisher.Mono;

import static org.dbs24.consts.SysConst.ALL_PACKAGES;
import static org.dbs24.tik.mobile.constant.ApiPath.ORDERS_CREATE;
import static org.dbs24.tik.mobile.constant.ApiPath.USER_LOGIN;
import static org.dbs24.tik.mobile.constant.ApiPath.USER_REGISTER;
import static org.springframework.http.MediaType.APPLICATION_JSON;

@Log4j2
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = {TikMobile.class})
@Import({TikMobileConfig.class})
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@EntityScan(basePackages = {ALL_PACKAGES})
@TestPropertySource(properties = "config.security.profile.webfilter.chain=development")
public class OrderFunctionalTest extends AbstractWebTest {
    private final String TIKTOK_VIDEO_URL = "https://www.tiktok.com/@khaby.lame/video/7009659785171815686?is_from_webapp=v1";
    private final String TIKTOK_USERNAME = "zolotova_vero";
    private final String USER_PASSWORD = "test";


    private final UserDao userDao;
    private final UserDepositDao userDepositDao;
    private final TokenHolder tokenHolder;

    @Autowired
    public OrderFunctionalTest(UserDao userDao,
                               UserDepositDao userDepositDao,
                               TokenHolder tokenHolder) {
        this.userDao = userDao;
        this.userDepositDao = userDepositDao;
        this.tokenHolder = tokenHolder;
    }


    @RepeatedTest(1)
    @Order(100)
    public void createOrderWithZeroBalance_expected4xxError() {
        String jwtToken = registerUser(TIKTOK_USERNAME, USER_PASSWORD);

        Mono<OrderDto> monoOrder = Mono.just(
                StmtProcessor.create(OrderDto.class,
                        orderDto -> {
                            orderDto.setOrderName("test");
                            orderDto.setOrderDuration(3);
                            orderDto.setActionsAmount(100);
                            orderDto.setTiktokUri(TIKTOK_VIDEO_URL);
                            orderDto.setOrderTypeId(4);
                        }
                )
        );


        webTestClient
                .post()
                .uri(uriBuilder
                        -> uriBuilder
                        .path(ORDERS_CREATE)
                        .build())
                .header(TikMobileRouter.AUTHORIZATION_HEADER_NAME, jwtToken)
                .body(monoOrder, OrderDto.class)
                .exchange()
                .expectStatus()
                .is4xxClientError();
    }

    @RepeatedTest(1)
    @Order(200)
    public void createOrderWithUncorrectedLink_expectedBadRequestError() {
        Mono<OrderDto> monoOrder = Mono.just(
                StmtProcessor.create(OrderDto.class,
                        orderDto -> {
                            orderDto.setOrderName("error");
                            orderDto.setOrderDuration(3);
                            orderDto.setActionsAmount(100);
                            orderDto.setTiktokUri("ERROR LINK");
                            orderDto.setOrderTypeId(4);
                        }
                )
        );

        webTestClient
                .post()
                .uri(uriBuilder
                        -> uriBuilder
                        .path(ORDERS_CREATE)
                        .build())
                .header(TikMobileRouter.AUTHORIZATION_HEADER_NAME, getJwtToken(TIKTOK_USERNAME, USER_PASSWORD))
                .body(monoOrder, OrderDto.class)
                .exchange()
                .expectStatus()
                .isBadRequest();
    }

    private String getJwtToken(String username, String password) {
        Mono<UserDto> userDtoMono = Mono.just(StmtProcessor.create(UserDto.class,
                userDto -> {
                    userDto.setTiktokAccountUsername(username);
                    userDto.setRawPassword(password);
                })
        );

        return webTestClient
                .post()
                .uri(uriBuilder
                        -> uriBuilder
                        .path(USER_LOGIN)
                        .build())
                .accept(APPLICATION_JSON)
                .body(userDtoMono, UserDto.class)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(TokenDto.class)
                .returnResult()
                .getResponseBody()
                .getJwt();
    }


    private String registerUser(String username, String password) {
        Mono<UserDto> userDtoMono = Mono.just(
                StmtProcessor.create(UserDto.class,
                        userDto -> {
                            userDto.setTiktokAccountUsername(username);
                            userDto.setRawPassword(password);
                        }
                )
        );

        return webTestClient
                .post()
                .uri(uriBuilder
                        -> uriBuilder
                        .path(USER_REGISTER)
                        .build())
                .body(userDtoMono, UserDto.class)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(TokenDto.class)
                .returnResult()
                .getResponseBody()
                .getJwt();
    }
}