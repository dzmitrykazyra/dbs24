package org.dbs24.tik.mobile.entity.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "tm_user_orders")
@Data
@Builder(setterPrefix = "with")
@AllArgsConstructor
@NoArgsConstructor
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_tm_user_orders")
    @SequenceGenerator(name = "seq_tm_user_orders", sequenceName = "seq_tm_user_orders", allocationSize = 1)
    @Column(name = "order_id")
    private Integer orderId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_status_id")
    private OrderStatus orderStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "action_type_id")
    private OrderActionType orderActionType;

    @Column(name = "actual_date")
    private LocalDateTime actualDate;

    @Column(name = "start_date")
    private LocalDateTime startDate;

    @Column(name = "end_date")
    private LocalDateTime endDate;

    @Column(name = "actions_amount")
    private Integer actionsAmount;

    @Column(name = "order_sum")
    private Integer orderSum;

    @Column(name = "order_name")
    private String orderName;

    @Column(name = "tiktok_uri")
    private String tiktokUri;

    @Column(name = "aweme_id")
    private String awemeId;

    @Column(name = "order_cover")
    private String cover;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;
}
