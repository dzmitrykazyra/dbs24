/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import org.dbs24.application.core.service.funcs.ServiceFuncs;
import org.dbs24.entity.core.AbstractActionEntity;
import javax.persistence.*;
import org.dbs24.entity.ContractSubject;
import org.dbs24.entity.Counterparty;
import org.dbs24.entity.core.AbstractPersistenceAction;
import org.dbs24.entity.core.api.Action;
import lombok.Data;
import org.dbs24.entity.kind.EntityKind;
import org.dbs24.entity.Currency;
import java.time.LocalDate;
import java.math.BigDecimal;
import org.dbs24.entity.tariff.AbstractTariffPlan;
import java.util.Collection;
import org.dbs24.entity.marks.EntityMark;
import org.dbs24.entity.debts.LiasDebt;
import org.dbs24.entity.bondschedule.PmtSchedule;
import org.dbs24.entity.tariff.TariffCalcRecord;
import javax.validation.constraints.NotNull;
import static org.dbs24.consts.SysConst.DATE_FORMAT;


@Data
@Entity
@Table(name = "core_EntityContracts")
@PrimaryKeyJoinColumn(name = "contract_id", referencedColumnName = "entity_id")
public abstract class AbstractEntityContract extends AbstractActionEntity {

    @ManyToOne(fetch = FetchType.LAZY)

    @JoinColumn(name = "contract_subject_id", referencedColumnName = "contract_subject_id", updatable = false)
    private ContractSubject contractSubject;
    // 
    //--------------------------------------------------------------------------    

    @Column(name = "contract_num")
    @NotNull
    private String contractNum;
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "counterparty_id", referencedColumnName = "counterparty_id")
    private Counterparty counterparty;
    // 
    //--------------------------------------------------------------------------    

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "entity_kind_id", referencedColumnName = "entity_kind_id", updatable = false)
    private EntityKind entityKind;
    // 
    //--------------------------------------------------------------------------    

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "currency_id", referencedColumnName = "currency_id")
    private Currency currency;
    // 
    //--------------------------------------------------------------------------    
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DATE_FORMAT)
    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)    
    @Column(name = "contract_date")
    @NotNull
    private LocalDate contractDate;
    // 
    //--------------------------------------------------------------------------    
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DATE_FORMAT)
    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)    
    @Column(name = "begin_date")
    @NotNull
    private LocalDate beginDate;
    // 
    //--------------------------------------------------------------------------    
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DATE_FORMAT)
    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)    
    @Column(name = "end_date")
    private LocalDate endDate;
    // 
    //--------------------------------------------------------------------------    

    @Column(name = "contract_summ")
    @NotNull
    private BigDecimal contractSumm;
    // 
    //--------------------------------------------------------------------------    
    @ManyToOne(targetEntity = AbstractTariffPlan.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "tariff_plan_id", referencedColumnName = "tariff_plan_id")
    private AbstractTariffPlan tariffPlan;
    // отметки на сущности
    //--------------------------------------------------------------------------
    @OneToMany
    @JoinColumn(name = "entity_id", referencedColumnName = "contract_id")
    private Collection<EntityMark> entityMarks;
    // исполенные действия
    //--------------------------------------------------------------------------
    @org.hibernate.annotations.BatchSize(size = 100)
    @OneToMany(targetEntity = AbstractPersistenceAction.class, mappedBy = "entity")
    @JsonIgnore
    private Collection<Action> entityActions;
    //--------------------------------------------------------------------------
    // задолженности по договору
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "debtContract")
    private Collection<LiasDebt> contractDebts;
    //--------------------------------------------------------------------------
    //графики по договору
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "entityContract")
    private Collection<PmtSchedule> pmtSchedules;
    //--------------------------------------------------------------------------
    // рассчеты сумм калькуляций
    //@org.hibernate.annotations.BatchSize(size = 500)
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "entity")
    @Transient
    private Collection<TariffCalcRecord> tariffCalcRecords = ServiceFuncs.<TariffCalcRecord>createCollection();

}
