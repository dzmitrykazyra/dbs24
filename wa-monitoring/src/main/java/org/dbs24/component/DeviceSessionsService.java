/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.component;

import java.time.LocalDateTime;
import java.util.Optional;
import lombok.extern.log4j.Log4j2;
import static org.dbs24.consts.WaConsts.Classes.*;
import org.dbs24.entity.*;
import org.dbs24.exception.AppUserIsNotFound;
import org.dbs24.repository.*;
import org.dbs24.spring.core.api.AbstractApplicationService;
import org.dbs24.stmt.StmtProcessor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import lombok.Data;

@Data
@Log4j2
@Component
@ConditionalOnProperty(name = "config.restfull.profile.name", havingValue = "wa-monitoring")
public class DeviceSessionsService extends AbstractApplicationService {

    final UserTokenRepository userTokenRepository;
    final DeviceSessionRepository deviceSessionRepository;

    public DeviceSessionsService(UserTokenRepository userTokenRepository, DeviceSessionRepository deviceSessionRepository) {
        this.userTokenRepository = userTokenRepository;
        this.deviceSessionRepository = deviceSessionRepository;
    }

    public void saveDeviceSession(DeviceSession deviceSession) {
        deviceSessionRepository.save(deviceSession);
    }

    public DeviceSession createDeviceSession() {
        return StmtProcessor.create(DEVICE_SESSION_CLASS, a -> {
            a.setActualDate(LocalDateTime.now());
        });
    }

    public DeviceSession findDeviceSession(Long deviceSessionId) {

        return deviceSessionRepository
                .findById(deviceSessionId)
                .orElseThrow(() -> new AppUserIsNotFound(String.format("DeviceSessionId not found ({})", deviceSessionId)));
    }

    public DeviceSession findOrCreateDeviceSession(Long deviceSessionId) {
        return (Optional.ofNullable(deviceSessionId)
                .orElseGet(() -> Long.valueOf(0)) > 0)
                ? findDeviceSession(deviceSessionId)
                : createDeviceSession();
    }

    public void saveUserToken(UserToken userToken) {
        userTokenRepository.save(userToken);
    }

    public UserToken createUserToken() {
        return StmtProcessor.create(USER_TOKEN_CLASS, a -> {
            a.setCreated(LocalDateTime.now());
        });
    }

    public UserToken findUserToken(Long userTokenId) {

        return userTokenRepository
                .findById(userTokenId)
                .orElseThrow(() -> new AppUserIsNotFound(String.format("UserTokenId not found ({})", userTokenId)));
    }

    public UserToken findOrCreateUserToken(Long userTokenId) {
        return (Optional.ofNullable(userTokenId)
                .orElseGet(() -> Long.valueOf(0)) > 0)
                ? findUserToken(userTokenId)
                : createUserToken();
    }
}
