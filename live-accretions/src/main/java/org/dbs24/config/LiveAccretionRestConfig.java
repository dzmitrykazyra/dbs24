/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.config;

import static org.dbs24.consts.SysConst.*;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.context.annotation.Bean;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;
import lombok.extern.log4j.Log4j2;
import org.dbs24.config.AbstractWebSecurityConfig;

@Log4j2
@Configuration
@ComponentScan(basePackages = {SERVICE_PACKAGE, RESTFUL_PACKAGE, COMPONENT_PACKAGE})
@PropertySource(APP_PROPERTIES)
@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
public class LiveAccretionRestConfig extends AbstractWebSecurityConfig {

    @Bean
    public RouterFunction<ServerResponse> routerLiveAccretions() {
        return addCommonRoutes();
    }
}
