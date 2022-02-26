package org.dbs24.tik.assist.repo;

import org.dbs24.spring.core.data.ApplicationJpaRepository;
import org.dbs24.tik.assist.entity.domain.*;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface UserPlanRepo extends ApplicationJpaRepository<UserPlan, Integer>, JpaSpecificationExecutor<UserPlan>, PagingAndSortingRepository<UserPlan, Integer> {

    List<UserPlan> findByUserAndPlanStatus(User user, PlanStatus planStatus);

    List<UserPlan> findByPlanStatusAndEndDateBefore(PlanStatus donePlanStatus, LocalDateTime currentDate);

    List<UserPlan> findByUserSubscription(UserSubscription userSubscription);

    List<UserPlan> findByPlanStatusAndTiktokAccount(PlanStatus PlanStatus, TiktokAccount tiktokAccount);

    Optional<UserPlan> findByUserAndTiktokAccountAndPlanStatus(User user, TiktokAccount tiktokAccount, PlanStatus planStatus);
}