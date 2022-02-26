package org.dbs24.tik.mobile.config;

import lombok.extern.log4j.Log4j2;
import org.dbs24.config.MainApplicationConfig;
import org.dbs24.stmt.StmtProcessor;
import org.dbs24.tik.mobile.dao.ReferenceDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.thymeleaf.spring5.templateresolver.SpringResourceTemplateResolver;
import org.thymeleaf.templatemode.TemplateMode;

import java.nio.charset.StandardCharsets;

@Configuration
@Log4j2
public class TikMobileConfig extends MainApplicationConfig {

    @Value("${reference.synchronize}")
    private Boolean isNeedSynchronization;

    private final ReferenceDao referenceDao;

    @Autowired
    public TikMobileConfig(ReferenceDao referenceDao) {
        this.referenceDao = referenceDao;
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    public void initialize() {
        super.initialize();

        StmtProcessor.ifTrue(
                isNeedSynchronization,
                referenceDao::saveAllReferences,
                () -> log.info("System references synchronization is disabled ")
        );
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
