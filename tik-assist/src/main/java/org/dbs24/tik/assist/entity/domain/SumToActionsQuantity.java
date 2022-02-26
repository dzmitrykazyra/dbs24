package org.dbs24.tik.assist.entity.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

import java.math.BigDecimal;

import static javax.persistence.FetchType.EAGER;
import static javax.persistence.FetchType.LAZY;

/**
 * Proportions entity to calculate user subscription/plan sum depends on tiktok bounded with it actions quantity
 * (for example plan depends on new followers' quantity not direct proportionality)
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tik_sum_to_actions_quantity")
public class SumToActionsQuantity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_tik_discount_to_accounts_quantity")
    @SequenceGenerator(name = "seq_tik_discount_to_accounts_quantity", sequenceName = "seq_tik_discount_to_accounts_quantity", allocationSize = 1)
    @NotNull
    @Column(name = "sum_id", updatable = false)
    private Integer sumId;

    @ManyToOne(fetch = EAGER)
    @NotNull
    @JoinColumn(name = "action_type_id", referencedColumnName = "action_type_id")
    private ActionType actionType;

    @NotNull
    @Column(name = "up_to_action_quantity")
    private Integer upToActionQuantity;

    /** Single action cost */
    @NotNull
    @Column(name = "sum")
    private BigDecimal sum;
}
