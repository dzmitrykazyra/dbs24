/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.config;

import lombok.Data;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;
import org.dbs24.spring.config.AbstractApplicationConfiguration;
import static org.dbs24.application.core.sysconst.SysConst.*;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@Configuration
@ComponentScan(basePackages = {SERVICE_PACKAGE})
@EntityScan(basePackages = {ENTITY_PACKAGE, SECURITY_PACKAGE})
@PropertySource(APP_PROPERTIES)
public class SecurityConfig extends AbstractApplicationConfiguration {

//    @Bean
//    SecurityRepository securityRepository() {
//        return NullSafe.createObject(SecurityRepository.class);
//    }
}
