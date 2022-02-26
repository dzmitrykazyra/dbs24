package org.dbs24.tik.mobile.service.impl;

import lombok.extern.log4j.Log4j2;
import org.dbs24.tik.mobile.entity.domain.User;
import org.dbs24.tik.mobile.entity.dto.user.UserEmailBoundingKeysetDto;
import org.dbs24.tik.mobile.service.StringEncoder;
import org.dbs24.tik.mobile.service.UserEmailKeyService;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Log4j2
@Component
public class UserEmailKeyServiceImpl implements UserEmailKeyService {

    private final Map<String, String> keyToEmail;

    private final StringEncoder stringEncoder;

    public UserEmailKeyServiceImpl(StringEncoder stringEncoder) {

        this.stringEncoder = stringEncoder;
        this.keyToEmail = new ConcurrentHashMap<>();
    }

    @Override
    public UserEmailBoundingKeysetDto generateEmailBoundingKeyset(String email, User user) {

        String key = generateKey(user);
        keyToEmail.put(key, email);

        return UserEmailBoundingKeysetDto.of(key);
    }

    /**
     * Key pattern is Base64 encoded 'sec_user_id'.'user_id'
     */
    private String generateKey(User user) {

        return stringEncoder.encode(
                user.getSecUserId()
                        .concat(".")
                        .concat(String.valueOf(user.getId()))
        );
    }

    @Override
    public String getEmailByKeyBoundingSet(UserEmailBoundingKeysetDto keyset) {

        return keyToEmail.get(keyset.getKey());
    }

    /**
     * As key consist of two parts, method decodes key and gets raw second part of key (separated by '.') as user_id
     */
    @Override
    public Integer getUserIdByKeyBoundingSet(UserEmailBoundingKeysetDto keyset) {

        return Integer.valueOf(
                stringEncoder.decode(keyset.getKey())
                        .split("\\.")[1]
        );
    }
}
