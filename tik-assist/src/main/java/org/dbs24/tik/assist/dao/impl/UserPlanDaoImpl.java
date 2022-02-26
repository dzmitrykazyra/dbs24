package org.dbs24.tik.assist.dao.impl;

import lombok.Data;
import lombok.extern.log4j.Log4j2;
import org.dbs24.tik.assist.dao.ReferenceDao;
import org.dbs24.tik.assist.dao.UserPlanDao;
import org.dbs24.tik.assist.entity.domain.*;
import org.dbs24.tik.assist.repo.UserPlanHistRepo;
import org.dbs24.tik.assist.repo.UserPlanRepo;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Log4j2
@Component
public class UserPlanDaoImpl implements UserPlanDao {

    private final ReferenceDao referenceDao;

    private final UserPlanRepo userPlanRepo;
    private final UserPlanHistRepo userPlanHistRepo;

    public UserPlanDaoImpl(UserPlanRepo userPlanRepo, ReferenceDao referenceDao, UserPlanHistRepo userPlanHistRepo) {

        this.userPlanRepo = userPlanRepo;
        this.referenceDao = referenceDao;
        this.userPlanHistRepo = userPlanHistRepo;
    }

    @Override
    public UserPlan saveUserPlan(UserPlan userPlan) {

        return userPlanRepo.saveAndFlush(userPlan);
    }

    @Override
    public List<UserPlan> saveUserPlans(List<UserPlan> userPlans) {

        return userPlanRepo.saveAllAndFlush(userPlans);
    }

    @Override
    public List<UserPlan> updateUserPlans(List<UserPlan> userPlans) {

        savePlanHistsByUserPlans(userPlans);
        return saveUserPlans(userPlans);
    }

    private List<PlanHist> savePlanHistsByUserPlans(List<UserPlan> userPlans) {

        return userPlanHistRepo.saveAll(
                userPlans
                        .stream()
                        .map(PlanHist::toPlanHist)
                        .collect(Collectors.toList())
        );
    }

    @Override
    public List<UserPlan> updateUserPlansToDone(List<UserPlan> userPlans) {

        return updateUserPlans(
                userPlans
                        .stream()
                        .map(userPlan -> {
                            userPlan.setCancelDate(LocalDateTime.now());
                            userPlan.setPlanStatus(referenceDao.findDonePlanStatus());
                            return userPlan;
                        })
                        .collect(Collectors.toList()));
    }

    @Override
    public UserPlan findUserPlanById(Integer userPlanId) {

        return userPlanRepo
                .findById(userPlanId)
                .orElseThrow(() -> new RuntimeException("Cannot find user plan by id"));
    }

    @Override
    public List<UserPlan> findActiveUserPlansByUser(User user) {

        return userPlanRepo.findByUserAndPlanStatus(
                user,
                referenceDao.findActivePlanStatus()
        );
    }

    @Override
    public List<UserPlan> findActiveUserPlansByTiktokAccount(TiktokAccount tiktokAccount) {
        PlanStatus activePlanStatus = referenceDao.findActivePlanStatus();

        List<UserPlan> userPlans = userPlanRepo.findByPlanStatusAndTiktokAccount(
                activePlanStatus,
                tiktokAccount
        );

        return userPlans;
    }

    @Override
    public List<UserPlan> findExpiredUserPlans() {

        return userPlanRepo.findByPlanStatusAndEndDateBefore(
                referenceDao.findActivePlanStatus(),
                LocalDateTime.now()
        );
    }

    @Override
    public List<UserPlan> findUserPlansBySubscription(UserSubscription userSubscription) {

        return userPlanRepo.findByUserSubscription(userSubscription);
    }

    @Override
    public Optional<UserPlan> findActualUserPlanByUserAndTiktokAccountAndStatus(User user, TiktokAccount tiktokAccount, PlanStatus planStatus) {
        return userPlanRepo.findByUserAndTiktokAccountAndPlanStatus(user, tiktokAccount, planStatus);
    }

}
