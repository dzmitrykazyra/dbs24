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
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
@Table(name = "tm_order_actions")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(setterPrefix = "with")
public class OrderAction {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_tm_order_actions")
    @SequenceGenerator(name = "seq_tm_order_actions", sequenceName = "seq_tm_order_actions", allocationSize = 1)
    @Column(name = "order_action_id")
    @NotNull
    private Integer orderActionId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "order_id")
    private Order order;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "start_date")
    private LocalDateTime startDate;

    @Column(name = "finish_date")
    private LocalDateTime finishDate;

    @Column(name = "metrics_amount_before")
    private Integer metricsAmountBefore;

    @Column(name = "metrics_amount_after")
    private Integer metricsAmountAfter;

    @Column(name = "is_success")
    private boolean isSuccess;
}
