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
import java.time.LocalDateTime;

import static javax.persistence.FetchType.LAZY;

@Data
@Entity
@Table(name = "tda_developers_contracts_hist")
@IdClass(ContractHistPK.class)
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
public class ContractHist extends ObjectRoot implements PersistenceEntity {

    @Id
    @Column(name = "contract_id", updatable = false)
    @EqualsAndHashCode.Include
    private Long contractId;

    @Id
    @EqualsAndHashCode.Include
    @Column(name = "actual_date")
    private LocalDateTime actualDate;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "contract_status_id", referencedColumnName = "contract_status_id")
    private ContractStatus contractStatus;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "developer_id", referencedColumnName = "developer_id")
    private Developer developer;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "tariff_plan_id", referencedColumnName = "tariff_plan_id")
    private TariffPlan tariffPlan;

    @Column(name = "begin_date")
    private LocalDateTime beginDate;

    @Column(name = "end_date")
    private LocalDateTime endDate;

    @Column(name = "cancel_date")
    private LocalDateTime cancelDate;

}
