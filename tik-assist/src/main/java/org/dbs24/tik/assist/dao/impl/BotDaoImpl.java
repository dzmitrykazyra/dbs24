/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.tik.assist.dao.impl;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

import lombok.Data;
import lombok.extern.log4j.Log4j2;
import org.dbs24.spring.core.api.DaoAbstractApplicationService;

import org.dbs24.tik.assist.constant.reference.ActionTypeDefine;
import org.dbs24.tik.assist.constant.CacheKey;
import org.dbs24.tik.assist.dao.BotDao;
import org.dbs24.tik.assist.dao.ReferenceDao;
import org.dbs24.tik.assist.dao.TiktokAccountDao;
import org.dbs24.tik.assist.entity.domain.Bot;
import org.dbs24.tik.assist.entity.domain.BotHist;
import org.dbs24.tik.assist.entity.domain.TiktokAccount;
import org.dbs24.tik.assist.repo.BotHistRepo;
import org.dbs24.tik.assist.repo.BotRepo;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

@Data
@Log4j2
@Component
public class BotDaoImpl extends DaoAbstractApplicationService implements BotDao {

    final BotRepo botRepo;
    final BotHistRepo botHistRepo;

    final ReferenceDao referenceDao;
    final TiktokAccountDao tiktokAccountDao;

    public BotDaoImpl(BotRepo botRepo, BotHistRepo botHistRepo, ReferenceDao referenceDao, TiktokAccountDao tiktokAccountDao) {

        this.botRepo = botRepo;
        this.botHistRepo = botHistRepo;
        this.referenceDao = referenceDao;
        this.tiktokAccountDao = tiktokAccountDao;
    }

    @Override
    @Cacheable(CacheKey.CACHE_BOT)
    public Bot findBotById(Integer botId) {

        return botRepo
                .findById(botId)
                .orElseThrow(() -> new RuntimeException("Bot not found by id " + botId));
    }
    @Override
    @Cacheable(CacheKey.CACHE_BOT_OPTIONAL)
    public Optional<Bot> findBotOptionalById(Integer botId) {

        return botRepo.findById(botId);
    }

    @Override
    public void saveBotHistory(BotHist botHist) {

        botHistRepo.save(botHist);
    }

    @Override
    public void saveBotHistoryByBot(Bot bot) {

         saveBotHistory(BotHist.toBotHist(bot));
    }

    @Override
    @CacheEvict(value = {CacheKey.CACHE_BOT, CacheKey.CACHE_VALID_BOTS}, allEntries = true, beforeInvocation = true)
    public Bot saveBot(Bot bot) {

         return botRepo.save(bot);
    }

    @Override
    public List<Bot> findAvailableForFollowBots(TiktokAccount tiktokAccount, Integer botsQuantity) {

        return botRepo.findFollowersAvailableBotsByBotStatusAndUserIdAndAwemeIdLimit(
                referenceDao.findActiveBotStatus().getBotStatusId(),
                referenceDao.findActionTypeById(ActionTypeDefine.AT_GET_FOLLOWERS.getId()).getActionTypeId(),
                tiktokAccount.getAccountId(),
                botsQuantity
        );
    }

    @Override
    public List<Bot> findAvailableForLikeBots(TiktokAccount tiktokAccount, String awemeId, Integer botsQuantity) {

        return botRepo.findLikesAvailableBotsByBotStatusAndUserIdAndAwemeIdLimit(
                referenceDao.findActiveBotStatus().getBotStatusId(),
                referenceDao.findActionTypeById(ActionTypeDefine.AT_GET_LIKES.getId()).getActionTypeId(),
                tiktokAccount.getAccountId(),
                awemeId,
                botsQuantity
        );
    }

    @Override
    public List<Bot> findAvailableForRepostBots(Integer botsQuantity) {

        return findValidBotsByQuantity(botsQuantity);
    }

    @Override
    public List<Bot> findAvailableForViewBots(Integer botsQuantity) {

        return findValidBotsByQuantity(botsQuantity);
    }

    @Override
    public List<Bot> findAvailableForCommentBots(Integer botsQuantity) {

        return findValidBotsByQuantity(botsQuantity);
    }

    @Override
    @Cacheable(CacheKey.CACHE_VALID_BOTS)
    public List<Bot> findActiveBots() {

        return botRepo.findByBotStatus(referenceDao.findActiveBotStatus());
    }

    @Override
    public List<Bot> findValidBotsByQuantity(Integer requiredBotsQuantity) {

        return botRepo.findByBotStatus(
                referenceDao.findActiveBotStatus(),
                PageRequest.of(0, requiredBotsQuantity)
        ).getContent();
    }

    @Override
    public Collection<Integer> getUsedBotsByAwemeId(String awemeId) {

        return botRepo.getUsedBotsByAwemeId(awemeId);
    }

    @Override
    public Collection<Bot> findExpiredBots() {

        return botRepo.findByBotStatus(referenceDao.findExpiredBotStatus());
    }

    @Override
    public Integer findAvailableBotsAmountByAwemeId(String awemeId) {

        return botRepo.getNotUsedBotsCountByBotStatusIdAndAwemeIdAndActionTypeId(
                referenceDao.findActiveBotStatus().getBotStatusId(),
                awemeId,
                referenceDao.findActionTypeById(ActionTypeDefine.AT_GET_LIKES.getId()).getActionTypeId()
        );
    }

    @Override
    public Integer findAvailableBotsAmountBySecUserId(String secUserId) {
        AtomicReference<Integer> amount = new AtomicReference<>(0);

        tiktokAccountDao
                .findBySecUserId(secUserId)
                .forEach(
                        tiktokAccount ->
                                amount.updateAndGet(
                                        value -> value + botRepo.getNotUsedBotsCountByBotStatusIdAndAccountIdAndActionTypeId(
                                                referenceDao.findActiveBotStatus().getBotStatusId(),
                                                tiktokAccount.getAccountId(),
                                                referenceDao.findActionTypeById(ActionTypeDefine.AT_GET_FOLLOWERS.getId()).getActionTypeId()))
                );

        return amount.get();
    }    
}
