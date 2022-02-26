package org.dbs24.tik.assist.entity.domain;

import lombok.*;
import org.dbs24.application.core.api.ObjectRoot;
import org.dbs24.persistence.api.PersistenceEntity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 * Proportions entity to calculate user discount depends on tiktok accounts quantity bounded with subscription/plan
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
@Table(name = "tik_discount_to_accounts_quantity")
public class DiscountToAccountsQuantity extends ObjectRoot implements PersistenceEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_tik_discount_to_subscriptions_quantity")
    @SequenceGenerator(name = "seq_tik_discount_to_subscriptions_quantity", sequenceName = "seq_tik_discount_to_subscriptions_quantity", allocationSize = 1)
    @NotNull
    @EqualsAndHashCode.Include
    @Column(name = "discount_id", updatable = false)
    private Integer discountId;

    @NotNull
    @Column(name = "up_to_accounts_quantity")
    private Integer upToAccountsQuantity;

    @NotNull
    @Column(name = "discount")
    private Integer discount;
}
