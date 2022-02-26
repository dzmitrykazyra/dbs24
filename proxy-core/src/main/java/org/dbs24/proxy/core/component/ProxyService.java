/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.proxy.core.component;

import lombok.Data;
import lombok.extern.log4j.Log4j2;
import org.dbs24.proxy.core.consts.ProxyConsts.ProxyProviderEnum;
import org.dbs24.proxy.core.dao.*;
import org.dbs24.proxy.core.entity.domain.AlgSelection;
import org.dbs24.proxy.core.entity.domain.Proxy;
import org.dbs24.proxy.core.entity.domain.ProxyRequest;
import org.dbs24.proxy.core.entity.dto.BookProxiesDto;
import org.dbs24.proxy.core.entity.dto.BookProxyByIdDto;
import org.dbs24.proxy.core.entity.dto.request.BookProxiesRequest;
import org.dbs24.proxy.core.entity.dto.request.BookProxyRequest;
import org.dbs24.proxy.core.entity.dto.response.BookedProxyResponse;
import org.dbs24.proxy.core.entity.dto.response.ProxyListResponse;
import org.dbs24.proxy.core.entity.dto.response.body.BookedProxy;
import org.dbs24.proxy.core.entity.dto.response.body.BookedProxyList;
import org.dbs24.proxy.core.validator.ProxyInfoValidator;
import org.dbs24.rest.api.service.AbstractRestApplicationService;
import org.dbs24.stmt.StmtProcessor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static org.dbs24.proxy.core.component.AlgorithmResolver.DEFAULT_ALG_SELECTION_NAME;
import static org.dbs24.rest.api.consts.RestApiConst.RestOperCode.OC_INVALID_ENTITY_ATTRS;
import static org.dbs24.rest.api.consts.RestApiConst.RestOperCode.OC_OK;

@Data
@Log4j2
@Component
@ConditionalOnProperty(name = "config.restfull.profile.name", havingValue = "proxy-core")
public class ProxyService extends AbstractRestApplicationService {

    final ReferenceDao referenceDao;
    final ProxyDao proxyDao;
    final RequestDao requestDao;
    final ProxyUsageDao proxyUsageDao;
    final ApplicationDao applicationDao;
    final ReferenceService referenceService;
    final ProxyInfoValidator proxyInfoValidator;
    final AlgorithmResolver algorithmResolver;

    public ProxyService(ReferenceDao referenceDao,
                        ProxyUsageDao proxyUsageDao,
                        ReferenceService referenceService,
                        ProxyDao proxyDao,
                        RequestDao requestDao,
                        ApplicationDao applicationDao,
                        ProxyInfoValidator proxyInfoValidator,
                        AlgorithmResolver algorithmResolver) {

        this.referenceDao = referenceDao;
        this.proxyUsageDao = proxyUsageDao;
        this.referenceService = referenceService;
        this.proxyDao = proxyDao;
        this.requestDao = requestDao;
        this.applicationDao = applicationDao;
        this.proxyInfoValidator = proxyInfoValidator;
        this.algorithmResolver = algorithmResolver;
    }

    @Transactional
    public ProxyListResponse bookProxies(Mono<BookProxiesRequest> monoRequest) {

        return this.<BookedProxyList, ProxyListResponse>createAnswer(
                ProxyListResponse.class,
                (responseBody, proxyList) -> processRequest(
                        monoRequest,
                        responseBody,
                        request -> responseBody.setErrors(proxyInfoValidator.validateConditional(
                                request.getEntityInfo(),
                                proxyRequestDto -> {
                                    log.info("PROXY_SERVICE.BOOK_PROXIES: got DTO with criteria: {}", proxyRequestDto);

                                    ProxyRequest savedProxyRequest = saveProxyRequestByProxyRequestDto(proxyRequestDto);
                                    log.info("PROXY_SERVICE.BOOK_PROXIES: saved ProxyRequest to db: {}", savedProxyRequest);

                                    synchronized (this){
                                        List<Proxy> availableProxies = findProxyListByAlgorithmIfExist(savedProxyRequest);
                                        log.info("PROXY_SERVICE.BOOK_PROXIES: found available proxies list with size: {}", availableProxies.size());

                                        availableProxies.forEach(proxy -> proxyUsageDao.saveUsage(proxy, savedProxyRequest));

                                        log.info("PROXY_SERVICE.BOOK_PROXIES: available proxies list: {}", availableProxies);
                                        proxyList.setBookedProxyList(availableProxies);
                                        proxyList.setRequestId(savedProxyRequest.getRequestId());
                                        responseBody.setCreatedEntity(proxyList);

                                        responseBody.setCode(OC_OK);
                                        responseBody.setMessage(String.format("Found available proxies for your requirements quantity: %s", availableProxies.size()));

                                        responseBody.complete();
                                    }

                                }, errorInfos -> {
                                    responseBody.setCode(OC_INVALID_ENTITY_ATTRS);
                                    responseBody.setMessage(errorInfos.stream().findFirst().orElseThrow().getErrorMsg().toUpperCase());

                                    responseBody.complete();
                                })))
        );
    }

