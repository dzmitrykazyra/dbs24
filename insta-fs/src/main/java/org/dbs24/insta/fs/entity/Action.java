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

import static javax.persistence.FetchType.LAZY;

@Data
@Entity
@Table(name = "ifs_actions")
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
public class Action extends ObjectRoot implements PersistenceEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_ifs_actions")
    @SequenceGenerator(name = "seq_ifs_actions", sequenceName = "seq_ifs_actions", allocationSize = 1)
    @NotNull
    @EqualsAndHashCode.Include
    @Column(name = "action_id", updatable = false)
    private Long actionId;

    @NotNull
    @Column(name = "action_start_date")
    private LocalDateTime actionStartDate;

    @NotNull
    @Column(name = "action_finish_date")
    private LocalDateTime actionFinishDate;

    @ManyToOne(fetch = LAZY)
    @NotNull
    @JoinColumn(name = "action_result_id", referencedColumnName = "action_result_id")
    private ActionResult actionResult;

    @ManyToOne(fetch = LAZY)
    @NotNull
    @JoinColumn(name = "action_type_id", referencedColumnName = "action_type_id")
    private ActionType actionType;

    @ManyToOne(fetch = LAZY)
    @NotNull
    @JoinColumn(name = "account_id", referencedColumnName = "account_id")
    private Account account;

    @ManyToOne(fetch = LAZY)
    //@NotNull
    @JoinColumn(name = "post_id", referencedColumnName = "post_id")
    private Post post;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "task_id", referencedColumnName = "task_id")
    private Task task;

    @Column(name = "action_note")
    private String actionNote;

    @Column(name = "error")
    private String error;

}
