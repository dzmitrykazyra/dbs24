/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.config;

import java.util.Collection;
import lombok.extern.log4j.Log4j2;
import org.dbs24.application.core.service.funcs.ServiceFuncs;
import static org.dbs24.consts.SysConst.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
;
import org.springframework.context.annotation.PropertySource;
import org.springframework.messaging.rsocket.annotation.support.RSocketMessageHandler;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.rsocket.EnableRSocketSecurity;
import org.springframework.security.config.annotation.rsocket.RSocketSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.rsocket.core.PayloadSocketAcceptorInterceptor;
import static org.dbs24.consts.ServicesMonitoringConst.R_SOCKET_URI_MONITORING;



@Configuration
//@EnableRSocketSecurity
//@EnableReactiveMethodSecurity 
@ComponentScan(basePackages = {SERVICE_PACKAGE, CONTROLLER_PACKAGE, COMPONENT_PACKAGE})
@EntityScan(basePackages = {ENTITY_PACKAGE, REFERENCE_PACKAGE})
@PropertySource(APP_PROPERTIES)
@Log4j2
public class ServicesMonitoringRSocketConfig extends AbstractRSocketConfig {

    @Value("${sm.uid:smuser}")
    private String uid = "smuser";

    @Value("${sm.pwd:smpwd}")
    private String pwd = "smpwd";

    @Value("${sm.role:SMROLE}")
    private String role = "SMROLE";

    @Value(R_SOCKET_URI_MONITORING)
    private String socketRoute = "mon2toring";

    @Override
    protected PayloadSocketAcceptorInterceptor initPayloadSocketAcceptorInterceptor(RSocketSecurity security) {

        log.info("initialize socket acceptor '/{}'", socketRoute);

        return security
                .authorizePayload(spec
                        -> spec
                        .setup().hasRole(role)
                        .route(socketRoute).hasRole(role)
                        .anyRequest().authenticated()
                        .anyExchange().authenticated()
                )
                .simpleAuthentication(Customizer.withDefaults())
                .build();
    }

    @Override
    protected Collection<UserDetails> initUserDetails(PasswordEncoder passwordEncoder) {

        final Collection<UserDetails> collection = ServiceFuncs.createCollection();
        collection.add(User.withUsername(uid)
                .passwordEncoder(passwordEncoder::encode)
                .password(pwd)
                .roles(role)
                .build());

        return collection;
    }
    
    @Override
    protected void initRSocketMessageHandler(RSocketMessageHandler mh) {
        //mh.
    }    
}
