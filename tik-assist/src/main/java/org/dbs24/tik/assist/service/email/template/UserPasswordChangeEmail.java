package org.dbs24.tik.assist.service.email.template;

import lombok.extern.log4j.Log4j2;
import org.dbs24.tik.assist.service.email.Email;

import java.util.HashMap;
import java.util.Map;

@Log4j2
public class UserPasswordChangeEmail implements Email {

    private final String CHANGE_PASSWORD_EMAIL_SUBJECT = "Tikitok change password link";
    private final String TEMPLATE_FILE_NAME = "change-password";
    private final String USER_KEY_VARIABLE_NAME = "userKey";
    private final String EXPIRATION_KEY_VARIABLE_NAME = "expirationKey";

    private final Map<String, Object> variableNameToValue;

    private String sendAddress;
    private String userKey;
    private String expirationKey;

    public UserPasswordChangeEmail(String sendAddress, String userKey, String expirationKey) {

        this.sendAddress = sendAddress;
        this.userKey = userKey;
        this.expirationKey = expirationKey;
        this.variableNameToValue = new HashMap<>();

        initializeTemplateVariables();
    }

    private void initializeTemplateVariables() {

        variableNameToValue.put(USER_KEY_VARIABLE_NAME, userKey);
        variableNameToValue.put(EXPIRATION_KEY_VARIABLE_NAME, expirationKey);
    }

    @Override
    public Map<String, Object> getTemplateVariables() {

        return variableNameToValue;
    }

    @Override
    public String getTemplateFileName() {

        return TEMPLATE_FILE_NAME;
    }

    @Override
    public String getSubject() {

        return CHANGE_PASSWORD_EMAIL_SUBJECT;
    }

    @Override
    public String getSendAddress() {

        return sendAddress;
    }
}
