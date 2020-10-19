/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.spring.core.api;

import org.dbs24.application.core.log.LogService;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

public interface ApplicationService extends ApplicationBean {

    @PostConstruct
    public default void afterConstruction() {
        LogService.LogInfo(this.getClass(), () -> String.format("Service has been created (%s)", this.getClass().getCanonicalName()));

        ServiceLocator.registerService(this);

    }

    @PreDestroy
    public default void beforeDestroy() {
        LogService.LogInfo(this.getClass(), () -> String.format("Service has been destroyed (%s)", this.getClass().getCanonicalName()));

        ServiceLocator.releaseService(this);
    }
}
