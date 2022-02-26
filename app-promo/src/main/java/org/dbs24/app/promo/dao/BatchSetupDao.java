/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.app.promo.dao;

import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import org.dbs24.app.promo.entity.BatchSetup;
import org.dbs24.app.promo.entity.BatchSetupHist;
import org.dbs24.app.promo.repo.BatchSetupHistRepo;
import org.dbs24.app.promo.repo.BatchSetupRepo;
import org.dbs24.spring.core.api.DaoAbstractApplicationService;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Optional;

import static org.dbs24.app.promo.consts.AppPromoutionConsts.Caches.CACHE_BATCHES_SETUP;

@Getter
@Log4j2
@Component
public class BatchSetupDao extends DaoAbstractApplicationService {

    final BatchSetupRepo batchSetupRepo;
    final BatchSetupHistRepo batchSetupHistRepo;
    final BatchTemplateDao batchTemplateDao;

    public BatchSetupDao(BatchSetupRepo batchSetupRepo, BatchSetupHistRepo batchSetupHistRepo, BatchTemplateDao batchTemplateDao) {
        this.batchSetupRepo = batchSetupRepo;
        this.batchSetupHistRepo = batchSetupHistRepo;
        this.batchTemplateDao = batchTemplateDao;
    }

    //==========================================================================
    public Optional<BatchSetup> findOptionalBatchSetup(Integer batchSetupId) {
        return batchSetupRepo.findById(batchSetupId);
    }

    public BatchSetup findBatchSetup(Integer batchSetupId) {
        return findOptionalBatchSetup(batchSetupId).orElseThrow();
    }

    public void saveBatchSetupHist(BatchSetupHist batchSetupHist) {
        batchSetupHistRepo.save(batchSetupHist);
    }

    @CacheEvict(value = {CACHE_BATCHES_SETUP}, allEntries = true, beforeInvocation = true)
    public void saveBatchSetup(BatchSetup batchSetup) {
        batchSetupRepo.save(batchSetup);
    }

    @Cacheable(CACHE_BATCHES_SETUP)
    public Collection<BatchSetup> findByBatchTemplate(Integer batchTemplateId) {
        log.warn("load from db: templateId={}".toUpperCase(), batchTemplateId);
        return batchSetupRepo.findByBatchTemplate(batchTemplateDao.findBatchTemplate(batchTemplateId));
    }

}
