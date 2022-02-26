package org.dbs24.component;

import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import org.dbs24.application.core.locale.NLS;
import org.dbs24.dao.ServicePeriodDao;
import org.dbs24.entity.ServicePeriod;
import org.dbs24.entity.ServicePeriodHist;
import org.dbs24.rest.api.service.AbstractRestApplicationService;
import org.dbs24.rest.dto.serviceperiod.CreateServicePeriodRequest;
import org.dbs24.rest.dto.serviceperiod.CreatedServicePeriod;
import org.dbs24.rest.dto.serviceperiod.CreatedServicePeriodResponse;
import org.dbs24.rest.dto.serviceperiod.ServicePeriodInfo;
import org.dbs24.rest.dto.serviceperiod.validator.ServicePeriodInfoValidator;
import org.dbs24.stmt.StmtProcessor;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.function.BiFunction;

import static org.dbs24.rest.api.consts.RestApiConst.RestOperCode.*;
import static org.springframework.core.Ordered.LOWEST_PRECEDENCE;

@Getter
@Log4j2
@Component
@Order(LOWEST_PRECEDENCE)
public class ServicePeriodsService extends AbstractRestApplicationService {

    final ServicePeriodDao servicePeriodDao;
    final ServicePeriodInfoValidator servicePeriodInfoValidator;

    public ServicePeriodsService(ServicePeriodDao servicePeriodDao, ServicePeriodInfoValidator servicePeriodInfoValidator) {
        this.servicePeriodDao = servicePeriodDao;
        this.servicePeriodInfoValidator = servicePeriodInfoValidator;
    }

    @FunctionalInterface
    interface ServicePeriodHistBuilder {
        void buildServicePeriodHist(ServicePeriod servicePeriod);
    }

    //==========================================================================
    final BiFunction<ServicePeriodInfo, ServicePeriod, ServicePeriod> assignDto = (servicePeriodInfo, servicePeriod) -> {

        servicePeriod.setNote(servicePeriodInfo.getNote());
        servicePeriod.setActualDate(LocalDateTime.now());
        servicePeriod.setServiceDateStart(NLS.long2LocalDateTime(servicePeriodInfo.getServiceDateStart()));
        servicePeriod.setServiceDateFinish(NLS.long2LocalDateTime(servicePeriodInfo.getServiceDateFinish()));

        return servicePeriod;
    };

    final BiFunction<ServicePeriodInfo, ServicePeriodsService.ServicePeriodHistBuilder, ServicePeriod> assignServicePeriodInfo = (servicePeriodInfo, servicePeriodsHistBuilder) -> {

        final ServicePeriod servicePeriod = findServicePeriod();

        // store history
        Optional.ofNullable(servicePeriod.getCreateDate()).ifPresent(borId -> servicePeriodsHistBuilder.buildServicePeriodHist(servicePeriod));

        assignDto.apply(servicePeriodInfo, servicePeriod);

        return servicePeriod;
    };

    //==================================================================================================================
    @Transactional
    public CreatedServicePeriodResponse createOrUpdateServicePeriod(Mono<CreateServicePeriodRequest> monoRequest) {

        return this.<CreatedServicePeriod, CreatedServicePeriodResponse>createAnswer(CreatedServicePeriodResponse.class,
                (responseBody, createdservicePeriod) -> processRequest(monoRequest, responseBody, request
                        -> responseBody.setErrors(servicePeriodInfoValidator.validateConditional(request.getEntityInfo(), servicePeriodInfo
                        -> {

                    log.debug("setup servicePeriod: {}", servicePeriodInfo);

                    final ServicePeriod servicePeriod = findOrCreateServicePeriod(servicePeriodInfo, batchSetupHist -> saveServicePeriodHist(createservicePeriodHist(batchSetupHist)));

                    final Boolean isNew = StmtProcessor.isNull(servicePeriod.getCreateDate());

                    final String finalMessage = String.format("servicePeriod is %s (%s -> %s)",
                            isNew ? "created" : "updated",
                            servicePeriod.getServiceDateStart(),
                            servicePeriod.getServiceDateFinish());

                    StmtProcessor.ifNull(servicePeriod.getCreateDate(), () -> servicePeriod.setCreateDate(LocalDateTime.now()));

                    servicePeriodDao.saveServicePeriod(servicePeriod);

                    createdservicePeriod.setServiceDateStart(NLS.localDateTime2long(servicePeriod.getServiceDateStart()));
                    createdservicePeriod.setServiceDateFinish(NLS.localDateTime2long(servicePeriod.getServiceDateFinish()));

                    responseBody.complete();

                    log.debug(finalMessage);

                    responseBody.setCode(OC_OK);
                    responseBody.setMessage(finalMessage);
                }, errorInfos -> {
                    responseBody.setCode(OC_INVALID_ENTITY_ATTRS);
                    responseBody.setMessage(errorInfos.stream().findFirst().orElseThrow().getErrorMsg().toUpperCase());
                }))));
    }

    @Transactional(readOnly = true)
    public CreatedServicePeriodResponse getServicePeriod() {

        return this.<CreatedServicePeriod, CreatedServicePeriodResponse>createAnswer(CreatedServicePeriodResponse.class,
                (responseBody, confirmedServicePeriod) -> processRequest(responseBody, () -> {

                    responseBody.setCode(OC_GENERAL_ERROR);

                    final ServicePeriod servicePeriod = findServicePeriod();

                    final String finalMessage = String.format("servicePeriod is retrieved (%s -> %s)",
                            servicePeriod.getServiceDateStart(),
                            servicePeriod.getServiceDateFinish());

                    confirmedServicePeriod.setServiceDateStart(NLS.localDateTime2long(servicePeriod.getServiceDateStart()));
                    confirmedServicePeriod.setServiceDateFinish(NLS.localDateTime2long(servicePeriod.getServiceDateFinish()));

                    responseBody.complete();
                    responseBody.setCode(OC_OK);
                    responseBody.setMessage(finalMessage);

                }));
    }

    //==================================================================================================================
    public ServicePeriod findServicePeriod() {
        return servicePeriodDao.getServicePeriod();
    }

    public ServicePeriod findOrCreateServicePeriod(ServicePeriodInfo servicePeriodInfo, ServicePeriodHistBuilder servicePeriodHistBuilder) {
        return assignServicePeriodInfo.apply(servicePeriodInfo, servicePeriodHistBuilder);
    }

    private ServicePeriodHist createservicePeriodHist(ServicePeriod servicePeriod) {
        return StmtProcessor.create(ServicePeriodHist.class, servicePeriodHist -> {

            servicePeriodHist.setServiceDateStart(servicePeriod.getServiceDateStart());
            servicePeriodHist.setServiceDateFinish(servicePeriod.getServiceDateFinish());
            servicePeriodHist.setActualDate(servicePeriod.getActualDate());
            servicePeriodHist.setNote(servicePeriod.getNote());

        });
    }

    private void saveServicePeriodHist(ServicePeriodHist servicePeriodHist) {
        getServicePeriodDao().saveServicePeriodHist(servicePeriodHist);
    }

}
