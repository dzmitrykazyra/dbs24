package org.dbs24.tik.assist.service.hierarchy;

import lombok.extern.log4j.Log4j2;
import org.dbs24.spring.core.api.AbstractApplicationService;
import org.dbs24.tik.assist.service.bot.BotChooseResolver;
import org.dbs24.tik.assist.dao.*;
import org.dbs24.tik.assist.entity.domain.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Log4j2
@Service
public class OrderActionService extends AbstractApplicationService {

    private final ReferenceDao referenceDao;
    private final OrderActionDao orderActionDao;

    private final BotChooseResolver botChooseResolver;

    public OrderActionService(OrderActionDao orderActionDao, ReferenceDao referenceDao, BotChooseResolver botChooseResolver) {

        this.orderActionDao = orderActionDao;
        this.referenceDao = referenceDao;
        this.botChooseResolver = botChooseResolver;
    }

    public List<OrderAction> createOrderActions(List<OrderAction> orderActions) {

        return orderActionDao.saveOrderActions(orderActions);
    }

    /**
     * Method allows set available bots to null-value order action fields
     * and change order action status to be ready for execution
     */
    public void setValidBotsToWaitingForBotTasks(List<OrderAction> waitingForBotOrderActions) {

        waitingForBotOrderActions
                .forEach(waitingOrderAction -> {
                    Bot availableBot = botChooseResolver.getBotAvailableForOrder(waitingOrderAction.getUserOrder());

                    if (availableBot != null ) {
                        waitingOrderAction.setBot(availableBot);
                        waitingOrderAction.setOrderActionResult(referenceDao.findCreatedOrderActionResult());

                        orderActionDao.saveOrderAction(waitingOrderAction);
                    }
                });
    }
}
