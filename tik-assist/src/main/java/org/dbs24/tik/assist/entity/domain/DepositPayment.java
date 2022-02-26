/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.tik.assist.entity.domain;

import java.time.LocalDateTime;
import java.math.BigDecimal;
import javax.persistence.Column;
import javax.persistence.Entity;
import static javax.persistence.FetchType.LAZY;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import lombok.Data;
import org.dbs24.application.core.api.ObjectRoot;
import org.dbs24.persistence.api.PersistenceEntity;

@Data
@Entity
@Table(name = "tik_user_deposit_payments")
public class DepositPayment extends ObjectRoot implements PersistenceEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_tik_deposit_payments")
    @SequenceGenerator(name = "seq_tik_deposit_payments", sequenceName = "seq_tik_deposit_payments", allocationSize = 1)
    @NotNull
    @Column(name = "payment_id", updatable = false)
    private Integer paymentId;

    @NotNull
    @Column(name = "payment_sum")
    private BigDecimal paymentSum;

    @ManyToOne(fetch = LAZY)
    @NotNull
    @JoinColumn(name = "payment_type_id", referencedColumnName = "payment_type_id")
    private DepositPaymentType depositPaymentType;

    @ManyToOne(fetch = LAZY)
    @NotNull
    @JoinColumn(name = "user_id", referencedColumnName = "user_id")
    private User user;

    @ManyToOne(fetch = LAZY)
    @NotNull
    @JoinColumn(name = "currency_iso", referencedColumnName = "currency_iso")
    private Currency currency;

    @NotNull
    @Column(name = "payment_date")
    private LocalDateTime paymentDate;

    @Column(name = "payment_note")
    private String paymentNote;
}
