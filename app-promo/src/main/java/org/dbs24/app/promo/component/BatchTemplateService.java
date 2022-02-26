/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.app.promo.component;

import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import org.dbs24.app.promo.dao.BatchTemplateDao;
import org.dbs24.app.promo.entity.BatchTemplate;
import org.dbs24.app.promo.entity.BatchTemplateHist;
import org.dbs24.app.promo.rest.dto.batchtemplate.BatchTemplateInfo;
import org.dbs24.app.promo.rest.dto.batchtemplate.CreateBatchTemplateRequest;
import org.dbs24.app.promo.rest.dto.batchtemplate.CreatedBatchTemplate;
import org.dbs24.app.promo.rest.dto.batchtemplate.CreatedBatchTemplateResponse;
import org.dbs24.app.promo.rest.dto.batchtemplate.validator.BatchTemplateInfoValidator;
import org.dbs24.application.core.locale.NLS;
import org.dbs24.rest.api.action.SimpleActionInfo;
import org.dbs24.rest.api.service.AbstractRestApplicationService;
import org.dbs24.stmt.StmtProcessor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Supplier;

import static org.dbs24.consts.SysConst.CURRENT_LOCALDATETIME;
import static org.dbs24.rest.api.consts.RestApiConst.RestOperCode.OC_INVALID_ENTITY_ATTRS;
import static org.dbs24.rest.api.consts.RestApiConst.RestOperCode.OC_OK;

@Getter
@Log4j2
@Component
@ConditionalOnProperty(name = "config.restfull.profile.name", havingValue = "app-promo")
public class BatchTemplateService extends AbstractRestApplicationService {

    final BatchTemplateDao batchTemplateDao;
    final RefsService refsService;
    final BatchTemplateInfoValidator batchTemplateInfoValidator;

    public BatchTemplateService(RefsService refsService, BatchTemplateDao batchTemplateDao, BatchTemplateInfoValidator batchTemplateInfoValidator) {
        this.refsService = refsService;
        this.batchTemplateDao = batchTemplateDao;
        this.batchTemplateInfoValidator = batchTemplateInfoValidator;
    }

    @FunctionalInterface
    interface BatchTemplatesHistBuilder {
        void buildBatchTemplatesHist(BatchTemplate batchTemplate);
    }

    final Supplier<BatchTemplate> createNewBatchTemplate = () -> StmtProcessor.create(BatchTemplate.class);


    final BiFunction<BatchTemplateInfo, BatchTemplate, BatchTemplate> assignDto = (batchTemplateInfo, batchTemplate) -> {

        batchTemplate.setTemplateName(batchTemplateInfo.getTemplateName());
        batchTemplate.setBatchType(getRefsService().findBatchType(batchTemplateInfo.getBatchTypeId()));
        batchTemplate.setActualDate(Optional.ofNullable(batchTemplateInfo.getActualDate())
                .map(NLS::long2LocalDateTime)
                .orElseGet(CURRENT_LOCALDATETIME));
        batchTemplate.setIsActual(batchTemplateInfo.getIsActual());
        batchTemplate.setProvider(getRefsService().findProvider(batchTemplateInfo.getProviderId()));

        return batchTemplate;
    };

    final BiFunction<BatchTemplateInfo, BatchTemplateService.BatchTemplatesHistBuilder, BatchTemplate> assignBatchTemplateInfo = (batchTemplateInfo, batchTemplatesHistBuilder) -> {

        final BatchTemplate batchTemplate = Optional.ofNullable(batchTemplateInfo.getBatchTemplateId())
                .map(getBatchTemplateDao()::findBatchTemplate)
                .orElseGet(createNewBatchTemplate);

        // store history
        Optional.ofNullable(batchTemplate.getBatchTemplateId()).ifPresent(borId -> batchTemplatesHistBuilder.buildBatchTemplatesHist(batchTemplate));

        assignDto.apply(batchTemplateInfo, batchTemplate);

        return batchTemplate;
    };

    //==========================================================================
    @Transactional
    public CreatedBatchTemplateResponse createOrUpdateBatchTemplate(Mono<CreateBatchTemplateRequest> monoRequest) {

        return this.<CreatedBatchTemplate, CreatedBatchTemplateResponse>createAnswer(CreatedBatchTemplateResponse.class,
                (responseBody, createdBatchTemplate) -> processRequest(monoRequest, responseBody, request
                        -> responseBody.setErrors(batchTemplateInfoValidator.validateConditional(request.getEntityInfo(), batchTemplateInfo
                        -> {

                    final SimpleActionInfo simpleActionInfo = request.getEntityAction();

                    //log.info("simpleActionInfo =  {}", simpleActionInfo);

                    log.debug("create/update batchTemplate: {}", batchTemplateInfo);

                    //StmtProcessor.assertNotNull(String.class, batchTemplateInfo.getPackageName(), "packageName name is not defined");

                    final BatchTemplate batchTemplate = findOrCreateBatchTemplate(batchTemplateInfo, batchTemplateHist -> saveBatchTemplateHist(createBatchTemplateHist(batchTemplateHist)));

                    final Boolean isNewSetting = StmtProcessor.isNull(batchTemplate.getBatchTemplateId());

                    getBatchTemplateDao().saveBatchTemplate(batchTemplate);

                    final String finalMessage = String.format("BatchTemplate is %s (BatchTemplateId=%d)",
                            isNewSetting ? "created" : "updated",
                            batchTemplate.getBatchTemplateId());

                    createdBatchTemplate.setCreatedBatchId(batchTemplate.getBatchTemplateId());
                    responseBody.complete();

                    log.debug(finalMessage);

                    responseBody.setCode(OC_OK);
                    responseBody.setMessage(finalMessage);
                }, errorInfos -> {
                    responseBody.setCode(OC_INVALID_ENTITY_ATTRS);
                    responseBody.setMessage(errorInfos.stream().findFirst().orElseThrow().getErrorMsg().toUpperCase());
                }))));
    }

    public BatchTemplate findOrCreateBatchTemplate(BatchTemplateInfo batchTemplateInfo, BatchTemplateService.BatchTemplatesHistBuilder batchTemplatesHistBuilder) {
        return assignBatchTemplateInfo.apply(batchTemplateInfo, batchTemplatesHistBuilder);
    }

    private BatchTemplateHist createBatchTemplateHist(BatchTemplate batchTemplate) {
        return StmtProcessor.create(BatchTemplateHist.class, batchTemplateHist -> {

            batchTemplateHist.setBatchTemplateId(batchTemplate.getBatchTemplateId());
            batchTemplateHist.setTemplateName(batchTemplate.getTemplateName());
            batchTemplateHist.setBatchType(batchTemplate.getBatchType());
            batchTemplateHist.setProvider(batchTemplate.getProvider());
            batchTemplateHist.setIsActual(batchTemplate.getIsActual());
            batchTemplateHist.setActualDate(batchTemplate.getActualDate());

        });
    }

    private void saveBatchTemplateHist(BatchTemplateHist batchTemplateHist) {
        getBatchTemplateDao().saveBatchTemplateHist(batchTemplateHist);
    }

    public BatchTemplate findBatchTemplate(Integer batchTemplateId) {
        return batchTemplateDao.findBatchTemplate(batchTemplateId);
    }
}
