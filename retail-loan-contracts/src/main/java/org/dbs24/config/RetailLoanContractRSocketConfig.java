/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.config;

import java.util.Collection;
import lombok.extern.log4j.Log4j2;
import org.dbs24.stmt.StmtProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.Customizer;
import static org.dbs24.consts.SysConst.*;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.config.annotation.rsocket.EnableRSocketSecurity;
import org.springframework.security.config.annotation.rsocket.RSocketSecurity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.rsocket.core.PayloadSocketAcceptorInterceptor;
import org.dbs24.application.core.service.funcs.ServiceFuncs;
import org.springframework.context.annotation.Profile;
import org.springframework.security.core.userdetails.User;
//import org.dbs24.rsocket.*;
import org.springframework.util.Assert;
//import org.dbs24.rsocket.codec.RetailLoanContractEncoder;
//import org.springframework.core.codec.Encoder;
//import org.springframework.core.codec.Decoder;

@Configuration
@ComponentScan(basePackages = {SERVICE_PACKAGE, CONTROLLER_PACKAGE, COMPONENT_PACKAGE})
@EntityScan(basePackages = {ENTITY_PACKAGE, REFERENCE_PACKAGE})
@PropertySource(APP_PROPERTIES)
@EnableRSocketSecurity
@Log4j2
public class RetailLoanContractRSocketConfig extends AbstractRSocketConfig {

    @Override
    @Profile("production")
    protected PayloadSocketAcceptorInterceptor initPayloadSocketAcceptorInterceptor(RSocketSecurity security) {
        return security
                .authorizePayload(spec
                        -> spec
                        .route("greetings").authenticated()
                        .anyExchange().permitAll()
                )
                .simpleAuthentication(Customizer.withDefaults())
                .build();
    }

    @Override
    @Profile("production")
    protected Collection<UserDetails> initUserDetails(PasswordEncoder passwordEncoder) {
        final Collection<UserDetails> collection = ServiceFuncs.createCollection();
        collection.add(User.withUsername("admin")
                .passwordEncoder(passwordEncoder::encode)
                .password("password")
                .roles("ADMIN")
                .build());

        collection.add(User.withUsername("setup")
                .passwordEncoder(passwordEncoder::encode)
                .password("password")
                .roles("SETUP")
                .build());
        return collection;

    }
}
