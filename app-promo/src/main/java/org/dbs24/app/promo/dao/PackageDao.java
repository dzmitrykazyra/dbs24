/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.app.promo.dao;

import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import org.dbs24.app.promo.entity.AppPackage;
import org.dbs24.app.promo.entity.AppPackageHist;
import org.dbs24.app.promo.repo.PackageHistRepo;
import org.dbs24.app.promo.repo.PackageRepo;
import org.dbs24.spring.core.api.DaoAbstractApplicationService;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.util.Optional;

import static org.dbs24.app.promo.consts.AppPromoutionConsts.Caches.CACHE_PACKAGE;

@Getter
@Log4j2
@Component
public class PackageDao extends DaoAbstractApplicationService {

    final PackageRepo packageRepo;
    final PackageHistRepo packageHistRepo;

    public PackageDao(PackageRepo packageRepo, PackageHistRepo packageHistRepo) {
        this.packageRepo = packageRepo;
        this.packageHistRepo = packageHistRepo;
    }

    //==========================================================================
    public Optional<AppPackage> findOptionalPackage(Integer packageId) {
        return packageRepo.findById(packageId);
    }

    @Cacheable(CACHE_PACKAGE)
    public AppPackage findPackage(Integer packageId) {
        return findOptionalPackage(packageId).orElseThrow();
    }

    public void savePackageHist(AppPackageHist packageHist) {
        packageHistRepo.save(packageHist);
    }

    @CacheEvict(value = {CACHE_PACKAGE}, beforeInvocation = true, key = "#appPackage.packageId", condition = "#appPackage.packageId>0")
    public void savePackage(AppPackage appPackage) {
        packageRepo.save(appPackage);
    }

}
