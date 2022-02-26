package org.dbs24.tik.assist.service.hierarchy.split;

import lombok.extern.log4j.Log4j2;
import org.dbs24.stmt.StmtProcessor;
import org.dbs24.tik.assist.constant.reference.ActionTypeDefine;
import org.dbs24.tik.assist.dao.ReferenceDao;
import org.dbs24.tik.assist.entity.domain.Bot;
import org.dbs24.tik.assist.entity.domain.OrderAction;
import org.dbs24.tik.assist.entity.domain.UserOrder;
import org.dbs24.tik.assist.service.bot.BotChooseResolver;
import org.dbs24.tik.assist.service.hierarchy.OrderActionService;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Log4j2
@Component
public class OrderToActionSplitter {

    private final BotChooseResolver botChooseResolver;
    private final OrderActionService orderActionService;

    private final ReferenceDao referenceDao;

    OrderToActionSplitter(BotChooseResolver botChooseResolver, OrderActionService orderActionService, ReferenceDao referenceDao) {

        this.botChooseResolver = botChooseResolver;
        this.orderActionService = orderActionService;
        this.referenceDao = referenceDao;
    }

    public List<OrderAction> splitAll(List<UserOrder> userOrders) {

        List<OrderAction> orderActionsToSave = userOrders
                .stream()
                .map(this::getOrderActionsFromOrder)
                .flatMap(List::stream)
                .collect(Collectors.toList());

        return orderActionService.createOrderActions(orderActionsToSave);
    }

    /**
     * Method process:
     *      - find available bots list for user order
     *      - create order action objects to save list
     *      - check if there are all action tasks have not-null bot (if true set these actions specific status)
     *      - save split order action list and save One-To-One matching 0% progress
     */
    public List<OrderAction> split(UserOrder userOrder) {

        return orderActionService.createOrderActions(getOrderActionsFromOrder(userOrder));
    }

    private List<OrderAction> getOrderActionsFromOrder(UserOrder userOrder) {

        List<Bot> botsFound = botChooseResolver.getBotsAvailableForOrder(userOrder);
        List<OrderAction> orderActions = splitOrderAndAssignBots(userOrder, botsFound);

        return assignBotDependingOnActionType(userOrder, orderActions, botsFound);
    }

    /**
     * One bot can follow one account once and like one video once,
     * this method assigns bots if available bots exist to OrderAction,
     * else creates null-bot OrderAction objects waiting for bot assign
     */
    private List<OrderAction> assignBotDependingOnActionType(UserOrder userOrder,
                                                             List<OrderAction> orderActions,
                                                             List<Bot> availableBots) {
        StmtProcessor.ifTrue(
                availableBots.size() < userOrder.getActionsAmount(),
                () -> {
                    int actionsToCreateQuantity = userOrder.getActionsAmount() - availableBots.size();

                    if (userOrder.getActionType().getActionTypeId().equals(ActionTypeDefine.AT_GET_FOLLOWERS.getId())
                            || userOrder.getActionType().getActionTypeId().equals(ActionTypeDefine.AT_GET_LIKES.getId())) {

                        orderActions.addAll(createWaitingForBotOrderActions(userOrder, actionsToCreateQuantity));
                    } else {

                        orderActions.addAll(createDuplicateBotsOrderActions(userOrder, actionsToCreateQuantity, availableBots));
                    }
                }
        );

        return orderActions;
    }

    private List<OrderAction> createWaitingForBotOrderActions(UserOrder userOrder, int quantity) {

        return Stream
                .generate(
                        () -> OrderAction.builder()
                                .orderActionResult(referenceDao.findWaitingForBotOrderActionResult())
                                .userOrder(userOrder)
                                .build()
                )
                .limit(quantity)
                .collect(Collectors.toList());
    }

    /**
     * Create OrderAction objects can use same bot for action execute (for example one bot can comment one video few times)
     */
    private List<OrderAction> createDuplicateBotsOrderActions(UserOrder userOrder, int quantity, List<Bot> bots) {

        return Stream
                .generate(
                        () ->  bots.stream()
                                .map(
                                        bot -> OrderAction.builder()
                                                .orderActionResult(referenceDao.findCreatedOrderActionResult())
                                                .userOrder(userOrder)
                                                .bot(bot)
                                                .build()
                                )
                                .collect(Collectors.toList())
                )
                .flatMap(List::stream)
                .limit(quantity)
                .collect(Collectors.toList());
    }

    private List<OrderAction> splitOrderAndAssignBots(UserOrder userOrder, List<Bot> botsToAssign) {

        return botsToAssign
                .stream()
                .map(
                        bot -> OrderAction.builder()
                                .orderActionResult(referenceDao.findCreatedOrderActionResult())
                                .userOrder(userOrder)
                                .bot(bot)
                                .build()
                )
                .collect(Collectors.toList());
    }
}
