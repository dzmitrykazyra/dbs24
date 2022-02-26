/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.app.promo.component;

import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import org.dbs24.app.promo.consts.AppPromoutionConsts.OrderStatusEnum;
import org.dbs24.app.promo.entity.*;
import org.dbs24.app.promo.repo.*;
import org.dbs24.google.api.OrderActionsConsts.ActionResultEnum;
import org.dbs24.spring.core.api.AbstractApplicationService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import static org.dbs24.app.promo.consts.AppPromoutionConsts.ACTIONS_LIST;
import static org.dbs24.app.promo.consts.AppPromoutionConsts.ACTIONS_RESULTS_LIST;
import static org.dbs24.app.promo.consts.AppPromoutionConsts.BatchTypeEnum.BATCH_TYPES_LIST;
import static org.dbs24.app.promo.consts.AppPromoutionConsts.BotStatusEnum.BOT_STATUSES_LIST;
import static org.dbs24.app.promo.consts.AppPromoutionConsts.Caches.*;
import static org.dbs24.app.promo.consts.AppPromoutionConsts.OrderStatusEnum.ORDER_STATUSES_LIST;
import static org.dbs24.app.promo.consts.AppPromoutionConsts.ProviderEnum.PROVIDERS_LIST;
import static org.springframework.core.Ordered.HIGHEST_PRECEDENCE;


@Getter
@Log4j2
@Component
@Order(HIGHEST_PRECEDENCE)
@EnableConfigurationProperties
@ConfigurationProperties(prefix = "refs")
@ConditionalOnProperty(name = "config.restfull.profile.name", havingValue = "app-promo")
public class RefsService extends AbstractApplicationService {

    final ProviderRepo providerRepo;
    final ActionRepo actionRepo;
    final ActionResultRepo actionResultRepo;
    final BotStatusRepo botStatusRepo;
    final OrderStatusRepo orderStatusRepo;
    final BatchTypeRepo batchTypeRepo;

    public RefsService(ProviderRepo providerRepo, ActionRepo actionRepo, ActionResultRepo actionResultRepo, BotStatusRepo botStatusRepo, OrderStatusRepo orderStatusRepo, BatchTypeRepo batchTypeRepo) {
        this.providerRepo = providerRepo;
        this.actionRepo = actionRepo;
        this.actionResultRepo = actionResultRepo;
        this.botStatusRepo = botStatusRepo;
        this.orderStatusRepo = orderStatusRepo;
        this.batchTypeRepo = batchTypeRepo;
    }

    //==========================================================================
    @Transactional
    public void synchronizeRefs() {

        log.info("synchronize system references");

        providerRepo.saveAllAndFlush(PROVIDERS_LIST);
        actionRepo.saveAllAndFlush(ACTIONS_LIST);
        actionResultRepo.saveAllAndFlush(ACTIONS_RESULTS_LIST);
        botStatusRepo.saveAllAndFlush(BOT_STATUSES_LIST);
        orderStatusRepo.saveAllAndFlush(ORDER_STATUSES_LIST);
        batchTypeRepo.saveAllAndFlush(BATCH_TYPES_LIST);

    }

    @Cacheable(CACHE_BOT_STATUS)
    public BotStatus findBotStatus(Integer botStatus) {

        return botStatusRepo.findById(botStatus).orElseThrow();
    }

    @Cacheable(CACHE_APP_PROVIDER)
    public Provider findProvider(Integer providerId) {

        return providerRepo.findById(providerId).orElseThrow();
    }

    @Cacheable(CACHE_BATCH_TYPE)
    public BatchType findBatchType(Integer batchTypeId) {

        return batchTypeRepo.findById(batchTypeId).orElseThrow();
    }

    @Cacheable(CACHE_ACTION)
    public Action findAction(Integer actionId) {

        return actionRepo.findById(actionId).orElseThrow();
    }

    @Cacheable(CACHE_ORDER_STATUS)
    public OrderStatus findOrderStatus(Integer orderStatusId) {

        return orderStatusRepo.findById(orderStatusId).orElseThrow();
    }

    public OrderStatus findOrderStatus(OrderStatusEnum orderStatusEnum) {

        return findOrderStatus(orderStatusEnum.getCode());
    }

    @Cacheable(CACHE_ACTION_RESULT)
    public ActionResult findActionResult(Integer actionResult) {

        return actionResultRepo.findById(actionResult).orElseThrow();
    }

    @Cacheable(CACHE_ACTION_RESULT_ENUM)
    public ActionResult findActionResult(ActionResultEnum actionResultEnum) {

        return actionResultRepo.findById(actionResultEnum.getCode()).orElseThrow();
    }

}
