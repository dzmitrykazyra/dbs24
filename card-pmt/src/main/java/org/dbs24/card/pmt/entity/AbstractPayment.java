/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.card.pmt.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.dbs24.application.core.api.ObjectRoot;
import org.dbs24.persistence.api.PersistenceEntity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;

import static javax.persistence.FetchType.LAZY;

@Data
@Entity
@Table(name = "pmt_payments")
@Inheritance(strategy = InheritanceType.JOINED)
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
@NoArgsConstructor
public abstract class AbstractPayment extends ObjectRoot implements PersistenceEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_pmt_payments")
    @SequenceGenerator(name = "seq_pmt_payments", sequenceName = "seq_pmt_payments", allocationSize = 1)
    @NotNull
    @Column(name = "payment_id", updatable = false)
    @EqualsAndHashCode.Include
    private Integer paymentId;

    @NotNull
    @Column(name = "actual_date")
    private LocalDateTime actualDate;

    @NotNull
    @Column(name = "payment_date")
    private LocalDateTime paymentDate;

    @ManyToOne(fetch = LAZY)
    @NotNull
    @JoinColumn(name = "application_id", referencedColumnName = "application_id")
    private Application application;

    @ManyToOne(fetch = LAZY)
    @NotNull
    @JoinColumn(name = "payer_id", referencedColumnName = "payer_id")
    private Payer payer;    
    
    @ManyToOne(fetch = LAZY)
    @NotNull
    @JoinColumn(name = "payment_status_id", referencedColumnName = "payment_status_id")
    private PaymentStatus paymentStatus;

    @ManyToOne(fetch = LAZY)
    @NotNull
    @JoinColumn(name = "payment_service_id", referencedColumnName = "payment_service_id")
    private PaymentService paymentService;

    @ManyToOne(fetch = LAZY)
    @NotNull
    @JoinColumn(name = "currency_iso", referencedColumnName = "currency_iso")
    private Currency currency;

    @NotNull
    @Column(name = "summ")
    private BigDecimal paymentSumm;
    
    @Column(name = "summ_micros")
    private Long paymentSummMicros;    

    @Column(name = "platform")
    private String platform;

    @Column(name = "package")
    private String appPackage;    

    @Column(name = "pmt_note")
    private String pmtNote;

}
