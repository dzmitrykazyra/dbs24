/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.app.promo.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dbs24.application.core.api.ObjectRoot;
import org.dbs24.persistence.api.PersistenceEntity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.SEQUENCE;
import static org.dbs24.app.promo.consts.AppPromoutionConsts.DbSequences.SEQ_GENERAL;

@Data
@Entity
@Table(name = "pr_orders_actions")
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
public class OrderAction extends ObjectRoot implements PersistenceEntity {

    @Id
    @GeneratedValue(strategy = SEQUENCE, generator = SEQ_GENERAL)
    @SequenceGenerator(name = SEQ_GENERAL, sequenceName = SEQ_GENERAL, allocationSize = 1)
    @NotNull
    @EqualsAndHashCode.Include
    @Column(name = "action_id", updatable = false)
    private Integer actionId;

    @NotNull
    @Column(name = "actual_date")
    private LocalDateTime actualDate;

    @NotNull
    @Column(name = "action_start_date", updatable = false)
    private LocalDateTime actionStartDate;

    @Column(name = "action_finish_date")
    private LocalDateTime actionFinishDate;

    @NotNull
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "act_ref_id", referencedColumnName = "act_ref_id")
    private Action action;

    @NotNull
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "batch_setup_id", referencedColumnName = "batch_setup_id")
    private BatchSetup batchSetup;

    @NotNull
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "bot_id", referencedColumnName = "bot_id")
    private Bot bot;

    @NotNull
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "action_result_id", referencedColumnName = "action_result_id")
    private ActionResult actionResult;

    @NotNull
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "order_id", referencedColumnName = "order_id")
    private Order order;

    @Column(name = "used_ip")
    private String usedIp;

    @NotNull
    @Column(name = "execution_order")
    private Integer executionOrder;

    @Column(name = "err_msg")
    private String errMsg;

}
