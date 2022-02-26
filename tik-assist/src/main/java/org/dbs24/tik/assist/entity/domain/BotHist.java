/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.tik.assist.entity.domain;

import java.io.Serializable;
import java.time.LocalDateTime;
import javax.persistence.*;
import static javax.persistence.FetchType.LAZY;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.dbs24.application.core.api.ObjectRoot;
import org.dbs24.persistence.api.PersistenceEntity;
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@IdClass(BotHistPK.class)
@Table(name = "tik_bots_hist")
public class BotHist extends ObjectRoot implements PersistenceEntity {

    @Id
    @NotNull
    @Column(name = "bot_id", updatable = false)
    private Integer botId;

    @Id
    @NotNull
    @Column(name = "actual_date")
    private LocalDateTime actualDate;

    @Column(name = "bot_status_id")
    private Integer botStatusId;

    @Column(name = "bot_registration_type_id")
    private Integer botRegistryTypeId;

    @Column(name = "create_date")
    private LocalDateTime createDate;

    @Column(name = "sec_user_id")
    private String secUserId;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "email")
    private String email;

    @Column(name = "pass")
    private String pass;

    @Column(name = "bot_note")
    private String botNote;
    
    @Basic(fetch = LAZY)    
    @Column(name = "attributes")
    private byte[] attributes;

    public static BotHist toBotHist(Bot bot) {

        return BotHist.builder()
                .botId(bot.getBotId())
                .actualDate(bot.getActualDate())
                .botStatusId(bot.getBotStatus().getBotStatusId())
                .botRegistryTypeId(bot.getBotRegistrationType().getBotRegistryTypeId())
                .createDate(bot.getCreateDate())
                .secUserId(bot.getSecUserId())
                .phoneNumber(bot.getPhoneNumber())
                .email(bot.getEmail())
                .botNote(bot.getBotNote())
                .pass(bot.getPass())
                .attributes(bot.getAttributes())
                .build();
    }
}

@Data
class BotHistPK implements Serializable {

    private Integer botId;
    private LocalDateTime actualDate;
}