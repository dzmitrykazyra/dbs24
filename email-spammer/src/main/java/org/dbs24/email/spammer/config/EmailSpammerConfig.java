/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.email.spammer.config;

import lombok.extern.log4j.Log4j2;
import org.dbs24.config.MainApplicationConfig;
import org.dbs24.email.spammer.dao.ReferenceDao;
import org.springframework.context.annotation.Configuration;

@Log4j2
@Configuration
public class EmailSpammerConfig extends MainApplicationConfig {

    private final ReferenceDao referenceDao;

    public EmailSpammerConfig(ReferenceDao referenceDao) {

        this.referenceDao = referenceDao;
    }

    @Override
    public void initialize() {
        referenceDao.saveAllApplications();
        super.initialize();
    }
}
