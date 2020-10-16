/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.spring.config;

import org.dbs24.application.core.nullsafe.NullSafe;
import org.dbs24.spring.core.application.setup.ExceptionsCollectorBean;
import org.dbs24.spring.core.mail.MailManager;
import org.dbs24.persistence.core.PersistanceEntityManager;
import org.springframework.context.annotation.Bean;
import org.dbs24.spring.core.repository.JpaRepositoriesCoolection;
import org.dbs24.service.WebClientMgmt;
import org.springframework.beans.factory.annotation.Value;
import lombok.Data;

/**
 *
 * @author Козыро Дмитрий
 */
@Data
public abstract class MainApplicationConfig extends AbstractApplicationConfiguration {

    @Value("${spring.profiles.active}")
    private String activeEnv;

    @Bean
    public PersistanceEntityManager entityManager() {
        return NullSafe.createObject(PersistanceEntityManager.class);
    }

    @Bean
    public ExceptionsCollectorBean exceptionManager() {
        return NullSafe.createObject(ExceptionsCollectorBean.class);
    }

    @Bean
    public WebClientMgmt webClientMgmt() {
        return NullSafe.<WebClientMgmt>createObject(WebClientMgmt.class);
    }

    @Bean
    public MailManager mailManager() {
        return NullSafe.<MailManager>createObject(MailManager.class);
    }

    @Bean
    public JpaRepositoriesCoolection repoManager() {
        return NullSafe.createObject(JpaRepositoriesCoolection.class);
    }
}
