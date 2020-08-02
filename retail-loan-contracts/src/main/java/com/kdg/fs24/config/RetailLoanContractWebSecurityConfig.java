/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kdg.fs24.config;

import com.kdg.fs24.application.core.sysconst.SysConst;
import com.kdg.fs24.entity.core.api.EntityContractConst;
import com.kdg.fs24.rest.RetailLoanContractRest;
import com.kdg.fs24.security.config.AbstractWebSecurityConfig;
import com.kdg.fs24.security.basic.*;
import com.kdg.fs24.security.bearer.*;
//import com.kdg.fs24.security.jwt.*;
import org.springframework.context.annotation.*;
import org.springframework.http.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.core.env.Environment;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;
import static org.springframework.web.reactive.function.server.RequestPredicates.*;
import static com.kdg.fs24.entity.core.api.EntityContractConst.*;
import com.kdg.fs24.service.RetailLoanContractActionsService;
import reactor.core.publisher.Mono;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UserDetailsRepositoryReactiveAuthenticationManager;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.MapReactiveUserDetailsService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authentication.AuthenticationWebFilter;
import org.springframework.security.web.server.authentication.ServerAuthenticationSuccessHandler;
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatchers;
import org.springframework.web.server.ServerWebExchange;
import java.util.function.Function;
import org.springframework.core.annotation.Order;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;

/**
 *
 * @author Козыро Дмитрий
 */
@Configuration
@ComponentScan(basePackages = {SysConst.SERVICE_PACKAGE, SysConst.RESTFUL_PACKAGE})
//@EntityScan(basePackages = {SysConst.ENTITY_PACKAGE, SysConst.REFERENCE_PACKAGE})
@PropertySource(SysConst.APP_PROPERTIES)
//@EnableWebFlux
@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
public class RetailLoanContractWebSecurityConfig extends AbstractWebSecurityConfig {

//    @Autowired
//    private ReactiveAuthenticationManager authenticationManager;    
    //==========================================================================
    @Bean
    public RouterFunction<ServerResponse> mgmtRetailLoanContract(final RetailLoanContractRest retailLoanContractHandler) {

        return route(POST(URI_CREATE_LOAN_CONTRACT).and(accept(MediaType.APPLICATION_JSON)), retailLoanContractHandler::createRetailLoanContract)
                .andRoute(POST(URI_EXECUTE_ACTION).and(accept(MediaType.APPLICATION_JSON)), retailLoanContractHandler::executeAction)
                .andRoute(GET("/findRetailLoanContract").and(accept(MediaType.APPLICATION_JSON)), retailLoanContractHandler::findRetailLoanContract);

    }
    //==========================================================================

//    @Bean
    //@Profile("dev")
    //@Profile(value={"dev", "production"})
    //@Profile(value="prod & !dev")
//    public RouterFunction<ServerResponse> executeAction(RetailLoanContractRest retailLoanContractHandler) {
//
//        return RouterFunctions
//                .route(RequestPredicates.POST(EntityContractConst.URI_EXECUTE_ACTION)
//                        .and(RequestPredicates.accept(MediaType.APPLICATION_JSON)),
//                        retailLoanContractHandler::executeAction);
//    }
//    //==========================================================================
//    @Bean
//    @Profile("dev")
//    public SecurityWebFilterChain springDevSecurityFilterChain(final ServerHttpSecurity http) {
//        return http
//                .csrf().disable()
//                .authorizeExchange().anyExchange().permitAll()
//                //.pathMatchers("/**").permitAll()
//                //     http.authorizeExchange().anyExchange().permitAll();
//                .and()
//                .csrf().disable()
//                .formLogin().disable()
//                //.formLogin()
//                //.httpBasic();
//                .build();
//    }
    //==========================================================================
//    private Mono<AuthorizationDecision> currentUserMatchesPath(Mono<Authentication> authentication, AuthorizationContext context) {
//        return authentication
//                .map(a -> context.getVariables().get("user").equals(a.getName()))
//                .map(granted -> new AuthorizationDecision(granted));
//    }
    @Bean
    @Profile("production")
    public SecurityWebFilterChain springSecurityFilterChain(final ServerHttpSecurity http) {

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

    //==========================================================================
//    @Bean
//    public PasswordEncoder encoder() {
//        return new BCryptPasswordEncoder();
//    }
    //==========================================================================
//    @Bean
//    public MapReactiveUserDetailsService reactiveUserDetailsService() {
//
//        PasswordEncoder encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
//
//        final UserDetails user = User.builder()
//                .passwordEncoder(encoder::encode)
//                .username(this.getEnvironment().getProperty("webflux.security.uid"))
//                //.password(encoder().encode(environment.getProperty("webflux.security.pwd")))
//                .password(this.getEnvironment().getProperty("webflux.security.pwd"))
//                //                                .username(this.uid)
//                //                                .password(this.pwd)
//                //.roles(environment.getProperty("webflux.security.role"))
//                .roles("NOROLE")
//                .build();
//
//        return new MapReactiveUserDetailsService(user);
//
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
