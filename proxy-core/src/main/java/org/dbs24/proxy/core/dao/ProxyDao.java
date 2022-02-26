/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.proxy.core.dao;

import lombok.Data;
import lombok.extern.log4j.Log4j2;
import org.dbs24.proxy.core.consts.ProxyConsts;
import org.dbs24.proxy.core.consts.ProxyConsts.ProxyProviderEnum;
import org.dbs24.proxy.core.entity.domain.Proxy;
import org.dbs24.proxy.core.entity.domain.ProxyRequest;
import org.dbs24.proxy.core.entity.domain.ProxyStatus;
import org.dbs24.proxy.core.entity.dto.BookProxiesDto;
import org.dbs24.proxy.core.repo.ProxyProviderRepo;
import org.dbs24.proxy.core.repo.ProxyRepo;
import org.dbs24.proxy.core.repo.ProxyStatusRepo;
import org.dbs24.proxy.core.repo.ProxyTypeRepo;
import org.dbs24.spring.core.api.DaoAbstractApplicationService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static org.dbs24.proxy.core.consts.ProxyConsts.Caches.CACHE_PROXY;
import static org.dbs24.proxy.core.consts.ProxyConsts.ProxyStatusEnum.PS_ACTUAL;
import static org.dbs24.proxy.core.consts.ProxyConsts.ProxyStatusEnum.PS_EXPRIRED;
import static org.dbs24.proxy.core.consts.ProxyConsts.ProxyTypeEnum.PT_STATIC;

@Data
@Log4j2
@Component
public class ProxyDao extends DaoAbstractApplicationService {

    final ProxyRepo proxyRepo;
    final ProxyTypeRepo proxyTypeRepo;
    final ProxyStatusRepo proxyStatusRepo;
    final ProxyProviderRepo proxyProviderRepo;
    final ProxyUsageBanDao proxyUsageBanDao;
    final ReferenceDao referenceDao;

    @Value("${config.algorithm-selection.min-used.days-to-search}")
    private Integer minUsedDaysSearch;
    @Value("${config.algorithm-selection.longest-not-used.days-to-search}")
    private Integer longestNotUsedDaysSearch;

    private final Integer ACTUAL_PROXY_STATUS_ID = PS_ACTUAL.getCode();
    private final Integer EXPIRED_PROXY_STATUS_ID = PS_EXPRIRED.getCode();

    public ProxyDao(ProxyRepo proxyRepo,
                    ProxyTypeRepo proxyTypeRepo,
                    ProxyStatusRepo proxyStatusRepo,
                    ProxyProviderRepo proxyProviderRepo,
                    ProxyUsageBanDao proxyUsageBanDao,
                    ReferenceDao referenceDao) {

        this.proxyRepo = proxyRepo;
        this.proxyTypeRepo = proxyTypeRepo;
        this.proxyStatusRepo = proxyStatusRepo;
        this.proxyProviderRepo = proxyProviderRepo;
        this.proxyUsageBanDao = proxyUsageBanDao;
        this.referenceDao = referenceDao;
    }

    /**
     * Method allows find available for customer proxies by its requirements(parameters)
     * without(or with 'usage.unknown') algorithm defined in
     * {@link org.dbs24.proxy.core.entity.domain.ProxyRequest} method parameter fields, converted from
     * {@link BookProxiesDto} object got from rest layer
     *
     * @param proxyRequest saved in db customers request for proxies usage
     * @return available(not banned) proxies by customers requirements
     */
    public List<Proxy> findProxy(ProxyRequest proxyRequest) {
        log.info("PROXY_DAO.FIND_PROXY_BY_MULTIPLE_PARAMETERS: started proxies selection with default algorithm");

        return proxyRepo.findWithAnyValuesLimit(
                getNotNullProxyTypeId(proxyRequest), proxyRequest.getProxyType() == null,
                getNotNullProxyProviderId(proxyRequest), proxyRequest.getProxyProvider() == null,
                getNotNullCountryId(proxyRequest), proxyRequest.getCountry() == null,
                proxyRequest.getApplication().getApplicationNetwork().getApplicationNetworkId(),
                LocalDateTime.now(),
                proxyRequest.getProxiesAmount()
        );
    }

