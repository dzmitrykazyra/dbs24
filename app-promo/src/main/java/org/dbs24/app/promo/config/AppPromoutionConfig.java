/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.app.promo.config;

import lombok.extern.log4j.Log4j2;
import org.dbs24.app.promo.component.RefsService;
import org.dbs24.config.MainApplicationConfig;
import org.dbs24.stmt.StmtProcessor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Log4j2
@Configuration
public class AppPromoutionConfig extends MainApplicationConfig {

    @Value("${refs.synchronize:true}")
    private Boolean needSynchronize;

    final RefsService refsService;

    public AppPromoutionConfig(RefsService refsService) {
        this.refsService = refsService;
    }

    @Override
    public void initialize() {

        super.initialize();

        StmtProcessor.ifTrue(needSynchronize, () -> refsService.synchronizeRefs(), () -> log.info("system references synchronization is disabled "));
    }
}
