package org.dbs24.tik.mobile.test.service;

import lombok.extern.log4j.Log4j2;
import org.dbs24.stmt.StmtProcessor;
import org.dbs24.test.core.AbstractWebTest;
import org.dbs24.tik.mobile.TikMobile;
import org.dbs24.tik.mobile.config.TikMobileConfig;
import org.dbs24.tik.mobile.config.TikMobileRouter;
import org.dbs24.tik.mobile.dao.OrderDao;
import org.dbs24.tik.mobile.dao.OrderExecutionProgressDao;
import org.dbs24.tik.mobile.entity.domain.OrderExecutionProgress;
import org.dbs24.tik.mobile.entity.dto.action.OrderActionDto;
import org.dbs24.tik.mobile.entity.dto.order.OrderDto;
import org.dbs24.tik.mobile.entity.dto.order.OrderIdDto;
import org.dbs24.tik.mobile.entity.dto.order.actual.ActualOrderDtoList;
import org.dbs24.tik.mobile.entity.dto.user.TokenDto;
import org.dbs24.tik.mobile.entity.dto.user.UserDto;
import org.dbs24.tik.mobile.service.TokenHolder;
import org.junit.jupiter.api.Assertions;
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
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.Objects;

import static org.dbs24.consts.SysConst.ALL_PACKAGES;
import static org.dbs24.tik.mobile.constant.ApiPath.ORDERS_CREATE;
import static org.dbs24.tik.mobile.constant.ApiPath.ORDERS_EXECUTION_COMPLETE;
import static org.dbs24.tik.mobile.constant.ApiPath.ORDERS_GET_TO_EXECUTE;
import static org.dbs24.tik.mobile.constant.ApiPath.ORDERS_SKIP;
import static org.dbs24.tik.mobile.constant.ApiPath.USER_LOGIN;
import static org.springframework.http.MediaType.APPLICATION_JSON;

@Log4j2
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = {TikMobile.class})
@Import({TikMobileConfig.class})
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@EntityScan(basePackages = {ALL_PACKAGES})
@TestPropertySource(properties = "config.security.profile.webfilter.chain=development")
public class OrderTest extends AbstractWebTest {
    private final String TEST_USER_NAME = "TEST";
    private final String TEST_USER_PASSWORD = "TEST";
    private final String TIKTOK_VIDEO_URL = "https://www.tiktok.com/@khaby.lame/video/7009659785171815686?is_from_webapp=v1";

    private final OrderDao orderDao;
    private final OrderExecutionProgressDao orderExecutionProgressDao;
    private final TokenHolder tokenHolder;

    private Integer createdOrderId;

    @Autowired
    public OrderTest(OrderDao orderDao,
                     OrderExecutionProgressDao orderExecutionProgressDao,
                     TokenHolder tokenHolder) {
        this.orderDao = orderDao;
        this.orderExecutionProgressDao = orderExecutionProgressDao;
        this.tokenHolder = tokenHolder;
    }


