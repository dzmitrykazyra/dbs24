/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.tik.assist.config;

import lombok.extern.log4j.Log4j2;
import org.dbs24.config.MainApplicationConfig;
import org.dbs24.stmt.StmtProcessor;
import org.dbs24.tik.assist.dao.ReferenceDao;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.thymeleaf.spring5.templateresolver.SpringResourceTemplateResolver;
import org.thymeleaf.templatemode.TemplateMode;

import java.nio.charset.StandardCharsets;

@Log4j2
@Configuration
public class TikAssistConfig extends MainApplicationConfig {

    @Value("${refs.synchronize:true}")
    private Boolean needSynchronize;

    final ReferenceDao referenceDao;

    public TikAssistConfig(ReferenceDao referenceDao) {

        this.referenceDao = referenceDao;
    }

    @Override
    public void initialize() {

        super.initialize();

        StmtProcessor.ifTrue(
                needSynchronize,
                referenceDao::saveAllReferences,
                () -> log.info("system references synchronization is disabled ")
        );
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {

        return new BCryptPasswordEncoder();
    }

    @Bean
    public SpringTemplateEngine springTemplateEngine() {
        SpringTemplateEngine templateEngine = new SpringTemplateEngine();
        templateEngine.addTemplateResolver(htmlTemplateResolver());
        return templateEngine;
    }

    @Bean
    public SpringResourceTemplateResolver htmlTemplateResolver(){
        SpringResourceTemplateResolver emailTemplateResolver = new SpringResourceTemplateResolver();
        emailTemplateResolver.setPrefix("classpath:/templates/");
        emailTemplateResolver.setSuffix(".html");
        emailTemplateResolver.setTemplateMode(TemplateMode.HTML);
        emailTemplateResolver.setCharacterEncoding(StandardCharsets.UTF_8.name());
        return emailTemplateResolver;
    }
}
