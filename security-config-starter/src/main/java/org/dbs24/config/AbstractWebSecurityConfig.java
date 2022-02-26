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

import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.log4j.Log4j2;
import org.dbs24.application.core.service.funcs.ServiceFuncs;
import org.dbs24.component.AuthenticationManager;
import org.dbs24.component.CommonRest;
import org.dbs24.component.SecurityContextRepository;
import org.dbs24.component.SecurityRest;
import org.dbs24.security.bearer.BearerTokenReactiveAuthenticationManager;
import org.dbs24.security.bearer.ServerHttpBearerAuthenticationConverter;
import org.dbs24.service.MainReactiveUserDetailsService;
import org.dbs24.stmt.StmtProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.web.embedded.netty.NettyReactiveWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.http.client.reactive.ReactorResourceFactory;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authentication.AuthenticationWebFilter;
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatchers;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;
import org.springframework.web.reactive.function.server.HandlerFunction;
import org.springframework.web.reactive.function.server.RequestPredicate;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import reactor.netty.resources.LoopResources;

import java.time.Duration;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.dbs24.consts.RestHttpConsts.*;
import static org.dbs24.consts.SysConst.INTEGER_ZERO;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.server.RequestPredicates.*;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;


@Data
@Log4j2
@EqualsAndHashCode(callSuper = true)
public abstract class AbstractWebSecurityConfig extends AbstractSecurityConfig {

    private String appUriPrefix;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private SecurityContextRepository securityContextRepository;

    @Autowired
    private Environment environment;

    @Autowired
    private CommonRest commonRest;

    @Autowired
    private SecurityRest securityRest;

    //@Autowired
    private MainReactiveUserDetailsService mainReactiveUserDetailsService;

    @Value("${network.allowed-origins.additional:*}")
    private final String[] allowedOriginsAdditional = {
            "http://localhost:4200",
            "https://localhost:4200"
    };

    @Value("${config.security.security-routes.enable:true}")
    private Boolean enableSecurityRoutes;

    @Value("${config.security.jwt.token.enabled:false}")
    private Boolean canTokenCreate;

    private final String[] allowedOriginsDefault = {
            //        "game-wchess-dev.myfide.net",
            "localhost:4200"
    };

    private String[] whiteListAuthUrl;

    protected String[] getWhiteListAuthUrl() {

        final String[] defaultWhiteListAuthUrl = {URI_STARTED, URI_LOGIN, URI_LIVENESS, URI_READINESS, URI_REGISTER_TOKEN, URI_SWAGGER_LINKS, URI_SWAGGER_WEBJARS_LINKS, URI_SWAGGER_API_DOCS, URI_SWAGGER_MAIN, URI_SPRING_BOOT_ACTUATOR};

        final Collection<String> whiteUrls = ServiceFuncs.createCollection();

        Arrays.asList(addWhiteListAuthUrl(defaultWhiteListAuthUrl))
                .forEach(url -> whiteUrls.add(buildNewRoute(url)));

        this.whiteListAuthUrl = whiteUrls.toArray(new String[]{});

        return whiteListAuthUrl;
    }

    protected String[] addWhiteListAuthUrl(String[] existsUrl) {

        return existsUrl;
    }

    //==========================================================================
    private String getRouteStatus(String route) {
        return Arrays.asList(whiteListAuthUrl)
                .stream().filter(r -> r.equals(route)).count() > 0 ? "*" : "\u00a9";
    }

    //==========================================================================
    protected String buildNewRoute(String route) {

        return StmtProcessor.notNull(this.getAppUriPrefix()) ? route.replaceAll(URI_API.concat("/"), URI_API.concat("/" + getAppUriPrefix() + ("/"))) : route;
    }

    //==========================================================================
    protected RequestPredicate postRoute(String route) {

        final String newRoute = buildNewRoute(route);

        log.info("{} register route POST {}", getRouteStatus(newRoute), newRoute);
        return POST(newRoute).and(accept(APPLICATION_JSON));
    }

    //==========================================================================
    protected RequestPredicate getRoute(String route) {

        final String newRoute = buildNewRoute(route);

        log.info("{} register route GET {} ", getRouteStatus(newRoute), newRoute);
        return GET(newRoute).and(accept(APPLICATION_JSON));
    }

    //==========================================================================
    public HandlerFunction getLoginHandlerFunction() {
        return securityRest::login;
    }

    //==========================================================================
    protected HandlerFunction getRegisterTokenHandlerFunction() {
        return securityRest::registerToken;
    }

    protected RouterFunction<ServerResponse> addCreateRouteToken(RouterFunction<ServerResponse> mainRoutes) {
        return canTokenCreate ? mainRoutes.andRoute(postRoute(URI_REGISTER_TOKEN), getRegisterTokenHandlerFunction()) : mainRoutes;
    }

