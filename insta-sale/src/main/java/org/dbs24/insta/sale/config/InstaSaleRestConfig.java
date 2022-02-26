/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.insta.sale.config;

import lombok.extern.log4j.Log4j2;
import org.dbs24.config.AbstractWebSecurityConfig;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
@Log4j2
@Configuration
@EnableCaching
@ConditionalOnProperty(name = "config.restfull.profile.name", havingValue = "insta-sale")
public class InstaSaleRestConfig extends AbstractWebSecurityConfig {

    @Bean
    public RouterFunction<ServerResponse> routerInstaSaleRest() {

        return addCommonRoutes();
                // Accounts
                //.andRoute(postRoute(URI_CREATE_OR_UPDATE_ACCOUNT), accountsRest::createOrUpdateAccount)
                //.andRoute(getRoute(URI_VALIDATE_ACCOUNT), accountsRest::validateInstaAccount)                
                // Posts

    }
}
