/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.insta.tmp.kafka.api;

import lombok.Data;
import org.dbs24.kafka.api.KafkaMessage;

@Data
public class IgSource extends KafkaMessage {

    private Boolean isAdded = Boolean.FALSE;
    private Long sourceId;
    private Long postId;
    private Long instaPostId;
    private Long actualDate;
    private Integer sourceStatusId;
    private byte[] mainFaceBox;
    private String sourceHash;
    private String sourceUrl;

}
