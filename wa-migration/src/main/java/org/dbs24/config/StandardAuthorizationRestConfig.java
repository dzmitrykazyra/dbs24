/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.config;

import lombok.extern.log4j.Log4j2;
import org.dbs24.component.WaMigrationRest;
import org.dbs24.repository.*;
import static org.dbs24.consts.WaConsts.*;
import org.springframework.beans.factory.annotation.Autowired;
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
@ConditionalOnProperty(name = "config.restfull.profile.name", havingValue = "wa-migration")
public class StandardAuthorizationRestConfig extends AbstractWebSecurityConfig {

    final SubscriptionStatusRepository subscriptionStatusRepository;

    @Autowired
    public StandardAuthorizationRestConfig(SubscriptionStatusRepository subscriptionStatusRepository) {
        this.subscriptionStatusRepository = subscriptionStatusRepository;
    }

    @Bean
    public RouterFunction<ServerResponse> routerAuthorizationRest(
            WaMigrationRest waMonitoringRest) {
        return addCommonRoutes()
                .andRoute(getRoute(URI_GET_AGENTS), waMonitoringRest::getAgents)
                .andRoute(getRoute(URI_GET_APP_USERS), waMonitoringRest::getUsers)
                .andRoute(getRoute(URI_GET_ALL_SUBSCRIPTIONS), waMonitoringRest::getSubscriptions)
                .andRoute(getRoute(URI_GET_ACTUAL_APP_USERS), waMonitoringRest::getActualUsers);
//                .andRoute(postRoute(URI_CREATE_AGENT), waMonitoringRest::createAuthKey)
//                .andRoute(postRoute(URI_CREATE_CONTRACT), waMonitoringRest::createAuthKey)
//                .andRoute(postRoute(URI_CREATE_SUBSCRIPTION), waMonitoringRest::createAuthKey)
//                .andRoute(postRoute(URI_CREATE_ACTIVITY), waMonitoringRest::createAuthKey);

    }
}
