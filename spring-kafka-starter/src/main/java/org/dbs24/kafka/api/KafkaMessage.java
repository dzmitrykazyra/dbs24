/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.kafka.api;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;

import static org.dbs24.application.core.service.funcs.StringFuncs.createRandomString;

@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
public abstract class KafkaMessage {

    @JsonIgnore
    @EqualsAndHashCode.Include
    final String msgId = createRandomString(50);
    @JsonIgnore
    private Integer partitionId;
}