    @Transactional
    public BookedProxyResponse bookProxy(Mono<BookProxyRequest> monoRequest) {

        return this.<BookedProxy, BookedProxyResponse>createAnswer(
                BookedProxyResponse.class,
                (responseBody, bookedProxy) -> processRequest(
                        monoRequest,
                        responseBody,
                        request -> {
                            final Optional<Proxy> proxyById = proxyDao.findProxyById(request.getEntityInfo().getProxyId());

                            proxyById.ifPresentOrElse(
                                    proxy -> {
                                        log.info("Required proxy: {}", proxy);
                                        final ProxyRequest savedProxyRequest = requestDao.saveProxyRequest(createProxyRequestForBookById(request.getEntityInfo()));

                                        proxyUsageDao.saveUsage(proxy, savedProxyRequest);

                                        bookedProxy.setRequestId(savedProxyRequest.getRequestId());
                                        bookedProxy.setBookedProxy(proxy);
                                        responseBody.setCode(OC_OK);
                                        responseBody.setMessage(String.format(
                                                "Proxy with id %s was successfully booked ",
                                                proxy.getProxyId()));

                                    },
                                    () -> {
                                        responseBody.setCode(OC_INVALID_ENTITY_ATTRS);
                                        responseBody.setMessage(String.format(
                                                "Cannot book proxy with id %s (maybe cause of it was already booked)",
                                                request.getEntityInfo().getProxyId()));

                                    }
                            );
                            responseBody.complete();
                        }
                ));
    }

    public Proxy createProxy() {

        return StmtProcessor.create(Proxy.class);
    }

    public Proxy findProxy(Integer proxyId) {

        return proxyDao.findProxyById(proxyId).orElseThrow(() -> new RuntimeException("Proxy by id was not found"));
    }

    public Optional<Proxy> findProxyOptional(Integer proxyId) {

        return proxyDao.findProxyOptional(proxyId);
    }

    public Optional<Proxy> findProxy(String url, Integer port) {

        return proxyDao.findProxy(url, port);
    }

    public Optional<Proxy> findSocksProxyIfActual(String externalIpAddress, Integer socksPort) {

        return proxyDao.findSocksProxyIfActual(externalIpAddress, socksPort);
    }

    public Collection<Proxy> findActualProxies(ProxyProviderEnum proxyProvider) {

        return proxyDao.findActualProxies(proxyProvider);
    }

    private ProxyRequest saveProxyRequestByProxyRequestDto(BookProxiesDto bookProxiesDto) {

        return requestDao.saveProxyRequest(createProxyRequest(bookProxiesDto));
    }

    private ProxyRequest createProxyRequest(BookProxiesDto bookProxiesDto) {

        return StmtProcessor.create(
                ProxyRequest.class,
                proxyRequest -> {
                    proxyRequest.setRequestDate(LocalDateTime.now());
                    proxyRequest.setProxiesAmount(bookProxiesDto.getAmount());
                    proxyRequest.setProxyType(referenceDao.findProxyTypeByName(bookProxiesDto.getProxyTypeName()).orElse(null));
                    proxyRequest.setProxyProvider(referenceDao.findProxyProviderByName(bookProxiesDto.getProviderName()).orElse(null));
                    proxyRequest.setCountry(referenceDao.findCountryByName(bookProxiesDto.getCountryName()).orElse(null));
                    proxyRequest.setApplication(applicationDao.findByApplicationName(bookProxiesDto.getApplicationName()));
                    proxyRequest.setAlgSelection(getAlgSelectionById(bookProxiesDto.getAlgorithmId()));
                    proxyRequest.setSessionStart(LocalDateTime.now());
                    proxyRequest.setSessionFinish(LocalDateTime.now().plusSeconds(bookProxiesDto.getBookingTimeMillis() / 1000));
                });
    }

    private ProxyRequest createProxyRequestForBookById(BookProxyByIdDto bookProxyByIdDto) {

        return StmtProcessor.create(
                ProxyRequest.class,
                proxyRequest -> {
                    proxyRequest.setRequestDate(LocalDateTime.now());
                    proxyRequest.setProxiesAmount(1);
                    proxyRequest.setProxyType(null);
                    proxyRequest.setProxyProvider(null);
                    proxyRequest.setCountry(null);
                    proxyRequest.setApplication(applicationDao.findByApplicationName(bookProxyByIdDto.getApplicationName()));
                    proxyRequest.setAlgSelection(getAlgSelectionById(DEFAULT_ALG_SELECTION_NAME.getCode()));
                    proxyRequest.setSessionStart(LocalDateTime.now());
                    proxyRequest.setSessionFinish(LocalDateTime.now().plusSeconds(bookProxyByIdDto.getBookingTimeMillis() / 1000));
                });
    }

    private AlgSelection getAlgSelectionById(Integer algSelectionId) {

        return algSelectionId != null
                ? referenceDao.findAlgSelectionById(algSelectionId).get()
                : referenceDao.findAlgSelectionById(DEFAULT_ALG_SELECTION_NAME.getCode()).get();
    }

    private List<Proxy> findProxyListByAlgorithmIfExist(ProxyRequest proxyRequest) {

        return algorithmResolver.findByAlgSelection(proxyRequest);
    }
}