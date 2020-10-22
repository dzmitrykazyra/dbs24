/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.config;

import static org.dbs24.application.core.sysconst.SysConst.*;
import static org.dbs24.consts.WorldChessConst.*;
import static org.springframework.web.reactive.function.server.RequestPredicates.*;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.dbs24.rest.ChessRestApi;

@Configuration
@ComponentScan(basePackages = {SERVICE_PACKAGE, RESTFUL_PACKAGE})
@PropertySource(APP_PROPERTIES)
@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
public class SandBoxWebSecurityConfig extends AbstractWebSecurityConfig {

    @Bean
    public RouterFunction<ServerResponse> restChessSandBox(ChessRestApi chessRestApi) {

        return route(POST(URI_CREATE_CHESS_PLAYER).and(accept(MediaType.APPLICATION_JSON)), chessRestApi::createChessPlayer);
        //.andRoute(POST(URI_EXECUTE_ACTION).and(accept(MediaType.APPLICATION_JSON)), retailLoanContractHandler::executeAction)
        //.andRoute(GET("/findRetailLoanContract").and(accept(MediaType.APPLICATION_JSON)), retailLoanContractHandler::findRetailLoanContract);
    }

    @Bean
    @Profile("production")
    public SecurityWebFilterChain springSecurityFilterChain(final ServerHttpSecurity http) {

        return http
                .csrf().disable()
                .formLogin().disable()
                .authorizeExchange()
                .pathMatchers(AbstractWebSecurityConfig.WHITELISTED_AUTH_URLS)
                .permitAll()
                .and()
                .addFilterAt(this.basicAuthenticationFilter(), SecurityWebFiltersOrder.HTTP_BASIC)
                .authorizeExchange()
                .pathMatchers("/api/**")//.hasRole(environment.getProperty("webflux.security.role"))
                .authenticated()
                .and()
                .addFilterAt(this.bearerAuthenticationFilter(), SecurityWebFiltersOrder.AUTHENTICATION)
                .authorizeExchange()
                .anyExchange().authenticated()
                .and().build();
    }
}
