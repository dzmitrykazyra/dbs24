package org.dbs24.tik.assist.entity.dto.cart;

import lombok.Data;
import org.dbs24.tik.assist.entity.dto.order.CreateFollowersOrderDto;
import org.dbs24.tik.assist.entity.dto.order.CreateVideoOrderDto;
import org.dbs24.tik.assist.entity.dto.plan.CustomUserPlanDto;
import org.dbs24.tik.assist.entity.dto.subscription.ByTemplateUserSubscriptionDto;
import org.dbs24.tik.assist.entity.dto.subscription.CustomUserSubscriptionDto;

import java.util.List;

@Data
public class CartOrderDto {

    private List<CreateVideoOrderDto> createLikesOrderDtos;
    private List<CreateVideoOrderDto> createViewsOrderDtos;
    private List<CreateFollowersOrderDto> createFollowersOrderDtos;

    private List<CustomUserPlanDto> createCustomUserPlanDtos;

    private List<CustomUserSubscriptionDto> createCustomUserSubscriptionDtos;
    private List<ByTemplateUserSubscriptionDto> createByTemplateUserSubscriptionDtos;
}
