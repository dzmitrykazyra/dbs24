/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.dao;

import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import org.dbs24.entity.ServicePeriod;
import org.dbs24.entity.ServicePeriodHist;
import org.dbs24.repo.ServicePeriodHistRepo;
import org.dbs24.repo.ServicePeriodRepo;
import org.dbs24.spring.core.api.DaoAbstractApplicationService;
import org.dbs24.stmt.StmtProcessor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.util.Collection;
import java.util.function.Supplier;

import static org.dbs24.consts.ServicePeriodsConsts.Caches.CACHE_OUT_OF_SERVICE;

@Getter
@Log4j2
@Component
public class ServicePeriodDao extends DaoAbstractApplicationService {

    final ServicePeriodRepo servicePeriodRepo;
    final ServicePeriodHistRepo servicePeriodHistRepo;

    public ServicePeriodDao(ServicePeriodRepo servicePeriodRepo, ServicePeriodHistRepo servicePeriodHistRepo) {
        this.servicePeriodRepo = servicePeriodRepo;
        this.servicePeriodHistRepo = servicePeriodHistRepo;
    }

    final Supplier<ServicePeriod> newServicePeriod = () -> StmtProcessor.create(ServicePeriod::new);

    @Cacheable(CACHE_OUT_OF_SERVICE)
    public ServicePeriod getServicePeriod() {

        final Collection<ServicePeriod> periods = servicePeriodRepo.findAll();

        Assert.isTrue(periods.size() <= 1, String.format("getServicePeriod: illegal collection size = %d ", periods.size()));

        final ServicePeriod servicePeriod = periods.stream().findAny().orElseGet(newServicePeriod);

        log.warn("load out-of-service records: {}}", servicePeriod);

        return servicePeriod;
    }

    @CacheEvict(value = {CACHE_OUT_OF_SERVICE}, allEntries = true, beforeInvocation = true)
    public void saveServicePeriod(ServicePeriod servicePeriod) {
        servicePeriodRepo.save(servicePeriod);
    }

    public void saveServicePeriodHist(ServicePeriodHist servicePeriodHist) {
        servicePeriodHistRepo.save(servicePeriodHist);
    }

}
