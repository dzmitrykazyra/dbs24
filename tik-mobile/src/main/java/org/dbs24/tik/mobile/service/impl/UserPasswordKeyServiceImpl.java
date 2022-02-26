package org.dbs24.tik.mobile.service.impl;

import lombok.extern.log4j.Log4j2;
import org.dbs24.tik.mobile.entity.domain.User;
import org.dbs24.tik.mobile.entity.dto.user.UserForgottenPasswordKeysetDto;
import org.dbs24.tik.mobile.service.StringEncoder;
import org.dbs24.tik.mobile.service.UserPasswordKeyService;
import org.dbs24.tik.mobile.service.exception.http.ForbiddenException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Log4j2
@Service
public class UserPasswordKeyServiceImpl implements UserPasswordKeyService {

    private final Map<Integer, UserForgottenPasswordKeysetDto> userIdToKeyset;

    private final StringEncoder stringEncoder;

    @Value("${config.security.user.password.keyset.lifetime}")
    private long keysetLifetimeMillis;

    public UserPasswordKeyServiceImpl(StringEncoder stringEncoder) {

        this.stringEncoder = stringEncoder;
        this.userIdToKeyset = new ConcurrentHashMap<>();
    }

    @Override
    public UserForgottenPasswordKeysetDto generateChangePasswordKeyset(User user) {

        UserForgottenPasswordKeysetDto keysetDto = new UserForgottenPasswordKeysetDto();

        String userKey = generateUserKey(user);
        String expirationKey = generateExpirationKey();

        keysetDto.setUserKey(userKey);
        keysetDto.setExpirationKey(expirationKey);

        userIdToKeyset.put(user.getId(), keysetDto);

        return keysetDto;
    }

    private String generateUserKey(User user) {

        return stringEncoder.encode(String.valueOf(user.getId()));
    }

    private String generateExpirationKey() {

        return stringEncoder.encode(String.valueOf(System.currentTimeMillis() + keysetLifetimeMillis));
    }

    @Override
    public Integer getUserIdByChangePasswordKeyset(UserForgottenPasswordKeysetDto keyset) {

        Integer userId = getUserId(keyset);
        removeExpiredKeysets();

        if (!userIdToKeyset.containsKey(userId)
                || isKeysetExpired(keyset)
                || !userIdToKeyset.get(userId).getExpirationKey().equals(keyset.getExpirationKey())
        ) {
            throw new ForbiddenException();
        }

        userIdToKeyset.remove(userId);

        return userId;
    }

    private Integer getUserId(UserForgottenPasswordKeysetDto keyset) {

        return stringEncoder.decodeToInteger(keyset.getUserKey());
    }

    private void removeExpiredKeysets() {

        userIdToKeyset.entrySet().removeIf(userIdToKeyset -> isKeysetExpired(userIdToKeyset.getValue()));
    }

    private boolean isKeysetExpired(UserForgottenPasswordKeysetDto keyset) {

        return System.currentTimeMillis() > getKeysetExpirationTime(keyset);
    }

    private long getKeysetExpirationTime(UserForgottenPasswordKeysetDto keyset) {

        return stringEncoder.decodeToLong(keyset.getExpirationKey());
    }
}
