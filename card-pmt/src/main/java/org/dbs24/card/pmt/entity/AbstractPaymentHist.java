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
@Table(name = "pmt_payments_hist")
@Inheritance(strategy = InheritanceType.JOINED)
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
@IdClass(AbstractPaymentHistPK.class)
public class AbstractPaymentHist extends ObjectRoot implements PersistenceEntity {

    @Id
    @NotNull
    @Column(name = "payment_id", updatable = false)
    @EqualsAndHashCode.Include
    private Integer paymentId;

    @Id
    @NotNull
    @Column(name = "actual_date")
    @EqualsAndHashCode.Include
    private LocalDateTime actualDate;

    @Column(name = "payment_date")
    private LocalDateTime paymentDate;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "application_id", referencedColumnName = "application_id")
    private Application application;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "payer_id", referencedColumnName = "payer_id")
    private Payer payer;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "payment_status_id", referencedColumnName = "payment_status_id")
    private PaymentStatus paymentStatus;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "payment_service_id", referencedColumnName = "payment_service_id")
    private PaymentService paymentService;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "currency_iso", referencedColumnName = "currency_iso")
    private Currency currency;

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

