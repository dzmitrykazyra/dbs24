package org.dbs24.tik.assist.service.scheduled.resolver;

import lombok.extern.log4j.Log4j2;
import org.dbs24.tik.assist.constant.reference.OrderActionResultDefine;
import org.dbs24.tik.assist.dao.ReferenceDao;
import org.dbs24.tik.assist.dao.UserOrderDao;
import org.dbs24.tik.assist.entity.domain.OrderAction;
import org.dbs24.tik.assist.entity.domain.UserOrder;
import org.dbs24.tik.assist.entity.dto.tiktok.TiktokPostIdentifierDto;
import org.dbs24.tik.assist.service.bot.BotChooseResolver;
import org.dbs24.tik.assist.service.tiktok.resolver.TiktokInteractor;
import org.springframework.stereotype.Component;

/**
 * Service class with methods to resolve OrderAction errors
 * (create new OrderAction objects to re-execute tasks)
 */
@Log4j2
@Component
public class OrderActionSelector {

    private final BotChooseResolver botChooseResolver;
    private final TiktokInteractor tiktokInteractor;

    private final ReferenceDao referenceDao;
    private final UserOrderDao userOrderDao;

    public OrderActionSelector(BotChooseResolver botChooseResolver, TiktokInteractor tiktokInteractor, ReferenceDao referenceDao, UserOrderDao userOrderDao) {

        this.botChooseResolver = botChooseResolver;
        this.tiktokInteractor = tiktokInteractor;
        this.referenceDao = referenceDao;
        this.userOrderDao = userOrderDao;
    }
    /**
     * Method allows creating new OrderAction objects based on elder OrderAction objects
     * by occurred during elder OrderAction error
     */
    public OrderAction reCreateOrderAction(OrderAction oldOrderAction) {

        OrderAction newOrderAction = OrderAction.builder()
                .userOrder(oldOrderAction.getUserOrder())
                .orderActionResult(referenceDao.findCreatedOrderActionResult())
                .bot(oldOrderAction.getBot())
                .build();

        if (OrderActionResultDefine.AR_WARN.getId()
                 .equals(oldOrderAction.getOrderActionId())) {

            UserOrder userOrder = oldOrderAction.getUserOrder();
            TiktokPostIdentifierDto lastTiktokPost = tiktokInteractor
                    .searchLatestUserPosts(1, userOrder.getTiktokAccount())
                    .stream()
                    .findAny()
                    .orElseThrow(() -> new RuntimeException("Cannot find any user post for account with id ".concat(String.valueOf(userOrder.getTiktokAccount().getAccountId()))));
            userOrder.setAwemeId(lastTiktokPost.getAwemeId());
            userOrder.setTiktokUri(lastTiktokPost.getTiktokUri());

            userOrderDao.updateOrder(userOrder);
        } else if (OrderActionResultDefine.AR_BOT_IS_EXPIRED.getId()
                 .equals(oldOrderAction.getOrderActionId())) {

            newOrderAction.setBot(botChooseResolver.getBotAvailableForOrder(oldOrderAction.getUserOrder()));
        }

        return newOrderAction;
    }
}
