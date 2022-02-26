package org.dbs24.tik.assist.test.service;

import lombok.extern.log4j.Log4j2;
import org.dbs24.stmt.StmtProcessor;
import org.dbs24.tik.assist.TikAssist;
import org.dbs24.tik.assist.config.TikAssistConfig;
import org.dbs24.tik.assist.dao.*;
import org.dbs24.tik.assist.entity.domain.OrderAction;
import org.dbs24.tik.assist.entity.domain.User;
import org.dbs24.tik.assist.entity.domain.UserOrder;
import org.dbs24.tik.assist.entity.dto.order.CreateFollowersOrderDto;
import org.dbs24.tik.assist.entity.dto.order.CreateVideoOrderDto;
import org.dbs24.tik.assist.entity.dto.order.CreatedUserOrderDto;
import org.dbs24.tik.assist.entity.dto.statistics.OrderStatisticsDto;
import org.dbs24.tik.assist.repo.UserRepo;
import org.dbs24.tik.assist.service.hierarchy.OrderExecutionProgressService;
import org.dbs24.tik.assist.service.hierarchy.UserOrderService;
import org.dbs24.tik.assist.service.hierarchy.split.OrderToActionSplitter;
import org.dbs24.tik.assist.service.tiktok.resolver.TiktokInteractor;
import org.dbs24.tik.assist.test.AbstractTikAssistTest;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static org.dbs24.consts.SysConst.ALL_PACKAGES;

@Log4j2
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = {TikAssist.class})
@Import({TikAssistConfig.class})
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@EntityScan(basePackages = {ALL_PACKAGES})
public class OrderTests extends AbstractTikAssistTest {

    private UserOrderService userOrderService;
    private OrderToActionSplitter orderToActionSplitter;
    private ReferenceDao referenceDao;
    private UserOrderDao userOrderDao;
    private TiktokAccountDao tiktokAccountDao;
    private UserDao userDao;
    private OrderActionDao orderActionDao;
    private OrderExecutionProgressDao orderExecutionProgressDao;
    private OrderExecutionProgressService orderExecutionProgressService;
    private TiktokInteractor tiktokInteractor;
    private UserRepo userRepo;

    @Autowired
    public OrderTests(UserOrderService userOrderService, OrderToActionSplitter orderToActionSplitter, ReferenceDao referenceDao, UserOrderDao userOrderDao, TiktokAccountDao tiktokAccountDao, UserDao userDao, OrderActionDao orderActionDao, OrderExecutionProgressDao orderExecutionProgressDao, OrderExecutionProgressService orderExecutionProgressService, TiktokInteractor tiktokInteractor, UserRepo userRepo) {
        this.userOrderService = userOrderService;
        this.orderToActionSplitter = orderToActionSplitter;
        this.referenceDao = referenceDao;
        this.userOrderDao = userOrderDao;
        this.tiktokAccountDao = tiktokAccountDao;
        this.userDao = userDao;
        this.orderActionDao = orderActionDao;
        this.orderExecutionProgressDao = orderExecutionProgressDao;
        this.orderExecutionProgressService = orderExecutionProgressService;
        this.tiktokInteractor = tiktokInteractor;
        this.userRepo = userRepo;
    }

/*    @Order(100)
    @RepeatedTest(1)
    @Transactional
    public void actionOfOneOrderQuantityByActionTypes() {

        runTest(() -> {
            int actionsQuantity = 123;

            TiktokAccount tiktokAccount = tiktokAccountDao.save(TiktokAccount.builder()
                    .accountUsername("usernametest")
                    .secUserId("seruseridtest")
                    .user(userDao.findUserById(54)).build());

            UserOrder userOrder = UserOrder.builder()
                    .user(User.builder().userId(1).build())
                    .actionsAmount(actionsQuantity)
                    .orderStatus(referenceDao.findActualOrderStatus())
                    .actualDate(LocalDateTime.now())
                    .beginDate(LocalDateTime.now())
                    .endDate(LocalDateTime.now())
                    .actionType(referenceDao.findActionTypeById(2))
                    .tiktokAccount(tiktokAccount)
                    .build();

            UserOrder savedUserOrder = userOrderDao.saveOrder(userOrder);

            List<OrderAction> orderActions = orderToActionSplitter.split(savedUserOrder);

            Assertions.assertEquals(actionsQuantity, orderActions.size());

            Assertions.assertEquals(orderToActionSplitter.splitAll(List.of(savedUserOrder)).size(), orderActions.size());

            //to perform rollback
            throw new RuntimeException();
        });
    }*/

    private User findRandomUser() {

        return userRepo.findAll().stream().findAny().orElseThrow();
    }

