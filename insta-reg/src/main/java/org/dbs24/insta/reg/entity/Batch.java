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

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Data
@Entity
@Table(name = "inst_actionbathesref")
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
public class Batch extends ObjectRoot implements PersistenceEntity {

    @Id
    @NotNull
    @EqualsAndHashCode.Include
    @Column(name = "batch_id", updatable = false)
    private Integer batchId;

    @Column(name = "batch_name")
    private String batchName;

}
