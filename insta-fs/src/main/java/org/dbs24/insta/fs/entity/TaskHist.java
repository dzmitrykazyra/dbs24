/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.insta.fs.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dbs24.application.core.api.ObjectRoot;
import org.dbs24.persistence.api.PersistenceEntity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "ifs_tasks_hist")
@IdClass(TaskHistPK.class)
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
public class TaskHist extends ObjectRoot implements PersistenceEntity {

    @Id
    @NotNull
    @EqualsAndHashCode.Include
    @Column(name = "task_id", updatable = false)
    private Long taskId;

    @Id    
    @NotNull
    @EqualsAndHashCode.Include
    @Column(name = "actual_date")
    private LocalDateTime actualDate;

    @JoinColumn(name = "account_id", referencedColumnName = "account_id")
    private Account account;

    @Column(name = "start_date")
    private LocalDateTime startDate;

    @Column(name = "finish_date")
    private LocalDateTime finishDate;

    @JoinColumn(name = "task_status_id", referencedColumnName = "task_status_id")
    private TaskStatus taskStatus;

    @Column(name = "last_page_index")
    private Long lastPageIndex;

    @Column(name = "post_cnt")
    private Integer postCnt;

    @Column(name = "post_ready")
    private Integer postReady;

    @Column(name = "last_error")
    private String lastError;
}
