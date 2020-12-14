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
import org.springframework.security.messaging.handler.invocation.reactive.AuthenticationPrincipalArgumentResolver;
import org.springframework.util.MimeTypeUtils;

@Log4j2
public abstract class AbstractRSocketConfig extends AbstractApplicationBean {

    @Value("${spring.rsocket.server.port:6666}")
    protected Integer rsocketPort;

    @Value("${spring.rsocket.server.mapping-path:/rsocket}")
    protected String rsocketMappingPath;

    @Bean
    RSocketMessageHandler messageHandler(RSocketStrategies rSocketStrategies) {
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
                .metadataExtractorRegistry(metadataExtractorRegistry
                        -> Stream.of(MimeTypes.values())
                        .forEach(mimeType -> metadataExtractorRegistry.metadataToExtract(
                        MimeType.valueOf(mimeType.getValue()),
                        String.class,
                        mimeType.toString())))
                .build();

        Assert.notNull(rSocketStrategies,
                "fucking RSocketStrategies is null!");

        log.info(
                "RSocketStrategies is created ({})", rSocketStrategies.toString());

        return rSocketStrategies;
    }

//    @Bean
//    public Mono<RSocketRequester> rSocketRequester(RSocketRequester.Builder builder) {
//
//        Mono<RSocketRequester> mono = Mono.just(builder
//                .rsocketConnector(rSocketConnector -> rSocketConnector.reconnect(Retry.fixedDelay(2, Duration.ofSeconds(2))))
//                .dataMimeType(MediaType.APPLICATION_CBOR)
//                //.dataMimeType(MimeTypeUtils.ALL)
//                //.connect(TcpClientTransport.create(rsocketPort));
//                .transport(TcpClientTransport.create(rsocketPort)));
//
//        Assert.notNull(mono, "fucking RSocketRequester is null!");
//
//        log.info("RSocketRequester is created ({}, {})", rsocketPort, mono);
//
//        return mono;
//    }
    @Bean
    @Primary
    public MapReactiveUserDetailsService userDetailsService(PasswordEncoder passwordEncoder) {

        return new MapReactiveUserDetailsService(initUserDetails(passwordEncoder));
    }

    // to override in child classes
    protected abstract Collection<UserDetails> initUserDetails(PasswordEncoder passwordEncoder);

    @Bean
    //@Primary
    public PayloadSocketAcceptorInterceptor authorization(RSocketSecurity security) {
        return this.initPayloadSocketAcceptorInterceptor(security);
    }

    // to override in child classes
    protected abstract PayloadSocketAcceptorInterceptor initPayloadSocketAcceptorInterceptor(RSocketSecurity security);

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
