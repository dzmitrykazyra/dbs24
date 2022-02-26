/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.insta.api;

import lombok.Data;
import org.dbs24.kafka.api.KafkaMessage;

@Data
public class IfsAccountIsCreated extends KafkaMessage {

    private Long instaId;
    private String instaUserName;
    private String instaFullName;
    private String instaBiography;
    private Integer mediacount;
    private Integer followers;
    private Integer followees;
    private Boolean isPrivate;
    private Boolean isVerified;
    private String profilePicUrl;
    private String profilePicHdUrl;
}
