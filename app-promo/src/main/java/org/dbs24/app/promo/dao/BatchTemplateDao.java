/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.app.promo.dao;

import lombok.Data;
import lombok.extern.log4j.Log4j2;
import org.dbs24.app.promo.entity.BatchTemplate;
import org.dbs24.app.promo.entity.BatchTemplateHist;
import org.dbs24.app.promo.repo.BatchTemplateHistRepo;
import org.dbs24.app.promo.repo.BatchTemplateRepo;
import org.dbs24.spring.core.api.DaoAbstractApplicationService;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Data
@Log4j2
@Component
public class BatchTemplateDao extends DaoAbstractApplicationService {

    final BatchTemplateRepo batchTemplateRepo;
    final BatchTemplateHistRepo batchTemplateHistRepo;

    public BatchTemplateDao(BatchTemplateRepo batchTemplateRepo, BatchTemplateHistRepo batchTemplateHistRepo) {
        this.batchTemplateRepo = batchTemplateRepo;
        this.batchTemplateHistRepo = batchTemplateHistRepo;
    }

    //==========================================================================
    public Optional<BatchTemplate> findOptionalBatchTemplate(Integer batchTemplateId) {
        return batchTemplateRepo.findById(batchTemplateId);
    }

    public BatchTemplate findBatchTemplate(Integer batchTemplateId) {
        return findOptionalBatchTemplate(batchTemplateId).orElseThrow(() -> new RuntimeException(String.format("Unknown batchTemplateId (%d)", batchTemplateId)));
    }

    public void saveBatchTemplateHist(BatchTemplateHist batchTemplateHist) {
        batchTemplateHistRepo.save(batchTemplateHist);
    }

    public void saveBatchTemplate(BatchTemplate batchTemplate) {
        batchTemplateRepo.save(batchTemplate);
    }

}
