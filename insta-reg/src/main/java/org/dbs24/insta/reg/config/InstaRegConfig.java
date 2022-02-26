/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.insta.reg.config;

import lombok.extern.log4j.Log4j2;
import org.dbs24.config.MainApplicationConfig;
import org.dbs24.insta.reg.component.RefsService;
import org.dbs24.insta.reg.component.EmailService;
import org.dbs24.insta.reg.component.AccountBuilder;
import org.dbs24.stmt.StmtProcessor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Log4j2
@Configuration
public class InstaRegConfig extends MainApplicationConfig {

    @Value("${refs.synchronize:true}")
    private Boolean needSynchronize;

    final RefsService refsService;
    final EmailService emailService;
    final AccountBuilder accountBuilder;

    public InstaRegConfig(RefsService refsService, EmailService emailService, AccountBuilder accountBuilder) {
        this.refsService = refsService;
        this.emailService = emailService;
        this.accountBuilder = accountBuilder;
    }

    @Override
    public void initialize() {

        super.initialize();

        StmtProcessor.ifTrue(needSynchronize, () -> {
            refsService.synchronizeRefs();
            emailService.synchronizeRefs();
        }, () -> log.info("system references synchronization is disabled "));

        accountBuilder.load();

    }
}
