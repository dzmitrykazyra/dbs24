/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.tik.dev.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dbs24.application.core.api.ObjectRoot;
import org.dbs24.persistence.api.PersistenceEntity;
import org.dbs24.tik.dev.entity.reference.ContractStatus;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.SEQUENCE;
import static org.dbs24.tik.dev.consts.TikDevApiConsts.Databases.SEQ_GENERAL;

@Data
@Entity
@Table(name = "tda_developers_contracts")
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
public class Contract extends ObjectRoot implements PersistenceEntity {

    @Id
    @GeneratedValue(strategy = SEQUENCE, generator = SEQ_GENERAL)
    @SequenceGenerator(name = SEQ_GENERAL, sequenceName = SEQ_GENERAL, allocationSize = 1)
    @NotNull
    @Column(name = "contract_id", updatable = false)
    @EqualsAndHashCode.Include
    private Long contractId;

    @NotNull
    @Column(name = "actual_date")
    private LocalDateTime actualDate;

    @NotNull
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "contract_status_id", referencedColumnName = "contract_status_id")
    private ContractStatus contractStatus;

    @NotNull
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "developer_id", referencedColumnName = "developer_id")
    private Developer developer;

    @NotNull
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "tariff_plan_id", referencedColumnName = "tariff_plan_id")
    private TariffPlan tariffPlan;

    @NotNull
    @Column(name = "begin_date")
    private LocalDateTime beginDate;

    @NotNull
    @Column(name = "end_date")
    private LocalDateTime endDate;

    @Column(name = "cancel_date")
    private LocalDateTime cancelDate;

}
