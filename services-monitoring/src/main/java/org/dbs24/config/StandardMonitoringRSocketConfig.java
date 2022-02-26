/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.config;

import lombok.AccessLevel;
import lombok.extern.log4j.Log4j2;
import static org.dbs24.consts.SysConst.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.messaging.rsocket.annotation.support.RSocketMessageHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.security.rsocket.core.PayloadSocketAcceptorInterceptor;
import static org.dbs24.consts.ServicesMonitoringConst.R_SOCKET_URI_MONITORING;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;

@Configuration
//@EnableRSocketSecurity
//@EnableReactiveMethodSecurity 
@ComponentScan(basePackages = {SERVICE_PACKAGE, CONTROLLER_PACKAGE, COMPONENT_PACKAGE})
@EntityScan(basePackages = {ENTITY_PACKAGE, REFERENCE_PACKAGE})
@ConditionalOnProperty(name = "config.rsocket.name", havingValue = "standard", matchIfMissing = true)
@Log4j2
public class StandardMonitoringRSocketConfig extends AbstractRSocketConfig {

    @Value(R_SOCKET_URI_MONITORING)
    private String socketRoute;

    @Override
    protected PayloadSocketAcceptorInterceptor initPayloadSocketAcceptorInterceptor() {

        log.info("initialize socket acceptor '/{}'", socketRoute);

        return this.buildSingleRoute(socketRoute);
    }

    @Override
    protected void initRSocketMessageHandler(RSocketMessageHandler mh) {
        //mh.
    }
}
