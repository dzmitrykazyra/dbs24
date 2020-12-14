/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.rsocket;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.Duration;
import lombok.Data;
import org.dbs24.entity.core.AbstractActionEntity;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import java.util.Collection;
import static org.dbs24.consts.EntityCoreConst.*;
import org.dbs24.mime.MimeTypes;
import org.dbs24.service.AbstractRSocketService;
import org.dbs24.stmt.StmtProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.MimeTypeUtils;

@Data
@Service
public class AbstractEntityRSocketClient<T extends AbstractActionEntity> {

    final AbstractRSocketService rSocketService;
    final ObjectMapper objectMapper;

    @Value("${spring.rsocket.server.block.delay:5000}") // ms
    protected Integer blockDelay;

    @Autowired
    public AbstractEntityRSocketClient(AbstractRSocketService rSocketService, ObjectMapper objectMapper) {
        this.rSocketService = rSocketService;
        this.objectMapper = objectMapper;
    }

    final FNFExecutor fnfExecutor = (route, string, clazz, remoteAddrr, remotePort, actionId, userId)
            -> getRSocketService()
                    .getRSocketRequester(remoteAddrr, remotePort)
                    .map(r -> r.route(route)
                    .metadata(metadataSpec -> {                        
                        metadataSpec.metadata(String.valueOf(actionId), MimeTypeUtils.parseMimeType(MimeTypes.ENTITY_ACTION_ID.getValue()));
                        metadataSpec.metadata(String.valueOf(userId), MimeTypeUtils.parseMimeType(MimeTypes.ENTITY_USER_ID.getValue()));
                        metadataSpec.metadata(clazz.getCanonicalName(), MimeTypeUtils.parseMimeType(MimeTypes.ENTITY_CLASS_NAME.getValue()));
                    })
                    .data(string))
                    .block(Duration.ofMillis(blockDelay))
                    .send();

    public Mono<Void> sendFNF(Collection<T> collection,
            Class<T> clazz,
            String remoteAddrr,
            Integer remotePort,
            Integer actionId,
            Long userId) {

        return fnfExecutor.execute(
                R_GENERIC_ENTITY,
                StmtProcessor.create(() -> objectMapper.writeValueAsString(collection)),
                clazz,
                remoteAddrr,
                remotePort,
                actionId,
                userId);
    }

    public Mono<Void> sendFNF(T entity,
            String remoteAddrr,
            Integer remotePort,
            Integer actionId,
            Long userId) {

        return fnfExecutor.execute(
                R_GENERIC_ENTITY_MONO,
                StmtProcessor.create(() -> objectMapper.writeValueAsString(entity)),
                entity.getClass(),
                remoteAddrr,
                remotePort,
                actionId,
                userId);
    }
}
