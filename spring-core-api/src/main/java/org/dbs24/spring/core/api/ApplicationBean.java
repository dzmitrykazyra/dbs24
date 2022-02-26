/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.spring.core.api;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;

public interface ApplicationBean extends InitializingBean, DisposableBean {

    void initialize();

    void shutdown();

    @Override
    default void afterPropertiesSet() throws Exception {
        ServiceLocator.registerService(this);
        initialize();
    }

    @Override
    default void destroy() throws Exception {
        ServiceLocator.releaseService(this);
        shutdown();
    }
}
