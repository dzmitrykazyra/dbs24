/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;

import org.dbs24.config.MainApplicationConfig;
import org.dbs24.service.RetailLoanContractActionsService;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import static org.dbs24.consts.SysConst.*;
import static org.dbs24.consts.SysConst.*;
import org.dbs24.config.RetailLoanContractCommonConfig;
import org.springframework.context.annotation.Bean;
//import org.springframework.security.oauth2.jwt.ReactiveJwtDecoder;

@Configuration
public class TestRetailLoanContractCommonConfig extends RetailLoanContractCommonConfig {

//    @Bean
//    public ReactiveJwtDecoder jwtDecoder() {
//        return new NimbusReactiveJwtDecoder(keySourceUrl);
//    }
//
//    @Bean
//    public ReactiveAuthenticationManager authenticationManager() {
//        return new JwtReactiveAuthenticationManager(jwtDecoder());
//    }
}
