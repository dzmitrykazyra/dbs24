/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.tik.assist.entity.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import javax.persistence.*;

import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.dbs24.application.core.api.ObjectRoot;
import org.dbs24.persistence.api.PersistenceEntity;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@IdClass(UserOrderHistPK.class)
@Table(name = "tik_user_orders_hist")
public class UserOrderHist extends ObjectRoot implements PersistenceEntity {

    @Id
    @NotNull
    @Column(name = "order_id", updatable = false)
    private Integer orderId;

    @Column(name = "plan_id")
    private Integer planId;

    @NotNull
    @Column(name = "order_status_id")
    private Integer orderStatusId;

    @NotNull
    @Column(name = "action_type_id")
    private Integer actionTypeId;

    @Id
    @NotNull
    @Column(name = "actual_date")
    private LocalDateTime actualDate;

    @Column(name = "user_id")
    private Integer userId;

    @NotNull
    @Column(name = "begin_date")
    private LocalDateTime beginDate;

    @Column(name = "end_date")
    private LocalDateTime endDate;

    @Column(name = "cancel_date")
    private LocalDateTime cancelDate;

    @NotNull
    @Column(name = "actions_amount")
    private Integer actionsAmount;

    @Column(name = "currency_iso")
    private String currencyIso;

    @NotNull
    @Column(name = "order_sum")
    private BigDecimal orderSum;

    @Column(name = "tiktok_uri")
    private String tiktokUri;

    @Column(name = "aweme_id")
    private String awemeId;

    @Column(name = "cid")
    private String cid;

    @Column(name = "promocode_id")
    private Integer promocodeId;

    @Column(name = "account_id")
    private Integer accountId;

    public static UserOrderHist toUserOrderHist(UserOrder userOrder) {

        UserOrderHist userOrderHist = UserOrderHist.builder()
                .orderId(userOrder.getOrderId())
                .orderStatusId(userOrder.getOrderStatus().getOrderStatusId())
                .actionTypeId(userOrder.getActionType().getActionTypeId())
                .actualDate(userOrder.getActualDate())
                .userId(userOrder.getUser().getUserId())
                .beginDate(userOrder.getBeginDate())
                .endDate(userOrder.getEndDate())
                .cancelDate(userOrder.getCancelDate())
                .actionsAmount(userOrder.getActionsAmount())
                .orderSum(userOrder.getOrderSum())
                .tiktokUri(userOrder.getTiktokUri())
                .awemeId(userOrder.getAwemeId())
                .cid(userOrder.getCid())
                .accountId(userOrder.getTiktokAccount().getAccountId())
                .build();

        if (userOrder.getUserPlan() != null) {
            userOrderHist.setPlanId(userOrder.getUserPlan().getPlanId());
        }
        if (userOrder.getPromocode() != null) {
            userOrderHist.setPromocodeId(userOrder.getPromocode().getPromocodeId());
        }

        return userOrderHist;
    }
}

@Data
class UserOrderHistPK implements Serializable {

    private Integer orderId;
    private LocalDateTime actualDate;
}
