/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.tik.assist.dao.impl;

import java.util.List;
import java.util.Optional;
import lombok.Data;
import lombok.extern.log4j.Log4j2;
import org.dbs24.spring.core.api.DaoAbstractApplicationService;
import org.dbs24.tik.assist.dao.UserDao;
import org.dbs24.tik.assist.entity.domain.User;
import org.dbs24.tik.assist.entity.domain.UserHist;
import org.dbs24.tik.assist.entity.domain.UserStatus;
import org.springframework.stereotype.Component;
import org.dbs24.tik.assist.repo.*;

@Data
@Log4j2
@Component
public class UserDaoImpl extends DaoAbstractApplicationService implements UserDao {

    final UserRepo userRepo;
    final UserHistRepo userHistRepo;

    public UserDaoImpl(UserRepo userRepo, UserHistRepo userHistRepo) {

        this.userRepo = userRepo;
        this.userHistRepo = userHistRepo;
    }

    @Override
    public User findUserById(Integer userId) {

        return userRepo
                .findById(userId)
                .orElseThrow(() -> new RuntimeException("User with such id not found"));
    }

    @Override
    public User findUserByEmail(String email) {

        return userRepo
                .findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User with such username not found"));
    }

    @Override
    public List<User> findAllUsersByEmail(String email) {
        return userRepo.findUsersListByEmail(email);
    }


    @Override
    public User saveUser(User user) {

        return userRepo.save(user);
    }

    @Override
    public Optional<User> findUserOptionalByEmailAndGoogleUserId(String email, String googleUserId) {

        return userRepo.findByEmailAndGoogleUserId(email, googleUserId);
    }

    @Override
    public Optional<User> findUserOptionalByFacebookUserId(String facebookUserId) {

        return userRepo.findByFacebookUserId(facebookUserId);
    }

    @Override
    public Optional<User> findUserOptionalById(Integer userId) {

        return userRepo.findById(userId);
    }

    @Override
    public Optional<User> findUserOptionalByEmail(String email) {

        return userRepo.findByEmail(email);
    }

    @Override
    public Optional<User> findOptionalByEmailAndStatusId(String email, UserStatus userStatus) {
        return userRepo.findByEmailAndUserStatus(email, userStatus);
    }

    @Override
    public UserHist saveUserHist(UserHist userHist) {

        return userHistRepo.save(userHist);
    }

    @Override
    public UserHist saveUserHistByUser(User user) {

        return saveUserHist(UserHist.toUserHist(user));
    }

    @Override
    public boolean isGoogleUser(User user) {

        return user.getGoogleUserId() != null;
    }

    @Override
    public boolean isFacebookUser(User user) {

        return user.getFacebookUserId() != null;
    }

    @Override
    public boolean isDefaultUser(User user) {

        return (user.getFacebookUserId() == null && user.getGoogleUserId() == null);
    }
}
