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
@Table(name = "pr_batches_templates")
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
public class BatchTemplate extends ObjectRoot implements PersistenceEntity {

    @Id
    @GeneratedValue(strategy = SEQUENCE, generator = SEQ_GENERAL)
    @SequenceGenerator(name = SEQ_GENERAL, sequenceName = SEQ_GENERAL, allocationSize = 1)
    @NotNull
    @Column(name = "batch_template_id", updatable = false)
    @EqualsAndHashCode.Include
    private Integer batchTemplateId;

    @NotNull
    @Column(name = "actual_date")
    private LocalDateTime actualDate;

    @NotNull
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "batch_type_id", referencedColumnName = "batch_type_id")
    private BatchType batchType;

    @NotNull
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "provider_id", referencedColumnName = "provider_id")
    private Provider provider;

    @Column(name = "template_name")
    private String templateName;

    @Column(name = "is_actual")
    private Boolean isActual;

}
