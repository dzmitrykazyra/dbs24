package org.dbs24.tik.assist.service.email.template;

import lombok.extern.log4j.Log4j2;
import org.dbs24.tik.assist.service.email.Email;

import java.util.HashMap;
import java.util.Map;

@Log4j2
public class UserActivationEmail implements Email {

    private final String ACTIVATION_EMAIL_SUBJECT = "Verify your email address to have fun with Tiki Top";
    private final String ACTIVATION_KEY_VARIABLE_NAME = "activationKeyVariable";
    private final String TEMPLATE_FILE_NAME = "activation";

    private final Map<String, Object> variableNameToValue;

    private String sendAddress;
    private String activationKey;

    public UserActivationEmail(String sendAddress, String activationKey) {

        this.variableNameToValue = new HashMap<>();
        this.sendAddress = sendAddress;
        this.activationKey = activationKey;

        initializeTemplateVariables();
    }

    private void initializeTemplateVariables() {

        variableNameToValue.put(ACTIVATION_KEY_VARIABLE_NAME, activationKey);
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

        return ACTIVATION_EMAIL_SUBJECT;
    }

    @Override
    public String getSendAddress() {

        return sendAddress;
    }
}