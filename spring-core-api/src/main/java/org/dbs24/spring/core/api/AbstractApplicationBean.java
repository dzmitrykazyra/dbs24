/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.spring.core.api;

import javax.annotation.PreDestroy;
import lombok.extern.log4j.Log4j2;

@Log4j2
public abstract class AbstractApplicationBean implements ApplicationBean {
    
    @Override
    public void initialize() {
    }
    
    @Override
    public void destroy() {
    }
    
    @PreDestroy
    public void shoutingDown() {
        log.debug("{}: Shoutting down", this.getClass().getSimpleName());
    }    
}
