package org.dbs24.tik.assist.entity.domain;

import lombok.*;
import org.dbs24.application.core.api.ObjectRoot;
import org.dbs24.persistence.api.PersistenceEntity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;

import static javax.persistence.FetchType.LAZY;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
@Table(name = "tik_user_subscriptions")
public class UserSubscription extends ObjectRoot implements PersistenceEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_tik_user_subscriptions")
    @SequenceGenerator(name = "seq_tik_user_subscriptions", sequenceName = "seq_tik_user_subscriptions", allocationSize = 1)
    @NotNull
    @EqualsAndHashCode.Include
    @Column(name = "user_subscription_id", updatable = false)
    private Integer userSubscriptionId;

    @NotNull
    @Column(name = "actual_date")
    private LocalDateTime actualDate;

    @NotNull
    @Column(name = "begin_date")
    private LocalDateTime beginDate;

    @Column(name = "end_date")
    private LocalDateTime endDate;

    @Column(name = "cancel_date")
    private LocalDateTime cancelDate;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "promocode_id", referencedColumnName = "promocode_id")
    private Promocode promocode;

    @ManyToOne(fetch = LAZY)
    @NotNull
    @JoinColumn(name = "user_subscription_status_id", referencedColumnName = "user_subscription_status_id")
    private UserSubscriptionStatus userSubscriptionStatus;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "currency_iso", referencedColumnName = "currency_iso")
    private Currency currency;

    @Column(name = "subscription_sum")
    private BigDecimal subscriptionSum;

    @ManyToOne(fetch = LAZY)
    @NotNull
    @JoinColumn(name = "user_id", referencedColumnName = "user_id")
    private User user;
}