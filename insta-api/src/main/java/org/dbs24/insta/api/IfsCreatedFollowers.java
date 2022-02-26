/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.insta.api;

import lombok.Data;
import org.dbs24.kafka.api.KafkaMessage;
import java.util.Collection;

@Data
public class IfsCreatedFollowers extends KafkaMessage {
    private Long instaId;
    private Collection<ShortFollowerRecord> followers;
    private String nextMaxId;
    private Boolean isMoreAvailAble;
}
