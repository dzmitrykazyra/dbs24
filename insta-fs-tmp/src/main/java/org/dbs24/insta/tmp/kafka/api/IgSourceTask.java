/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.insta.tmp.kafka.api;

import lombok.Data;
import org.dbs24.kafka.api.KafkaMessage;

@Data
public final class IgSourceTask extends KafkaMessage{

    private Long sourceId;
    private String sourceUrl;
}
