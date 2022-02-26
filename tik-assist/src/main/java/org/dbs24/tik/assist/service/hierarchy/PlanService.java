package org.dbs24.tik.assist.service.hierarchy;

import io.vavr.control.Either;
import lombok.extern.log4j.Log4j2;
import org.dbs24.spring.core.api.AbstractApplicationService;
import org.dbs24.tik.assist.dao.PromocodeDao;
import org.dbs24.tik.assist.dao.ReferenceDao;
import org.dbs24.tik.assist.dao.UserDao;
import org.dbs24.tik.assist.dao.UserPlanDao;
import org.dbs24.tik.assist.entity.domain.*;
import org.dbs24.tik.assist.entity.dto.plan.CustomPlanConstraint;
import org.dbs24.tik.assist.entity.dto.plan.CustomUserPlanDto;
import org.dbs24.tik.assist.entity.dto.plan.response.CreatedCustomUserPlanDto;
import org.dbs24.tik.assist.service.hierarchy.resolver.SumResolver;
import org.dbs24.tik.assist.service.hierarchy.split.PlanToOrderSplitter;
import org.dbs24.tik.assist.service.tiktok.resolver.TiktokInteractor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Log4j2
@Service
public class PlanService extends AbstractApplicationService {

    @Value("${constraint.plan.duration-in-days}")
    private int userPlanDurationInDays;

    private final UserDao userDao;
    private final UserPlanDao userPlanDao;
    private final ReferenceDao referenceDao;
    private final PromocodeDao promocodeDao;

    private final PlanToOrderSplitter planToOrderSplitter;
    private final TiktokInteractor tiktokInteractor;
    private final SumResolver sumResolver;

    public PlanService(UserDao userDao,
                       UserPlanDao userPlanDao,
                       ReferenceDao referenceDao,
                       PromocodeDao promocodeDao,
                       PlanToOrderSplitter planToOrderSplitter,
                       TiktokInteractor tiktokInteractor,
                       SumResolver sumResolver) {
        this.userDao = userDao;
        this.userPlanDao = userPlanDao;
        this.referenceDao = referenceDao;
        this.promocodeDao = promocodeDao;
        this.planToOrderSplitter = planToOrderSplitter;
        this.tiktokInteractor = tiktokInteractor;
        this.sumResolver = sumResolver;
    }

    public List<UserPlan> createUserPlans(List<UserPlan> userPlansToSave) {

        List<UserPlan> savedUserPlans = userPlanDao.saveUserPlans(userPlansToSave);
    //    planToOrderSplitter.splitAll(userPlansToSave);

        return savedUserPlans;
    }

    public CreatedCustomUserPlanDto createCustomPlan(Integer userId, CustomUserPlanDto customUserPlanDto) {

        User user = userDao.findUserById(userId);
        Promocode promocode = promocodeDao.activatePromocode(user.getUserId(), customUserPlanDto.getPromocodeValue());
        BigDecimal totalPlanSum = sumResolver.calculatePlanSum(
                Either.right(customUserPlanDto.getCustomPlanConstraint()),
                Optional.ofNullable(promocode)
        );

        UserPlan customUserPlan = defaultActiveCustomPlanBuilder(customUserPlanDto.getCustomPlanConstraint())
                .planSum(totalPlanSum)
                .user(user)
                .tiktokAccount(tiktokInteractor.getTiktokAccount(customUserPlanDto.getAccountUsername(), user))
                .promocode(promocode)
                .build();

        UserPlan savedUserPlan = userPlanDao.saveUserPlan(customUserPlan);
        planToOrderSplitter.split(customUserPlan);

        return CreatedCustomUserPlanDto.toDto(savedUserPlan);
    }

    /**
     * Method allows get existing user plans by required user subscription
     * and create new user plans(with null userPlanId, active status, updated user plan dates values)
     */
    public List<UserPlan> createPlansByExistingSubscription(UserSubscription userSubscription) {

        return userPlanDao
                .findUserPlansBySubscription(userSubscription)
                .stream()
                .map(userPlan -> UserPlan.builder()
                        .user(userPlan.getUser())
                        .tiktokAccount(userPlan.getTiktokAccount())
                        .planTemplate(userPlan.getPlanTemplate())
                        .planStatus(referenceDao.findActivePlanStatus())
                        .beginDate(LocalDateTime.now())
                        .endDate(userSubscription.getEndDate())
                        .actualDate(LocalDateTime.now())
                        .planSum(userPlan.getPlanSum())
                        .userSubscription(userPlan.getUserSubscription())
                        .viewsAmount(userPlan.getViewsAmount())
                        .likesAmount(userPlan.getLikesAmount())
                        .followersAmount(userPlan.getFollowersAmount())
                        .commentsAmount(userPlan.getCommentsAmount())
                        .repostsAmount(userPlan.getRepostsAmount())
                        .build()
                )
                .collect(Collectors.toList());
    }

    public UserPlan buildPlanByTemplate(UserSubscription userSubscription,
                                        TiktokAccount subscriptionAccount,
                                        PlanTemplate planTemplate) {

        return defaultActiveBySubscriptionPlanBuilder(userSubscription, subscriptionAccount)
                .planTemplate(planTemplate)
                .viewsAmount(planTemplate.getViewsAmount())
                .likesAmount(planTemplate.getLikesAmount())
                .followersAmount(planTemplate.getFollowersAmount())
                .commentsAmount(planTemplate.getCommentsAmount())
                .repostsAmount(planTemplate.getRepostsAmount())
                .build();
    }

    public UserPlan buildPlanBySubscriptionCustom(UserSubscription userSubscription,
                                                  TiktokAccount subscriptionAccount,
                                                  CustomPlanConstraint customPlanConstraint) {

        return defaultActiveBySubscriptionPlanBuilder(userSubscription, subscriptionAccount)
                .viewsAmount(customPlanConstraint.getViewsAmount())
                .likesAmount(customPlanConstraint.getLikesAmount())
                .followersAmount(customPlanConstraint.getFollowersAmount())
                .commentsAmount(customPlanConstraint.getCommentsAmount())
                .repostsAmount(customPlanConstraint.getRepostsAmount())
                .build();
    }

    private UserPlan.UserPlanBuilder defaultActiveBySubscriptionPlanBuilder(UserSubscription userSubscription, TiktokAccount tiktokAccount) {

        return UserPlan.builder()
                .user(userSubscription.getUser())
                .tiktokAccount(tiktokAccount)
                .userSubscription(userSubscription)
                .planStatus(referenceDao.findActivePlanStatus())
                .actualDate(LocalDateTime.now())
                .beginDate(userSubscription.getBeginDate())
                .endDate(userSubscription.getEndDate())
                .promocode(userSubscription.getPromocode());
    }

    private UserPlan.UserPlanBuilder defaultActiveCustomPlanBuilder(CustomPlanConstraint customPlanConstraint) {

        return UserPlan.builder()
                .planStatus(referenceDao.findActivePlanStatus())
                .actualDate(LocalDateTime.now())
                .beginDate(LocalDateTime.now())
                .endDate(LocalDateTime.now().plusDays(userPlanDurationInDays))
                .viewsAmount(customPlanConstraint.getViewsAmount())
                .likesAmount(customPlanConstraint.getLikesAmount())
                .followersAmount(customPlanConstraint.getFollowersAmount())
                .commentsAmount(customPlanConstraint.getCommentsAmount())
                .repostsAmount(customPlanConstraint.getRepostsAmount());
    }
}
