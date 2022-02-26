package org.dbs24.tik.assist.service.user.resolver;

import lombok.extern.log4j.Log4j2;
import org.dbs24.tik.assist.entity.domain.User;
import org.dbs24.tik.assist.service.email.EmailSender;
import org.dbs24.tik.assist.service.email.template.UserActivationEmail;
import org.dbs24.tik.assist.service.exception.ActivationKeyDoesNotExistException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

@Log4j2
@Service
public class UserActivationResolver {

    private final Map<String, String> activationKeyToEmail;

    private final EmailSender emailSender;
    private final Base64Resolver base64Resolver;

    @Value("${config.security.redirect.activation.key.length}")
    private int activationKeyLength;

    @Value("${config.security.redirect.url}")
    private String activationUrl;
    @Value("${config.security.redirect.activation.api}")
    private String activationApi;

    public UserActivationResolver(EmailSender emailSender, Base64Resolver base64Resolver) {

        this.base64Resolver = base64Resolver;
        this.activationKeyToEmail = new HashMap<>();
        this.emailSender = emailSender;
    }

    public String generateActivationKeyAndSendEmail(User user) {

        String activationKey = generateActivationKey(user);

        emailSender.sendEmail(
                new UserActivationEmail(
                        user.getEmail(),
                        activationKey
                )
        );

        putActivationKey(activationKey, user.getEmail());

        return activationKey;
    }

    private void putActivationKey(String key, String address) {

        activationKeyToEmail.put(key, address);
    }

    /**
     * Method creates Base64 key from user email
     */
    private String generateActivationKey(User user) {

        return base64Resolver.encodeStringWithBase64(user.getEmail());
    }

    public String activateCodeAndGetUserEmail(String activationKey) {

        if (!activationKeyToEmail.containsKey(activationKey)
                || !activationKeyToEmail.get(activationKey).equals(base64Resolver.decodeStringWithBase64(activationKey))) {
            throw new ActivationKeyDoesNotExistException(HttpStatus.BAD_REQUEST);
        }

        String userEmail = activationKeyToEmail.get(activationKey);
        activationKeyToEmail.remove(activationKey);

        return userEmail;
    }
}
