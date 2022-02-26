package org.dbs24.tik.assist.service.hierarchy.split;

import lombok.extern.log4j.Log4j2;
import org.dbs24.tik.assist.constant.reference.ActionTypeDefine;
import org.dbs24.tik.assist.dao.ReferenceDao;
import org.dbs24.tik.assist.entity.domain.ActionType;
import org.dbs24.tik.assist.entity.domain.UserOrder;
import org.dbs24.tik.assist.entity.domain.UserPlan;
import org.dbs24.tik.assist.entity.dto.tiktok.TiktokPostIdentifierDto;
import org.dbs24.tik.assist.service.hierarchy.UserOrderService;
import org.dbs24.tik.assist.service.tiktok.resolver.TiktokInteractor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@Log4j2
@Component
public class PlanToOrderSplitter {

    /** Latest posts on required account quantity to check and make activities */
    @Value("${constraint.subscription.account.target-posts-quantity}")
    private int targetPostsQuantity;

    private final UserOrderService userOrderService;
    private final TiktokInteractor tiktokInteractor;

    private final ReferenceDao referenceDao;

    public PlanToOrderSplitter(UserOrderService userOrderService, TiktokInteractor tiktokInteractor, ReferenceDao referenceDao) {

        this.userOrderService = userOrderService;
        this.tiktokInteractor = tiktokInteractor;
        this.referenceDao = referenceDao;
    }

    public List<UserOrder> split(UserPlan userPlan) {

        List<UserOrder> userOrders = createDisparateActionTypeOrders(userPlan, tiktokInteractor.searchLatestUserPosts(
                targetPostsQuantity,
                userPlan.getTiktokAccount()
        ));

        return userOrderService.createUserOrders(userOrders);
    }

    /**
     * Method allows splitting user plans to many orders
     * (for example, make like action to latest 20 required posts(each post requires new order)
     * and make followers action to the same account (only one action))
     * @return plan splitted by required orders
     */
    public List<UserOrder> splitAll(List<UserPlan> userPlans) {

        List<UserOrder> ordersFromPlanToSave = userPlans
                .stream()
                .map(userPlan ->
                        createDisparateActionTypeOrders(
                                userPlan,
                                tiktokInteractor.searchLatestUserPosts(
                                        targetPostsQuantity,
                                        userPlan.getTiktokAccount()
                        ))
                )
                .flatMap(List::stream)
                .collect(Collectors.toList());

        return userOrderService.createUserOrders(ordersFromPlanToSave);
    }

    /**
     * Method allows creating map consisting of entries:
     *      key - enumerated action name
     *      value - getter for amount of required actions(likes, views amount) by user plan
     */
    private Map<ActionTypeDefine, Supplier<Integer>> createActionToAmountMap(UserPlan userPlan) {

        Map<ActionTypeDefine, Supplier<Integer>> actionToAmount = new HashMap<>();

        actionToAmount.put(ActionTypeDefine.AT_GET_LIKES, userPlan::getLikesAmount);
        actionToAmount.put(ActionTypeDefine.AT_GET_VIEWS, userPlan::getViewsAmount);
        actionToAmount.put(ActionTypeDefine.AT_GET_COMMENTS, userPlan::getCommentsAmount);
        actionToAmount.put(ActionTypeDefine.AT_GET_REPOSTS, userPlan::getRepostsAmount);
        actionToAmount.put(ActionTypeDefine.AT_GET_FOLLOWERS, userPlan::getFollowersAmount);

        return actionToAmount;
    }

    private List<UserOrder> createDisparateActionTypeOrders(UserPlan userPlan, List<TiktokPostIdentifierDto> latestAccountPosts) {

        Map<ActionTypeDefine, Supplier<Integer>> actionToAmount = createActionToAmountMap(userPlan);

        return Arrays.stream(ActionTypeDefine.values())
                .map(actionType -> getOrderFromPlanByActionType(
                        userPlan,
                        actionType,
                        latestAccountPosts,
                        actionToAmount))
                .flatMap(List::stream)
                .collect(Collectors.toList());
    }

    /**
     * Method allows split plan part with specific action type into N orders
     * (N = target posts quantity or N = 1 in 'get followers' case)
     * @param actionType specific user plan action
     * @param accountPosts
     * @param actionToAmount
     * @return
     */
    private List<UserOrder> getOrderFromPlanByActionType(
            UserPlan userPlan,
            ActionTypeDefine actionType,
            List<TiktokPostIdentifierDto> accountPosts,
            Map<ActionTypeDefine, Supplier<Integer>> actionToAmount
    ) {
        ActionType orderActionType = referenceDao.findActionTypeByName(actionType.getActionTypeValue());
        List<UserOrder> userOrdersBySingleAction;
        Integer actionsAmount = actionToAmount.get(actionType).get();

        if (Integer.valueOf(0).equals(actionsAmount)) {

            userOrdersBySingleAction = List.of();
        } else if (actionType.equals(ActionTypeDefine.AT_GET_FOLLOWERS)) {

            userOrdersBySingleAction = List.of(
                    setOrderToWholeAccount(
                            userPlan,
                            orderActionType,
                            actionsAmount
                    )
            );
        } else {

            userOrdersBySingleAction = setSpecificOrderToEachPost(
                    userPlan,
                    accountPosts,
                    orderActionType,
                    actionsAmount / accountPosts.size()
            );
        }

        return userOrdersBySingleAction;
    }

    private List<UserOrder> setSpecificOrderToEachPost(UserPlan userPlan, List<TiktokPostIdentifierDto> accountPosts, ActionType actionType, int actionsAmount) {

        return accountPosts.stream().map(
                accountPost -> orderTemplateBuilder(userPlan, actionType, actionsAmount)
                        .tiktokUri(accountPost.getTiktokUri())
                        .awemeId(accountPost.getAwemeId())
                        .cid(accountPost.getCid())
                        .build()
        ).collect(Collectors.toList());
    }

    private UserOrder setOrderToWholeAccount(UserPlan userPlan, ActionType actionType, int actionsAmount) {

        return orderTemplateBuilder(userPlan, actionType, actionsAmount)
                .tiktokAccount(userPlan.getTiktokAccount())
                .build();
    }

    /**
     * Method allows getting UserOrder builder with filled fields which are common to different action types
     * @return UserOrder builder to fill null fields, which are required for specific method
     */
    private UserOrder.UserOrderBuilder orderTemplateBuilder(UserPlan userPlan, ActionType actionType, int actionsAmount) {

        return UserOrder.builder()
                .actionsAmount(actionsAmount)
                .actionType(actionType)
                .userPlan(userPlan)
                .orderStatus(referenceDao.findActualOrderStatus())
                .actualDate(userPlan.getActualDate())
                .user(userPlan.getUser())
                .beginDate(userPlan.getBeginDate())
                .endDate(userPlan.getEndDate())
                .currency(userPlan.getCurrency())
                .promocode(userPlan.getPromocode())
                .tiktokAccount(userPlan.getTiktokAccount());
    }
}
