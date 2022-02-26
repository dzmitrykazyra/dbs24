package org.dbs24.tik.assist.service.hierarchy;

import lombok.extern.log4j.Log4j2;
import org.dbs24.tik.assist.entity.dto.cart.CartOrderDto;
import org.dbs24.tik.assist.entity.dto.cart.CreatedCartOrderDto;
import org.dbs24.tik.assist.entity.dto.order.CreateFollowersOrderDto;
import org.dbs24.tik.assist.entity.dto.order.CreateVideoOrderDto;
import org.dbs24.tik.assist.entity.dto.order.CreatedUserOrderDto;
import org.dbs24.tik.assist.entity.dto.plan.CustomUserPlanDto;
import org.dbs24.tik.assist.entity.dto.plan.response.CreatedCustomUserPlanDto;
import org.dbs24.tik.assist.entity.dto.subscription.ByTemplateUserSubscriptionDto;
import org.dbs24.tik.assist.entity.dto.subscription.CustomUserSubscriptionDto;
import org.dbs24.tik.assist.entity.dto.subscription.UserSubscriptionIdDto;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Log4j2
@Component
public class CartService {

    private final UserSubscriptionService userSubscriptionService;
    private final PlanService planService;
    private final UserOrderService userOrderService;

    public CartService(UserSubscriptionService userSubscriptionService, PlanService planService, UserOrderService userOrderService) {

        this.userSubscriptionService = userSubscriptionService;
        this.planService = planService;
        this.userOrderService = userOrderService;
    }

    //todo change ugly method!!!
    public Mono<CreatedCartOrderDto> createAllHierarchyEntities(Integer userId, Mono<CartOrderDto> cartOrderDtoMono) {

        CartOrderDto cartOrderDto = cartOrderDtoMono.toProcessor().block();

        List<CreateVideoOrderDto> createLikesOrderDtos = cartOrderDto.getCreateLikesOrderDtos();
        List<CreateVideoOrderDto> createViewsOrderDtos = cartOrderDto.getCreateViewsOrderDtos();
        List<CreateFollowersOrderDto> createFollowersOrderDtos = cartOrderDto.getCreateFollowersOrderDtos();
        List<CustomUserPlanDto> createCustomUserPlanDtos = cartOrderDto.getCreateCustomUserPlanDtos();
        List<CustomUserSubscriptionDto> createCustomUserSubscriptionDtos = cartOrderDto.getCreateCustomUserSubscriptionDtos();
        List<ByTemplateUserSubscriptionDto> createByTemplateUserSubscriptionDtos = cartOrderDto.getCreateByTemplateUserSubscriptionDtos();

        List<CreatedUserOrderDto> createdLikesOrderDtos = new ArrayList<>();
        List<CreatedUserOrderDto> createdViewsOrderDtos = new ArrayList<>();
        List<CreatedUserOrderDto> createdFollowersOrderDtos = new ArrayList<>();
        List<CreatedCustomUserPlanDto> createdCustomUserPlanDto = new ArrayList<>();
        List<UserSubscriptionIdDto> createdCustomSubscriptionsDto = new ArrayList<>();
        List<UserSubscriptionIdDto> createdTemplateSubscriptionsDto = new ArrayList<>();

        if (createLikesOrderDtos != null) {

            createdLikesOrderDtos = createLikesOrderDtos
                    .stream()
                    .map(dto -> userOrderService.createLikesOrder(userId, dto))
                    .collect(Collectors.toList());
        }

        if (createViewsOrderDtos != null) {

            createdViewsOrderDtos = createViewsOrderDtos
                    .stream()
                    .map(dto -> userOrderService.createViewsOrder(userId, dto))
                    .collect(Collectors.toList());
        }

        if (createFollowersOrderDtos != null) {

            createdFollowersOrderDtos = createFollowersOrderDtos
                    .stream()
                    .map(dto -> userOrderService.createFollowersOrder(userId, dto))
                    .collect(Collectors.toList());
        }

        if (createCustomUserPlanDtos != null) {

            createdCustomUserPlanDto = createCustomUserPlanDtos
                    .stream()
                    .map(dto -> planService.createCustomPlan(userId, dto))
                    .collect(Collectors.toList());
        }

        if (createCustomUserSubscriptionDtos != null) {

            createdCustomSubscriptionsDto = createCustomUserSubscriptionDtos
                    .stream()
                    .map(dto -> userSubscriptionService.createUserSubscriptionCustom(userId, dto))
                    .collect(Collectors.toList());
        }

        if (createByTemplateUserSubscriptionDtos != null) {

            createdTemplateSubscriptionsDto = createByTemplateUserSubscriptionDtos
                    .stream()
                    .map(dto -> userSubscriptionService.createUserSubscriptionByTemplate(userId, dto))
                    .collect(Collectors.toList());
        }


        return Mono.just(CreatedCartOrderDto.builder()
                .viewsOrder(createdViewsOrderDtos)
                .likesOrder(createdLikesOrderDtos)
                .followersOrder(createdFollowersOrderDtos)
                .customPlan(createdCustomUserPlanDto)
                .customSubscription(createdCustomSubscriptionsDto)
                .templateSubscription(createdTemplateSubscriptionsDto)
                .build());
    }
}
