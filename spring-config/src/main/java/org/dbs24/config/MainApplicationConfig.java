/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.dbs24.application.core.nullsafe.NullSafe;
import org.dbs24.spring.core.mail.MailManager;
import org.springframework.context.annotation.Bean;
//import org.dbs24.service.WebClientMgmt;
import lombok.Data;
import static com.fasterxml.jackson.databind.DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT;
import static com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES;
import static org.dbs24.consts.SysConst.*;

@Data
public abstract class MainApplicationConfig extends AbstractApplicationConfiguration {

//    @Bean
//    public ExceptionsCollectorBean exceptionManager() {
//        return NullSafe.createObject(ExceptionsCollectorBean.class);
//    }
    @Bean
    public ObjectMapper objectMapper() {

        final ObjectMapper objectMapper = new ObjectMapper();

        objectMapper.enable(ACCEPT_EMPTY_STRING_AS_NULL_OBJECT);
        // игнорируем ненужные поля
        objectMapper.configure(FAIL_ON_UNKNOWN_PROPERTIES, BOOLEAN_FALSE);
        
//        objectMapper.registerModule(new ParameterNamesModule());
//        objectMapper.registerModule(new Jdk8Module());
//        objectMapper.registerModule(new JavaTimeModule());
//        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        

        return objectMapper;
    }

//    @Bean
//    public WebClientMgmt webClientMgmt() {
//        return NullSafe.<WebClientMgmt>createObject(WebClientMgmt.class);
//    }

    @Bean
    public MailManager mailManager() {
        return NullSafe.<MailManager>createObject(MailManager.class);
    }

//    @Bean
//    public JpaRepositoriesCoolection repoManager() {
//        return NullSafe.createObject(JpaRepositoriesCoolection.class);
//    }
}
