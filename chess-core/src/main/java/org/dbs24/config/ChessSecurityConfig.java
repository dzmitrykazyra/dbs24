/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.config;

import static org.dbs24.application.core.sysconst.SysConst.*;
import static org.dbs24.consts.WorldChessConst.URI_CREATE_CHESS_PLAYER;
import static org.dbs24.consts.WorldChessConst.URI_CREATE_CHESS_GAME;
import static org.dbs24.consts.WorldChessConst.URI_FIND_CHESS_PLAYER;
import static org.dbs24.consts.WorldChessConst.URI_EXECUTE_ACTION;
//import static org.dbs24.entity.core.api.EntityContractConst.URI_EXECUTE_ACTION;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.http.MediaType;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RequestPredicates.accept;
import org.springframework.web.reactive.function.server.RouterFunction;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.dbs24.rest.*;
import lombok.extern.slf4j.Slf4j; 

@Slf4j 
@Configuration
@ComponentScan(basePackages = {SERVICE_PACKAGE, RESTFUL_PACKAGE, COMPONENT_PACKAGE})
//@EntityScan(basePackages = {ENTITY_PACKAGE, REFERENCE_PACKAGE})
@PropertySource(APP_PROPERTIES)
//@EnableWebFlux
@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
public class ChessSecurityConfig extends AbstractWebSecurityConfig {

    @Bean
    public RouterFunction<ServerResponse> restChessBean(ChessRestApi chessRestApiHandler) {

        return route(POST(URI_CREATE_CHESS_PLAYER).and(accept(MediaType.APPLICATION_JSON)), chessRestApiHandler::createChessPlayer)
                .andRoute(POST(URI_CREATE_CHESS_GAME).and(accept(MediaType.APPLICATION_JSON)), chessRestApiHandler::createClassisChessGame)
                .andRoute(POST(URI_EXECUTE_ACTION).and(accept(MediaType.APPLICATION_JSON)), chessRestApiHandler::executeAction)
                .andRoute(GET(URI_FIND_CHESS_PLAYER).and(accept(MediaType.APPLICATION_JSON)), chessRestApiHandler::findChessPlayer);

    }
    //==========================================================================

    @Bean
    @Profile("production")
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
        
        log.debug("create bean");
//        final AuthenticationWebFilter authenticationJWT = new AuthenticationWebFilter(new UserDetailsRepositoryReactiveAuthenticationManager(userDetailsService()));
//        authenticationJWT.setAuthenticationSuccessHandler(new JWTAuthSuccessHandler());
        return http
                .csrf().disable()
                //.httpBasic().disable()
                .formLogin().disable()
                .authorizeExchange()
                .pathMatchers(AbstractWebSecurityConfig.WHITELISTED_AUTH_URLS)
                .permitAll()
                .and()
                .addFilterAt(this.basicAuthenticationFilter(), SecurityWebFiltersOrder.HTTP_BASIC)
                .authorizeExchange()
                //.pathMatchers(HttpMethod.POST, "/employees/update").hasRole("ADMIN").authenticated()
                //                .pathMatchers(HttpMethod.POST, EntityContractConst.URI_CREATE_LOAN_CONTRACT).authenticated()
                //                .pathMatchers(HttpMethod.POST, EntityContractConst.URI_EXECUTE_ACTION).authenticated()

                //                .pathMatchers(HttpMethod.POST, EntityContractConst.URI_CREATE_LOAN_CONTRACT).authenticated()
                //           .access(this::currentUserMatchesPath)
                //                .hasRole(environment.getProperty("webflux.security.role"))
                .pathMatchers("/api/**")//.hasRole(environment.getProperty("webflux.security.role"))
                .authenticated()
                .and()
                .addFilterAt(this.bearerAuthenticationFilter(), SecurityWebFiltersOrder.AUTHENTICATION)
                .authorizeExchange()
                .anyExchange().authenticated()
                //.and().formLogin()
                .and().build();

//                .and()
//                .csrf().disable()
//                .formLogin().disable()
//                //.formLogin()
        //            .pathMatchers("/api/**").authenticated()
        // .pathMatchers("/**", "/login", "/logout").permitAll()
//                .httpBasic();
        //.authenticationManager(authenticationManager);
    }
}
