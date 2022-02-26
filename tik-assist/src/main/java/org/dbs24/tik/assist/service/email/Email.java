package org.dbs24.tik.assist.service.email;

import java.util.Map;

public interface Email {

    String getSubject();

    String getSendAddress();

    Map<String, Object> getTemplateVariables();

    String getTemplateFileName();
}
