/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.app.promo.dao;

import lombok.Data;
import lombok.extern.log4j.Log4j2;
import org.dbs24.app.promo.entity.Bot;
import org.dbs24.app.promo.entity.BotHist;
import org.dbs24.app.promo.repo.BotHistRepo;
import org.dbs24.app.promo.repo.BotRepo;
import org.dbs24.spring.core.api.DaoAbstractApplicationService;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.util.Optional;

import static org.dbs24.app.promo.consts.AppPromoutionConsts.Caches.CACHE_BOT;

@Data
@Log4j2
@Component
public class BotDao extends DaoAbstractApplicationService {

    final BotRepo botRepo;
    final BotHistRepo botHistRepo;

    public BotDao(BotRepo botRepo, BotHistRepo botHistRepo) {
        this.botRepo = botRepo;
        this.botHistRepo = botHistRepo;
    }

    //==========================================================================
    public Optional<Bot> findOptionalBot(Integer botId) {
        return botRepo.findById(botId);
    }

    @Cacheable(CACHE_BOT)
    public Bot findBot(Integer botId) {
        return findOptionalBot(botId).orElseThrow();
    }

    public void saveBotHist(BotHist botHist) {
        botHistRepo.save(botHist);
    }

    @CacheEvict(value = {CACHE_BOT}, beforeInvocation = true, key = "#bot.botId", condition = "#bot.botId>0")
    public void saveBot(Bot bot) {
        botRepo.save(bot);
    }

}
