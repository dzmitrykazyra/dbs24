package org.dbs24.proxy.core.component;

import lombok.extern.log4j.Log4j2;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import org.dbs24.proxy.core.component.socket.SocksSocketFactory;
import org.dbs24.proxy.core.consts.ProxyConsts;
import org.dbs24.proxy.core.dao.ProxyDao;
import org.dbs24.proxy.core.dao.ProxyUsageDao;
import org.dbs24.proxy.core.dao.RequestDao;
import org.dbs24.proxy.core.entity.domain.Proxy;
import org.dbs24.proxy.core.entity.domain.ProxyRequest;
import org.dbs24.proxy.core.entity.domain.ProxyUsage;
import org.dbs24.proxy.core.entity.dto.CheckProxyRelevanceDto;
import org.dbs24.proxy.core.entity.dto.request.UpdateBookProxiesRequest;
import org.dbs24.proxy.core.entity.dto.response.CheckProxyRelevanceResponse;
import org.dbs24.proxy.core.entity.dto.response.ProxyListResponse;
import org.dbs24.rest.api.service.AbstractRestApplicationService;
import org.dbs24.stmt.StmtProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.*;
import java.util.stream.Collectors;

import static java.lang.Math.abs;
import static org.dbs24.rest.api.consts.RestApiConst.RestOperCode.OC_NO_PROXY_AVAILABLE;
import static org.dbs24.rest.api.consts.RestApiConst.RestOperCode.OC_OK;

@Component
@Log4j2
public class UpdateProxyService extends AbstractRestApplicationService {

    private final RequestDao requestDao;
    private final ReferenceService referenceService;
    private final ProxyUsageDao proxyUsageDao;
    private final ProxyDao proxyDao;

    @Autowired
    public UpdateProxyService(RequestDao requestDao,
                              ReferenceService referenceService,
                              ProxyUsageDao proxyUsageDao,
                              ProxyDao proxyDao) {

        this.requestDao = requestDao;
        this.referenceService = referenceService;
        this.proxyUsageDao = proxyUsageDao;
        this.proxyDao = proxyDao;
    }

    @Transactional
    public ProxyListResponse updateBookedProxies(Mono<UpdateBookProxiesRequest> monoRequest) {

        return createAnswer(
                ProxyListResponse.class,
                (responseBody, proxyList) -> processRequest(
                        monoRequest,
                        responseBody,
                        request -> {
                            log.info("Update proxies for request id = {}", request.getEntityInfo().getRequestId());

                            ProxyRequest proxyRequestToUpdate = requestDao.findProxyRequestById(request.getEntityInfo().getRequestId());

                            List<Proxy> actualProxiesRequest = proxyUsageDao.findByProxyRequest(proxyRequestToUpdate)
                                    .stream()
                                    .map(ProxyUsage::getProxy)
                                    .filter(proxy -> proxy.getProxyStatus().equals(referenceService.findProxyStatus(ProxyConsts.ProxyStatusEnum.PS_ACTUAL)))
                                    .collect(Collectors.toList());

                            ProxyRequest savedProxyRequest = requestDao.saveProxyRequest(
                                    updateAmountAndBookTimeProxyRequest(
                                            proxyRequestToUpdate,
                                            request.getEntityInfo().getAmount(),
                                            request.getEntityInfo().getBookingTimeMills()
                                    )
                            );

                            fillMissingProxies(request.getEntityInfo().getAmount(), savedProxyRequest, actualProxiesRequest);

                            requestDao.setCompleteSessionAndSave(proxyRequestToUpdate);

                            actualProxiesRequest.forEach(proxy -> proxyUsageDao.saveUsage(proxy, savedProxyRequest));

                            proxyList.setBookedProxyList(actualProxiesRequest);
                            proxyList.setRequestId(savedProxyRequest.getRequestId());

                            responseBody.setCode(OC_OK);
                            responseBody.setCreatedEntity(proxyList);

                            responseBody.complete();
                        }
                )
        );
    }

