package org.dbs24.tik.assist.service.user.resolver;

import lombok.extern.log4j.Log4j2;
import org.dbs24.application.core.locale.NLS;
import org.dbs24.tik.assist.entity.domain.User;
import org.dbs24.tik.assist.entity.dto.user.ChangeForgottenPasswordDto;
import org.dbs24.tik.assist.service.email.EmailSender;
import org.dbs24.tik.assist.service.email.template.UserPasswordChangeEmail;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Log4j2
@Service
public class UserPasswordResolver {

    /**
     * Collection to guarantee uniqueness of userKey - expirationKey pair.
     * Putting keySet if user would send forgot password request.
     * Removing keySet if user used one-time email link or expirationKey is invalid
     */
    private Map<String, String> userKeyToExpirationKey;

    private final EmailSender emailSender;
    private final Base64Resolver base64Resolver;

    @Value("${config.security.redirect.password-change.key.lifetime-hours}")
    private int expirationKeyLifetimeHours;

    public UserPasswordResolver(EmailSender emailSender, Base64Resolver base64Resolver) {

        this.userKeyToExpirationKey = new ConcurrentHashMap<>();
        this.emailSender = emailSender;
        this.base64Resolver = base64Resolver;
    }

    public Integer sendChangePasswordEmail(User user) {

        String userKey = generateUserKey(user);
        String expirationKey = generateExpirationKey();
        putKeys(userKey, expirationKey);

        emailSender.sendEmail(new UserPasswordChangeEmail(user.getEmail(), userKey, expirationKey));

        return user.getUserId();
    }

    public String getUserEmailByForgotPasswordDto(ChangeForgottenPasswordDto forgottenPasswordDto) {

        return base64Resolver.decodeStringWithBase64(
                        forgottenPasswordDto.getUserKey()
        );
    }

    public String generateUserKey(User user) {

        return base64Resolver.encodeStringWithBase64(user.getEmail());
    }

    public String generateExpirationKey() {

        return base64Resolver.encodeStringWithBase64(
                String.valueOf(
                        NLS.localDateTime2long(
                                LocalDateTime
                                        .now()
                                        .plusHours(expirationKeyLifetimeHours)
                        )
                )
        );
    }

    public boolean useKeys(ChangeForgottenPasswordDto forgottenPasswordDto) {

        boolean isDtoValid = isKeySetValid(forgottenPasswordDto.getUserKey(), forgottenPasswordDto.getExpirationKey());

        refreshKeysCollection();
        removeKeys(forgottenPasswordDto);

        return isDtoValid;
    }

    public boolean isKeySetValid(String userKey, String expirationKey) {

        return userKeyToExpirationKey.containsKey(userKey)
                && userKeyToExpirationKey.containsValue(expirationKey);
    }

    private void putKeys(String userKey, String userValue) {

        userKeyToExpirationKey.put(userKey, userValue);
    }

    private void removeKeys(ChangeForgottenPasswordDto forgottenPasswordDto) {

        userKeyToExpirationKey.remove(forgottenPasswordDto.getUserKey());
    }

    /**
     * Method allows remove values with expiration_time < current_time
     */
    private void refreshKeysCollection() {

        userKeyToExpirationKey = userKeyToExpirationKey
                .entrySet()
                .stream()
                .filter(entry -> Long.parseLong(base64Resolver.decodeStringWithBase64(entry.getValue())) > System.currentTimeMillis())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }
}
