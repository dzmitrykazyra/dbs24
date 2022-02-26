/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.proxy.core.component;

import lombok.Data;
import lombok.extern.log4j.Log4j2;
import org.dbs24.application.core.service.funcs.ServiceFuncs;
import org.dbs24.proxy.core.dao.ApplicationDao;
import org.dbs24.proxy.core.dao.ProxyUsageDao;
import org.dbs24.proxy.core.dao.RequestDao;
import org.dbs24.proxy.core.entity.domain.Application;
import org.dbs24.proxy.core.entity.domain.ProxyRequest;
import org.dbs24.proxy.core.entity.dto.request.FinalizeRequestByApplicationId;
import org.dbs24.proxy.core.entity.dto.request.FinalizeRequestById;
import org.dbs24.proxy.core.entity.dto.response.FinalizeUsagesResponse;
import org.dbs24.proxy.core.entity.dto.response.body.FinalizedRequestDto;
import org.dbs24.proxy.core.validator.ProxyRequestInfoValidator;
import org.dbs24.rest.api.consts.RestApiConst;
import org.dbs24.rest.api.service.AbstractRestApplicationService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

@Data
@Log4j2
@Component
@ConditionalOnProperty(name = "config.restfull.profile.name", havingValue = "proxy-core")
public class RequestService extends AbstractRestApplicationService {

    final ReferenceService referenceService;
    final ProxyService proxyService;

    final RequestDao requestDao;
    final ProxyUsageDao proxyUsageDao;
    final ApplicationDao applicationDao;

    final ProxyRequestInfoValidator proxyRequestInfoValidator;

    final Map<Integer, LocalDateTime> lastUsedProxies = ServiceFuncs.createConcurencyMap();

    public RequestService(ReferenceService referenceService, ProxyService proxyService, RequestDao requestDao, ProxyRequestInfoValidator proxyRequestInfoValidator, ProxyUsageDao proxyUsageDao, ApplicationDao applicationDao) {
        this.referenceService = referenceService;
        this.proxyService = proxyService;
        this.requestDao = requestDao;
        this.proxyRequestInfoValidator = proxyRequestInfoValidator;
        this.proxyUsageDao = proxyUsageDao;
        this.applicationDao = applicationDao;
    }

    public FinalizeUsagesResponse finalizeByRequestId(Mono<FinalizeRequestById> monoRequest) {

        return this.<FinalizedRequestDto, FinalizeUsagesResponse>createAnswer(
                FinalizeUsagesResponse.class,
                (responseBody, finalizedRequestDto) -> processRequest(
                        monoRequest,
                        responseBody,
                        request -> {
                            final Integer requestId = request.getRequestId();

                            ProxyRequest proxyRequestFound = requestDao.findProxyRequestById(requestId);
                            requestDao.setCompleteSessionAndSave(proxyRequestFound);

                            finalizedRequestDto.setFinalisedProxiesAmount(findUsagesCount(proxyRequestFound));

                            setSuccessAttributesToResponse((FinalizeUsagesResponse) responseBody);
                        }
                )
        );
    }

    @Transactional
    public FinalizeUsagesResponse finalizeByApplicationId(Mono<FinalizeRequestByApplicationId> monoRequest) {

        return this.<FinalizedRequestDto, FinalizeUsagesResponse>createAnswer(
                FinalizeUsagesResponse.class,
                (responseBody, finalizedRequestDto) -> processRequest(
                        monoRequest,
                        responseBody,
                        request -> {
                            final Integer applicationId = request.getApplicationId();
                            final AtomicInteger usagesCounter = new AtomicInteger();
                            final Application applicationToFind = applicationDao.findByApplicationId(applicationId);

                            requestDao
                                    .findActualProxyRequestsByApplicationId(applicationToFind)
                                    .forEach(proxyRequest -> {

                                        usagesCounter.addAndGet(findUsagesCount(proxyRequest));
                                        requestDao.setCompleteSessionAndSave(proxyRequest);
                                    });

                            finalizedRequestDto.setFinalisedProxiesAmount(usagesCounter.get());
                            setSuccessAttributesToResponse((FinalizeUsagesResponse) responseBody);

                            responseBody.complete();
                        }
                )
        );
    }

    private Integer findUsagesCount(ProxyRequest proxyRequest) {

        return proxyUsageDao.findByProxyRequest(proxyRequest).size();
    }

    private void setSuccessAttributesToResponse(FinalizeUsagesResponse response) {

        response.setCode(RestApiConst.RestOperCode.OC_OK);
        response.setMessage("Successfully finalized usages");
        response.setErrors(null);
        response.complete();
    }
}
