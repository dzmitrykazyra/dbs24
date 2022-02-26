/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.insta.tmp.entity;

import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import static javax.persistence.FetchType.LAZY;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import lombok.Data;

@Data
@Entity
@Table(name = "BOTS_HIST")
@IdClass(BotHistPK.class)
public class BotHist {
    
    @Id
    @NotNull
    @Column(name = "BOT_ID", updatable = false)
    private Integer botId;

    @Id
    @NotNull
    @Column(name = "actual_date")
    private LocalDateTime actualDate;

    @ManyToOne(fetch = LAZY)
    @NotNull
    @JoinColumn(name = "BOT_STATUS_ID", referencedColumnName = "BOT_STATUS_ID")
    private BotStatus botStatus;

    @Column(name = "EMAIL")
    private String email;

    @Column(name = "PASSWORD")
    private String password;

    @Column(name = "USERNAME")
    private String userName;

    @Column(name = "SESSIONID")
    private String sessionId;

    @Column(name = "USER_AGENT")
    private String userAgent;
    
    public void assign(Bot bot) {
        this.setActualDate(bot.getActualDate());
        this.setBotId(bot.getBotId());
        this.setBotStatus(bot.getBotStatus());
        this.setEmail(bot.getEmail());
        this.setPassword(bot.getPassword());
        this.setSessionId(bot.getSessionId());
        this.setUserAgent(bot.getUserAgent());
        this.setUserName(bot.getUserName());
    }    
}