    @Transactional
    public CheckProxyRelevanceResponse checkProxiesRelevance(Integer requestId) {

        return this.createAnswer(
                CheckProxyRelevanceResponse.class,
                (responseBody, checkedProxies) -> processRequest(
                        responseBody,
                        () -> {
                            final List<Future<CheckProxyRelevanceDto>> futuresDto = new ArrayList<>();

                            final ProxyRequest proxyRequest = requestDao.findProxyRequestById(requestId);

                            ExecutorService executorService = Executors.newFixedThreadPool(proxyRequest.getProxiesAmount());

                            proxyUsageDao.findByProxyRequest(proxyRequest)
                                    .stream()
                                    .map(ProxyUsage::getProxy)
                                    .forEach(proxy -> futuresDto.add(executorService.submit(createConnectCallableTask(proxy))));

                            StmtProcessor.ifTrue(futuresDto.size() > 0,
                                    () -> {
                                        final List<CheckProxyRelevanceDto> proxiesRelevanceDtoList = receiveDtoListFromFutures(futuresDto);

                                        checkedProxies.setProxyInfoList(proxiesRelevanceDtoList);
                                        checkedProxies.setTotalProxyAmount(proxiesRelevanceDtoList.size());
                                        //todo: remove stream
                                        checkedProxies.setActualProxyAmount((int) proxiesRelevanceDtoList.stream().filter(CheckProxyRelevanceDto::isActual).count());

                                        responseBody.setCode(OC_OK);
                                        responseBody.setCreatedEntity(checkedProxies);
                                    },
                                    () -> {
                                        responseBody.setCode(OC_NO_PROXY_AVAILABLE);
                                        responseBody.setError("No proxies in request");
                                    });
                            responseBody.complete();
                        }
                )
        );
    }

    private Callable<CheckProxyRelevanceDto> createConnectCallableTask(Proxy proxy) {

        return () -> {
            final SocksSocketFactory socksSocketFactory = new SocksSocketFactory(proxy, 100);

            final OkHttpClient httpClient = new OkHttpClient.Builder().socketFactory(socksSocketFactory).build();

            final Request request = new Request
                    .Builder()
                    .url("https://www.google.com/")
                    .build();

            boolean isProxyActual = false;

            if (proxy.getProxyStatus().equals(proxyDao.findActualProxyStatus())) {
                try {
                    httpClient.newCall(request).execute().body();
                    isProxyActual = true;
                } catch (IOException e) {
                    log.error("Proxy with id = {} not actual", proxy.getProxyId());
                }
            }

            return CheckProxyRelevanceDto.of(proxy, isProxyActual);
        };
    }

    private List<CheckProxyRelevanceDto> receiveDtoListFromFutures(List<Future<CheckProxyRelevanceDto>> futures) {
        return futures.stream()
                .map(future -> {
                    try {
                        return future.get();
                    } catch (InterruptedException | ExecutionException e) {
                        log.error("UpdateProxyService - failed to future.get(). Exception message: {}", e.getMessage());
                        return null;
                    }
                })
                .collect(Collectors.toList());
    }


    private ProxyRequest updateAmountAndBookTimeProxyRequest(ProxyRequest proxyRequestToUpdate,
                                                             Integer proxyAmount,
                                                             Integer timeToBook) {
        return StmtProcessor.create(
                ProxyRequest.class,
                proxyRequest -> {
                    proxyRequest.setProxyProvider(proxyRequestToUpdate.getProxyProvider());
                    proxyRequest.setProxyType(proxyRequestToUpdate.getProxyType());
                    proxyRequest.setProxiesAmount(proxyAmount);
                    proxyRequest.setSessionStart(LocalDateTime.now());
                    proxyRequest.setSessionFinish(LocalDateTime.now().plus(timeToBook, ChronoUnit.MILLIS));
                    proxyRequest.setCountry(proxyRequestToUpdate.getCountry());
                    proxyRequest.setApplication(proxyRequestToUpdate.getApplication());
                    proxyRequest.setAlgSelection(proxyRequestToUpdate.getAlgSelection());
                    proxyRequest.setRequestDate(LocalDateTime.now());
                });
    }

    private void fillMissingProxies(Integer requestProxyAmount, ProxyRequest proxyRequest, List<Proxy> actualProxiesRequest) {
        int amountMissingProxies = requestProxyAmount - actualProxiesRequest.size();

        StmtProcessor.ifTrue(amountMissingProxies > 0,
                () -> actualProxiesRequest.addAll(proxyDao.findProxiesByProxyRequestAndAmount(proxyRequest, amountMissingProxies)),
                () -> {
                    Collections.shuffle(actualProxiesRequest);
                    actualProxiesRequest.subList(0, abs(amountMissingProxies)).clear();
                }
        );
    }
}
