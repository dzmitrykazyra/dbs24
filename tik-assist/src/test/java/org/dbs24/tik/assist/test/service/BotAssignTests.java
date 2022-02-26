package org.dbs24.tik.assist.test.service;

import lombok.extern.log4j.Log4j2;
import org.dbs24.stmt.StmtProcessor;
import org.dbs24.tik.assist.TikAssist;
import org.dbs24.tik.assist.config.TikAssistConfig;
import org.dbs24.tik.assist.dao.BotDao;
import org.dbs24.tik.assist.dao.OrderActionDao;
import org.dbs24.tik.assist.dao.ReferenceDao;
import org.dbs24.tik.assist.dao.UserOrderDao;
import org.dbs24.tik.assist.entity.domain.Bot;
import org.dbs24.tik.assist.entity.domain.OrderAction;
import org.dbs24.tik.assist.entity.domain.User;
import org.dbs24.tik.assist.entity.domain.UserOrder;
import org.dbs24.tik.assist.entity.dto.order.CreateFollowersOrderDto;
import org.dbs24.tik.assist.entity.dto.order.CreateVideoOrderDto;
import org.dbs24.tik.assist.entity.dto.order.CreatedUserOrderDto;
import org.dbs24.tik.assist.repo.UserRepo;
import org.dbs24.tik.assist.service.hierarchy.UserOrderService;
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
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.dbs24.consts.SysConst.ALL_PACKAGES;

@Log4j2
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = {TikAssist.class})
@Import({TikAssistConfig.class})
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@EntityScan(basePackages = {ALL_PACKAGES})
public class BotAssignTests extends AbstractTikAssistTest {

    private final OrderActionDao orderActionDao;
    private final UserOrderService userOrderService;
    private final BotDao botDao;
    private final UserOrderDao userOrderDao;
    private final ReferenceDao referenceDao;
    private final UserRepo userRepo;

    @Autowired
    public BotAssignTests(OrderActionDao orderActionDao, UserOrderService userOrderService, BotDao botDao, UserOrderDao userOrderDao, ReferenceDao referenceDao, UserRepo userRepo) {
        this.orderActionDao = orderActionDao;
        this.userOrderService = userOrderService;
        this.botDao = botDao;
        this.userOrderDao = userOrderDao;
        this.referenceDao = referenceDao;
        this.userRepo = userRepo;
    }

    private User findRandomUser() {

        return userRepo.findAll().stream().findAny().orElseThrow();
    }

    @Order(100)
    @RepeatedTest(1)
    @Transactional
    public void nullableBotFieldToFollowOrderAction() {

        runTest(() -> {

            int actionsDiff = 50;
            int validBotsQuantity = botDao.findActiveBots().size();
            int orderActionsQuantity = validBotsQuantity + actionsDiff;

            CreatedUserOrderDto createdUserOrderDto = userOrderService.createFollowersOrder(
                    findRandomUser().getUserId(),
                    StmtProcessor.create(
                            CreateFollowersOrderDto.class,
                            createFollowersOrder -> {
                                createFollowersOrder.setTiktokAccountUsername("buzova86");
                                createFollowersOrder.setOrderActionsQuantity(orderActionsQuantity);
                                createFollowersOrder.setExecutionDaysQuantity(1);
                            }
                    )
            );

            UserOrder createdOrder = userOrderDao.findOrderById(createdUserOrderDto.getCreatedOrderId());

            List<OrderAction> createdOrderActions = orderActionDao.findByUserOrder(createdOrder);

            long waitingForBotActionsCount = createdOrderActions
                    .stream()
                    .filter(
                            orderAction -> (
                                    orderAction.getBot() == null
                                            && orderAction.getOrderActionResult().getOrderActionResultId()
                                            .equals(referenceDao.findWaitingForBotOrderActionResult().getOrderActionResultId()))
                    )
                    .count();

            Assertions.assertTrue(waitingForBotActionsCount >= actionsDiff);
        });
    }

