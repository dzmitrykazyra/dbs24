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
@Table(name = "pmt_apple_payments_hist")
@IdClass(ApplePaymentHistPK.class)
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
@PrimaryKeyJoinColumn(name = "payment_id", referencedColumnName = "payment_id")
public class ApplePaymentHist extends ObjectRoot implements PersistenceEntity {

    @Id
    @NotNull
    @Column(name = "payment_id")
    @EqualsAndHashCode.Include
    private Integer paymentId;

    @Id
    @NotNull
    @Column(name = "actual_date")
    @EqualsAndHashCode.Include
    private LocalDateTime actualDate;

    @Column(name = "apple_transaction_id")
    private String appleTransactionId;

    @Column(name = "apple_original_transaction_id")
    private String appleOriginalTransactionId;

    @Column(name = "apple_product_id")
    private String appleProductId;

}
