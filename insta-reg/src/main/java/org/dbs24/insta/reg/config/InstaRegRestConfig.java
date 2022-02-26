/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.insta.reg.config;

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
import org.dbs24.insta.reg.rest.InstaRegCommonRest;
import static org.dbs24.insta.reg.consts.InstaConsts.UriConsts.*;

@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
@Log4j2
@Configuration
@EnableCaching
@ConditionalOnProperty(name = "config.restfull.profile.name", havingValue = "insta-reg")
public class InstaRegRestConfig extends AbstractWebSecurityConfig {

    @Bean
    public RouterFunction<ServerResponse> routerInstaRegRest(
            InstaRegCommonRest instaRegCommonRest) {

        return addCommonRoutes()
                // fakedMails
                .andRoute(getRoute(URI_GET_FAKED_MAIL), instaRegCommonRest::getFakedMail)
                .andRoute(getRoute(URI_GET_LATEST_EXCEPTIONS), instaRegCommonRest::getLatestExceptions);
    }
}
