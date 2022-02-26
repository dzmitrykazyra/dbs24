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

@Entity
@Table(name = "tm_order_execution_progresses")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderExecutionProgress {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_tm_order_execution_progress")
    @SequenceGenerator(name = "seq_tm_order_execution_progress", sequenceName = "seq_tm_order_execution_progress", allocationSize = 1)
    @Column(name = "order_execution_progress_id")
    private Integer orderExecutionProgressId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "order_id")
    private Order order;

    @Column(name = "done_actions_quantity")
    private Integer doneActionsQuantity;
}
