package org.dbs24.tik.assist.service.email.template;

import org.dbs24.tik.assist.service.email.Email;

import java.util.HashMap;
import java.util.Map;

public class UserPromocodeEmail implements Email {

    private final String PROMOCODE_EMAIL_SUBJECT = "Congrats!!! Now you can use this promocode in TikiTop application and enjoy your life";
    private final String PROMOCODE_KEY_VARIABLE_NAME = "promocodeKeyVariable";
    private final String TEMPLATE_FILE_NAME = "promocode";

    private final Map<String, Object> variableNameToValue;

    private String sendAddress;
    private String promocodeValue;

    public UserPromocodeEmail(String sendAddress, String promocodeValue) {

        this.variableNameToValue = new HashMap<>();
        this.sendAddress = sendAddress;
        this.promocodeValue = promocodeValue;

        initializeTemplateVariables();
    }

    private void initializeTemplateVariables() {

        variableNameToValue.put(PROMOCODE_KEY_VARIABLE_NAME, promocodeValue);
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

        return PROMOCODE_EMAIL_SUBJECT;
    }

    @Override
    public String getSendAddress() {

        return sendAddress;
    }
}
