/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.insta.tmp.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import lombok.Data;
import org.dbs24.application.core.api.ObjectRoot;
import org.dbs24.persistence.api.PersistenceEntity;

@Data
@Entity
@Table(name = "SOURCE_STATUSES_REF")
public class SourceStatus extends ObjectRoot implements PersistenceEntity {

    @Id
    @NotNull
    @Column(name = "source_status_id", updatable = false)
    private Integer sourceStatusId;

    @NotNull
    @Column(name = "source_status_name")
    private String sourceStatusName;
}
