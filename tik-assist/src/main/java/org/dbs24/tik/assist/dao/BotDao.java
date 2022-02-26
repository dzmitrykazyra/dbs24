package org.dbs24.tik.assist.dao;

import org.dbs24.tik.assist.entity.domain.Bot;
import org.dbs24.tik.assist.entity.domain.BotHist;
import org.dbs24.tik.assist.entity.domain.TiktokAccount;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface BotDao {

    Bot findBotById(Integer botId);
    Optional<Bot> findBotOptionalById(Integer botId);

    Bot saveBot(Bot bot);
    void saveBotHistory(BotHist botHist);
    void saveBotHistoryByBot(Bot bot);

    List<Bot> findAvailableForFollowBots(TiktokAccount tiktokAccount, Integer botsQuantity);
    List<Bot> findAvailableForLikeBots(TiktokAccount tiktokAccount, String awemeId, Integer botsQuantity);
    List<Bot> findAvailableForRepostBots(Integer botsQuantity);
    List<Bot> findAvailableForViewBots(Integer botsQuantity);
    List<Bot> findAvailableForCommentBots(Integer botsQuantity);

    List<Bot> findActiveBots();
    Collection<Integer> getUsedBotsByAwemeId(String awemeId);
    Collection<Bot> findExpiredBots();
    List<Bot> findValidBotsByQuantity(Integer requiredBotsQuantity);

    Integer findAvailableBotsAmountByAwemeId(String awemeId);
    Integer findAvailableBotsAmountBySecUserId(String secUserId);
}
