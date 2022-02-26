package org.dbs24.tik.assist.service.hierarchy.split;

import io.vavr.control.Either;
import lombok.extern.log4j.Log4j2;
import org.dbs24.tik.assist.entity.domain.*;
import org.dbs24.tik.assist.entity.dto.plan.CustomPlanConstraint;
import org.dbs24.tik.assist.entity.dto.subscription.AbstractUserSubscriptionDto;
import org.dbs24.tik.assist.entity.dto.subscription.CustomUserSubscriptionDto;
import org.dbs24.tik.assist.service.hierarchy.PlanService;
import org.dbs24.tik.assist.service.hierarchy.resolver.SumResolver;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Log4j2
@Component
public class SubscriptionToPlanSplitter {

    private final PlanService planService;
    private final SumResolver sumResolver;


    public SubscriptionToPlanSplitter(PlanService planService, SumResolver sumResolver) {

        this.planService = planService;
        this.sumResolver = sumResolver;
    }

   /* public List<UserPlan> split(UserSubscription userSubscription,
                                List<TiktokAccount> subscriptionTiktokAccounts,
                                Either<PlanTemplate, CustomPlanConstraint> templateOrCustomInfo) {

        return planService.createUserPlans(
                subscriptionTiktokAccounts.stream().map(

                        tiktokAccount -> {
                            UserPlan userPlan;

                            if (templateOrCustomInfo.isLeft()) {
                                userPlan = planService.buildPlanByTemplate(
                                        userSubscription,
                                        tiktokAccount,
                                        templateOrCustomInfo.getLeft()
                                );
                            } else {
                                userPlan = planService.buildPlanBySubscriptionCustom(
                                        userSubscription,
                                        tiktokAccount,
                                        templateOrCustomInfo.get()
                                );
                            }

                            return userPlan;
                        }
                ).collect(Collectors.toList()));
    }*/

    public List<UserPlan> split(UserSubscription userSubscription,
                               List<TiktokAccount> tiktokAccount,
                                Either<PlanTemplate, CustomPlanConstraint> templateOrCustomInfo) {

        return planService.createUserPlans(
                tiktokAccount.stream().map(

                        account -> {
                            UserPlan userPlan;

                            if (templateOrCustomInfo.isLeft()) {
                                userPlan = planService.buildPlanByTemplate(
                                        userSubscription,
                                        account,
                                        templateOrCustomInfo.getLeft()
                                );
                            } else {
                                userPlan = planService.buildPlanBySubscriptionCustom(
                                        userSubscription,
                                        account,
                                        templateOrCustomInfo.get()
                                );
                            }
                            userPlan.setPlanSum(sumResolver.calculatePlanSum(templateOrCustomInfo, Optional.ofNullable(null)));

                            return userPlan;
                        }
                ).collect(Collectors.toList()));

    }

    /**
     * Method allows save new user plans based on elder user plans bounded with required subscription
     */
    public List<UserPlan> saveSplitBefore(UserSubscription userSubscription) {

        return planService.createUserPlans(
                planService.createPlansByExistingSubscription(userSubscription)
        );
    }
}
