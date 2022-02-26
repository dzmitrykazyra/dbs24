package org.dbs24.tik.assist.dao;

import org.dbs24.tik.assist.entity.domain.User;
import org.dbs24.tik.assist.entity.domain.UserHist;
import org.dbs24.tik.assist.entity.domain.UserStatus;

import java.util.List;
import java.util.Optional;

public interface UserDao {

    User findUserById(Integer userId);
    User findUserByEmail(String email);
    List<User> findAllUsersByEmail(String email);
    Optional<User> findUserOptionalByEmailAndGoogleUserId(String email, String googleUserId);
    Optional<User> findUserOptionalByFacebookUserId(String facebookUserId);
    Optional<User> findUserOptionalById(Integer userId);
    Optional<User> findUserOptionalByEmail(String email);

    Optional<User> findOptionalByEmailAndStatusId(String email, UserStatus userStatus);

    User saveUser(User user);
    UserHist saveUserHist(UserHist userHist);
    UserHist saveUserHistByUser(User user);

    boolean isGoogleUser(User user);
    boolean isFacebookUser(User user);
    boolean isDefaultUser(User user);
}
