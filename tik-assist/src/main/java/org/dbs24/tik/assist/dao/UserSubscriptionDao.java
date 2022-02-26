package org.dbs24.tik.assist.dao;

import org.dbs24.tik.assist.entity.domain.User;
import org.dbs24.tik.assist.entity.domain.UserSubscription;
import org.dbs24.tik.assist.entity.domain.UserSubscriptionHist;

import java.util.List;
import java.util.Optional;

public interface UserSubscriptionDao {

    UserSubscription saveUserSubscription(UserSubscription userSubscription);
    UserSubscription updateUserSubscription(UserSubscription userSubscription);
    UserSubscription updateUserSubscriptionToNonActive(UserSubscription userSubscription);
    List<UserSubscription> updateUserSubscriptions(List<UserSubscription> userSubscriptions);

    Optional<UserSubscription> findUserSubscriptionById(Integer userSubscriptionId);
    List<UserSubscription> findExpiredUserSubscriptions();
    Optional<UserSubscription> findLatestUserSubscription(User user);

    Optional<UserSubscription> findUserSubscriptionByUserAndId(User user, Integer userSubscriptionId);

    void removeById(Integer userSubscriptionId);

    UserSubscriptionHist saveUserSubscriptionHistByUserSubscription(UserSubscription userSubscription);
}
