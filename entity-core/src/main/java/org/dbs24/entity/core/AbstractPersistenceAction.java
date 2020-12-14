/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.entity.core;

import org.dbs24.entity.core.api.Action;
import org.dbs24.entity.core.api.ActionEntity;
import org.dbs24.entity.action.ActionCode;
import org.dbs24.persistence.api.PersistenceSetup;
import static org.dbs24.consts.SysConst.*;
import java.time.LocalDateTime;
import java.time.LocalTime;
import javax.persistence.*;
import lombok.Data;

/**
 *
 * @author Козыро Дмитрий
 */
@PersistenceSetup(
        persistence_unit = "core_persistence",
        table = "core_Actions")
@Entity
@Data
@Table(name = "core_Actions")
//@Inheritance(strategy = InheritanceType.JOINED)
public class AbstractPersistenceAction implements Action {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_action_id")
    @SequenceGenerator(name = "seq_action_id", sequenceName = "seq_action_id", allocationSize = 1)
    @Column(name = "action_id", updatable = false)
    private Long actionId;
    @ManyToOne(targetEntity = AbstractPersistenceEntity.class)
    @JoinColumn(name = "entity_id", referencedColumnName = "entity_id")
    private ActionEntity entity;

    @Column(name = "user_id", updatable = false)
    private Long userId = SERVICE_USER_ID;
    //@ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @ManyToOne
    @JoinColumn(name = "action_code", referencedColumnName = "action_code", updatable = false)
    private ActionCode actionCode;
    @Column(name = "execute_date", updatable = false)
    private LocalDateTime executeDate = LocalDateTime.now();
    @Column(name = "action_address", updatable = false)
    private String actionAddress = UNKNOWN;
    @Column(name = "err_msg")
    private String errMsg;
    @Column(name = "action_duration", updatable = false)
    private LocalTime actionDuration;
    @Column(name = "notes", updatable = false)
    private String notes;
  
}
