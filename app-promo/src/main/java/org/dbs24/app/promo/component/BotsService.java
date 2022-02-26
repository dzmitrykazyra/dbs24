/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.app.promo.component;

import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import org.dbs24.app.promo.dao.BotDao;
import org.dbs24.app.promo.dao.BotDetailDao;
import org.dbs24.app.promo.entity.Bot;
import org.dbs24.app.promo.entity.BotDetail;
import org.dbs24.app.promo.entity.BotHist;
import org.dbs24.app.promo.rest.dto.bot.BotInfo;
import org.dbs24.app.promo.rest.dto.bot.CreateBotRequest;
import org.dbs24.app.promo.rest.dto.bot.CreatedBot;
import org.dbs24.app.promo.rest.dto.bot.CreatedBotResponse;
import org.dbs24.app.promo.rest.dto.bot.validator.BotInfoValidator;
import org.dbs24.rest.api.action.SimpleActionInfo;
import org.dbs24.rest.api.service.AbstractRestApplicationService;
import org.dbs24.stmt.StmtProcessor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Supplier;

import static org.dbs24.rest.api.consts.RestApiConst.RestOperCode.OC_INVALID_ENTITY_ATTRS;
import static org.dbs24.rest.api.consts.RestApiConst.RestOperCode.OC_OK;

@Getter
@Log4j2
@Component
@ConditionalOnProperty(name = "config.restfull.profile.name", havingValue = "app-promo")
public class BotsService extends AbstractRestApplicationService {

    final BotDao botDao;
    final RefsService refsService;
    final BotInfoValidator botInfoValidator;
    private final BotDetailDao botDetailDao;

    public BotsService(RefsService refsService,
                       BotDao botDao,
                       BotInfoValidator botInfoValidator,
                       BotDetailDao botDetailDao) {

        this.refsService = refsService;
        this.botDao = botDao;
        this.botInfoValidator = botInfoValidator;
        this.botDetailDao = botDetailDao;
    }

    @FunctionalInterface
    interface BotsHistBuilder {
        void buildBotsHist(Bot bot);
    }

    final Supplier<Bot> createNewBot = () -> StmtProcessor.create(Bot.class);


    final BiFunction<BotInfo, Bot, Bot> assignDto = (botInfo, bot) -> {

        bot.setBotNote(botInfo.getBotNote());
        bot.setBotStatus(getRefsService().findBotStatus(botInfo.getBotStatusId()));
        bot.setLogin(botInfo.getLogin());
        bot.setPass(botInfo.getPass());
        bot.setSessionId(botInfo.getSessionId());
        bot.setProvider(getRefsService().findProvider(botInfo.getProviderId()));

        return bot;
    };

    final BiFunction<BotInfo, BotsService.BotsHistBuilder, Bot> assignBotsInfo = (botInfo, botsHistBuilder) -> {

        final Bot bot = Optional.ofNullable(botInfo.getBotId())
                .map(this::findBot)
                .orElseGet(createNewBot);

        // store history
        Optional.ofNullable(bot.getBotId()).ifPresent(borId -> botsHistBuilder.buildBotsHist(bot));

        assignDto.apply(botInfo, bot);

        return bot;
    };

    //==========================================================================
    @Transactional
    public CreatedBotResponse createOrUpdateBot(Mono<CreateBotRequest> monoRequest) {

        return this.<CreatedBot, CreatedBotResponse>createAnswer(CreatedBotResponse.class,
                (responseBody, createdBot) -> processRequest(monoRequest, responseBody, request
                        -> responseBody.setErrors(botInfoValidator.validateConditional(request.getEntityInfo(), botInfo
                        -> {

                    final SimpleActionInfo simpleActionInfo = request.getEntityAction();

                    //log.info("simpleActionInfo =  {}", simpleActionInfo);

                    log.debug("create/update bot: {}", botInfo);

                    //StmtProcessor.assertNotNull(String.class, botInfo.getPackageName(), "packageName name is not defined");

                    final Bot bot = findOrCreateBots(botInfo, botHist -> saveBotHist(createBotHist(botHist)));

                    final Boolean isNewSetting = StmtProcessor.isNull(bot.getBotId());

                    getBotDao().saveBot(bot);

                    final String finalMessage = String.format("Bot is %s (BotId=%d)",
                            isNewSetting ? "created" : "updated",
                            bot.getBotId());

                    createdBot.setCreatedBotId(bot.getBotId());
                    responseBody.complete();

                    log.debug(finalMessage);

                    responseBody.setCode(OC_OK);
                    responseBody.setMessage(finalMessage);
                }, errorInfos -> {
                    responseBody.setCode(OC_INVALID_ENTITY_ATTRS);
                    responseBody.setMessage(errorInfos.stream().findFirst().orElseThrow().getErrorMsg().toUpperCase());
                }))));
    }

    public Bot findOrCreateBots(BotInfo botInfo, BotsService.BotsHistBuilder botsHistBuilder) {
        return assignBotsInfo.apply(botInfo, botsHistBuilder);
    }

    private BotHist createBotHist(Bot bot) {
        return StmtProcessor.create(BotHist.class, botHist -> {
            botHist.setBotId(bot.getBotId());
            botHist.setActualDate(LocalDateTime.now());
            botHist.setBotStatus(bot.getBotStatus());
            botHist.setLogin(bot.getLogin());
            botHist.setPass(bot.getPass());
            botHist.setSessionId(bot.getSessionId());
            botHist.setBotNote(bot.getBotNote());
            botHist.setProvider(bot.getProvider());

        });
    }

    private void saveBotHist(BotHist botHist) {
        getBotDao().saveBotHist(botHist);
    }

    public BotDetail findDetailsByBotId(Integer botId) {
        return botDetailDao.findDetailsByBotId(botId);
    }

    public Bot findBot(Integer botId) {
        return getBotDao().findBot(botId);
    }

//    public Bot retrieveValidBot(String appPackage, ActionType actionType, String commentId) {

//    TODO:
//         1) Bot doesn't perform any task
//         2) Bot hasn't installed the appPackage before (actionType = install)
//         3) Bot hasn't rated comment with [id == commentId] before (actionType = rate comment)
//         4) Bot installed the appPackage before (actionType = send activity)
//         5) Bot status != BANNED

//      return botDao.getBotRepo().findValidBot();
//    }

}
