/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.auth.server.config;


import org.dbs24.auth.server.wa.WaSecurityRest;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.dbs24.config.AbstractWebSecurityConfig;
import org.dbs24.stmt.StmtProcessor;
import org.dbs24.auth.server.component.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.reactive.function.server.HandlerFunction;

@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
@Log4j2
@Configuration
@EnableCaching
@ConditionalOnProperty(name = "config.restfull.security.wa-monitoring.enabled", havingValue = "true")
public class WaAuthorizationRestConfig extends AbstractWebSecurityConfig {

    @Value("${refs.synchronize:true}")
    private Boolean needSynchronize;

    final RefsService refsService;
    final WaSecurityRest waSecurityRest;

    public WaAuthorizationRestConfig(RefsService refsService, WaSecurityRest waSecurityRest) {
        this.refsService = refsService;
        this.waSecurityRest = waSecurityRest;
    }
   
    @Override
    public HandlerFunction getLoginHandlerFunction() {
        return waSecurityRest::login;
    }

    @Bean
    public RouterFunction<ServerResponse> routerWaAuthorizationRest() {
        return addCommonRoutes();
    }

    @Override
    public void initialize() {

        super.initialize();

        StmtProcessor.ifTrue(needSynchronize, () -> {
            refsService.synchronizeRefs();

        }, () -> log.info("system references synchronization is disabled "));

    }
}
