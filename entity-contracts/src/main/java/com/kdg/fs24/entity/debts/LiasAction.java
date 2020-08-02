/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kdg.fs24.entity.debts;

import com.kdg.fs24.references.liases.actiontype.LiasActionType;
import com.kdg.fs24.references.liases.finopercode.LiasFinOperCode;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import com.kdg.fs24.lias.opers.attrs.*;
//import com.kdg.fs24.log.mgmt.LogService;
import com.kdg.fs24.application.core.api.ObjectRoot;
import com.kdg.fs24.application.core.sysconst.SysConst;
import com.kdg.fs24.lias.opers.napi.SaveAccretionHist;
import com.kdg.fs24.persistence.api.PersistenceEntity;
import com.kdg.fs24.entity.document.Document;
import javax.persistence.*;
import lombok.Data;
import com.kdg.fs24.references.liases.status.LiasOperStatus;

/**
 *
 * @author kazyra_d
 */
@Data
@Entity
@Table(name = "liasActions")
public class LiasAction extends ObjectRoot implements PersistenceEntity {

    // код финансовой операции
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_lias_action_id")
    @SequenceGenerator(name = "seq_lias_action_id", sequenceName = "seq_lias_action_id", allocationSize = 1)
    @Column(name = "lias_action_id")
    private Long liasActionId;

    @ManyToOne
    @JoinColumn(name = "lias_id", referencedColumnName = "lias_id", updatable = false)
    private Lias lias;
    
    @ManyToOne
    @JoinColumn(name = "fin_oper_code", referencedColumnName = "fin_oper_code", updatable = false)
    private LiasFinOperCode liasFinOperCode;
    // тип финансовой операции
    @ManyToOne
    @JoinColumn(name = "action_type_id", referencedColumnName = "action_type_id", updatable = false)
    private LiasActionType liasActionType;
    // сумма финансовой операции
    @Column(name = "lias_sum", updatable = false)
    private BigDecimal liasSum;
    // дата финансовой операции
    @Column(name = "lias_date", updatable = false)
    private LocalDate liasDate;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "doc_id", referencedColumnName = "doc_id", updatable = false)
    private Document document;

    @Column(name = "server_date", updatable = false)
    private LocalDateTime serverDate = LocalDateTime.now();
    
    @ManyToOne
    @JoinColumn(name = "fin_oper_status_id", referencedColumnName = "fin_oper_status_id")
    private LiasOperStatus liasOperStatus;
    
    @Transient
    private int liasOperHC;

    //private SaveAccretionHist sah;
}
