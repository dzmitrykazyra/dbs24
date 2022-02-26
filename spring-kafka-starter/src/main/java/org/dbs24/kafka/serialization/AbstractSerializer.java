/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.kafka.serialization;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.common.serialization.Serializer;
import org.dbs24.stmt.StmtProcessor;

public abstract class AbstractSerializer<T extends Object> implements Serializer<T> {

    final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public byte[] serialize(String topic, T data) {
        return StmtProcessor.create(() -> objectMapper.writeValueAsBytes(data));
    }
}
