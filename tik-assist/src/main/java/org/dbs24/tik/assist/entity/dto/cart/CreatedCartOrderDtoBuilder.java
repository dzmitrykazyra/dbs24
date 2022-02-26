package org.dbs24.tik.assist.entity.dto.cart;


import lombok.AllArgsConstructor;
import lombok.Getter;
import org.dbs24.stmt.StmtProcessor;
import org.dbs24.tik.assist.entity.dto.order.CreatedUserOrderDto;
import org.dbs24.tik.assist.entity.dto.plan.response.CreatedCustomUserPlanDto;
import org.dbs24.tik.assist.entity.dto.subscription.UserSubscriptionIdDto;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Builder to fill map with created cart order data
 */
public class CreatedCartOrderDtoBuilder {

    private Map<String, List<Integer>> entityTitleToCreatedIds;

    public CreatedCartOrderDtoBuilder() {

        this.entityTitleToCreatedIds = new HashMap<>();
    }

    public CreatedCartOrderDto build() {

        return StmtProcessor.create(
                CreatedCartOrderDto.class,
                createdCartOrderDto -> createdCartOrderDto.setEntityTitleToCreatedIds(entityTitleToCreatedIds)
        );
    }

    private void put(HierarchyEntityTitle title, List<Integer> ids) {

        entityTitleToCreatedIds.put(title.getTitle(), ids);
    }

    public CreatedCartOrderDtoBuilder viewsOrder(List<CreatedUserOrderDto> createdViewsUserOrders) {
        put(HierarchyEntityTitle.VIEWS_ORDER, createdViewsUserOrders.stream().map(CreatedUserOrderDto::getCreatedOrderId).collect(Collectors.toList()));
        return this;
    }

    public CreatedCartOrderDtoBuilder likesOrder(List<CreatedUserOrderDto> createdLikesUserOrders) {
        put(HierarchyEntityTitle.LIKES_ORDER, createdLikesUserOrders.stream().map(CreatedUserOrderDto::getCreatedOrderId).collect(Collectors.toList()));
        return this;
    }

    public CreatedCartOrderDtoBuilder followersOrder(List<CreatedUserOrderDto> createdFollowersUserOrders) {
        put(HierarchyEntityTitle.FOLLOWERS_ORDER, createdFollowersUserOrders.stream().map(CreatedUserOrderDto::getCreatedOrderId).collect(Collectors.toList()));
        return this;
    }

    public CreatedCartOrderDtoBuilder customPlan(List<CreatedCustomUserPlanDto> createdCustomUserPlans) {
        put(HierarchyEntityTitle.CUSTOM_PLAN, createdCustomUserPlans.stream().map(CreatedCustomUserPlanDto::getCreatedPlanId).collect(Collectors.toList()));
        return this;
    }

    public CreatedCartOrderDtoBuilder customSubscription(List<UserSubscriptionIdDto> createdUserSubscriptions) {
        put(HierarchyEntityTitle.CUSTOM_SUBSCRIPTION, createdUserSubscriptions.stream().map(UserSubscriptionIdDto::getUserSubscriptionId).collect(Collectors.toList()));
        return this;
    }

    public CreatedCartOrderDtoBuilder templateSubscription(List<UserSubscriptionIdDto> createdUserSubscriptions) {
        put(HierarchyEntityTitle.TEMPLATE_SUBSCRIPTION, createdUserSubscriptions.stream().map(UserSubscriptionIdDto::getUserSubscriptionId).collect(Collectors.toList()));
        return this;
    }

    @Getter
    @AllArgsConstructor
    enum HierarchyEntityTitle {

        VIEWS_ORDER("VIEWS_ORDER"),
        LIKES_ORDER("LIKES_ORDER"),
        FOLLOWERS_ORDER("FOLLOWERS_ORDER"),
        CUSTOM_PLAN("CUSTOM_PLAN"),
        CUSTOM_SUBSCRIPTION("CUSTOM_SUBSCRIPTION"),
        TEMPLATE_SUBSCRIPTION("TEMPLATE_SUBSCRIPTION");

        private String title;
    }
}
