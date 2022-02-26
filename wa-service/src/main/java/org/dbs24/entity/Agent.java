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
@Table(name = "wa_agents")
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
public class Agent extends ObjectRoot implements PersistenceEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_wa_agents")
    @SequenceGenerator(name = "seq_wa_agents", sequenceName = "seq_wa_agents", allocationSize = 1)
    @NotNull
    @EqualsAndHashCode.Include
    @Column(name = "agent_id", updatable = false)
    private Integer agentId;

    @NotNull
    @Column(name = "actual_date")
    private LocalDateTime actualDate;

    @ManyToOne(fetch = LAZY)
    @NotNull
    @JoinColumn(name = "agent_status_id", referencedColumnName = "agent_status_id")    
    private AgentStatus agentStatus;
            
    @NotNull
    @Column(name = "phone_num")
    private String phoneNum;

    //@NotNull
    @Column(name = "payload")
    private String payload;

    @NotNull
    @Column(name = "created")
    private LocalDateTime created;    

    @Column(name = "agent_note")
    private String agentNote; 
    
}
