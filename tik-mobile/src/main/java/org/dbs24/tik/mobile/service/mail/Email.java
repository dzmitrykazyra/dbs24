package org.dbs24.tik.mobile.service.mail;

import java.util.Map;

public interface Email {

    String getSubject();

    String getSendAddress();

    Map<String, Object> getTemplateVariables();

    String getTemplateFileName();
}

