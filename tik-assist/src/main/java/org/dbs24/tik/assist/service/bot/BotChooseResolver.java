package org.dbs24.tik.assist.service.bot;

import lombok.extern.log4j.Log4j2;
import org.dbs24.tik.assist.constant.reference.ActionTypeDefine;
import org.dbs24.tik.assist.dao.BotDao;
import org.dbs24.tik.assist.entity.domain.Bot;
import org.dbs24.tik.assist.entity.domain.UserOrder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

@Log4j2
@Service
public class BotChooseResolver {

    private final BotDao botDao;

    public BotChooseResolver(BotDao botDao) {

        this.botDao = botDao;
    }

    /**
     * Method allows choosing N available for required user order bots (N = user order actions to execute amount)
     */
    public List<Bot> getBotsAvailableForOrder(UserOrder userOrder) {

        return createActionToBotChooseMethodMap(
                userOrder,
                userOrder.getActionsAmount()
        ).get(userOrder.getActionType().getActionTypeId()).get();
    }

    /**
     * Method allows choosing single available for required user order bot
     */
    public Bot getBotAvailableForOrder(UserOrder userOrder) {

        return createActionToBotChooseMethodMap(
                userOrder,
                1
        ).get(userOrder.getActionType().getActionTypeId()).get().get(1);
    }

    /**
     * Method allows creating Map:
     *      key - action type id,
     *      value - supplier method to choose bot by action type.
     * Method can be used like substitute if-else chain/switch case pattern
     */
    private Map<Integer, Supplier<List<Bot>>> createActionToBotChooseMethodMap(UserOrder userOrder, Integer requiredBotsQuantity) {

        Map<Integer, Supplier<List<Bot>>> actionToBotChooseMethod = new HashMap<>();

        actionToBotChooseMethod.put(ActionTypeDefine.AT_GET_FOLLOWERS.getId(), () -> botDao.findAvailableForFollowBots(
                userOrder.getTiktokAccount(),
                requiredBotsQuantity
        ));
        actionToBotChooseMethod.put(ActionTypeDefine.AT_GET_LIKES.getId(), () -> botDao.findAvailableForLikeBots(
                userOrder.getTiktokAccount(),
                userOrder.getAwemeId(),
                requiredBotsQuantity
        ));
        actionToBotChooseMethod.put(ActionTypeDefine.AT_GET_REPOSTS.getId(), () -> botDao.findAvailableForRepostBots(
                requiredBotsQuantity
        ));
        actionToBotChooseMethod.put(ActionTypeDefine.AT_GET_VIEWS.getId(), () -> botDao.findAvailableForViewBots(
                requiredBotsQuantity
        ));
        actionToBotChooseMethod.put(ActionTypeDefine.AT_GET_COMMENTS.getId(), () -> botDao.findAvailableForCommentBots(
                requiredBotsQuantity
        ));

        return actionToBotChooseMethod;
    }
}
