package org.dbs24.tik.assist.entity.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.dbs24.application.core.api.ObjectRoot;
import org.dbs24.persistence.api.PersistenceEntity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tik_order_execution_progresses")
public class OrderExecutionProgress extends ObjectRoot implements PersistenceEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_tik_order_action_execution_progresses")
    @SequenceGenerator(name = "seq_tik_order_action_execution_progresses", sequenceName = "seq_tik_order_action_execution_progresses", allocationSize = 1)
    @NotNull
    @Column(name = "order_execution_progress_id", updatable = false)
    private Integer orderExecutionProgressId;

    @NotNull
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "order_id", referencedColumnName = "order_id")
    private UserOrder userOrder;

    @Column(name = "done_actions_quantity")
    @NotNull
    private Integer doneActionsQuantity;
}
