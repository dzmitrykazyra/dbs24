/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.insta.tmp.rest.api;

import org.dbs24.insta.tmp.entity.Bot;

import lombok.Data;
import org.dbs24.application.core.locale.NLS;

@Data
public class BotInfo {

    private Integer botId;
    private Long actualDate;
    private Integer botStatusId;
    private String email;
    private String password;
    private String userName;
    private String sessionId;
    private String userAgent;

    public void assign(Bot bot) {
        setActualDate(NLS.localDateTime2long(bot.getActualDate()));
        setBotId(bot.getBotId());
        setBotStatusId(bot.getBotStatus().getBotSStatusId());
        setEmail(bot.getEmail());
        setPassword(bot.getPassword());
        setSessionId(bot.getSessionId());
        setUserAgent(bot.getUserAgent());
        setUserName(bot.getUserName());
    }
}
