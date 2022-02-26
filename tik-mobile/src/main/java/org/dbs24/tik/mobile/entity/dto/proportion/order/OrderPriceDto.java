package org.dbs24.tik.mobile.entity.dto.proportion.order;

import lombok.Data;
import org.dbs24.stmt.StmtProcessor;
import org.dbs24.tik.mobile.entity.domain.OrderPrice;

@Data
public class OrderPriceDto {

    private Integer actionTypeId;
    private Integer upToActionsQuantity;
    private Integer heartsSum;

    public static OrderPriceDto of(OrderPrice orderPrice) {

        return StmtProcessor.create(
                OrderPriceDto.class,
                orderCostDto -> {
                    orderCostDto.setActionTypeId(orderPrice.getOrderActionType().getOrderActionTypeId());
                    orderCostDto.setUpToActionsQuantity(orderPrice.getUpToActionsQuantity());
                    orderCostDto.setHeartsSum(orderPrice.getSum());
                }
        );
    }

    public OrderPrice toDefault() {

        return StmtProcessor.create(
                OrderPrice.class,
                costToQuantity -> {
                    costToQuantity.setUpToActionsQuantity(this.upToActionsQuantity);
                    costToQuantity.setSum(this.heartsSum);
                }
        );
    }
}
