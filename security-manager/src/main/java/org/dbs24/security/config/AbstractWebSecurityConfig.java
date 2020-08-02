/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.security.config;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import org.dbs24.service.MainReactiveUserDetailsService;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.context.annotation.Bean;
import org.dbs24.security.basic.BasicAuthenticationSuccessHandler;
import org.dbs24.security.bearer.BearerTokenReactiveAuthenticationManager;
import org.dbs24.security.bearer.ServerHttpBearerAuthenticationConverter;
import java.util.function.Function;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UserDetailsRepositoryReactiveAuthenticationManager;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.MapReactiveUserDetailsService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authentication.AuthenticationWebFilter;
import org.springframework.security.web.server.authentication.ServerAuthenticationSuccessHandler;
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatchers;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 *
 * @author Козыро Дмитрий
 */
@Data
public abstract class AbstractWebSecurityConfig { //extends WebSecurityConfigurerAdapter {

    protected static final String[] WHITELISTED_AUTH_URLS = {
        "/login",
        "/",
        "/auth/**"
    };

    @Autowired
    private Environment environment;

    @Autowired
    private MainReactiveUserDetailsService mainReactiveUserDetailsService;

    @Value("${webflux.security.uid}")
    private String uid = "no_uid";

    @Value("${webflux.security.pwd}")
    private String pwd = "no_pwd";

    @Value("${webflux.security.role}")
    private String role = "no_pwd";//

//    @Override
//    @Bean
//    public AuthenticationManager authenticationManagerBean() throws Exception {
//        return super.authenticationManagerBean();
//    }
////
//    @Override
//    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//        //auth.userDetailsService(this.reactiveUserDetailsService()).passwordEncoder(new BCryptPasswordEncoder());
//    }
    @Bean
    public PasswordEncoder encoder() {
        //return new BCryptPasswordEncoder();
        DelegatingPasswordEncoder delPasswordEncoder = (DelegatingPasswordEncoder) PasswordEncoderFactories.createDelegatingPasswordEncoder();
        BCryptPasswordEncoder bcryptPasswordEncoder = new BCryptPasswordEncoder();
        delPasswordEncoder.setDefaultPasswordEncoderForMatches(bcryptPasswordEncoder);
        return delPasswordEncoder;
    }

    //==========================================================================
    @Bean
    @Profile("dev")
    public SecurityWebFilterChain springDevSecurityFilterChain(final ServerHttpSecurity http) {
        return http
                .csrf().disable()
                .authorizeExchange().anyExchange().permitAll()
                //.pathMatchers("/**").permitAll()
                //     http.authorizeExchange().anyExchange().permitAll();
                .and()
                .csrf().disable()
                .formLogin().disable()
                //.formLogin()
                //.httpBasic();
                .build();
    }

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
//                .roles("DEFAULT_ROLE")
//                .build();
//
//        return new MapReactiveUserDetailsService(user);
//    }
//        final UserDetails rob = User.withDefaultPasswordEncoder()
//                .username(environment.getProperty("webflux.security.uid"))
//                .password(environment.getProperty("webflux.security.pwd"))
//                .roles(environment.getProperty("webflux.security.role")).build();
//        return new MapReactiveUserDetailsService(rob);
//        UserDetails userWebFlux = User
//                .withUsername(environment.getProperty("webflux.security.uid"))
//                .password(environment.getProperty("webflux.security.pwd"))
//                .roles(environment.getProperty("webflux.security.role")).build();
//        return new MapReactiveUserDetailsService(userWebFlux);
    //==========================================================================
    /**
     * Use the already implemented logic in AuthenticationWebFilter and set a
     * custom SuccessHandler that will return a JWT when a user is authenticated
     * with user/password Create an AuthenticationManager using the
     * UserDetailsService defined above
     *
     * @return AuthenticationWebFilter
     */
    protected AuthenticationWebFilter basicAuthenticationFilter() {
        final UserDetailsRepositoryReactiveAuthenticationManager authManager;
        final AuthenticationWebFilter basicAuthenticationFilter;
        final ServerAuthenticationSuccessHandler successHandler;

        //authManager = new UserDetailsRepositoryReactiveAuthenticationManager(reactiveUserDetailsService());
        authManager = new UserDetailsRepositoryReactiveAuthenticationManager(mainReactiveUserDetailsService);
        successHandler = new BasicAuthenticationSuccessHandler();

        basicAuthenticationFilter = new AuthenticationWebFilter(authManager);
        basicAuthenticationFilter.setAuthenticationSuccessHandler(successHandler);

        return basicAuthenticationFilter;

    }

    /**
     * Use the already implemented logic by AuthenticationWebFilter and set a
     * custom converter that will handle requests containing a Bearer token
     * inside the HTTP Authorization header. Set a dummy authentication manager
     * to this filter, it's not needed because the converter handles this.
     *
     * @return bearerAuthenticationFilter that will authorize requests
     * containing a JWT
     */
    protected AuthenticationWebFilter bearerAuthenticationFilter() {
        AuthenticationWebFilter bearerAuthenticationFilter;
        Function<ServerWebExchange, Mono<Authentication>> bearerConverter;
        final ReactiveAuthenticationManager authManager;

        authManager = new BearerTokenReactiveAuthenticationManager();
        bearerAuthenticationFilter = new AuthenticationWebFilter(authManager);
        bearerConverter = new ServerHttpBearerAuthenticationConverter();

        bearerAuthenticationFilter.setAuthenticationConverter(bearerConverter);
        bearerAuthenticationFilter.setRequiresAuthenticationMatcher(ServerWebExchangeMatchers.pathMatchers("/api/**"));

        return bearerAuthenticationFilter;
    }

}
