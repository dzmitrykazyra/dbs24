package org.dbs24.tik.assist.entity.dto.subscription;

import lombok.Data;
import org.dbs24.stmt.StmtProcessor;

import java.math.BigDecimal;

@Data
public class SubscriptionCostDto {

    private BigDecimal subscriptionCost;

    public static SubscriptionCostDto of(BigDecimal cost) {

        return StmtProcessor.create(
                SubscriptionCostDto.class,
                subscriptionCostDto -> subscriptionCostDto.setSubscriptionCost(cost)
        );
    }
}
