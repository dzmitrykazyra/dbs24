/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.config;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import org.dbs24.service.MainReactiveUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.dbs24.security.basic.BasicAuthenticationSuccessHandler;
import org.dbs24.security.bearer.BearerTokenReactiveAuthenticationManager;
import org.dbs24.security.bearer.ServerHttpBearerAuthenticationConverter;
import java.util.function.Function;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UserDetailsRepositoryReactiveAuthenticationManager;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authentication.AuthenticationWebFilter;
import org.springframework.security.web.server.authentication.ServerAuthenticationSuccessHandler;
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatchers;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RequestPredicates.accept;
import org.springframework.web.reactive.function.server.RouterFunction;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.dbs24.component.*;
import static org.dbs24.consts.RestHttpConsts.*;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;
import java.time.Duration;
import java.util.*;
import lombok.extern.log4j.Log4j2;
import lombok.Data;

@Data
@Log4j2
public abstract class AbstractWebSecurityConfig  extends AbstractSecurityConfig {

    protected static final String[] WHITELISTED_AUTH_URLS = {
        "/login",
        "/",
        "/auth/**"
    };

    @Autowired
    private Environment environment;

    @Autowired
    private CommonRest commonRest;

    @Autowired
    private SecurityRest securityRest;

    //@Autowired
    private MainReactiveUserDetailsService mainReactiveUserDetailsService;

    @Value("${webflux.security.uid}")
    private String uid = "no_uid";

    @Value("${webflux.security.pwd}")
    private String pwd = "no_pwd";

    @Value("${webflux.security.role}")
    private String role = "no_pwd";//

    @Value("${network.allowed-origins.additional:*}")
    private final String[] allowedOriginsAdditional = {
        "http://localhost:4200",
        "https://localhost:4200"
    };

    /**
     * Protocols (https://, http://) prepended separately
     */
    private final String[] allowedOriginsDefault = {
        "game-wchess-dev.myfide.net",
        "game-wchess-preprod.myfide.net",
        "game-wchess-stage.myfide.net",
        "wchess-dev.myfide.net",
        "wchess-preprod.myfide.net",
        "wchess-stage.myfide.net",
        "localhost:4200"
    };

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
    //==========================================================================
    protected RouterFunction<ServerResponse> addSecurityRoutes() {

        return route(POST(URI_CREATE_USER).and(accept(MediaType.APPLICATION_JSON)), securityRest::authCreateUser)
                .andRoute(POST(URI_CREATE_ROLE).and(accept(MediaType.APPLICATION_JSON)), securityRest::authCreateRole);
    }

    //==========================================================================
    protected RouterFunction<ServerResponse> addCommonRoutes() {

        return addSecurityRoutes()
                //.and(routeSecurityRests())
                .andRoute(POST(URI_LIVENESS).and(accept(MediaType.APPLICATION_JSON)), commonRest::liveness)
                .andRoute(POST(URI_READINESS).and(accept(MediaType.APPLICATION_JSON)), commonRest::readiness)
                .andRoute(POST(URI_SHOUTDOWN).and(accept(MediaType.APPLICATION_JSON)), commonRest::shoutdown);
    }

    //==========================================================================
    @Bean
    //@Profile("dev")
    public SecurityWebFilterChain springDevSecurityFilterChain(ServerHttpSecurity http) {
        return http
                .csrf().disable()
                .authorizeExchange().anyExchange().permitAll()
                //.authorizeExchange().pathMatchers("/api/**", "/websocket/**").permitAll().anyExchange().authenticated()
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
//    public ReactiveAdapterRegistry reactiveAdapterRegistry() {
//        return new ReactiveAdapterRegistry();
//    }
    //==========================================================================
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

    @Bean
    public CorsWebFilter corsFilter() {
        final CorsConfiguration cfg = new CorsConfiguration();
//        final List<String> allowedOrigins = new ArrayList<>(UrlUtils.prependProtocols(Stream.concat(
//                Arrays.stream(allowedOriginsAdditional),
//                Arrays.stream(allowedOriginsDefault)
//        ).collect(Collectors.toList())));

        final List<String> allowedOrigins = new ArrayList<>();

        log.debug("corsFilter allowedOrigins: {}", allowedOrigins);
        cfg.setAllowedOrigins(allowedOrigins);
        cfg.addAllowedHeader("*");
        cfg.addAllowedMethod("*");
        cfg.setAllowCredentials(true);
        cfg.setMaxAge(Duration.ofHours(1L));
        final UrlBasedCorsConfigurationSource src = new UrlBasedCorsConfigurationSource();
        src.registerCorsConfiguration("/**", cfg);
        return new CorsWebFilter(src);
    }
}
