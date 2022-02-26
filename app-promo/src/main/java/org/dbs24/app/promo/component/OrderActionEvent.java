package org.dbs24.app.promo.component;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.dbs24.app.promo.consts.AppPromoutionConsts.ActionEventEnum;
import org.dbs24.app.promo.entity.Order;
import org.dbs24.app.promo.entity.OrderAction;
import org.dbs24.stmt.StmtProcessor;

@Data
@NoArgsConstructor
public class OrderActionEvent {

    private ActionEventEnum actionEventEnum;
    private Order order;
    private OrderAction orderAction;

    public static OrderActionEvent create(ActionEventEnum actionEventEnum, Order order) {
        return StmtProcessor.create(OrderActionEvent.class, oae -> {
            oae.setActionEventEnum(actionEventEnum);
            oae.setOrder(order);
            oae.setOrderAction(null);
        });
    }

    public static OrderActionEvent create(ActionEventEnum actionEventEnum, OrderAction orderAction) {
        return StmtProcessor.create(OrderActionEvent.class, oae -> {
            oae.setActionEventEnum(actionEventEnum);
            oae.setOrder(null);
            oae.setOrderAction(orderAction);
        });
    }

}
