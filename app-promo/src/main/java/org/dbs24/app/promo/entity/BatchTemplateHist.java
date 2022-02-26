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
@IdClass(BatchTemplateHistPK.class)
@Table(name = "pr_batches_templates_hist")
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
public class BatchTemplateHist extends ObjectRoot implements PersistenceEntity {

    @Id
    @NotNull
    @Column(name = "batch_template_id", updatable = false)
    @EqualsAndHashCode.Include
    private Integer batchTemplateId;

    @Id
    @NotNull
    @Column(name = "actual_date", updatable = false)
    @EqualsAndHashCode.Include
    private LocalDateTime actualDate;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "batch_type_id", referencedColumnName = "batch_type_id")
    private BatchType batchType;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "provider_id", referencedColumnName = "provider_id")
    private Provider provider;

    @Column(name = "template_name")
    private String templateName;

    @Column(name = "is_actual")
    private Boolean isActual;

}
