/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dbs24.application.core.api.ObjectRoot;
import org.dbs24.persistence.api.PersistenceEntity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

import static javax.persistence.FetchType.LAZY;

@Data
@Entity
@Table(name = "wa_agent_messages_hist")
@IdClass(AgentMessagePK.class)
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
public class AgentMessageHist extends ObjectRoot implements PersistenceEntity {

    @Id
    @NotNull
    @EqualsAndHashCode.Include
    @Column(name = "message_id", updatable = false)
    private Integer messageId;

    @Id
    @NotNull
    @EqualsAndHashCode.Include
    @Column(name = "actual_date")
    private LocalDateTime actualDate;

    @Column(name = "create_date")
    private LocalDateTime createDate;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "agent_id", referencedColumnName = "agent_id")
    private Agent agent;

    @Column(name = "phone_num")
    private String phoneNum;

    @Column(name = "is_tracking_allowed")
    private Boolean isTrackingAllowed;

    @Column(name = "message_note")
    private String messageNote;
    
}