    //==========================================================================
    protected RouterFunction<ServerResponse> addSecurityRoutes() {

        final RouterFunction<ServerResponse> mainRoutes = route(postRoute(URI_LOGIN), getLoginHandlerFunction());

        return addCreateRouteToken(mainRoutes);
    }

    //==========================================================================
    protected RouterFunction<ServerResponse> addCommonRoutes() {

        final RouterFunction<ServerResponse> routerFunction = route(getRoute(URI_STARTED), commonRest::startTime)
                .andRoute(getRoute(URI_LIVENESS), commonRest::liveness)
                .andRoute(getRoute(URI_READINESS), commonRest::readiness)
                .andRoute(postRoute(URI_SHUTDOWN), commonRest::shutdown)
                .andRoute(postRoute(URI_CAN_SHUTDOWN), commonRest::canShutdown);

        return enableSecurityRoutes ? routerFunction.and(addSecurityRoutes()) : routerFunction;

    }

    @Value("${server.port}")
    public Integer serverPort;

    @Value("${server.event-loop-groups:0}")
    public Integer eventLoopGroups;

    @Bean
    public NioEventLoopGroup nioEventLoopGroup() {

        if (eventLoopGroups.equals(INTEGER_ZERO)) {
            eventLoopGroups = Runtime.getRuntime().availableProcessors() * 2;
        }

        return new NioEventLoopGroup(eventLoopGroups);

    }

    @Bean
    public NettyReactiveWebServerFactory factory(NioEventLoopGroup eventLoopGroup) {
        NettyReactiveWebServerFactory factory = new NettyReactiveWebServerFactory();
        factory.setServerCustomizers(Collections.singletonList(httpServer -> {
            eventLoopGroup.register(new NioServerSocketChannel());
            httpServer.port(serverPort);
            httpServer.accessLog(true);
            log.info("registry webServer factory ({}, {}, {} threads loop)",
                    factory.getClass().getSimpleName(),
                    httpServer.getClass().getSimpleName(),
                    eventLoopGroup.executorCount());
            return httpServer.runOn(eventLoopGroup);

        }));
        return factory;
    }

    @Bean
    public ReactorResourceFactory reactorResourceFactory(NioEventLoopGroup eventLoopGroup) {
        ReactorResourceFactory f = new ReactorResourceFactory();
        f.setLoopResources(new LoopResources() {
            @Override
            public EventLoopGroup onServer(boolean b) {
                return eventLoopGroup;
            }
        });
        f.setUseGlobalResources(false);
        return f;
    }

    @Bean
    public ReactorClientHttpConnector reactorClientHttpConnector(ReactorResourceFactory r) {
        return new ReactorClientHttpConnector(r, m -> m);
    }

//    @Bean
//    public WebClient webClient(ReactorClientHttpConnector r) {
//        return WebClient.builder().baseUrl("http://localhost:9000").clientConnector(r).build();
//    }

    //==================================================================================================================
    @Bean
    @ConditionalOnProperty(name = "config.security.profile.webfilter.chain", havingValue = "development")
    public SecurityWebFilterChain devSecurityFilterChain(ServerHttpSecurity http) {

        log.info("devSecurityFilterChain is activated");

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
    @Bean
    @ConditionalOnProperty(name = "config.security.profile.webfilter.chain", havingValue = "production", matchIfMissing = true)
    public SecurityWebFilterChain defaultSecurityFilterChain(ServerHttpSecurity http) {

        log.info("defaultSecurityFilterChain is activated");

        return http
                .cors().disable()
                .exceptionHandling()
                .authenticationEntryPoint((swe, e) -> Mono.fromRunnable(() -> swe.getResponse().setStatusCode(UNAUTHORIZED)))
                .accessDeniedHandler((swe, e) -> Mono.fromRunnable(() -> swe.getResponse().setStatusCode(FORBIDDEN)))
                .and().csrf().disable()
                .httpBasic().disable()
                .formLogin().disable()
                .logout().disable()
                .authenticationManager(authenticationManager)
                .securityContextRepository(securityContextRepository)
                .authorizeExchange()
                .pathMatchers(getWhiteListAuthUrl()).permitAll()
                .anyExchange().authenticated()
                .and().authorizeExchange()
                .and().build();
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

        final List<String> allowedOrigins = (Stream.concat(
                Arrays.stream(allowedOriginsAdditional),
                Arrays.stream(allowedOriginsDefault)
        ).collect(Collectors.toList()));

        //final List<String> allowedOrigins = new ArrayList<>();
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
