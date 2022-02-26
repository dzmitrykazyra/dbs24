/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.config;

import io.rsocket.transport.netty.client.TcpClientTransport;
import org.springframework.context.annotation.Bean;
import org.springframework.http.codec.cbor.Jackson2CborDecoder;
import org.springframework.http.codec.cbor.Jackson2CborEncoder;
import org.springframework.messaging.rsocket.RSocketStrategies;
import lombok.extern.log4j.Log4j2;
import org.dbs24.spring.core.api.AbstractApplicationBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.codec.Encoder;
import org.springframework.core.codec.Decoder;
import org.springframework.messaging.rsocket.annotation.support.RSocketMessageHandler;
import org.springframework.util.Assert;
import java.util.Collection;
import java.util.stream.Stream;
import org.dbs24.application.core.service.funcs.ServiceFuncs;
import org.dbs24.mime.MimeTypes;
import org.springframework.util.MimeType;
import org.springframework.security.config.annotation.rsocket.RSocketSecurity;
import org.springframework.security.core.userdetails.MapReactiveUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.rsocket.metadata.SimpleAuthenticationEncoder;
import org.springframework.security.rsocket.metadata.BearerTokenAuthenticationEncoder;
import org.springframework.security.rsocket.core.PayloadSocketAcceptorInterceptor;
import org.springframework.boot.rsocket.messaging.RSocketStrategiesCustomizer;
import org.springframework.context.annotation.Primary;
import org.springframework.security.config.Customizer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.messaging.handler.invocation.reactive.AuthenticationPrincipalArgumentResolver;
//import org.springframework.context.annotation.Conditional;
//import org.springframework.context.annotation.Condition;

@Log4j2
public abstract class AbstractRSocketConfig extends MainApplicationConfig {

    @Value("${spring.rsocket.server.port:6666}")
    protected Integer rsocketPort;

    @Value("${spring.rsocket.server.mapping-path:/rsocket}")
    protected String rsocketMappingPath;

    @Value("${sm.uid:smuser}")
    protected String uid = "smuser";

    @Value("${sm.pwd:smpwd}")
    protected String pwd = "smpwd";

    @Value("${sm.role:SMROLE}")
    protected String role = "SMROLE";

    private RSocketSecurity rSocketSecurity;

    @Bean
//    @Conditional
    public RSocketMessageHandler messageHandler(RSocketStrategies rSocketStrategies) {
        final RSocketMessageHandler mh = new RSocketMessageHandler();
        mh.getArgumentResolverConfigurer().addCustomResolver(new AuthenticationPrincipalArgumentResolver());
        mh.setRSocketStrategies(rSocketStrategies);

        this.initRSocketMessageHandler(mh);

        Assert.notNull(mh, "fucking RSocketMessageHandler is null!");

        log.info("RSocketMessageHandler is created ({})", mh.getBeanName());

        return mh;
    }

    protected void initRSocketMessageHandler(RSocketMessageHandler rmh) {

    }

    @Bean
    public RSocketStrategies rSocketStrategies() {

        final RSocketStrategies rSocketStrategies = RSocketStrategies.builder()
                .encoders(encoders -> addEncoders().forEach(encoder -> encoders.add(encoder)))
                .decoders(decoders -> addDecoders().forEach(decoder -> decoders.add(decoder)))
                .metadataExtractorRegistry(metadataExtractorRegistry -> Stream.of(MimeTypes.values())
                .forEach(mimeType -> metadataExtractorRegistry.metadataToExtract(MimeType.valueOf(mimeType.getValue()), String.class,
                mimeType.toString())))
                .build();

        Assert.notNull(rSocketStrategies,
                "fucking RSocketStrategies is null!");

        log.info("RSocketStrategies is created ({})", rSocketStrategies.toString());

        return rSocketStrategies;
    }

    @Bean
    @Primary
    public MapReactiveUserDetailsService rSocketUserDetailsService(
            PasswordEncoder passwordEncoder) {
        return new MapReactiveUserDetailsService(initUserDetails(passwordEncoder));
    }

    protected Collection<UserDetails> initUserDetails(PasswordEncoder passwordEncoder) {

        final Collection<UserDetails> collection = ServiceFuncs.createCollection();
        collection.add(User.withUsername(uid)
                .passwordEncoder(passwordEncoder::encode)
                .password(pwd)
                .roles(role)
                .build());

        return collection;
    }

    protected PayloadSocketAcceptorInterceptor buildSingleRoute(String socketRoute) {

        return this.buildRoutes(socketRoute);
    }

    protected PayloadSocketAcceptorInterceptor buildRoutes(String... socketRoutes) {

        return rSocketSecurity
                .authorizePayload(spec
                        -> this.buildRoute(spec
                        .setup().hasRole(role), role, socketRoutes)
                        .anyRequest().authenticated()
                        .anyExchange().authenticated()
                )
                .simpleAuthentication(Customizer.withDefaults())
                .build();
    }

    private RSocketSecurity.AuthorizePayloadsSpec buildRoute(RSocketSecurity.AuthorizePayloadsSpec pls, String role, String... socketRoutes) {

        RSocketSecurity.AuthorizePayloadsSpec apls = pls;

        for (String route : socketRoutes) {
            apls = apls.route(route).hasRole(role);
        }

        return apls;
    }

    @Bean
    public PayloadSocketAcceptorInterceptor authorization(RSocketSecurity rSocketSecurity) {
        this.rSocketSecurity = rSocketSecurity;
        return this.initPayloadSocketAcceptorInterceptor();
    }

    // to override in child classes
    protected abstract PayloadSocketAcceptorInterceptor initPayloadSocketAcceptorInterceptor();

    //==========================================================================
    @Bean
    public RSocketStrategiesCustomizer rSocketStrategiesCustomizer() {
        return initRSocketStrategiesCustomizer();
    }

    protected RSocketStrategiesCustomizer initRSocketStrategiesCustomizer() {
        return strategies -> strategies.encoder(new SimpleAuthenticationEncoder());
    }

    //==========================================================================
    protected Collection<Encoder> addEncoders() {
        final Collection<Encoder> collection = ServiceFuncs.<Encoder>createCollection();

        collection.add(new Jackson2CborEncoder());
        collection.add(new SimpleAuthenticationEncoder());
        collection.add(new BearerTokenAuthenticationEncoder());

        return collection;
    }

    //==========================================================================
    protected Collection<Decoder> addDecoders() {
        final Collection<Decoder> collection = ServiceFuncs.<Decoder>createCollection();

        collection.add(new Jackson2CborDecoder());

        return collection;
    }
}
