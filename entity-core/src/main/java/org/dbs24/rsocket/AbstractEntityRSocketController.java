/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.rsocket;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.Duration;
import java.util.Collection;
import java.util.ArrayList;
import java.util.Map;
import org.springframework.messaging.handler.annotation.MessageMapping;
import reactor.core.publisher.Mono;
import lombok.extern.log4j.Log4j2;
import static org.dbs24.consts.EntityCoreConst.*;
import org.dbs24.entity.core.AbstractActionEntity;
import org.dbs24.entity.reactor.GenericEntityReactor;
import org.dbs24.stmt.StmtProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import java.util.Optional;
import org.dbs24.exception.EntityMetadataIsNotDefined;
import org.dbs24.mime.MimeTypes;
import com.fasterxml.jackson.databind.JavaType;

@Log4j2
public abstract class AbstractEntityRSocketController<T extends AbstractActionEntity> {

    @Autowired
    private GenericEntityReactor genericEntityReactor;

    @Autowired
    private ObjectMapper objectMapper;

    @MessageMapping("echo")
    public void echoRoute(Mono<String> echo) {
        // cancel and refund asynchronously

        log.debug(R_ECHO_ROUTE);

        echo
                .doOnNext(t -> log.debug("create echo :: " + t))
                .subscribe();
    }

    @MessageMapping(R_GENERIC_ENTITY)
    public void createGenericEntity(
            @Payload String collectionString,
            @Headers Map<String, Object> metadata) {

        StmtProcessor.runNewThread(() -> StmtProcessor.execute(() -> {

            final String entityClass = this.getMetaDataValue(metadata, MimeTypes.ENTITY_CLASS_NAME);

            final Class<T> newClass = (Class<T>) StmtProcessor.create(() -> Class.forName(entityClass));

            final JavaType javaType = objectMapper.getTypeFactory().constructCollectionType(ArrayList.class, newClass);

            final ArrayList<T> entityCollection = StmtProcessor.create(() -> objectMapper.readValue(collectionString, javaType));

            Mono.just(entityCollection)
                    .doOnNext(col -> col.forEach(entity -> entity.setMetaData(metadata)))
                    .subscribe(genericEntityReactor);
        }));
    }

//    @MessageMapping(R_GENERIC_ENTITY_MONO)
//    public void createGenericEntityMono(
//            @Payload String entityString,
//            @Headers Map<String, Object> metadata) {
//
//        StmtProcessor.runNewThread(() -> StmtProcessor.execute(() -> {
//
//            final String entityClass = this.getMetaDataValue(metadata, MimeTypes.ENTITY_CLASS_NAME);
//
//            final Class<T> newClass = (Class<T>) StmtProcessor.create(() -> Class.forName(entityClass));
//
//            final T entity = StmtProcessor.create(() -> objectMapper.readValue(entityString, newClass));
//            entity.setMetaData(metadata);
//            
//            Mono.just(entity)
//                    .subscribe(genericEntityReactor);
//        }));
//    }
    //==========================================================================
    protected <V> V getMetaDataValue(Map<String, Object> metaData, MimeTypes key) {

        return (V) Optional.ofNullable((Optional.ofNullable(metaData)
                .orElseThrow(() -> new EntityMetadataIsNotDefined("entity metaData is not defined"))).get(key.toString()))
                .orElseThrow(() -> new EntityMetadataIsNotDefined(String.format("mimeType is not defined {%s}", key.getValue())));
    }
}
