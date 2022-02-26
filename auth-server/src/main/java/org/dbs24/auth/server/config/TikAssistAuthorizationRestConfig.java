/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.auth.server.config;

import lombok.extern.log4j.Log4j2;
import org.dbs24.auth.server.component.TikAssistSecurityRest;
import org.dbs24.config.AbstractWebSecurityConfig;
import org.dbs24.stmt.StmtProcessor;
import org.springframework.beans.factory.annotation.Value;
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
//@ConditionalOnProperty(name = "config.restfull.security.tik-assist.enabled", havingValue = "true")
public class TikAssistAuthorizationRestConfig extends AbstractWebSecurityConfig {

    @Value("${refs.synchronize:true}")
    private Boolean needSynchronize;

    private final static String URI_GET_TIK_USER_JWT_BY_GOOGLE = "/api/getTikUserJwtByGoogle";
    private final static String URI_GET_TIK_USER_JWT_BY_FACEBOOK = "/api/getTikUserJwtByFacebook";

    @Override
    public String getAppUriPrefix() {

        return "tik";
    }

    @Bean
    public RouterFunction<ServerResponse> routerTikAssistAuthorizationRest(TikAssistSecurityRest tikAssistSecurityRest) {

        return addCommonRoutes()
                .andRoute(postRoute(URI_GET_TIK_USER_JWT_BY_GOOGLE), tikAssistSecurityRest::getTikUserJwtByGoogle)
                .andRoute(postRoute(URI_GET_TIK_USER_JWT_BY_FACEBOOK), tikAssistSecurityRest::getTikUserJwtFacebook);
    }

    @Override
    public void initialize() {

        super.initialize();

        StmtProcessor.ifTrue(needSynchronize, () -> {
            //refsService.synchronizeRefs();

        }, () -> log.info("system references synchronization is disabled "));
    }
}
