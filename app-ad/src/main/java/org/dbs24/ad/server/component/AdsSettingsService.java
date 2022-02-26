/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.ad.server.component;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import org.dbs24.ad.server.dao.AdsSettingsDao;
import org.dbs24.ad.server.entity.AdsSettings;
import org.dbs24.ad.server.entity.AdsSettingsHist;
import org.dbs24.ad.server.entity.dto.AdsSettingsDto;
import org.dbs24.ad.server.entity.dto.CreatedAdsSettings;
import org.dbs24.rest.api.service.AbstractRestApplicationService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import java.util.function.BiFunction;
import java.util.function.Supplier;

import static java.lang.String.format;
import static java.time.LocalDateTime.now;
import static java.util.Optional.ofNullable;
import static org.dbs24.application.core.locale.NLS.long2LocalDateTime;
import static org.dbs24.stmt.StmtProcessor.create;
import static org.dbs24.stmt.StmtProcessor.isNull;
import static reactor.core.publisher.Mono.just;

@Getter
@Log4j2
@Component
@EqualsAndHashCode(callSuper = true)
public class AdsSettingsService extends AbstractRestApplicationService {

    final AdsSettingsDao adsSettingsDao;
    final RefsService refsService;
    final ModelMapper modelMapper;

    public AdsSettingsService(AdsSettingsDao adsSettingsDao, RefsService refsService, ModelMapper modelMapper) {
        this.adsSettingsDao = adsSettingsDao;
        this.refsService = refsService;
        this.modelMapper = modelMapper;
    }

    @FunctionalInterface
    interface AdsSettingsHistBuilder {
        void buildAdsSettingsHist(AdsSettings adsSettings);
    }

    final Supplier<AdsSettings> createNewAdsSettings = () -> create(AdsSettings.class);

    final BiFunction<AdsSettingsDto, AdsSettingsHistBuilder, AdsSettings> assignAdsSettingsDto = (adsSettingsDto, adsSettingsHistBuilder) -> {

        final var adsSettings = ofNullable(adsSettingsDto.getSettingId())
                .map(this::findAdsSettings)
                .orElseGet(createNewAdsSettings);

        // store history
        ofNullable(adsSettings.getSettingId()).ifPresent(adsSettingsId -> adsSettingsHistBuilder.buildAdsSettingsHist(adsSettings));

        getModelMapper().map(adsSettingsDto, adsSettings);

        return adsSettings;
    };

    @Transactional(readOnly = true)
    public AdsSettingsDto getAdsSettings(Long actualDate, String appPackage) {

        var settingDate = isNull(actualDate) ? now() : long2LocalDateTime(actualDate);

        log.debug("getAdsSettings: {}", settingDate);

        return create(AdsSettingsDto.class, adsSettingsDto -> getModelMapper()
                .map(adsSettingsDao.findActualSettings(settingDate, appPackage), adsSettingsDto));
    }

    @Transactional
    public Mono<CreatedAdsSettings> createOrUpdateAdsSettings(AdsSettingsDto adsSettingsDto) {

        final var adsSettings = findOrCreateAdsSettings(adsSettingsDto, this::createAdsSettingsHist);

        final var isNewSetting = isNull(adsSettings.getSettingId());

        adsSettingsDao.saveAdsSettings(adsSettings);

        final String finalAdsSettings = format("AdsSettings is %s (adsSettingsId=%d)",
                isNewSetting ? "created" : "updated",
                adsSettings.getSettingId());

        return just(create(CreatedAdsSettings.class, ca -> {

            ca.setAdsSettingId(adsSettings.getSettingId());
            ca.setNote(finalAdsSettings);
            log.debug(finalAdsSettings);

        }));
    }

    public AdsSettings findOrCreateAdsSettings(AdsSettingsDto adsSettingsDto, AdsSettingsService.AdsSettingsHistBuilder adsSettingsHistBuilder) {
        return assignAdsSettingsDto.apply(adsSettingsDto, adsSettingsHistBuilder);
    }

    private void createAdsSettingsHist(AdsSettings adsSettings) {
        saveAdsSettingsHist(getModelMapper().map(adsSettings, AdsSettingsHist.class));
    }

    private void saveAdsSettingsHist(AdsSettingsHist adsSettingsHist) {
        getAdsSettingsDao().saveAdsSettingsHist(adsSettingsHist);
    }

    public AdsSettings findAdsSettings(Integer adsSettingsId) {
        return getAdsSettingsDao().findAdsSettings(adsSettingsId);
    }

    private AdsSettingsDto createDto(AdsSettings adsSettings) {
        return getModelMapper().map(adsSettings, AdsSettingsDto.class);
    }
}
