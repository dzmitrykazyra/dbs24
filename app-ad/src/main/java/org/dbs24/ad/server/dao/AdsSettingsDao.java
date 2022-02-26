/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.ad.server.dao;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import org.dbs24.ad.server.entity.AdsSettings;
import org.dbs24.ad.server.entity.AdsSettingsHist;
import org.dbs24.ad.server.repo.AdsSettingsHistRepo;
import org.dbs24.ad.server.repo.AdsSettingsRepo;
import org.dbs24.spring.core.api.DaoAbstractApplicationService;
import org.dbs24.stmt.StmtProcessor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.function.Supplier;

import static java.time.LocalDateTime.now;
import static org.dbs24.ad.server.consts.AdConsts.Caches.CACHE_ADS_SETTINGS;
import static org.dbs24.consts.SysConst.BOOLEAN_FALSE;
import static org.dbs24.consts.SysConst.INTEGER_NULL;

@Getter
@Log4j2
@Component
@EqualsAndHashCode(callSuper = true)
public class AdsSettingsDao extends DaoAbstractApplicationService {

    final AdsSettingsRepo adsSettingsRepo;
    final AdsSettingsHistRepo adsSettingsHistRepo;

    public AdsSettingsDao(AdsSettingsRepo adsSettingsRepo, AdsSettingsHistRepo adsSettingsHistRepo) {
        this.adsSettingsRepo = adsSettingsRepo;
        this.adsSettingsHistRepo = adsSettingsHistRepo;
    }

    //==========================================================================
    public Optional<AdsSettings> findOptionalAdsSettings(Integer adsSettingsId) {
        return adsSettingsRepo.findById(adsSettingsId);
    }

    //
    @Cacheable(CACHE_ADS_SETTINGS)
    public AdsSettings findAdsSettings(Integer adsSettingsId) {
        return findOptionalAdsSettings(adsSettingsId).orElseThrow();
    }

    //
    public void saveAdsSettingsHist(AdsSettingsHist adsSettingsHist) {
        adsSettingsHistRepo.save(adsSettingsHist);
    }

    //
    @CacheEvict(value = {CACHE_ADS_SETTINGS}, beforeInvocation = false, key = "#adsSettings.settingId", condition = "#adsSettings.settingId>0")
    public void saveAdsSettings(AdsSettings adsSettings) {
        adsSettingsRepo.save(adsSettings);
    }

    public AdsSettings findActualSettings(LocalDateTime actualDate, String appPackage) {
        return adsSettingsRepo.findActualSettings(actualDate, "%" + appPackage + "%")
                .orElseGet(defaultAdsSettings);
    }

    final Supplier<AdsSettings> defaultAdsSettings = () -> StmtProcessor.create(AdsSettings.class, adsSettings -> {

        adsSettings.setSettingId(INTEGER_NULL);
        adsSettings.setModifyDate(now());
        adsSettings.setCreateDate(adsSettings.getModifyDate());
        adsSettings.setIsActual(BOOLEAN_FALSE);
    });
}
