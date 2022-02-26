/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.proxy.core.config;

import lombok.extern.log4j.Log4j2;
import org.dbs24.config.MainApplicationConfig;
import org.dbs24.proxy.core.component.ReferenceService;
import org.dbs24.stmt.StmtProcessor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Log4j2
@Configuration
public class ProxyCoreConfig extends MainApplicationConfig {

    @Value("${refs.synchronize:true}")
    private Boolean needSynchronize;

    final ReferenceService referenceService;

    public ProxyCoreConfig(ReferenceService referenceService) {
        this.referenceService = referenceService;
    }

    @Override
    public void initialize() {

        super.initialize();

        StmtProcessor.ifTrue(needSynchronize, () -> {
            referenceService.synchronizeRefs();
        }, () -> log.info("system references synchronization is disabled "));
    }
}
