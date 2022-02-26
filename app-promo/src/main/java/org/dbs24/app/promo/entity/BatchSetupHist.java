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
@IdClass(BatchSetupHistPK.class)
@Table(name = "pr_batches_setup_hist")
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
public class BatchSetupHist extends ObjectRoot implements PersistenceEntity {

    @Id
    @NotNull
    @Column(name = "batch_setup_id", updatable = false)
    @EqualsAndHashCode.Include
    private Integer batchSetupId;

    @Id
    @NotNull
    @Column(name = "actual_date", updatable = false)
    @EqualsAndHashCode.Include
    private LocalDateTime actualDate;

    @NotNull
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "batch_template_id", referencedColumnName = "batch_template_id")
    private BatchTemplate batchTemplate;

    @Column(name = "execution_order")
    private Integer executionOrder;

    @Column(name = "min_delay")
    private Integer minDelay;

    @Column(name = "max_delay")
    private Integer maxDelay;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "act_ref_id", referencedColumnName = "act_ref_id")
    private Action action;

    @Column(name = "is_actual")
    private Boolean isActual;

    @Column(name = "batch_note")
    private String batchNote;

}
