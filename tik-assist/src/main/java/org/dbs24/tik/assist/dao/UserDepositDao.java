package org.dbs24.tik.assist.dao;

import org.dbs24.tik.assist.entity.domain.User;
import org.dbs24.tik.assist.entity.domain.UserDeposit;

import java.math.BigDecimal;

public interface UserDepositDao {

    UserDeposit createZeroUserDeposit(User user);
    UserDeposit increaseUserDeposit(User user, BigDecimal increaseAmount);
    UserDeposit decreaseUserDeposit(User user, BigDecimal decreaseAmount);
    UserDeposit findOrCreateUserDepositByUser(User user);
}
