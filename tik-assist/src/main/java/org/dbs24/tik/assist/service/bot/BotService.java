/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.tik.assist.service.bot;

import java.time.LocalDateTime;
import java.util.Collection;

import lombok.extern.log4j.Log4j2;
import org.dbs24.spring.core.api.AbstractApplicationService;
import org.dbs24.stmt.StmtProcessor;
import org.dbs24.tik.assist.dao.BotDao;
import org.dbs24.tik.assist.dao.ReferenceDao;
import org.dbs24.tik.assist.entity.domain.Bot;
import org.dbs24.tik.assist.entity.domain.BotStatus;
import org.dbs24.tik.assist.entity.dto.bot.BotDto;
import org.dbs24.tik.assist.entity.dto.bot.BotIdList;
import org.dbs24.tik.assist.entity.dto.bot.response.AvailableBotsByAwemeIdResponse;
import org.dbs24.tik.assist.entity.dto.bot.response.AvailableBotsBySecUserIdResponse;
import org.dbs24.tik.assist.entity.dto.bot.CreatedBotDto;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.BodyInserter;
import reactor.core.publisher.Mono;

@Log4j2
@Service
public class BotService {

    final ReferenceDao referenceDao;
    final BotDao botDao;

    public BotService(ReferenceDao referenceDao, BotDao botDao) {

        this.referenceDao = referenceDao;
        this.botDao = botDao;
    }

    @Transactional
    public Mono<CreatedBotDto> createOrUpdateBot(Mono<BotDto> botDtoMono) {

        return botDtoMono.map(
                botDto -> {
                    final CreatedBotDto createdBotDto = new CreatedBotDto();

                    Bot botToSave = BotDto.defaultBotFromDto(botDto);

                    botToSave.setBotRegistrationType(referenceDao.findBotRegistrationTypeById(botDto.getBotRegistrationTypeId()));
                    botToSave.setBotStatus(referenceDao.findBotStatusById(botDto.getBotStatusId()));

                    if (botDto.getBotId() != null) {

                        Bot bot = botDao.findBotById(botDto.getBotId());
                        botDao.saveBotHistoryByBot(bot);
                    } else {
                        botToSave.setCreateDate(LocalDateTime.now());
                    }

                    createdBotDto.setCreatedBotId(botDao.saveBot(botToSave).getBotId());

                    return createdBotDto;
                });
    }

    @Transactional(readOnly = true)
    public Mono<BotDto> getBotById(Integer botId) {

        return Mono.just(BotDto.toBotDto(botDao.findBotById(botId)));
    }

    @Transactional(readOnly = true)
    public Mono<AvailableBotsByAwemeIdResponse> getAvailableBotsAmountByAwemeId(String awemeId) {

        return Mono.just(
                AvailableBotsByAwemeIdResponse
                        .toDto(awemeId, botDao.findAvailableBotsAmountByAwemeId(awemeId))
        );
    }

    @Transactional(readOnly = true)
    public Mono<AvailableBotsBySecUserIdResponse> getAvailableBotsAmountBySecUserId(String secUserId) {

        return Mono.just(
                AvailableBotsBySecUserIdResponse
                        .toDto(secUserId, botDao.findAvailableBotsAmountBySecUserId(secUserId))
        );
    }

    @Transactional(readOnly = true)
    public Collection<Bot> findExpiredBots() {

        return botDao.findExpiredBots();
    }

    @Transactional
    public void updateBotStatus(Integer botId, BotStatus botStatus) {

        final Bot bot = botDao.findBotById(botId);

        botDao.saveBotHistoryByBot(bot);

        bot.setBotStatus(botStatus);
        bot.setActualDate(LocalDateTime.now());

        botDao.saveBot(bot);
    }

    public Mono<BotIdList> getActiveBotsIds() {

        return Mono.just(BotIdList.of(botDao.findActiveBots()));
    }
}
