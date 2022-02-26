package org.dbs24.tik.assist.entity.dto.subscription;

import lombok.Data;
import org.dbs24.application.core.locale.NLS;
import org.dbs24.stmt.StmtProcessor;
import org.dbs24.tik.assist.entity.domain.PlanTemplate;
import org.dbs24.tik.assist.entity.domain.UserPlan;
import org.dbs24.tik.assist.entity.domain.UserSubscription;
import org.dbs24.tik.assist.entity.dto.plan.CustomPlanConstraint;

import java.math.BigDecimal;

@Data
public class UserSubscriptionPlanStatisticsDto {

    private Integer userSubscriptionId;
    private String subscriptionTitle;
    private BigDecimal subscriptionCost;
    private CustomPlanConstraint customPlanConstraint;
    private Long nextBillingDateMillis;

    /**
     * Method allows creating statistics DTO based on subscription and one of subscription plans(which have appeared when hierarchy splitting)
     */
    public static UserSubscriptionPlanStatisticsDto toDto(UserPlan userPlan) {

        UserSubscriptionPlanStatisticsDto dto = null;

        if (userPlan.getUserSubscription() != null) {

            dto = StmtProcessor.create(
                    UserSubscriptionPlanStatisticsDto.class,
                    userSubscriptionPlanStatisticsDto -> {

                        userSubscriptionPlanStatisticsDto.setUserSubscriptionId(userPlan.getUserSubscription().getUserSubscriptionId());
                        userSubscriptionPlanStatisticsDto.setNextBillingDateMillis(NLS.localDateTime2long(userPlan.getEndDate()));
                        userSubscriptionPlanStatisticsDto.setSubscriptionCost(userPlan.getUserSubscription().getSubscriptionSum());

                        if (userPlan.getPlanTemplate() != null) {
                            userSubscriptionPlanStatisticsDto.setSubscriptionTitle(userPlan.getPlanTemplate().getPlanTemplateName());
                        } else {
                            userSubscriptionPlanStatisticsDto.setSubscriptionTitle("CUSTOM");
                        }

                        userSubscriptionPlanStatisticsDto.setCustomPlanConstraint(CustomPlanConstraint.toDto(userPlan));
                    }
            );
        }

        return dto;
    }
}
