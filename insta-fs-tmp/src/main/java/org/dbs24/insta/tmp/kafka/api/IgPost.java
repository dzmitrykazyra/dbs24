/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.insta.tmp.kafka.api;

import lombok.Getter;
import lombok.Setter;
import org.dbs24.kafka.api.KafkaMessage;

@Getter
public class IgPost extends KafkaMessage {

    @Setter
    private Boolean isAdded = Boolean.FALSE;
    private Long actualDate;
    private Long instaId;
    private Long instaPostId;
    private Integer postStatusId;
    private Integer postTypeId;
    private Long mediaId;
    private String shortCode;

}