    String getJwtToken() {
        Mono<UserDto> userDtoMono = Mono.just(StmtProcessor.create(UserDto.class,
                userDto -> {
                    userDto.setTiktokAccountUsername(TEST_USER_NAME);
                    userDto.setRawPassword(TEST_USER_PASSWORD);
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


    @Order(100)
    @RepeatedTest(1)
    public void createOrder() {

        runTest(() -> {

            log.info("testing {}", ORDERS_CREATE);

            final Mono<OrderDto> monoOrder = Mono.just(
                    StmtProcessor.create(OrderDto.class,
                            orderDto -> {
                                orderDto.setOrderName("Test order");
                                orderDto.setOrderDuration(3);
                                orderDto.setActionsAmount(100);
                                orderDto.setTiktokUri(TIKTOK_VIDEO_URL);
                                orderDto.setOrderTypeId(2);

                            }
                    )
            );

            final OrderIdDto orderIdDto = webTestClient
                    .post()
                    .uri(uriBuilder
                            -> uriBuilder
                            .path(ORDERS_CREATE)
                            .build())
                    .accept(APPLICATION_JSON)
                    .header(TikMobileRouter.AUTHORIZATION_HEADER_NAME, getJwtToken())
                    .body(monoOrder, OrderDto.class)
                    .exchange()
                    .expectStatus()
                    .isOk()
                    .expectBody(OrderIdDto.class)
                    .returnResult()
                    .getResponseBody();

            log.info("Successfully created order with id = " + orderIdDto.getOrderId());
            createdOrderId = orderIdDto.getOrderId();
        });
    }

    @Order(400)
    @RepeatedTest(1)
    @Transactional
    public void completeOrderAction() {
        runTest(() -> {
            log.info("testing {}, get order id = {}", ORDERS_EXECUTION_COMPLETE, createdOrderId);

            final Mono<OrderActionDto> orderActionDtoMono = Mono.just(StmtProcessor.create(OrderActionDto.class,
                    orderActionDto -> {
                        orderActionDto.setOrderId(createdOrderId);
                        orderActionDto.setStartDate(LocalDateTime.now().minusMinutes(10));
//                        orderActionDto.setAmountToIncreaseDeposit(100);
                    }));

            webTestClient
                    .post()
                    .uri(uriBuilder
                            -> uriBuilder
                            .path(ORDERS_EXECUTION_COMPLETE)
                            .build())
                    .accept(APPLICATION_JSON)
                    .header(TikMobileRouter.AUTHORIZATION_HEADER_NAME, getJwtToken())
                    .body(orderActionDtoMono, OrderActionDto.class)
                    .exchange()
                    .expectStatus()
                    .isOk()
                    .expectBody(OrderIdDto.class)
                    .returnResult()
                    .getResponseBody();

            final OrderExecutionProgress orderProgress = orderExecutionProgressDao.findExecutionProgressByOrder(orderDao.findOrderById(createdOrderId));
            Assertions.assertEquals(1, orderProgress.getDoneActionsQuantity());
        });
    }

    @Order(500)
    @RepeatedTest(1)
    @Transactional
    public void getActualOrders() {

        runTest(() -> {

            log.info("testing {}", ORDERS_GET_TO_EXECUTE);

            final ActualOrderDtoList actualOrdersDto = webTestClient
                    .get()
                    .uri(uriBuilder
                            -> uriBuilder
                            .path(ORDERS_GET_TO_EXECUTE)
                            .build())
                    .accept(APPLICATION_JSON)
                    .header(TikMobileRouter.AUTHORIZATION_HEADER_NAME, getJwtToken())
                    .exchange()
                    .expectStatus()
                    .isOk()
                    .expectBody(ActualOrderDtoList.class)
                    .returnResult()
                    .getResponseBody();

            log.info("{} test result is : {}", ORDERS_GET_TO_EXECUTE, actualOrdersDto);
        });
    }

    @Order(600)
    @RepeatedTest(1)
    @Transactional
    public void skipOrder() {
        runTest(() -> {

            log.info("testing {}", ORDERS_SKIP);

            Mono<OrderIdDto> orderIdDtoMono = Mono.just(OrderIdDto.of(findRandomActualOrder()));

            OrderIdDto orderIdResponse = webTestClient
                    .post()
                    .uri(uriBuilder
                            -> uriBuilder
                            .path(ORDERS_SKIP)
                            .build())
                    .body(orderIdDtoMono, OrderIdDto.class)
                    .exchange()
                    .expectStatus()
                    .isOk()
                    .expectBody(OrderIdDto.class)
                    .returnResult()
                    .getResponseBody();

            log.info("{} test result is: {}", ORDERS_SKIP, orderIdResponse);
        });

    }

    private org.dbs24.tik.mobile.entity.domain.Order findRandomActualOrder() {
        return orderDao.findAllActiveOrders().stream()
                .filter(order -> !Objects.equals(order.getUser().getId(), tokenHolder.getUserIdByToken(getJwtToken())))
                .findFirst()
                .get();
    }

}
