/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.config;

import static org.dbs24.consts.SysConst.*;
import org.dbs24.rest.RetailLoanContractRest;
import org.springframework.context.annotation.*;
import org.springframework.http.*;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import static org.springframework.web.reactive.function.server.RequestPredicates.*;
import static org.dbs24.consts.EntityReferenceConst.*;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;

@Configuration
@ComponentScan(basePackages = {SERVICE_PACKAGE, RESTFUL_PACKAGE, COMPONENT_PACKAGE})
@PropertySource(APP_PROPERTIES)
@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
public class RetailLoanContractWebSecurityConfig extends AbstractWebSecurityConfig {

    //==========================================================================
    @Bean
    public RouterFunction<ServerResponse> routerRetailLoanContract(RetailLoanContractRest retailLoanContractHandler) {

        return addCommonRoutes()
                .andRoute(POST(URI_CREATE_LOAN_CONTRACT).and(accept(MediaType.APPLICATION_JSON)), retailLoanContractHandler::createRetailLoanContract)
                .andRoute(POST(URI_EXECUTE_ACTION).and(accept(MediaType.APPLICATION_JSON)), retailLoanContractHandler::executeAction)
                .andRoute(GET("/findRetailLoanContract").and(accept(MediaType.APPLICATION_JSON)), retailLoanContractHandler::findRetailLoanContract);

    }
    //==========================================================================
//    @Bean
//    @Profile("production")
//    public SecurityWebFilterChain springSecurityFilter1Chain( ServerHttpSecurity http) {
//
////        final AuthenticationWebFilter authenticationJWT = new AuthenticationWebFilter(new UserDetailsRepositoryReactiveAuthenticationManager(userDetailsService()));
////        authenticationJWT.setAuthenticationSuccessHandler(new JWTAuthSuccessHandler());
//        return http
//                .csrf().disable()
//                //.httpBasic().disable()
//                .formLogin().disable()
//                .authorizeExchange()
//                .pathMatchers(AbstractWebSecurityConfig.WHITELISTED_AUTH_URLS)
//                .permitAll()
//                .and()
//                .addFilterAt(this.basicAuthenticationFilter(), SecurityWebFiltersOrder.HTTP_BASIC)
//                .authorizeExchange()
//                .pathMatchers("/api/**")//.hasRole(environment.getProperty("webflux.security.role"))
//                .authenticated()
//                .and()
//                .addFilterAt(this.bearerAuthenticationFilter(), SecurityWebFiltersOrder.AUTHENTICATION)
//                .authorizeExchange()
//                .anyExchange().authenticated()
//                //.and().formLogin()
//                .and().build();
//    }
    //==========================================================================
    /**
     * Use the already implemented logic in AuthenticationWebFilter and set a
     * custom SuccessHandler that will return a JWT when a user is authenticated
     * with user/password Create an AuthenticationManager using the
     * UserDetailsService defined above
     *
     * @return AuthenticationWebFilter
     */
//    private AuthenticationWebFilter basicAuthenticationFilter() {
//        final UserDetailsRepositoryReactiveAuthenticationManager authManager;
//        final AuthenticationWebFilter basicAuthenticationFilter;
//        final ServerAuthenticationSuccessHandler successHandler;
//
//        authManager = new UserDetailsRepositoryReactiveAuthenticationManager(userDetailsService());
//        successHandler = new BasicAuthenticationSuccessHandler();
//
//        basicAuthenticationFilter = new AuthenticationWebFilter(authManager);
//        basicAuthenticationFilter.setAuthenticationSuccessHandler(successHandler);
//
//        return basicAuthenticationFilter;
//
//    }
    /**
     * Use the already implemented logic by AuthenticationWebFilter and set a
     * custom converter that will handle requests containing a Bearer token
     * inside the HTTP Authorization header. Set a dummy authentication manager
     * to this filter, it's not needed because the converter handles this.
     *
     * @return bearerAuthenticationFilter that will authorize requests
     * containing a JWT
     */
//    private AuthenticationWebFilter bearerAuthenticationFilter() {
//        AuthenticationWebFilter bearerAuthenticationFilter;
//        Function<ServerWebExchange, Mono<Authentication>> bearerConverter;
//        final ReactiveAuthenticationManager authManager;
//
//        authManager = new BearerTokenReactiveAuthenticationManager();
//        bearerAuthenticationFilter = new AuthenticationWebFilter(authManager);
//        bearerConverter = new ServerHttpBearerAuthenticationConverter();
//
//        bearerAuthenticationFilter.setAuthenticationConverter(bearerConverter);
//        bearerAuthenticationFilter.setRequiresAuthenticationMatcher(ServerWebExchangeMatchers.pathMatchers("/api/**"));
//
//        return bearerAuthenticationFilter;
//    }
}
