package org.dbs24.tik.assist.dao.impl;

import lombok.extern.log4j.Log4j2;
import org.dbs24.tik.assist.dao.UserDepositDao;
import org.dbs24.tik.assist.entity.domain.User;
import org.dbs24.tik.assist.entity.domain.UserDeposit;
import org.dbs24.tik.assist.entity.domain.UserDepositHist;
import org.dbs24.tik.assist.repo.UserDepositHistRepo;
import org.dbs24.tik.assist.repo.UserDepositRepo;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Log4j2
@Component
public class UserDepositDaoImpl implements UserDepositDao {

    private final UserDepositRepo userDepositRepo;
    private final UserDepositHistRepo userDepositHistRepo;

    public UserDepositDaoImpl(UserDepositRepo userDepositRepo, UserDepositHistRepo userDepositHistRepo) {

        this.userDepositRepo = userDepositRepo;
        this.userDepositHistRepo = userDepositHistRepo;
    }

    @Override
    public UserDeposit createZeroUserDeposit(User user) {

        return userDepositRepo.save(
                UserDeposit.builder()
                        .userId(user.getUserId())
                        .actualDate(LocalDateTime.now())
                        .restSum(BigDecimal.ZERO)
                        .build()
        );
    }

    public UserDeposit updateUserDeposit(UserDeposit userDeposit) {

        UserDeposit oldUserDepositState = userDepositRepo.findByUserId(userDeposit.getUserId()).orElseThrow();
        userDepositHistRepo.saveAndFlush(UserDepositHist.of(oldUserDepositState));

        userDeposit.setActualDate(LocalDateTime.now());

        return userDepositRepo.save(userDeposit);
    }

    @Override
    public UserDeposit increaseUserDeposit(User user, BigDecimal increaseAmount) {

        UserDeposit userDepositToIncrease = findOrCreateUserDepositByUser(user);
        BigDecimal increasedDepositBalance = userDepositToIncrease
                .getRestSum()
                .add(increaseAmount);
        userDepositToIncrease.setRestSum(increasedDepositBalance);

        return updateUserDeposit(userDepositToIncrease);
    }

    @Override
    public UserDeposit decreaseUserDeposit(User user, BigDecimal decreaseAmount) {


        UserDeposit userDepositToIncrease = findOrCreateUserDepositByUser(user);
        BigDecimal increasedDepositBalance = userDepositToIncrease
                .getRestSum()
                .subtract(decreaseAmount);
        userDepositToIncrease.setRestSum(increasedDepositBalance);

        return updateUserDeposit(userDepositToIncrease);
    }

    @Override
    public UserDeposit findOrCreateUserDepositByUser(User user) {

        return userDepositRepo
                .findById(user.getUserId())
                .orElseGet(() -> createZeroUserDeposit(user));
    }
}