    /**
     * Method allows find available for customer proxies by proxy request and fetch custom proxies amount
     *
     * @param proxyRequest  saved in DB customers request for proxies usage
     * @param proxiesAmount amount proxies to fetch
     * @return available(not banned) proxies by customers requirements
     */
    public List<Proxy> findProxiesByProxyRequestAndAmount(ProxyRequest proxyRequest, Integer proxiesAmount) {
        log.info("PROXY_DAO.FIND_PROXIES_BY_PROXY_REQUEST_AND_AMOUNT");

        return proxyRepo.findWithAnyValuesLimit(
                getNotNullProxyTypeId(proxyRequest), proxyRequest.getProxyType() == null,
                getNotNullProxyProviderId(proxyRequest), proxyRequest.getProxyProvider() == null,
                getNotNullCountryId(proxyRequest), proxyRequest.getCountry() == null,
                proxyRequest.getApplication().getApplicationNetwork().getApplicationNetworkId(),
                LocalDateTime.now(),
                proxiesAmount
        );
    }

    /**
     * Method allows find available proxies by 'usage.min' algorithm
     *
     * @see ProxyDao#findProxy default search algorithm method
     */
    public List<Proxy> findMinUsedProxy(ProxyRequest proxyRequest) {
        log.info("PROXY_DAO.FIND_PROXY_BY_MULTIPLE_PARAMETERS: started proxies selection with 'min used' algorithm");

        return proxyRepo.findWithAnyValuesLimitByUsageAmount(
                LocalDate.now().minusDays(minUsedDaysSearch),
                getNotNullProxyTypeId(proxyRequest), proxyRequest.getProxyType() == null,
                getNotNullProxyProviderId(proxyRequest), proxyRequest.getProxyProvider() == null,
                getNotNullCountryId(proxyRequest), proxyRequest.getCountry() == null,
                proxyRequest.getApplication().getApplicationNetwork().getApplicationNetworkId(),
                LocalDateTime.now(),
                proxyRequest.getProxiesAmount()
        );
    }

    /**
     * Method allows find available proxies by 'usage.longest.not.used' algorithm
     *
     * @see ProxyDao#findProxy default search algorithm method
     */
    public List<Proxy> findLongestNotUsedProxy(ProxyRequest proxyRequest) {
        log.info("PROXY_DAO.FIND_PROXY_BY_MULTIPLE_PARAMETERS: started proxies selection with 'longest not used' algorithm");

        return proxyRepo.findWithAnyValuesLimitByLastTimeUsage(
                LocalDate.now().minusDays(longestNotUsedDaysSearch),
                getNotNullProxyTypeId(proxyRequest), proxyRequest.getProxyType() == null,
                getNotNullProxyProviderId(proxyRequest), proxyRequest.getProxyProvider() == null,
                getNotNullCountryId(proxyRequest), proxyRequest.getCountry() == null,
                proxyRequest.getApplication().getApplicationNetwork().getApplicationNetworkId(),
                LocalDateTime.now(),
                proxyRequest.getProxiesAmount()
        );
    }

    /**
     * Method allows find available proxies by 'added.newest' algorithm
     *
     * @see ProxyDao#findProxy default search algorithm method
     */
    public List<Proxy> findNewestAddedProxy(ProxyRequest proxyRequest) {
        log.info("PROXY_DAO.FIND_PROXY_BY_MULTIPLE_PARAMETERS: started proxies selection with 'newest added' algorithm");

        return proxyRepo.findWithAnyValuesOrderByActualDateLimit(
                getNotNullProxyTypeId(proxyRequest), proxyRequest.getProxyType() == null,
                getNotNullProxyProviderId(proxyRequest), proxyRequest.getProxyProvider() == null,
                getNotNullCountryId(proxyRequest), proxyRequest.getCountry() == null,
                proxyRequest.getApplication().getApplicationNetwork().getApplicationNetworkId(),
                LocalDateTime.now(),
                proxyRequest.getProxiesAmount()
        );
    }

