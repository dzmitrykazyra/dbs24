/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.insta.tmp.entity;

import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import static javax.persistence.FetchType.LAZY;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import lombok.Data;
import org.dbs24.application.core.api.ObjectRoot;
import org.dbs24.persistence.api.PersistenceEntity;

@Data
@Entity
@Table(name = "TASKS")
public class Task extends ObjectRoot implements PersistenceEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_tasks")
    @SequenceGenerator(name = "seq_tasks", sequenceName = "seq_tasks", allocationSize = 1)
    @Column(name = "TASK_ID", updatable = false)
    private Long taskId;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "PARENT_TASK_ID", referencedColumnName = "TASK_ID")
    private Task parentTaskId;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "ACCOUNT_ID", referencedColumnName = "ACCOUNT_ID")
    private Account account;

    @NotNull
    @Column(name = "actual_date")
    private LocalDateTime actualDate;

    @ManyToOne(fetch = LAZY)
    @NotNull
    @JoinColumn(name = "TASK_TYPE_ID", referencedColumnName = "TASK_TYPE_ID")
    private TaskType taskType;

    @ManyToOne(fetch = LAZY)
    @NotNull
    @JoinColumn(name = "TASK_RESULT_ID", referencedColumnName = "TASK_RESULT_ID")
    private TaskResult taskResult;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "BOT_EXECUTOR_ID", referencedColumnName = "BOT_ID")
    private Bot bot;

    @Column(name = "TASK_START_DATE")
    private LocalDateTime taskStartDate;

    @Column(name = "TASK_FINISH_DATE")
    private LocalDateTime taskFinishDate;

    @Column(name = "ERROR")
    private String error;

    @Column(name = "REQUEST_PAGINATION_ID")
    private String requestPaginationId;
}