    @Order(200)
    @RepeatedTest(1)
    @Transactional
    public void followersOrderTest() {

        //check requiredFollowersQuantity=actionsQuantity
        //every bot is unique
        runTest(() -> {

            CreatedUserOrderDto createdUserOrderDto = userOrderService.createFollowersOrder(
                    findRandomUser().getUserId(),
                    StmtProcessor.create(
                            CreateFollowersOrderDto.class,
                            createFollowersOrder -> {
                                createFollowersOrder.setTiktokAccountUsername("a4omg");
                                createFollowersOrder.setExecutionDaysQuantity(50);
                                createFollowersOrder.setOrderActionsQuantity(50);
                            })
            );

            UserOrder createdFollowerdOrder = userOrderDao.findOrderById(createdUserOrderDto.getCreatedOrderId());

            List<OrderAction> orderActions = orderActionDao.findByUserOrder(createdFollowerdOrder);

            Assertions.assertEquals(createdFollowerdOrder.getActionsAmount(), orderActions.size());

            long orderActionsUniqueBotsQuantity = orderActions.stream().map(OrderAction::getBot).distinct().count();
            long nullBotsQuantity = orderActions.stream().filter(orderAction -> orderAction.getBot() == null).count();

            // '-1' is cause of null value is unique with distinct operation
            Assertions.assertEquals(nullBotsQuantity + orderActionsUniqueBotsQuantity - 1, orderActions.size());
        });
    }

    @Order(200)
    @RepeatedTest(1)
    @Transactional
    public void viewsOrderTest() {

        //check requiredViewsQuantity=actionsQuantity
        runTest(() -> {

            CreatedUserOrderDto createdViewsOrder = userOrderService.createViewsOrder(
                    findRandomUser().getUserId(),
                    StmtProcessor.create(
                            CreateVideoOrderDto.class,
                            createViewsOrderDto -> {
                                createViewsOrderDto.setVideoLink("https://www.tiktok.com/@khaby.lame/video/7009659785171815686?is_from_webapp=v1");
                                createViewsOrderDto.setExecutionDaysQuantity(50);
                                createViewsOrderDto.setOrderActionsQuantity(50);
                            })
            );

            UserOrder foundViewsOrder = userOrderDao.findOrderById(createdViewsOrder.getCreatedOrderId());

            List<OrderAction> orderActions = orderActionDao.findByUserOrder(foundViewsOrder);

            Assertions.assertEquals(foundViewsOrder.getActionsAmount(), orderActions.size());

/*            Assertions.assertFalse(
                    orderActions.stream().anyMatch(orderAction -> orderAction.getBot() == null)
            );*/
        });
    }

    @Order(300)
    @RepeatedTest(1)
    @Transactional
    public void likesOrderTest() {

        //check for bots uniqueness
        runTest(() -> {

            CreatedUserOrderDto createdLikesOrderDto = userOrderService.createLikesOrder(
                    findRandomUser().getUserId(),
                    StmtProcessor.create(
                            CreateVideoOrderDto.class,
                            createViewsOrderDto -> {
                                createViewsOrderDto.setVideoLink("https://www.tiktok.com/@olgakfur/video/6981010599878626561?is_from_webapp=v1");
                                createViewsOrderDto.setExecutionDaysQuantity(50);
                                createViewsOrderDto.setOrderActionsQuantity(50);
                            })
            );

            UserOrder createdLikesOrder = userOrderDao.findOrderById(createdLikesOrderDto.getCreatedOrderId());

            List<OrderAction> orderActions = orderActionDao.findByUserOrder(createdLikesOrder);

            Assertions.assertEquals(createdLikesOrder.getActionsAmount(), orderActions.size());

            long orderActionsUniqueBotsQuantity = orderActions.stream().map(OrderAction::getBot).distinct().count();
            long nullBotsQuantity = orderActions.stream().filter(orderAction -> orderAction.getBot() == null).count();

            // '-1' is cause of null value is unique with distinct operation
            Assertions.assertEquals(nullBotsQuantity + orderActionsUniqueBotsQuantity - 1, orderActions.size());
        });
    }

