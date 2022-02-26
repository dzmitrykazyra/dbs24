/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.insta.fs.entity;

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
@Table(name = "ifs_task_statuses_ref")
public class TaskStatus extends ObjectRoot implements PersistenceEntity {

    @Id
    @NotNull
    @Column(name = "task_status_id", updatable = false)
    private Integer taskStatusId;

    @NotNull
    @Column(name = "task_status_name")
    private String taskStatusName;
}
