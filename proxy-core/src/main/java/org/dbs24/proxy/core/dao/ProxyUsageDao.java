/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.proxy.core.dao;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import lombok.Data;
import lombok.extern.log4j.Log4j2;
import org.dbs24.proxy.core.entity.domain.Proxy;
import org.dbs24.proxy.core.entity.domain.ProxyRequest;
import org.dbs24.proxy.core.repo.AlgSelectionRepo;
import org.dbs24.proxy.core.repo.ProxyProviderRepo;
import org.dbs24.spring.core.api.DaoAbstractApplicationService;
import org.dbs24.stmt.StmtProcessor;
import org.springframework.stereotype.Component;
import org.dbs24.proxy.core.repo.ProxyUsageRepo;
import org.dbs24.proxy.core.entity.domain.ProxyUsage;
import static org.dbs24.proxy.core.consts.ProxyConsts.Caches.*;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;

@Log4j2
@Component
public class ProxyUsageDao extends DaoAbstractApplicationService {

    final ProxyUsageRepo proxyUsageRepo;

    public ProxyUsageDao(ProxyUsageRepo proxyUsageRepo, AlgSelectionRepo algSelectionRepo, ProxyProviderRepo proxyProviderRepo) {
        this.proxyUsageRepo = proxyUsageRepo;
    }

    @CacheEvict(value = {CACHE_PROXY_USAGE, CACHE_ACTUAL_PROXY_USAGE}, allEntries = true, beforeInvocation = true)
    public void saveUsage(Proxy bookedProxy, ProxyRequest requestToBook) {

        proxyUsageRepo.save(StmtProcessor.create(
                ProxyUsage.class,
                proxyUsage -> {
                    proxyUsage.setProxy(bookedProxy);
                    proxyUsage.setProxyRequest(requestToBook);
                }));
    }
    
    @Cacheable(CACHE_ACTUAL_PROXY_USAGE)
    public Collection<ProxyUsage> findActualProxyUsages() {

        return proxyUsageRepo.findActualProxyUsages();
    }

    @Cacheable(CACHE_PROXY_USAGE)
    public Optional<ProxyUsage> findByProxyUsageId(Integer proxyUsageId) {

        return proxyUsageRepo.findById(proxyUsageId);
    }

    public List<ProxyUsage> findByProxyRequest(ProxyRequest proxyRequest) {

        return proxyUsageRepo.findByProxyRequest(proxyRequest);
    }
}
