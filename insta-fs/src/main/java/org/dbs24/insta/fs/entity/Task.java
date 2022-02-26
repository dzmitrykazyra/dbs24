/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.insta.fs.entity;

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
@Table(name = "ifs_tasks")
public class Task extends ObjectRoot implements PersistenceEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_ifs_tasks")
    @SequenceGenerator(name = "seq_ifs_tasks", sequenceName = "seq_ifs_tasks", allocationSize = 1)
    @NotNull
    @Column(name = "task_id", updatable = false)
    private Long taskId;

    @NotNull
    @Column(name = "actual_date")
    private LocalDateTime actualDate;

    @ManyToOne(fetch = LAZY)
    @NotNull
    @JoinColumn(name = "account_id", referencedColumnName = "account_id")
    private Account account;

    @NotNull
    @Column(name = "start_date")
    private LocalDateTime startDate;

    @NotNull
    @Column(name = "finish_date")
    private LocalDateTime finishDate;

    @ManyToOne(fetch = LAZY)
    @NotNull
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
