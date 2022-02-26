package org.dbs24.tik.mobile.service.mail.template;

import org.dbs24.tik.mobile.entity.dto.user.UserForgottenPasswordKeysetDto;
import org.dbs24.tik.mobile.service.mail.Email;
import org.springframework.beans.factory.annotation.Value;

import java.util.HashMap;
import java.util.Map;

public class ForgotPasswordMail implements Email {

    private String subject = "Hello! It is TT Boost!";
    private String fileName = "forgot-password";

    private final String USER_KEY_TEMPLATE_VAR = "userKey";
    private final String EXPIRATION_KEY_TEMPLATE_VAR = "expirationKey";

    private String sendAddress;
    private final UserForgottenPasswordKeysetDto keyset;

    private final Map<String, Object> variableNameToValue;

    public ForgotPasswordMail(String sendAddress, UserForgottenPasswordKeysetDto keyset) {

        this.sendAddress = sendAddress;
        this.keyset = keyset;
        this.variableNameToValue = new HashMap<>();

        initializeTemplateVariables();
    }

    private void initializeTemplateVariables() {

        variableNameToValue.put(USER_KEY_TEMPLATE_VAR, keyset.getUserKey());
        variableNameToValue.put(EXPIRATION_KEY_TEMPLATE_VAR, keyset.getExpirationKey());
    }

    @Override
    public String getSubject() {

        return subject;
    }

    @Override
    public String getSendAddress() {

        return sendAddress;
    }

    @Override
    public Map<String, Object> getTemplateVariables() {

        return variableNameToValue;
    }

    @Override
    public String getTemplateFileName() {

        return fileName;
    }
}
