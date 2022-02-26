package org.dbs24.tik.assist.entity.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.dbs24.application.core.api.ObjectRoot;
import org.dbs24.persistence.api.PersistenceEntity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;

import static javax.persistence.FetchType.EAGER;
import static javax.persistence.FetchType.LAZY;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tik_user_plans")
public class UserPlan extends ObjectRoot implements PersistenceEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_tik_user_plans")
    @SequenceGenerator(name = "seq_tik_user_plans", sequenceName = "seq_tik_user_plans", allocationSize = 1)
    @NotNull
    @Column(name = "plan_id", updatable = false)
    private Integer planId;

    @ManyToOne(fetch = LAZY)
    @NotNull
    @JoinColumn(name = "user_id", referencedColumnName = "user_id")
    private User user;

    @ManyToOne(fetch = EAGER)
    @NotNull
    @JoinColumn(name = "account_id", referencedColumnName = "account_id")
    private TiktokAccount tiktokAccount;

    @ManyToOne(fetch = EAGER)
    @JoinColumn(name = "plan_template_id", referencedColumnName = "plan_template_id")
    private PlanTemplate planTemplate;

    @ManyToOne(fetch = EAGER)
    @JoinColumn(name = "user_subscription_id", referencedColumnName = "user_subscription_id")
    private UserSubscription userSubscription;

    @ManyToOne(fetch = LAZY)
    @NotNull
    @JoinColumn(name = "plan_status_id", referencedColumnName = "plan_status_id")
    private PlanStatus planStatus;

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
    @JoinColumn(name = "currency_iso", referencedColumnName = "currency_iso")
    private Currency currency;

    @Column(name = "plan_sum")
    private BigDecimal planSum;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "promocode_id", referencedColumnName = "promocode_id")
    private Promocode promocode;

    @Column(name = "views_amount")
    private Integer viewsAmount;

    @Column(name = "likes_amount")
    private Integer likesAmount;

    @Column(name = "followers_amount")
    private Integer followersAmount;

    @Column(name = "comments_amount")
    private Integer commentsAmount;

    @Column(name = "reposts_amount")
    private Integer repostsAmount;
}