/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.insta.reg.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dbs24.application.core.api.ObjectRoot;
import org.dbs24.persistence.api.PersistenceEntity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

import static javax.persistence.FetchType.LAZY;

@Data
@Entity
@Table(name = "inst_actionbatсhes")
@IdClass(ActionBatchPK.class)
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
public class ActionBatches extends ObjectRoot implements PersistenceEntity {

    @Id
    @NotNull
    @EqualsAndHashCode.Include
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "action_ref_id", referencedColumnName = "action_ref_id")
    private Action actionRefId;

    @Id
    @NotNull
    @EqualsAndHashCode.Include
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "batch_id", referencedColumnName = "batch_id")
    private Batch batchId;

    @Column(name = "action_delay")
    private Integer actionDelay;

}
