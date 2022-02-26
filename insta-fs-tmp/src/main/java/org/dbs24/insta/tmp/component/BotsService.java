/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.insta.tmp.component;

import java.time.LocalDateTime;
import java.util.Optional;
import lombok.Data;
import lombok.extern.log4j.Log4j2;
import org.dbs24.application.core.locale.NLS;
import org.dbs24.insta.tmp.entity.BotStatus;
import org.dbs24.insta.tmp.entity.Bot;
import org.dbs24.insta.tmp.entity.BotHist;
import org.dbs24.insta.tmp.repo.BotHistRepo;
import org.dbs24.insta.tmp.repo.BotRepo;
import org.dbs24.insta.tmp.rest.api.BotInfo;
import org.dbs24.insta.tmp.rest.api.CreatedBot;
import static org.dbs24.insta.tmp.consts.IfsConst.References.BotStatuses.BS_ACTUAL;
import org.dbs24.spring.core.api.AbstractApplicationService;
import org.dbs24.stmt.StmtProcessor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import java.util.Collection;
import org.dbs24.application.core.service.funcs.ServiceFuncs;

@Data
@Log4j2
@Component
@ConditionalOnProperty(name = "config.restfull.profile.name", havingValue = "insta-tmp")
public class BotsService extends AbstractApplicationService {

    final BotRepo botRepo;
    final BotHistRepo botHistRepo;
    final RefsService refsService;

    public BotsService(BotRepo botRepo, BotHistRepo botHistRepo, RefsService refsService) {
        this.botRepo = botRepo;
        this.botHistRepo = botHistRepo;
        this.refsService = refsService;
    }
    //==========================================================================

    @Transactional
    public CreatedBot createOrUpdateBot(BotInfo botInfo) {

        final Bot bot = findOrCreateBot(botInfo.getBotId());

        // copy 2 history
        saveBotHistory(bot);

        bot.setActualDate((LocalDateTime) StmtProcessor.nvl(NLS.long2LocalDateTime(botInfo.getActualDate()), LocalDateTime.now()));
        bot.setBotStatus(refsService.findBotStatus(botInfo.getBotStatusId()));
        bot.setUserName(botInfo.getUserName());
        bot.setEmail(botInfo.getEmail());
        bot.setPassword(botInfo.getPassword());
        bot.setSessionId(botInfo.getSessionId());
        bot.setUserAgent(botInfo.getUserAgent());
        bot.setUserName(botInfo.getUserName());

        saveBot(bot);

        return StmtProcessor.create(CreatedBot.class, ca -> {

            ca.setBotId(bot.getBotId());

            log.debug("try 2 create/update bot: {}", bot.getBotId());

        });
    }

    //==========================================================================
    public BotInfo getBot(Integer botId) {

        return StmtProcessor.create(BotInfo.class, bi -> {

            botRepo.findById(botId).ifPresent(bot -> bi.assign(bot));

            log.info("return botId {}: {}", botId, bi);
        });
    }

    //==========================================================================
    public Collection<BotInfo> getBotsList(Integer botsLimit, Integer botStatus) {

        return ServiceFuncs.createCollection(botsList -> {

            final BotStatus actualBotStatus = refsService.findBotStatus(botStatus);

            botRepo.findByBotStatus(actualBotStatus)
                    .stream()
                    .limit(botsLimit)
                    .forEach(bot -> botsList.add(StmtProcessor.create(BotInfo.class, bi -> bi.assign(bot))));

            log.debug("return actual bots: {}", botsList.size());

        });
    }

    //==========================================================================
    public Bot createBot() {
        return StmtProcessor.create(Bot.class, a -> {
            a.setActualDate(LocalDateTime.now());
        });
    }

    public Bot findBot(Integer botId) {

        return botRepo
                .findById(botId)
                .orElseThrow(() -> new RuntimeException(String.format("botId not found (%d)", botId)));
    }

    public Bot findOrCreateBot(Integer botId) {
        return (Optional.ofNullable(botId)
                .orElseGet(() -> 0) > 0)
                ? findBot(botId)
                : createBot();
    }

    public void saveBotHistory(BotHist botHist) {
        botHistRepo.save(botHist);
    }

    public void saveBotHistory(Bot bot) {
        Optional.ofNullable(bot.getBotId())
                .ifPresent(id -> saveBotHistory((StmtProcessor.create(BotHist.class, botHist -> botHist.assign(bot)))));
    }

    public void saveBot(Bot bot) {
        botRepo.save(bot);
    }

}
