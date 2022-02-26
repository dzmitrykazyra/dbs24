/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.app.promo.component;

import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import org.dbs24.app.promo.dao.BatchSetupDao;
import org.dbs24.app.promo.entity.BatchSetup;
import org.dbs24.app.promo.entity.BatchSetupHist;
import org.dbs24.app.promo.entity.BatchTemplate;
import org.dbs24.app.promo.rest.dto.batchsetup.BatchSetupInfo;
import org.dbs24.app.promo.rest.dto.batchsetup.CreateBatchSetupRequest;
import org.dbs24.app.promo.rest.dto.batchsetup.CreatedBatchSetup;
import org.dbs24.app.promo.rest.dto.batchsetup.CreatedBatchSetupResponse;
import org.dbs24.app.promo.rest.dto.batchsetup.validator.BatchSetupInfoValidator;
import org.dbs24.application.core.locale.NLS;
import org.dbs24.rest.api.action.SimpleActionInfo;
import org.dbs24.rest.api.service.AbstractRestApplicationService;
import org.dbs24.stmt.StmtProcessor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import java.util.Collection;
import java.util.Comparator;
import java.util.function.BiFunction;
import java.util.function.Supplier;

import static java.util.Optional.ofNullable;
import static org.dbs24.consts.SysConst.CURRENT_LOCALDATETIME;
import static org.dbs24.rest.api.consts.RestApiConst.RestOperCode.OC_INVALID_ENTITY_ATTRS;
import static org.dbs24.rest.api.consts.RestApiConst.RestOperCode.OC_OK;

@Getter
@Log4j2
@Component
@ConditionalOnProperty(name = "config.restfull.profile.name", havingValue = "app-promo")
public class BatchSetupService extends AbstractRestApplicationService {

    final BatchSetupDao batchSetupDao;
    final RefsService refsService;
    final BatchTemplateService batchTemplateService;
    final BatchSetupInfoValidator batchSetupInfoValidator;

    static final Comparator<BatchSetup> latestBatchSetupExecOrder = (a, b) -> b.getExecutionOrder().compareTo(a.getExecutionOrder());

    public BatchSetupService(RefsService refsService, BatchSetupDao batchSetupDao, BatchSetupInfoValidator batchSetupInfoValidator, BatchTemplateService batchTemplateService) {
        this.refsService = refsService;
        this.batchSetupDao = batchSetupDao;
        this.batchSetupInfoValidator = batchSetupInfoValidator;
        this.batchTemplateService = batchTemplateService;
    }

    @FunctionalInterface
    interface BatchSetupsHistBuilder {
        void buildBatchSetupsHist(BatchSetup batchSetup);
    }

    final Supplier<BatchSetup> createNewBatchSetup = () -> StmtProcessor.create(BatchSetup.class);


    final BiFunction<BatchSetupInfo, BatchSetup, BatchSetup> assignDto = (batchSetupInfo, batchSetup) -> {

        batchSetup.setBatchNote(batchSetupInfo.getBatchNote());
        batchSetup.setBatchTemplate(getBatchTemplateService().findBatchTemplate(batchSetupInfo.getBatchTemplateId()));
        batchSetup.setActualDate(ofNullable(batchSetupInfo.getActualDate())
                .map(NLS::long2LocalDateTime)
                .orElseGet(CURRENT_LOCALDATETIME));
        batchSetup.setIsActual(batchSetupInfo.getIsActual());
        batchSetup.setMaxDelay(batchSetupInfo.getMaxDelay());
        batchSetup.setMinDelay(batchSetupInfo.getMinDelay());
        batchSetup.setExecutionOrder(batchSetupInfo.getExecutionOrder());
        batchSetup.setAction(getRefsService().findAction(batchSetupInfo.getActRefId()));
        return batchSetup;
    };

    final BiFunction<BatchSetupInfo, BatchSetupService.BatchSetupsHistBuilder, BatchSetup> assignBatchSetupInfo = (batchSetupInfo, batchSetupsHistBuilder) -> {

        final BatchSetup batchSetup = ofNullable(batchSetupInfo.getBatchSetupId())
                .map(getBatchSetupDao()::findBatchSetup)
                .orElseGet(createNewBatchSetup);

        // store history
        ofNullable(batchSetup.getBatchSetupId()).ifPresent(borId -> batchSetupsHistBuilder.buildBatchSetupsHist(batchSetup));

        assignDto.apply(batchSetupInfo, batchSetup);

        return batchSetup;
    };

