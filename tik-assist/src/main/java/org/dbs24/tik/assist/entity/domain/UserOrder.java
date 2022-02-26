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

import static javax.persistence.FetchType.EAGER;
import static javax.persistence.FetchType.LAZY;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
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
@Table(name = "tik_user_orders")
public class UserOrder extends ObjectRoot implements PersistenceEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_tik_orders")
    @SequenceGenerator(name = "seq_tik_orders", sequenceName = "seq_tik_orders", allocationSize = 1)
    @NotNull
    @Column(name = "order_id", updatable = false)
    private Integer orderId;

    @ManyToOne(fetch = EAGER)
    @JoinColumn(name = "plan_id", referencedColumnName = "plan_id")
    private UserPlan userPlan;

    @ManyToOne(fetch = LAZY)
    @NotNull
    @JoinColumn(name = "order_status_id", referencedColumnName = "order_status_id")
    private OrderStatus orderStatus;

    @ManyToOne(fetch = EAGER)
    @NotNull
    @JoinColumn(name = "action_type_id", referencedColumnName = "action_type_id")
    private ActionType actionType;

    @NotNull
    @Column(name = "actual_date")
    private LocalDateTime actualDate;

    @ManyToOne(fetch = LAZY)
    @NotNull
    @JoinColumn(name = "user_id", referencedColumnName = "user_id")
    private User user;

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

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "currency_iso", referencedColumnName = "currency_iso")
    private Currency currency;

    @Column(name = "order_sum")
    private BigDecimal orderSum;

    @Column(name = "tiktok_uri")
    private String tiktokUri;

    @Column(name = "aweme_id")
    private String awemeId;

    @Column(name = "cid")
    private String cid;

    @ManyToOne(fetch = EAGER)
    @JoinColumn(name = "account_id", referencedColumnName = "account_id")
    private TiktokAccount tiktokAccount;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "promocode_id", referencedColumnName = "promocode_id")
    private Promocode promocode;
}
