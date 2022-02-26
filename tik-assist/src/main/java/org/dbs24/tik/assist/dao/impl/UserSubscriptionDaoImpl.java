package org.dbs24.tik.assist.dao.impl;

import lombok.Data;
import lombok.extern.log4j.Log4j2;
import org.dbs24.spring.core.api.DaoAbstractApplicationService;
import org.dbs24.tik.assist.dao.ReferenceDao;
import org.dbs24.tik.assist.dao.UserSubscriptionDao;
import org.dbs24.tik.assist.entity.domain.*;
import org.dbs24.tik.assist.repo.UserSubscriptionHistRepo;
import org.dbs24.tik.assist.repo.UserSubscriptionRepo;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Data
@Log4j2
@Component
public class UserSubscriptionDaoImpl extends DaoAbstractApplicationService implements UserSubscriptionDao {

    private final ReferenceDao referenceDao;

    private final UserSubscriptionRepo userSubscriptionRepo;
    private final UserSubscriptionHistRepo userSubscriptionHistRepo;

    public UserSubscriptionDaoImpl(ReferenceDao referenceDao, UserSubscriptionRepo userSubscriptionRepo, UserSubscriptionHistRepo userSubscriptionHistRepo) {

        this.referenceDao = referenceDao;
        this.userSubscriptionRepo = userSubscriptionRepo;
        this.userSubscriptionHistRepo = userSubscriptionHistRepo;
    }

    @Override
    public UserSubscription saveUserSubscription(UserSubscription userSubscription) {

        return userSubscriptionRepo.save(userSubscription);
    }

    @Override
    public UserSubscription updateUserSubscription(UserSubscription userSubscription) {

        userSubscription.setActualDate(LocalDateTime.now());
        userSubscriptionHistRepo.save(UserSubscriptionHist.toUserSubscriptionHist(userSubscription));
        return userSubscriptionRepo.save(userSubscription);
    }

    @Override
    public List<UserSubscription> updateUserSubscriptions(List<UserSubscription> userSubscriptions) {

        userSubscriptions.forEach(userSubscription -> userSubscription.setActualDate(LocalDateTime.now()));
        saveUserSubscriptionHistsByUserSubscriptions(userSubscriptions);
        return userSubscriptionRepo.saveAll(userSubscriptions);
    }

    @Override
    public UserSubscription updateUserSubscriptionToNonActive(UserSubscription userSubscription) {

        userSubscription.setUserSubscriptionStatus(referenceDao.findNonActiveUserSubscriptionStatus());
        userSubscription.setActualDate(LocalDateTime.now());
        userSubscription.setCancelDate(LocalDateTime.now());

        return updateUserSubscription(userSubscription);
    }

    @Override
    public Optional<UserSubscription> findUserSubscriptionById(Integer userSubscriptionId) {

        return userSubscriptionRepo.findById(userSubscriptionId);
    }

    @Override
    public List<UserSubscription> findExpiredUserSubscriptions() {

        return userSubscriptionRepo.findByUserSubscriptionStatusAndEndDateBefore(
                referenceDao.findActiveUserSubscriptionStatus(),
                LocalDateTime.now()
        );
    }

    @Override
    public Optional<UserSubscription> findLatestUserSubscription(User user) {

        return userSubscriptionRepo.findByUser(user)
                .stream()
                .max(Comparator.comparing(UserSubscription::getEndDate));
    }

    @Override
    public Optional<UserSubscription> findUserSubscriptionByUserAndId(User user, Integer userSubscriptionId) {
        return userSubscriptionRepo.findByUserAndUserSubscriptionId(user, userSubscriptionId);
    }

    @Override
    public void removeById(Integer userSubscriptionId) {
        userSubscriptionRepo.removeByUserSubscriptionId(userSubscriptionId);
    }

    @Override
    public UserSubscriptionHist saveUserSubscriptionHistByUserSubscription(UserSubscription userSubscription) {

        return userSubscriptionHistRepo.save(UserSubscriptionHist.toUserSubscriptionHist(userSubscription));
    }

    private List<UserSubscriptionHist> saveUserSubscriptionHistsByUserSubscriptions(List<UserSubscription> userSubscriptions) {

        return userSubscriptionHistRepo.saveAll(
                userSubscriptions
                        .stream()
                        .map(UserSubscriptionHist::toUserSubscriptionHist)
                        .collect(Collectors.toList())
        );
    }
}