    public List<Proxy> findExpiredStaticProxies() {

        return proxyRepo.findAllByDateEndLessThanAndProxyType(
                LocalDateTime.now(),
                referenceDao.findProxyTypeById(PT_STATIC.getCode())
                        .orElseThrow(() -> new RuntimeException("Cannot find static proxy type"))
        );
    }

    private List<Proxy> findTargetBannedProxies(ProxyRequest proxyRequest) {

        return proxyUsageBanDao.findBannedProxyIdsByApplicationNetwork(
                proxyRequest.getApplication().getApplicationNetwork()
        );
    }

    private Integer getNotNullProxyTypeId(ProxyRequest proxyRequest) {
        return proxyRequest.getProxyType() == null ? -1 : proxyRequest.getProxyType().getProxyTypeId();
    }

    private Integer getNotNullProxyProviderId(ProxyRequest proxyRequest) {
        return proxyRequest.getProxyProvider() == null ? -1 : proxyRequest.getProxyProvider().getProviderId();
    }

    private Integer getNotNullCountryId(ProxyRequest proxyRequest) {
        return proxyRequest.getCountry() == null ? -1 : proxyRequest.getCountry().getCountryId();
    }


    @Cacheable(CACHE_PROXY)
    public Optional<Proxy> findProxyById(Integer proxyId) {

        return proxyRepo.findById(proxyId);
    }

    public Optional<Proxy> findProxyOptional(Integer proxyId) {

        return proxyRepo
                .findById(proxyId);
    }

    public Optional<Proxy> findProxy(String url, Integer port) {

        return proxyRepo.findByUrlAndSocksPort(url, port);
    }

    public Optional<Proxy> findSocksProxyIfActual(String externalIpAddress, Integer socksPort) {
        ProxyStatus actualProxyType = findActualProxyStatus();

        return proxyRepo.findByExternalIpAddressAndSocksPortAndProxyStatus(externalIpAddress, socksPort, actualProxyType);
    }

    public ProxyStatus findActualProxyStatus() {

        return referenceDao.findProxyStatusById(ACTUAL_PROXY_STATUS_ID).orElseThrow(() -> new RuntimeException("Cannot find actual proxy status in DAO layer"));
    }

    public ProxyStatus findExpiredProxyStatus() {

        return referenceDao.findProxyStatusById(EXPIRED_PROXY_STATUS_ID).orElseThrow(() -> new RuntimeException("Cannot find expired proxy status in DAO layer"));
    }

    @Override
    public void initialize() {
        final String className = this.getClass().getSimpleName();
        log.info("Service '{}' is activated", className.indexOf("$$") > 0 ? className.substring(0, className.indexOf("$$")) : className);
    }

    @Override
    public void destroy() {
        final String className = this.getClass().getSimpleName();
        log.info("Service '{}' is destroyed", className.indexOf("$$") > 0 ? className.substring(0, className.indexOf("$$")) : className);
    }


    @CacheEvict(value = {CACHE_PROXY}, allEntries = true, beforeInvocation = true)
    public Proxy saveProxy(Proxy proxy) {

        return proxyRepo.save(proxy);
    }

    public List<Proxy> batchSaveProxies(List<Proxy> proxies) {

        return proxyRepo.saveAllAndFlush(proxies);
    }

    public void batchSaveProxies(Collection<Proxy> proxies) {

        proxyRepo.saveAllAndFlush(proxies);
    }

    public Collection<Proxy> findActualProxies(ProxyProviderEnum proxyProvider) {

        return proxyRepo.findByProxyStatusAndProxyProvider(
                referenceDao
                        .findProxyStatusById(PS_ACTUAL.getCode())
                        .orElseThrow(() -> new RuntimeException("ProxyStatus is unknown or not found - " + PS_ACTUAL)),
                referenceDao
                        .findProxyProviderById(proxyProvider.getId())
                        .orElseThrow(() -> new RuntimeException("ProxyProvider is unknown or not found - " + proxyProvider)
                        ));
    }
}
