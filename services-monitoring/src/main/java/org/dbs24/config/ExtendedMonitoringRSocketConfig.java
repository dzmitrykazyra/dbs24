/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.config;

import lombok.extern.log4j.Log4j2;
import static org.dbs24.consts.SysConst.COMPONENT_PACKAGE;
import static org.dbs24.consts.SysConst.CONTROLLER_PACKAGE;
import static org.dbs24.consts.SysConst.ENTITY_PACKAGE;
import static org.dbs24.consts.SysConst.REFERENCE_PACKAGE;
import static org.dbs24.consts.SysConst.SERVICE_PACKAGE;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
//@EnableRSocketSecurity
//@EnableReactiveMethodSecurity 
@ComponentScan(basePackages = {SERVICE_PACKAGE, CONTROLLER_PACKAGE, COMPONENT_PACKAGE})
@EntityScan(basePackages = {ENTITY_PACKAGE, REFERENCE_PACKAGE})
@ConditionalOnProperty(name = "config.rsocket.name", havingValue = "extended")
@Log4j2
public class ExtendedMonitoringRSocketConfig extends StandardMonitoringRSocketConfig {
    
}
