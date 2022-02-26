package org.dbs24.tik.mobile.dao;

import org.dbs24.tik.mobile.entity.domain.User;
import org.dbs24.tik.mobile.entity.domain.UserDeposit;

public interface UserDepositDao {

    UserDeposit findUserDepositByUserId(Integer userId);

    void saveUserDeposit(UserDeposit userDeposit);

    void saveUserDepositHist(UserDeposit userDeposit);

    UserDeposit saveZeroUserDepositByUser(User user);

    UserDeposit increaseUserDeposit(User user, Integer amountToIncrease);

    void decreaseUserDeposit(User user, Integer amountToDecrease);

    UserDeposit findOrCreateUserDepositByUser(User user);

}
