package org.dbs24.tik.assist.entity.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.dbs24.application.core.api.ObjectRoot;
import org.dbs24.persistence.api.PersistenceEntity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

import static javax.persistence.FetchType.LAZY;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@IdClass(UserSubscriptionHistPK.class)
@Table(name = "tik_user_subscriptions_hist")
public class UserSubscriptionHist extends ObjectRoot implements PersistenceEntity {

    @Id
    @NotNull
    @Column(name = "user_subscription_id", updatable = false)
    private Integer userSubscriptionId;

    @Id
    @NotNull
    @Column(name = "actual_date")
    private LocalDateTime actualDate;

    @Column(name = "begin_date")
    private LocalDateTime beginDate;

    @Column(name = "end_date")
    private LocalDateTime endDate;

    @Column(name = "cancel_date")
    private LocalDateTime cancelDate;

    @Column(name = "promocode_id")
    private Integer promocodeId;

    @Column(name = "user_subscription_status_id")
    private Integer userSubscriptionStatusId;

    @Column(name = "currency_iso")
    private String currencyIso;

    @Column(name = "subscription_sum")
    private BigDecimal subscriptionSum;

    @ManyToOne(fetch = LAZY)
    @NotNull
    @JoinColumn(name = "user_id", referencedColumnName = "user_id")
    private User user;

    public static UserSubscriptionHist toUserSubscriptionHist(UserSubscription userSubscription) {

        return UserSubscriptionHist.builder()
                .userSubscriptionId(userSubscription.getUserSubscriptionId())
                .actualDate(userSubscription.getActualDate())
                .beginDate(userSubscription.getBeginDate())
                .endDate(userSubscription.getEndDate())
                .cancelDate(userSubscription.getCancelDate())
                .userSubscriptionStatusId(userSubscription.getUserSubscriptionStatus().getUserSubscriptionStatusId())
                .subscriptionSum(userSubscription.getSubscriptionSum())
                .user(userSubscription.getUser())
                .build();
    }
}

@Data
class UserSubscriptionHistPK implements Serializable {

    private Integer userSubscriptionId;
    private LocalDateTime actualDate;
}