    //==========================================================================
    @Transactional
    public CreatedBatchSetupResponse createOrUpdateBatchSetup(Mono<CreateBatchSetupRequest> monoRequest) {

        return this.<CreatedBatchSetup, CreatedBatchSetupResponse>createAnswer(CreatedBatchSetupResponse.class,
                (responseBody, createdBatchSetup) -> processRequest(monoRequest, responseBody, request
                        -> responseBody.setErrors(batchSetupInfoValidator.validateConditional(request.getEntityInfo(), batchSetupInfo
                        -> {

                    final SimpleActionInfo simpleActionInfo = request.getEntityAction();

                    //log.info("simpleActionInfo =  {}", simpleActionInfo);

                    log.debug("create/update batchSetup: {}", batchSetupInfo);

                    //StmtProcessor.assertNotNull(String.class, batchSetupInfo.getPackageName(), "packageName name is not defined");

                    final BatchSetup batchSetup = findOrCreateBatchSetup(batchSetupInfo, batchSetupHist -> saveBatchSetupHist(createBatchSetupHist(batchSetupHist)));

                    final Boolean isNewSetting = StmtProcessor.isNull(batchSetup.getBatchSetupId());

                    getBatchSetupDao().saveBatchSetup(batchSetup);

                    final String finalMessage = String.format("BatchSetup is %s (BatchSetupId=%d)",
                            isNewSetting ? "created" : "updated",
                            batchSetup.getBatchSetupId());

                    createdBatchSetup.setCreatedBatchId(batchSetup.getBatchSetupId());
                    responseBody.complete();

                    log.debug(finalMessage);

                    responseBody.setCode(OC_OK);
                    responseBody.setMessage(finalMessage);
                }, errorInfos -> {
                    responseBody.setCode(OC_INVALID_ENTITY_ATTRS);
                    responseBody.setMessage(errorInfos.stream().findFirst().orElseThrow().getErrorMsg().toUpperCase());
                }))));
    }

    public BatchSetup findOrCreateBatchSetup(BatchSetupInfo batchSetupInfo, BatchSetupService.BatchSetupsHistBuilder batchSetupsHistBuilder) {
        return assignBatchSetupInfo.apply(batchSetupInfo, batchSetupsHistBuilder);
    }

    private BatchSetupHist createBatchSetupHist(BatchSetup batchSetup) {
        return StmtProcessor.create(BatchSetupHist.class, batchSetupHist -> {

            batchSetupHist.setBatchSetupId(batchSetup.getBatchSetupId());
            batchSetupHist.setBatchNote(batchSetup.getBatchNote());
            batchSetupHist.setBatchTemplate(batchSetup.getBatchTemplate());
            batchSetupHist.setAction(batchSetup.getAction());
            batchSetupHist.setIsActual(batchSetup.getIsActual());
            batchSetupHist.setExecutionOrder(batchSetup.getExecutionOrder());
            batchSetupHist.setActualDate(batchSetup.getActualDate());
            batchSetupHist.setMinDelay(batchSetup.getMinDelay());
            batchSetupHist.setMaxDelay(batchSetup.getMaxDelay());

        });
    }

    private void saveBatchSetupHist(BatchSetupHist batchSetupHist) {
        getBatchSetupDao().saveBatchSetupHist(batchSetupHist);
    }

    public BatchSetup findBatchSetup(Integer batchSetupId) {
        return batchSetupDao.findBatchSetup(batchSetupId);
    }

    public Collection<BatchSetup> findByBatchTemplate(BatchTemplate batchTemplate) {
        return findByBatchTemplate(batchTemplate.getBatchTemplateId());
    }

    public Collection<BatchSetup> findByBatchTemplate(Integer batchTemplateId) {
        return getBatchSetupDao().findByBatchTemplate(batchTemplateId);
    }

}
