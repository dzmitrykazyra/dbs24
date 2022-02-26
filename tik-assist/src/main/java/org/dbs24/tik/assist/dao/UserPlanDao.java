package org.dbs24.tik.assist.dao;

import org.dbs24.tik.assist.entity.domain.*;

import java.util.List;
import java.util.Optional;

public interface UserPlanDao {

    UserPlan saveUserPlan(UserPlan userPlan);
    List<UserPlan> saveUserPlans(List<UserPlan> userPlans);
    List<UserPlan> updateUserPlans(List<UserPlan> userPlans);
    List<UserPlan> updateUserPlansToDone(List<UserPlan> userPlans);

    UserPlan findUserPlanById(Integer userPlanId);
    List<UserPlan> findExpiredUserPlans();
    List<UserPlan> findActiveUserPlansByUser(User user);
    List<UserPlan> findActiveUserPlansByTiktokAccount(TiktokAccount tiktokAccount);
    List<UserPlan> findUserPlansBySubscription(UserSubscription userSubscription);

    Optional<UserPlan> findActualUserPlanByUserAndTiktokAccountAndStatus(User user, TiktokAccount tiktokAccount, PlanStatus planStatus);

}
