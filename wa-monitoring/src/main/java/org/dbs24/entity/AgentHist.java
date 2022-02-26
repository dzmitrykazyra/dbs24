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

@Data
@Entity
@IdClass(AgentHistPK.class)
@Table(name = "wa_agents_hist")
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
public class AgentHist extends ObjectRoot implements PersistenceEntity {

    @Id
    @NotNull
    @Column(name = "agent_id", updatable = false)
    @EqualsAndHashCode.Include
    private Integer agentId;

    @Id
    @NotNull
    @Column(name = "actual_date")
    @EqualsAndHashCode.Include
    private LocalDateTime actualDate;

    @ManyToOne
    @JoinColumn(name = "agent_status_id", referencedColumnName = "agent_status_id")
    private AgentStatus agentStatus;

    @ManyToOne
    @JoinColumn(name = "agent_os_type_id", referencedColumnName = "agent_os_type_id")
    private AgentOsType agentOsType;
            
    @Column(name = "phone_num")
    private String phoneNum;

    //@NotNull
    @Column(name = "payload")
    private String payload;

    @Column(name = "created")
    private LocalDateTime created;  
    
    @Column(name = "agent_note")
    private String agentNote;    

}
