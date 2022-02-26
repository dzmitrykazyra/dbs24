package org.dbs24.tik.mobile.dao.impl;

import org.dbs24.tik.mobile.dao.UserDepositDao;
import org.dbs24.tik.mobile.entity.domain.User;
import org.dbs24.tik.mobile.entity.domain.UserDeposit;
import org.dbs24.tik.mobile.entity.domain.UserDepositHist;
import org.dbs24.tik.mobile.repo.UserDepositHistRepo;
import org.dbs24.tik.mobile.repo.UserDepositRepo;
import org.dbs24.tik.mobile.service.exception.http.BadRequestException;
import org.dbs24.tik.mobile.service.exception.http.PaymentRequiredException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class UserDepositDaoImpl implements UserDepositDao {

    private final UserDepositRepo userDepositRepo;
    private final UserDepositHistRepo userDepositHistRepo;

    @Autowired
    public UserDepositDaoImpl(UserDepositRepo userDepositRepo, UserDepositHistRepo userDepositHistRepo) {
        this.userDepositRepo = userDepositRepo;
        this.userDepositHistRepo = userDepositHistRepo;
    }

    @Override
    public UserDeposit findUserDepositByUserId(Integer userId) {

        return userDepositRepo.findUserDepositByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Cannot find deposit with user id = " + userId));
    }

    @Override
    public void saveUserDeposit(UserDeposit userDeposit) {

        userDepositRepo.save(userDeposit);
    }

    @Override
    public void saveUserDepositHist(UserDeposit userDeposit) {

        userDepositHistRepo.saveAndFlush(UserDepositHist.toUserDepositHist(userDeposit));
    }

    @Override
    public UserDeposit saveZeroUserDepositByUser(User user) {

        return userDepositRepo.save(
                UserDeposit.builder()
                        .userId(user.getId())
                        .actualDate(LocalDateTime.now())
                        .restSum(0)
                        .build()
        );
    }

    @Override
    public UserDeposit increaseUserDeposit(User user, Integer amountToIncrease) {
        UserDeposit userDeposit = findOrCreateUserDepositByUser(user);

        saveUserDepositHist(userDeposit);
        userDeposit.setRestSum(userDeposit.getRestSum() + amountToIncrease);

        return updateUserDeposit(userDeposit);
    }

    @Override
    public void decreaseUserDeposit(User user, Integer amountToDecrease) {
        UserDeposit userDeposit = findOrCreateUserDepositByUser(user);

        if (userDeposit.getRestSum() < amountToDecrease) {
            throw new PaymentRequiredException();
        }

        saveUserDepositHist(userDeposit);
        userDeposit.setRestSum(userDeposit.getRestSum() - amountToDecrease);
        updateUserDeposit(userDeposit);
    }

    @Override
    public UserDeposit findOrCreateUserDepositByUser(User user) {

        return userDepositRepo.findById(user.getId())
                .orElse(saveZeroUserDepositByUser(user));
    }

    private UserDeposit updateUserDeposit(UserDeposit userDeposit) {
        userDeposit.setActualDate(LocalDateTime.now());
        return userDepositRepo.save(userDeposit);
    }
}