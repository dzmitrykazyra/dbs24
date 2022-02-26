package org.dbs24.tik.mobile.entity.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(setterPrefix = "with")
@Entity
@IdClass(OrderHistPK.class)
@Table(name = "tm_user_orders_hist")
public class OrderHist {

    @Id
    @NotNull
    @Column(name = "order_id", updatable = false)
    private Integer orderId;

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

    @NotNull
    @Column(name = "start_date")
    private LocalDateTime startDate;

    @NotNull
    @Column(name = "end_date")
    private LocalDateTime endDate;

    @NotNull
    @Column(name = "actions_amount")
    private Integer actionsAmount;

    @NotNull
    @Column(name = "order_sum")
    private Integer orderSum;

    @NotNull
    @Column(name = "order_name")
    private String orderName;

    @NotNull
    @Column(name = "tiktok_uri")
    private String tiktokUri;

    @Column(name = "aweme_id")
    private String awemeId;

    @NotNull
    @Column(name = "user_id")
    private Integer userId;

    public static OrderHist of(Order order) {
        return OrderHist.builder()
                .withUserId(order.getUser().getId())
                .withActionsAmount(order.getActionsAmount())
                .withActionTypeId(order.getOrderActionType().getOrderActionTypeId())
                .withActualDate(order.getActualDate())
                .withOrderId(order.getOrderId())
                .withAwemeId(order.getAwemeId())
                .withOrderName(order.getOrderName())
                .withStartDate(order.getStartDate())
                .withEndDate(order.getEndDate())
                .withOrderStatusId(order.getOrderStatus().getOrderStatusId())
                .withOrderSum(order.getOrderSum())
                .withTiktokUri(order.getTiktokUri())
                .build();
    }

}

@Data
class OrderHistPK implements Serializable {

    private Integer orderId;
    private LocalDateTime actualDate;
}
