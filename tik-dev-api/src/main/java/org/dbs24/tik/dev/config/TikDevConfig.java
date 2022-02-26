/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.tik.dev.config;

import lombok.extern.log4j.Log4j2;
import org.dbs24.config.MainApplicationConfig;
import org.dbs24.tik.dev.dao.ReferencesDao;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import static org.dbs24.stmt.StmtProcessor.ifTrue;

@Log4j2
@Configuration
public class TikDevConfig extends MainApplicationConfig {

    final ReferencesDao referencesDao;

    @Value("${refs.synchronize:true}")
    private Boolean needSynchronize;

    public TikDevConfig(ReferencesDao referencesDao) {
        this.referencesDao = referencesDao;
    }

    @Override
    public void initialize() {

        super.initialize();

        ifTrue(needSynchronize, referencesDao::saveAllReferences,
                () -> log.info("system references synchronization is disabled ")
        );
    }
}
