/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.component;

import java.time.LocalDateTime;
import java.util.Optional;
import lombok.Data;
import lombok.extern.log4j.Log4j2;
import org.dbs24.application.core.service.funcs.StringFuncs;
import static org.dbs24.consts.SysConst.*;
import static org.dbs24.consts.WaConsts.Classes.*;
import org.dbs24.entity.*;
import org.dbs24.exception.AppUserIsNotFound;
import org.dbs24.repository.*;
import org.dbs24.rest.api.*;
import org.dbs24.spring.core.api.AbstractApplicationService;
import org.dbs24.stmt.StmtProcessor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@Data
@Log4j2
@Component
@ConditionalOnProperty(name = "config.restfull.profile.name", havingValue = "wa-monitoring")
public class UsersService extends AbstractApplicationService {

    @Value("${config.wa.user.login.length:30}")
    private Integer loginLength;

    @Value("${config.wa.user.login.prefix:}")
    private String loginTokenPrefix;

    final UserRepository userRepository;
    final UserHistRepository userHistRepository;

    public UsersService(UserRepository userRepository, UserHistRepository userHistRepository) {
        this.userRepository = userRepository;
        this.userHistRepository = userHistRepository;
    }

    public void saveUser(User user) {
        userRepository.save(user);
    }

    public User createUser() {
        return StmtProcessor.create(USER_CLASS, a -> {
            a.setActualDate(LocalDateTime.now());
            a.setLoginToken(loginTokenPrefix.concat(StringFuncs.createRandomString(loginLength-loginTokenPrefix.length())));
        });
    }

    public User findUser(String loginToken) {

        StmtProcessor.assertNotNull(String.class, loginToken, "parameter 'loginToken'");

        return userRepository
                .findByLoginToken(loginToken)
                .orElseThrow(() -> new AppUserIsNotFound(String.format("userLogin not found ('%s', length=%d)", loginToken, loginToken.length())));
    }

    public User findUser(Integer userId) {

        return userRepository
                .findById(userId)
                .orElseThrow(() -> new AppUserIsNotFound(String.format("userId not found (%d)", userId)));
    }

    public User findLastUser() {
        return userRepository.findLastUser();
    }

    public User findUserByLoginToken(String loginToken) {

        return userRepository
                .findByLoginToken(loginToken)
                .orElseThrow(() -> new AppUserIsNotFound(String.format("userToken is not found (%s)", loginToken)));
    }

    public Optional<User> findOptionalUserByLoginToken(String loginToken) {

        return userRepository
                .findByLoginToken(loginToken);
    }

    public User findOrCreateUser(String loginToken) {
        return !(Optional.ofNullable(loginToken)
                .orElseGet(() -> EMPTY_STRING).isEmpty())
                ? findUser(loginToken)
                : createUser();
    }

    public void saveUserHist(UserHist userHist) {
        userHistRepository.save(userHist);
    }

    public void saveUserHist(User user) {
        Optional.ofNullable(user.getUserId())
                .ifPresent(id -> saveUserHist(StmtProcessor.create(USER_HIST_CLASS, userHist -> {
            userHist.setUserId(id);
            userHist.setCountry(user.getCountry());
            userHist.setActualDate(user.getActualDate());
            userHist.setEmail(user.getEmail());
            userHist.setLoginToken(user.getLoginToken());
            userHist.setUserName(user.getUserName());
            userHist.setUserPhoneNum(user.getUserPhoneNum());
        })));
    }

    //==========================================================================
    public LoginTokenInfo checkTokenValidity(String loginToken) {

        return StmtProcessor.create(LOGIN_TOKEN_INFO_CLASS, loginTokenInfo -> {

            loginTokenInfo.setIsValid(findOptionalUserByLoginToken(loginToken).isPresent());

            log.info("checkTokenValidity: loginToken {}, answer {}", loginToken, loginTokenInfo);

        });
    }
}
