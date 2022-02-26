package org.dbs24.tik.mobile.test.service;

import lombok.extern.log4j.Log4j2;
import org.dbs24.stmt.StmtProcessor;
import org.dbs24.test.core.AbstractWebTest;
import org.dbs24.tik.mobile.TikMobile;
import org.dbs24.tik.mobile.config.TikMobileConfig;
import org.dbs24.tik.mobile.config.TikMobileRouter;
import org.dbs24.tik.mobile.constant.RequestQueryParam;
import org.dbs24.tik.mobile.dao.OrderActionTypeDao;
import org.dbs24.tik.mobile.dao.OrderDao;
import org.dbs24.tik.mobile.dao.OrderExecutionProgressDao;
import org.dbs24.tik.mobile.dao.OrderStatusDao;
import org.dbs24.tik.mobile.entity.domain.OrderStatus;
import org.dbs24.tik.mobile.entity.dto.order.details.OrderDetailsDto;
import org.dbs24.tik.mobile.entity.dto.order.details.OrderDetailsDtoList;
import org.dbs24.tik.mobile.entity.dto.order.OrderDto;
import org.dbs24.tik.mobile.entity.dto.order.OrderIdDto;
import org.dbs24.tik.mobile.entity.dto.user.TokenDto;
import org.dbs24.tik.mobile.entity.dto.user.UserDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
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

import static org.dbs24.consts.SysConst.ALL_PACKAGES;
import static org.dbs24.tik.mobile.constant.ApiPath.ORDERS_CREATE;
import static org.dbs24.tik.mobile.constant.ApiPath.STATISTICS_CLEAR_SINGLE_ORDER_HISTORY;
import static org.dbs24.tik.mobile.constant.ApiPath.STATISTICS_GET_ACTIVE_ORDERS_PROGRESSES;
import static org.dbs24.tik.mobile.constant.ApiPath.STATISTICS_GET_SINGLE_ORDER_DETAILS;
import static org.dbs24.tik.mobile.constant.ApiPath.STATISTICS_INVALIDATE_ACTIVE_ORDER;
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
public class OrderStatisticTest extends AbstractWebTest {
    private final String TEST_USER_NAME = "aidarkin";
    private final String TEST_USER_PASSWORD = "TEST";
    private final String TIKTOK_VIDEO_URL = "https://www.tiktok.com/@khaby.lame/video/7009659785171815686?is_from_webapp=v1";

    private final OrderDao orderDao;
    private final OrderExecutionProgressDao orderExecutionProgressDao;
    private final OrderActionTypeDao orderActionTypeDao;
    private final OrderStatusDao orderStatusDao;
    private Integer createdOrderId;

    @Autowired
    public OrderStatisticTest(OrderDao orderDao,
                              OrderExecutionProgressDao orderExecutionProgressDao,
                              OrderActionTypeDao orderActionTypeDao,
                              OrderStatusDao orderStatusDao) {
        this.orderDao = orderDao;
        this.orderExecutionProgressDao = orderExecutionProgressDao;
        this.orderActionTypeDao = orderActionTypeDao;
        this.orderStatusDao = orderStatusDao;
    }

    @BeforeAll
    private void createOrder() {

        final Mono<OrderDto> monoOrder = Mono.just(
                StmtProcessor.create(OrderDto.class,
                        orderDto -> {
                            orderDto.setOrderName("Test order from orderStatisticTest");
                            orderDto.setOrderDuration(3);
                            orderDto.setActionsAmount(150);
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

        log.info("Successfully initialize order with id = " + orderIdDto.getOrderId());
        createdOrderId = orderIdDto.getOrderId();
    }

    private String getJwtToken() {
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


    @Order(300)
    @RepeatedTest(1)
    public void getOrderInfoById() {
        runTest(() -> {

            log.info("testing {}, get order id = {}", STATISTICS_GET_SINGLE_ORDER_DETAILS, createdOrderId);

            final OrderDetailsDto orderDetails = webTestClient
                    .get()
                    .uri(uriBuilder
                            -> uriBuilder
                            .path(STATISTICS_GET_SINGLE_ORDER_DETAILS)
                            .queryParam(RequestQueryParam.QP_ORDER_ID, createdOrderId)
                            .build())
                    .exchange()
                    .expectStatus()
                    .isOk()
                    .expectBody(OrderDetailsDto.class)
                    .returnResult()
                    .getResponseBody();

            log.info("{}: test result is '{}' ", STATISTICS_GET_SINGLE_ORDER_DETAILS, orderDetails);
        });
    }

    @Order(700)
    @RepeatedTest(1)
    @Transactional
    public void getActiveOrders() {
        runTest(() -> {

            log.info("testing {}, get order id = {}", STATISTICS_GET_ACTIVE_ORDERS_PROGRESSES, createdOrderId);

            final OrderDetailsDtoList orderDetailsDtoList = webTestClient
                    .get()
                    .uri(uriBuilder
                            -> uriBuilder
                            .path(STATISTICS_GET_ACTIVE_ORDERS_PROGRESSES)
                            .build())
                    .header(TikMobileRouter.AUTHORIZATION_HEADER_NAME, getJwtToken())
                    .exchange()
                    .expectStatus()
                    .isOk()
                    .expectBody(OrderDetailsDtoList.class)
                    .returnResult()
                    .getResponseBody();

            final OrderDetailsDto createdOrderDetails = OrderDetailsDto.of(
                    orderDao.findOrderById(createdOrderId),
                    orderExecutionProgressDao.findExecutionProgressByOrder(
                            orderDao.findOrderById(createdOrderId)
                    )
            );

            Assertions.assertTrue(orderDetailsDtoList.getOrderDetailsDtoList().contains(createdOrderDetails));
        });
    }

    @Order(800)
    @RepeatedTest(1)
    @Transactional
    public void clearOrderHistory() {

        runTest(() -> {
            log.info("testing {}, get order id = {}", STATISTICS_CLEAR_SINGLE_ORDER_HISTORY, createdOrderId);

            webTestClient
                    .delete()
                    .uri(uriBuilder
                            -> uriBuilder
                            .path(STATISTICS_CLEAR_SINGLE_ORDER_HISTORY)
                            .queryParam(RequestQueryParam.QP_ORDER_ID, createdOrderId)
                            .build())
                    .exchange()
                    .expectStatus()
                    .isOk();

            OrderStatus actualOrderStatus = orderDao.findOrderById(createdOrderId).getOrderStatus();
            Assertions.assertEquals(orderStatusDao.findHistoryClearedOrderStatus(), actualOrderStatus);
        });
    }

    @Order(900)
    @RepeatedTest(1)
    @Transactional
    public void invalidateOrder() {

        runTest(() -> {
            log.info("testing {}, get order id = {}", STATISTICS_INVALIDATE_ACTIVE_ORDER, createdOrderId);

            Integer activeOrderId = getRandomActiveOrderId();

            webTestClient
                    .delete()
                    .uri(uriBuilder
                            -> uriBuilder
                            .path(STATISTICS_INVALIDATE_ACTIVE_ORDER)
                            .queryParam(RequestQueryParam.QP_ORDER_ID, activeOrderId)
                            .build())
                    .exchange()
                    .expectStatus()
                    .isOk();

            OrderStatus actualOrderStatus = orderDao.findOrderById(activeOrderId).getOrderStatus();
            Assertions.assertEquals(orderStatusDao.findInvalidOrderStatus(), actualOrderStatus);
        });
    }

    private Integer getRandomActiveOrderId(){
        return orderDao.findAllActiveOrders().stream().findFirst().get().getOrderId();
    }
}
