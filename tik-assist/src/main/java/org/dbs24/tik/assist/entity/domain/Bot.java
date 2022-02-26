/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.tik.assist.entity.domain;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.*;
import org.dbs24.application.core.api.ObjectRoot;
import org.dbs24.persistence.api.PersistenceEntity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

import static javax.persistence.FetchType.LAZY;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
@Table(name = "tik_bots")
@JsonDeserialize(builder = Bot.BotBuilder.class)
public class Bot extends ObjectRoot implements PersistenceEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_tik_bots")
    @SequenceGenerator(name = "seq_tik_bots", sequenceName = "seq_tik_bots", allocationSize = 1)
    @Column(name = "bot_id", updatable = false)
    @EqualsAndHashCode.Include
    private Integer botId;

    @NotNull
    @Column(name = "actual_date")
    private LocalDateTime actualDate;

    @ManyToOne(fetch = LAZY)
    @NotNull
    @JoinColumn(name = "bot_status_id", referencedColumnName = "bot_status_id")
    private BotStatus botStatus;
    
    @ManyToOne(fetch = LAZY)
    @NotNull
    @JoinColumn(name = "bot_registration_type_id", referencedColumnName = "bot_registration_type_id")
    private BotRegistrationType botRegistrationType;
    
    @NotNull
    @Column(name = "create_date")
    private LocalDateTime createDate;
    
    @Column(name = "sec_user_id")
    private String secUserId;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "email")
    private String email;
    
    @Column(name = "bot_note")
    private String botNote;
    
    //@NotNull
    @Column(name = "pass")
    private String pass;

    @Basic(fetch = LAZY)    
    @Column(name = "attributes")
    private byte[] attributes;
}
