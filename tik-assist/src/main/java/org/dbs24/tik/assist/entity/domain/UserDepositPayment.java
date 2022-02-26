package org.dbs24.tik.assist.entity.domain;

import lombok.Data;
import org.dbs24.application.core.api.ObjectRoot;
import org.dbs24.persistence.api.PersistenceEntity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

import static javax.persistence.FetchType.LAZY;

@Data
@Entity
@Table(name = "tik_user_deposit_payments")
public class UserDepositPayment extends ObjectRoot implements PersistenceEntity {

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
    @JoinColumn(name = "user_subscription_id", referencedColumnName = "user_subscription_id")
    private UserSubscription userSubscription;

    @ManyToOne(fetch = LAZY)
    @NotNull
    @JoinColumn(name = "plan_id", referencedColumnName = "plan_id")
    private UserPlan userPlan;

    @ManyToOne(fetch = LAZY)
    @NotNull
    @JoinColumn(name = "order_id", referencedColumnName = "order_id")
    private UserOrder userOrder;
}