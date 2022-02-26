package org.dbs24.card.pmt.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dbs24.application.core.api.ObjectRoot;
import org.dbs24.persistence.api.PersistenceEntity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@Entity
@IdClass(GooglePaymentHistPK.class)
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
@Table(name = "pmt_google_payments_hist")
public class GooglePaymentHist extends ObjectRoot implements PersistenceEntity {

    @Id
    @NotNull
    @EqualsAndHashCode.Include
    @Column(name = "payment_id")
    private Integer paymentId;

    @Id
    @NotNull
    @EqualsAndHashCode.Include
    @Column(name = "actual_date")
    private LocalDateTime actualDate;

    @Column(name = "google_purchase_token")
    private String googlePurchaseToken;

    @Column(name = "google_order_id")
    private String googleOrderId;

    @Column(name = "google_sku")
    private String googleSku;
}