    @Order(200)
    @RepeatedTest(1)
    @Transactional
    public void distinctBotsForSingleAccountFollowers() {

        runTest(() -> {

            int actionsDiff = 50;
            int validBotsQuantity = botDao.findActiveBots().size();
            int orderActionsQuantity = validBotsQuantity + actionsDiff;

            User user = findRandomUser();

            CreatedUserOrderDto firstOrderDto = userOrderService.createFollowersOrder(
                    user.getUserId(),
                    StmtProcessor.create(
                            CreateFollowersOrderDto.class,
                            createFollowersOrder -> {
                                createFollowersOrder.setTiktokAccountUsername("buzova86");
                                createFollowersOrder.setOrderActionsQuantity(orderActionsQuantity);
                                createFollowersOrder.setExecutionDaysQuantity(1);
                            }
                    )
            );

            CreatedUserOrderDto secondOrderDto = userOrderService.createFollowersOrder(
                    user.getUserId(),
                    StmtProcessor.create(
                            CreateFollowersOrderDto.class,
                            createFollowersOrder -> {
                                createFollowersOrder.setTiktokAccountUsername("buzova86");
                                createFollowersOrder.setOrderActionsQuantity(orderActionsQuantity);
                                createFollowersOrder.setExecutionDaysQuantity(1);
                            }
                    )
            );

            UserOrder createdFirstOrder = userOrderDao.findOrderById(firstOrderDto.getCreatedOrderId());
            UserOrder createdSecondOrder = userOrderDao.findOrderById(secondOrderDto.getCreatedOrderId());

            List<OrderAction> createdFirstOrderActions = orderActionDao.findByUserOrder(createdFirstOrder);
            List<OrderAction> createdSecondOrderActions = orderActionDao.findByUserOrder(createdSecondOrder);

            List<Bot> notNullBots = Stream.concat(
                            createdFirstOrderActions.stream(),
                            createdSecondOrderActions.stream()
                    )
                    .map(OrderAction::getBot)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());

            long notNullBotQuantity = notNullBots.size();

            List<Bot> distinctBots = notNullBots.stream().distinct().collect(Collectors.toList());

            long distinctBotQuantity = distinctBots.size();

            Assertions.assertEquals(notNullBotQuantity, distinctBotQuantity);
        });
    }

    @Order(300)
    @RepeatedTest(1)
    @Transactional
    public void nullableBotFieldToLikeOrderAction() {

        runTest(() -> {

            long actionsDiff = 50;
            long validBotsQuantity = botDao.findActiveBots().size();
            long orderActionsQuantity = validBotsQuantity + actionsDiff;

            User user = findRandomUser();

            CreatedUserOrderDto createdUserOrderDto = userOrderService.createLikesOrder(
                    user.getUserId(),
                    StmtProcessor.create(
                            CreateVideoOrderDto.class,
                            createLikesOrderDto -> {
                                createLikesOrderDto.setVideoLink("https://www.tiktok.com/@stremobzor/video/7007472618441936129?is_from_webapp=v1&lang=en");
                                createLikesOrderDto.setOrderActionsQuantity((int)orderActionsQuantity);
                                createLikesOrderDto.setExecutionDaysQuantity(1);
                            }
                    )
            );

            UserOrder createdOrder = userOrderDao.findOrderById(createdUserOrderDto.getCreatedOrderId());

            List<OrderAction> createdOrderActions = orderActionDao.findByUserOrder(createdOrder);

            long notNullOrderActionsCount = createdOrderActions.stream().map(OrderAction::getBot).filter(Objects::nonNull).count();

            Assertions.assertEquals(((long) createdOrderActions.size() - actionsDiff), notNullOrderActionsCount);
        });
    }

    @Order(400)
    @RepeatedTest(1)
    @Transactional
    public void nullableBotFieldToViewOrderAction() {

        runTest(() -> {

            long actionsDiff = 50;
            long validBotsQuantity = botDao.findActiveBots().size();
            long orderActionsQuantity = validBotsQuantity + actionsDiff;

            User user = findRandomUser();

            CreatedUserOrderDto createdUserOrderDto = userOrderService.createViewsOrder(
                    user.getUserId(),
                    StmtProcessor.create(
                            CreateVideoOrderDto.class,
                            createViewsOrderDto -> {
                                createViewsOrderDto.setVideoLink("https://www.tiktok.com/@stremobzor/video/7007472618441936129?is_from_webapp=v1&lang=en");
                                createViewsOrderDto.setOrderActionsQuantity((int)orderActionsQuantity);
                                createViewsOrderDto.setExecutionDaysQuantity(1);
                            }
                    )
            );

            UserOrder createdOrder = userOrderDao.findOrderById(createdUserOrderDto.getCreatedOrderId());

            List<OrderAction> createdOrderActions = orderActionDao.findByUserOrder(createdOrder);

            long notNullOrderActionsCount = createdOrderActions.stream().map(OrderAction::getBot).filter(Objects::nonNull).count();

            Assertions.assertEquals(notNullOrderActionsCount, createdOrderActions.size());
        });
    }

    @Order(500)
    @RepeatedTest(1)
    @Transactional
    public void distinctBotsForSingleVideoLikes() {

        runTest(() -> {

            int actionsDiff = 50;
            int validBotsQuantity = botDao.findActiveBots().size();
            int orderActionsQuantity = validBotsQuantity + actionsDiff;

            User user = findRandomUser();

            CreatedUserOrderDto createdFirstUserOrderDto = userOrderService.createLikesOrder(
                    user.getUserId(),
                    StmtProcessor.create(
                            CreateVideoOrderDto.class,
                            createLikesOrderDto -> {
                                createLikesOrderDto.setVideoLink("https://www.tiktok.com/@stremobzor/video/7007472618441936129?is_from_webapp=v1&lang=en");
                                createLikesOrderDto.setOrderActionsQuantity(orderActionsQuantity);
                                createLikesOrderDto.setExecutionDaysQuantity(1);
                            }
                    )
            );

            CreatedUserOrderDto createdSecondUserOrderDto = userOrderService.createLikesOrder(
                    user.getUserId(),
                    StmtProcessor.create(
                            CreateVideoOrderDto.class,
                            createLikesOrderDto -> {
                                createLikesOrderDto.setVideoLink("https://www.tiktok.com/@stremobzor/video/7007472618441936129?is_from_webapp=v1&lang=en");
                                createLikesOrderDto.setOrderActionsQuantity(orderActionsQuantity);
                                createLikesOrderDto.setExecutionDaysQuantity(1);
                            }
                    )
            );

            UserOrder createdFirstOrder = userOrderDao.findOrderById(createdFirstUserOrderDto.getCreatedOrderId());
            UserOrder createdSecondOrder = userOrderDao.findOrderById(createdSecondUserOrderDto.getCreatedOrderId());

            List<OrderAction> createdFirstOrderActions = orderActionDao.findByUserOrder(createdFirstOrder);
            List<OrderAction> createdSecondOrderActions = orderActionDao.findByUserOrder(createdSecondOrder);

            List<Bot> notNullBots = Stream.concat(
                            createdFirstOrderActions.stream(),
                            createdSecondOrderActions.stream()
                    )
                    .map(OrderAction::getBot)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());

            long notNullBotQuantity = notNullBots.size();

            List<Bot> distinctBots = notNullBots.stream().distinct().collect(Collectors.toList());

            long distinctBotQuantity = distinctBots.size();

            Assertions.assertEquals(notNullBotQuantity, distinctBotQuantity);
        });
    }

    @Order(600)
    @RepeatedTest(1)
    @Transactional
    public void coincideBotsForSingleVideoViews() {

        runTest(() -> {

            int actionsDiff = 50;
            int validBotsQuantity = botDao.findActiveBots().size();
            int orderActionsQuantity = validBotsQuantity + actionsDiff;

            User user = findRandomUser();

            CreatedUserOrderDto createdFirstUserOrderDto = userOrderService.createViewsOrder(
                    user.getUserId(),
                    StmtProcessor.create(
                            CreateVideoOrderDto.class,
                            createViewsOrderDto -> {
                                createViewsOrderDto.setVideoLink("https://www.tiktok.com/@stremobzor/video/7007472618441936129?is_from_webapp=v1&lang=en");
                                createViewsOrderDto.setOrderActionsQuantity((int)orderActionsQuantity);
                                createViewsOrderDto.setExecutionDaysQuantity(1);
                            }
                    )
            );

            CreatedUserOrderDto createdSecondUserOrderDto = userOrderService.createViewsOrder(
                    user.getUserId(),
                    StmtProcessor.create(
                            CreateVideoOrderDto.class,
                            createViewsOrderDto -> {
                                createViewsOrderDto.setVideoLink("https://www.tiktok.com/@stremobzor/video/7007472618441936129?is_from_webapp=v1&lang=en");
                                createViewsOrderDto.setOrderActionsQuantity((int)orderActionsQuantity);
                                createViewsOrderDto.setExecutionDaysQuantity(1);
                            }
                    )
            );

            UserOrder createdFirstOrder = userOrderDao.findOrderById(createdFirstUserOrderDto.getCreatedOrderId());
            UserOrder createdSecondOrder = userOrderDao.findOrderById(createdSecondUserOrderDto.getCreatedOrderId());

            List<OrderAction> createdFirstOrderActions = orderActionDao.findByUserOrder(createdFirstOrder);
            List<OrderAction> createdSecondOrderActions = orderActionDao.findByUserOrder(createdSecondOrder);

            List<Bot> notNullBots = Stream.concat(
                            createdFirstOrderActions.stream(),
                            createdSecondOrderActions.stream()
                    )
                    .map(OrderAction::getBot)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());

            long notNullBotQuantity = notNullBots.size();

            Assertions.assertEquals(notNullBotQuantity, createdFirstOrderActions.size() + createdSecondOrderActions.size());
        });
    }
}
