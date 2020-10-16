/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;

import org.dbs24.spring.config.MainApplicationConfig;
import org.dbs24.service.RetailLoanContractActionsService;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.dbs24.application.core.sysconst.SysConst;
import static org.dbs24.application.core.sysconst.SysConst.*;
import org.dbs24.config.RetailLoanContractCommonConfig;
import org.springframework.context.annotation.Bean;
//import org.springframework.security.oauth2.jwt.ReactiveJwtDecoder;

/**
 *
 * @author Козыро Дмитрий
 */
@Configuration
@ComponentScan(basePackages = {SERVICE_PACKAGE, RESTFUL_PACKAGE, COMPONENT_PACKAGE})
@EntityScan(basePackages = {ENTITY_PACKAGE, REFERENCE_PACKAGE})
@PropertySource(SysConst.APP_PROPERTIES)
//@EnableJpaRepositories(basePackages = SysConst.REPOSITORY_PACKAGE)
@Import({RetailLoanContractActionsService.class})
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