    @Order(400)
    @RepeatedTest(1)
    @Transactional
    public void orderExecutionProgressTest() {

        //check for bots uniqueness
        runTest(() -> {

            CreatedUserOrderDto createdLikesOrderDto = userOrderService.createLikesOrder(
                    findRandomUser().getUserId(),
                    StmtProcessor.create(
                            CreateVideoOrderDto.class,
                            createViewsOrderDto -> {
                                createViewsOrderDto.setVideoLink("https://www.tiktok.com/@olgakfur/video/6981010599878626561?is_from_webapp=v1");
                                createViewsOrderDto.setExecutionDaysQuantity(1);
                                createViewsOrderDto.setOrderActionsQuantity(50);
                            })
            );

            UserOrder createdLikesOrder = userOrderDao.findOrderById(createdLikesOrderDto.getCreatedOrderId());

            List<OrderAction> orderActions = orderActionDao.findByUserOrder(createdLikesOrder);

            long orderActionsToFinishQuantity = orderActions.size() / 2;

            List<OrderAction> finishedOrderActions = orderActions.
                    stream()
                    .limit(orderActionsToFinishQuantity)
                    .peek(orderAction -> orderExecutionProgressDao.finishOrderAction(orderAction))
                    .collect(Collectors.toList());


            long finishedOrderActionsQuantity = orderActionDao
                    .findByUserOrder(createdLikesOrder)
                    .stream()
                    .filter(
                            orderAction -> orderAction.getOrderActionResult().getOrderActionResultId()
                                    .equals(referenceDao.findFinishedOrderActionResult().getOrderActionResultId())
                    ).count();

            Assertions.assertEquals(orderActionsToFinishQuantity, finishedOrderActionsQuantity);

            OrderStatisticsDto orderStatistics = OrderStatisticsDto
                    .fromProgressToDto(orderExecutionProgressDao.findProgressByUserOrder(createdLikesOrder));

            Assertions.assertEquals(50, orderStatistics.getProgressPercents());
        });
    }

    @Order(500)
    @RepeatedTest(1)
    @Transactional
    public void clearSingleOrderHistoryTest() {

        runTest(() -> {
            User user = findRandomUser();

            CreatedUserOrderDto createdViewsOrder = userOrderService.createViewsOrder(
                    user.getUserId(),
                    StmtProcessor.create(
                            CreateVideoOrderDto.class,
                            createViewsOrderDto -> {
                                createViewsOrderDto.setVideoLink("https://www.tiktok.com/@khaby.lame/video/7009659785171815686?is_from_webapp=v1");
                                createViewsOrderDto.setExecutionDaysQuantity(1);
                                createViewsOrderDto.setOrderActionsQuantity(50);
                            })
            );

            CreatedUserOrderDto createdLikesOrderDto = userOrderService.createLikesOrder(
                    user.getUserId(),
                    StmtProcessor.create(
                            CreateVideoOrderDto.class,
                            createViewsOrderDto -> {
                                createViewsOrderDto.setVideoLink("https://www.tiktok.com/@olgakfur/video/6981010599878626561?is_from_webapp=v1");
                                createViewsOrderDto.setExecutionDaysQuantity(1);
                                createViewsOrderDto.setOrderActionsQuantity(50);
                            })
            );

            //active orders check
            Integer activeUserOrdersQuantity = userOrderDao.findActiveOrdersByTiktokAccount(tiktokInteractor.getTiktokAccount("olgakfur", user)).size();

            Integer activeToHistoryOrdersQuantity = orderExecutionProgressService
                    .getActiveOrderProgressesByTiktokUsernameAndUserId("olgakfur", user.getUserId())
                    .getOrderStatisticsDtoList()
                    .size();

            log.info("ACTIVE ORDERS SIZE {}", activeToHistoryOrdersQuantity);
            Assertions.assertEquals(activeUserOrdersQuantity, activeToHistoryOrdersQuantity);

            //finished orders check
            userOrderDao.changeOrderStatusToFinished(
                    userOrderDao.findOrderById(
                            createdLikesOrderDto.getCreatedOrderId()
                    )
            );

            Integer newActiveToHistoryOrdersQuantity = orderExecutionProgressService
                    .getActiveOrderProgressesByTiktokUsernameAndUserId("olgakfur", user.getUserId())
                    .getOrderStatisticsDtoList()
                    .size();

            int finishedOrdersSize = orderExecutionProgressService
                    .getOrdersHistoryByTiktokUsernameAndUserId("olgakfur", 0, user.getUserId())
                    .getOrderStatisticsDtoList()
                    .size();

            log.info("ORDER HIST SIZE BEFORE SINGLE ORDER HIST REMOVING {}", finishedOrdersSize);

            Assertions.assertEquals(activeUserOrdersQuantity - 1, newActiveToHistoryOrdersQuantity);
            Assertions.assertTrue(finishedOrdersSize >= 1);

            //cleared history orders
            orderExecutionProgressService.clearOrderHistoryById(createdLikesOrderDto.getCreatedOrderId());

            int orderHistorySize = orderExecutionProgressService.getOrdersHistoryByTiktokUsernameAndUserId("olgakfur", 0, user.getUserId()).getOrderStatisticsDtoList().size();

            log.info("ORDER HIST SIZE AFTER SINGLE ORDER HIST REMOVING {}", orderHistorySize);

            Assertions.assertEquals(finishedOrdersSize - 1, orderHistorySize);
        });
    }
}
