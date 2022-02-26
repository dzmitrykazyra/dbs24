/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.app.promo.rest.dto.bot;

import lombok.Data;
import org.dbs24.spring.core.api.EntityInfo;

@Data
public class BotInfo implements EntityInfo {

    private Integer botId;
    private Integer botStatusId;
    private Integer providerId;
    private String pass;
    private String login;
    private String sessionId;
    private String botNote;

}
