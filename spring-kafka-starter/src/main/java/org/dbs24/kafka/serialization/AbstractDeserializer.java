/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.kafka.serialization;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.common.serialization.Deserializer;
import org.dbs24.application.core.service.funcs.GenericFuncs;
import org.dbs24.stmt.StmtProcessor;

public abstract class AbstractDeserializer<T extends Object> implements Deserializer<T> {

    final ObjectMapper objectMapper = new ObjectMapper();
    final Class<T> clazz = (Class<T>) GenericFuncs.getTypeParameterClass(this.getClass());

    @Override
    public T deserialize(String topic, byte[] data) {

        return (T) StmtProcessor.create(() -> objectMapper.readValue(data, clazz));
    }
}
