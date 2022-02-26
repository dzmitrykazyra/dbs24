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

@Data
@Entity
@IdClass(OrderHistPK.class)
@Table(name = "pr_package_orders_hist")
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
public class OrderHist extends ObjectRoot implements PersistenceEntity {

    @Id
    @NotNull
    @EqualsAndHashCode.Include
    @Column(name = "order_id", updatable = false)
    private Integer orderId;

    @Id
    @NotNull
    @EqualsAndHashCode.Include
    @Column(name = "actual_date")
    private LocalDateTime actualDate;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "package_id", referencedColumnName = "package_id")
    private AppPackage appPackage;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "batch_template_id", referencedColumnName = "batch_template_id")
    private BatchTemplate batchTemplate;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "order_status_id", referencedColumnName = "order_status_id")
    private OrderStatus orderStatus;

    @Column(name = "order_name")
    private String orderName;

    @Column(name = "ordered_batches_amount")
    private Integer orderedBatchesAmount;

    @Column(name = "success_batches_amount")
    private Integer successBatchesAmount;

    @Column(name = "fail_batches_amount")
    private Integer failBatchesAmount;

    @Column(name = "exec_start_date")
    private LocalDateTime execStartDate;

    @Column(name = "exec_finish_date")
    private LocalDateTime execFinishDate;

    @Column(name = "exec_last_date")
    private LocalDateTime execLastDate;

    @Column(name = "order_note")
    private String orderNote;

}
