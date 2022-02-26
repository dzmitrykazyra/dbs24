/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.service;

import org.dbs24.application.core.service.funcs.ServiceFuncs;
import org.dbs24.stmt.StmtProcessor;
import org.dbs24.mime.MimeTypes;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Map;
import java.util.AbstractMap;
import lombok.Data;
import lombok.extern.log4j.Log4j2;
import org.springframework.messaging.rsocket.RSocketRequester;
import org.springframework.util.Assert;
import reactor.core.publisher.Mono;
import io.rsocket.util.DefaultPayload;
import io.rsocket.Payload;
import org.springframework.beans.factory.annotation.Autowired;
import reactor.util.retry.Retry;
import org.springframework.util.MimeType;
import org.springframework.http.codec.cbor.Jackson2CborDecoder;
import org.springframework.http.codec.cbor.Jackson2CborEncoder;
import org.springframework.security.rsocket.metadata.SimpleAuthenticationEncoder;
import org.springframework.messaging.rsocket.RSocketRequester.Builder;
import java.time.Duration;
import java.util.stream.Stream;
import org.dbs24.reactor.AbstractHotSubscriber;

@Data
@Log4j2
public abstract class AbstractRSocketService<T> extends AbstractHotSubscriber<T> implements RSocketService {

    final Map<ConnectionInfo, Mono<RSocketRequester>> clients = ServiceFuncs.createMap();

    @Autowired
    private ObjectMapper objectMapper;

    static final RSocketProcessor emptyRSocketProcessor = requester -> {
    };

    static final RSocketBuilderProcessor emptyRSocketBuilderProcessor = builder -> {
    };

    //==========================================================================
    public Mono<RSocketRequester> getRSocketRequester(String url, Integer port) {

        return getRSocketRequester(url, port, emptyRSocketBuilderProcessor);
    }

    //==========================================================================
    public Mono<RSocketRequester> getRSocketRequester(String url, Integer port, RSocketBuilderProcessor rSocketBuilderProcessor) {

        return clients
                .entrySet()
                .stream()
                .filter(predicate -> {
                    final ConnectionInfo ci = predicate.getKey();
                    return (ci.getUrl().equals(url) && ci.getPort().equals(port));
                })
                .findFirst()
                .orElseGet(() -> {

                    log.debug("build RSocketRequester 2 ({}, {}) ", url, port);

                    final ConnectionInfo connectionInfo = StmtProcessor.create(ConnectionInfo.class, ci -> {
                        ci.setUrl(url);
                        ci.setPort(port);
                    });

                    final Builder builder = RSocketRequester.builder();

                    rSocketBuilderProcessor.processRSocketBuilder(builder);

                    final RSocketRequester rSocketRequester = builder
                            //.setupMetadata(log, mimeType)
                            .rsocketConnector(rSocketConnector -> rSocketConnector.reconnect(Retry.fixedDelay(2, Duration.ofSeconds(2))))
                            .rsocketStrategies(b -> {
                                b.encoder(new Jackson2CborEncoder());
                                b.encoder(new SimpleAuthenticationEncoder());
                                b.decoder(new Jackson2CborDecoder());

                                b.metadataExtractorRegistry(metadataExtractorRegistry
                                        -> Stream.of(MimeTypes.values())
                                                .forEach(mimeType -> metadataExtractorRegistry.metadataToExtract(
                                                MimeType.valueOf(mimeType.getValue()),
                                                String.class,
                                                mimeType.toString()))
                                );
                            })
                            .tcp(url, port);

                    final Mono<RSocketRequester> mono = Mono.just(rSocketRequester);

                    Assert.notNull(mono, "fucking RSocketRequester is null!");

                    log.debug("RSocketRequester is created ({},{},{})", url, port, mono);

                    clients.put(connectionInfo, mono);

                    return new AbstractMap.SimpleEntry<ConnectionInfo, Mono<RSocketRequester>>(connectionInfo, mono);
                })
                .getValue();
    }

    //==========================================================================
    public <T> Payload toPayload(T t) {
        return StmtProcessor.create(() -> DefaultPayload.create(toJson(t)));
    }

    //==========================================================================
    public <T> T fromPayload(Payload payload, Class<T> clazz) {
        return StmtProcessor.create(() -> fromJson(payload.getDataUtf8(), clazz));
    }

    //==========================================================================
    public <T> String toJson(T t) {
        return StmtProcessor.create(() -> objectMapper.writeValueAsString(t));
    }

    //==========================================================================
    public <T> T fromJson(String s, Class<T> clazz) {
        return StmtProcessor.create(() -> objectMapper.readValue(s, clazz));
    }
}
