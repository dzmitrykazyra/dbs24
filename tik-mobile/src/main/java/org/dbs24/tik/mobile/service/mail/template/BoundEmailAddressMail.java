package org.dbs24.tik.mobile.service.mail.template;

import org.dbs24.tik.mobile.entity.dto.user.UserEmailBoundingKeysetDto;
import org.dbs24.tik.mobile.entity.dto.user.UserForgottenPasswordKeysetDto;
import org.dbs24.tik.mobile.service.mail.Email;
import org.springframework.beans.factory.annotation.Value;

import java.util.HashMap;
import java.util.Map;

public class BoundEmailAddressMail implements Email {

    private String subject = "Hello! It is TT Boost!";
    private String fileName = "bound-email";

    private final String KEY_TEMPLATE_VAR = "key";

    private String sendAddress;
    private final UserEmailBoundingKeysetDto keyset;

    private final Map<String, Object> variableNameToValue;

    public BoundEmailAddressMail(String sendAddress, UserEmailBoundingKeysetDto keyset) {

        this.sendAddress = sendAddress;
        this.keyset = keyset;
        this.variableNameToValue = new HashMap<>();

        initializeTemplateVariables();
    }

    private void initializeTemplateVariables() {

        variableNameToValue.put(KEY_TEMPLATE_VAR, keyset.getKey());
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
