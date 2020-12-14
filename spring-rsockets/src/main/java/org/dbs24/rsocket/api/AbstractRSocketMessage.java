/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.rsocket.api;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.dbs24.stmt.StmtProcessor;

@Data
@NoArgsConstructor
public abstract class AbstractRSocketMessage<T> implements RSocketAppMessage {

    private MessageType messageType;
    private T body;

    public static <T> AbstractRSocketMessage<T> create(Class<? extends AbstractRSocketMessage<T>> clazz, MessageType messageType, T t) {

        return StmtProcessor.create(clazz, rsm -> {
            rsm.setBody(t);
            rsm.setMessageType(messageType);
        });
    }
}
