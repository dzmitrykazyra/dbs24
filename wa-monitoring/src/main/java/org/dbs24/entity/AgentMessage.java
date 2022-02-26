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
import static javax.persistence.GenerationType.SEQUENCE;

@Data
@Entity
@Table(name = "wa_agent_messages")
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
public class AgentMessage extends ObjectRoot implements PersistenceEntity {

    @Id
    @GeneratedValue(strategy = SEQUENCE, generator = "seq_wa_agent_messages")
    @SequenceGenerator(name = "seq_wa_agent_messages", sequenceName = "seq_wa_agent_messages", allocationSize = 1)
    @NotNull
    @EqualsAndHashCode.Include
    @Column(name = "message_id", updatable = false)
    private Integer messageId;

    @NotNull
    @Column(name = "create_date")
    private LocalDateTime createDate;

    @NotNull
    @Column(name = "actual_date")
    private LocalDateTime actualDate;

    @ManyToOne(fetch = LAZY)
    @NotNull
    @JoinColumn(name = "agent_id", referencedColumnName = "agent_id")
    private Agent agent;

    @NotNull
    @Column(name = "phone_num")
    private String phoneNum;

    @NotNull
    @Column(name = "is_tracking_allowed")
    private Boolean isTrackingAllowed;

    @Column(name = "message_note")
    private String messageNote;
    
}
