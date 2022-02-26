package org.dbs24.tik.assist.entity.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@IdClass(PlanHistPK.class)
@Table(name = "tik_user_plans_hist")
public class PlanHist {

    @Id
    @NotNull
    @Column(name = "plan_id", updatable = false)
    private Integer planId;

    @Id
    @NotNull
    @Column(name = "actual_date")
    private LocalDateTime actualDate;

    @Column(name = "user_id")
    private Integer userId;

    @Column(name = "account_id")
    private Integer tiktokAccountId;

    @Column(name = "plan_template_id")
    private Integer planTemplateId;

    @Column(name = "user_subscription_id")
    private Integer userSubscriptionId;

    @Column(name = "plan_status_id")
    private Integer planStatusId;

    @Column(name = "begin_date")
    private LocalDateTime beginDate;

    @Column(name = "end_date")
    private LocalDateTime endDate;

    @Column(name = "cancel_date")
    private LocalDateTime cancelDate;

    @Column(name = "currency_iso")
    private String currencyIso;

    @Column(name = "plan_sum")
    private BigDecimal planSum;

    @Column(name = "promocode_id")
    private Integer promocodeId;

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

    public static PlanHist toPlanHist(UserPlan userPlan) {

        return PlanHist.builder()
                .planId(userPlan.getPlanId())
                .actualDate(userPlan.getActualDate())
                .userId(userPlan.getUser().getUserId())
                .tiktokAccountId(userPlan.getTiktokAccount().getAccountId())
                .planStatusId(userPlan.getPlanStatus().getPlanStatusId())
                .beginDate(userPlan.getBeginDate())
                .endDate(userPlan.getEndDate())
                .cancelDate(userPlan.getCancelDate())
                .planSum(userPlan.getPlanSum())
                .viewsAmount(userPlan.getViewsAmount())
                .likesAmount(userPlan.getLikesAmount())
                .followersAmount(userPlan.getFollowersAmount())
                .commentsAmount(userPlan.getCommentsAmount())
                .repostsAmount(userPlan.getRepostsAmount())
                .build();
    }
}

@Data
class PlanHistPK implements Serializable {

    private Integer planId;
    private LocalDateTime actualDate;
}