package org.dbs24.tik.mobile.entity.domain;

import lombok.AllArgsConstructor;
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

@Entity
@Table(name = "tm_order_prices")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderPrice {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_tm_order_prices")
    @SequenceGenerator(name = "seq_tm_order_prices", sequenceName = "seq_tm_order_prices", allocationSize = 1)
    @Column(name = "cost_id")
    @NotNull
    private Integer costId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "action_type_id")
    @NotNull
    private OrderActionType orderActionType;

    @Column(name = "up_to_actions_quantity")
    @NotNull
    private Integer upToActionsQuantity;

    @Column(name = "sum")
    @NotNull
    private Integer sum;
}