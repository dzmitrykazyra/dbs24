/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.app.promo.component;

import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import org.dbs24.app.promo.dao.PackageDao;
import org.dbs24.app.promo.entity.AppPackage;
import org.dbs24.app.promo.entity.AppPackageHist;
import org.dbs24.app.promo.rest.dto.apppackage.CreatePackageRequest;
import org.dbs24.app.promo.rest.dto.apppackage.CreatedPackage;
import org.dbs24.app.promo.rest.dto.apppackage.CreatedPackageResponse;
import org.dbs24.app.promo.rest.dto.apppackage.PackageInfo;
import org.dbs24.app.promo.rest.dto.apppackage.validator.PackageInfoValidator;
import org.dbs24.application.core.locale.NLS;
import org.dbs24.rest.api.action.SimpleActionInfo;
import org.dbs24.rest.api.service.AbstractRestApplicationService;
import org.dbs24.stmt.StmtProcessor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Supplier;

import static org.dbs24.consts.SysConst.CURRENT_LOCALDATETIME;
import static org.dbs24.rest.api.consts.RestApiConst.RestOperCode.OC_INVALID_ENTITY_ATTRS;
import static org.dbs24.rest.api.consts.RestApiConst.RestOperCode.OC_OK;

@Getter
@Log4j2
@Component
@ConditionalOnProperty(name = "config.restfull.profile.name", havingValue = "app-promo")
public class PackagesService extends AbstractRestApplicationService {

    final PackageDao packageDao;
    final RefsService refsService;
    final PackageInfoValidator packageInfoValidator;

    public PackagesService(RefsService refsService, PackageDao packageDao, PackageInfoValidator packageInfoValidator) {
        this.refsService = refsService;
        this.packageDao = packageDao;
        this.packageInfoValidator = packageInfoValidator;
    }

    @FunctionalInterface
    interface PackagesHistBuilder {
        void buildPackagesHist(AppPackage appPackage);
    }

    final Supplier<AppPackage> createNewPackage = () -> StmtProcessor.create(AppPackage.class);


    final BiFunction<PackageInfo, AppPackage, AppPackage> assignDto = (appPackageInfo, appPackage) -> {

        appPackage.setPackageName(appPackageInfo.getPackageName());
        appPackage.setPackageNote(appPackageInfo.getPackageNote());
        appPackage.setIsActual(appPackageInfo.getIsActual());
        appPackage.setActualDate(Optional.ofNullable(appPackageInfo.getActualDate())
                .map(ad -> NLS.long2LocalDateTime(ad))
                .orElseGet(CURRENT_LOCALDATETIME));
        appPackage.setProvider(getRefsService().findProvider(appPackageInfo.getProviderId()));

        return appPackage;
    };

    final BiFunction<PackageInfo, PackagesService.PackagesHistBuilder, AppPackage> assignPackagesInfo = (appPackageInfo, appPackagesHistBuilder) -> {

        final AppPackage appPackage = Optional.ofNullable(appPackageInfo.getPackageId())
                .map(getPackageDao()::findPackage)
                .orElseGet(createNewPackage);

        // store history
        Optional.ofNullable(appPackage.getPackageId()).ifPresent(packageId -> appPackagesHistBuilder.buildPackagesHist(appPackage));

        assignDto.apply(appPackageInfo, appPackage);

        return appPackage;
    };

    //==========================================================================
    @Transactional
    public CreatedPackageResponse createOrUpdatePackage(Mono<CreatePackageRequest> monoRequest) {

        return this.<CreatedPackage, CreatedPackageResponse>createAnswer(CreatedPackageResponse.class,
                (responseBody, createdPackage) -> processRequest(monoRequest, responseBody, request
                        -> responseBody.setErrors(packageInfoValidator.validateConditional(request.getEntityInfo(), appPackageInfo
                        -> {

                    final SimpleActionInfo simpleActionInfo = request.getEntityAction();

                    //log.info("simpleActionInfo =  {}", simpleActionInfo);

                    log.debug("create/update appPackage: {}", appPackageInfo);

                    //StmtProcessor.assertNotNull(String.class, appPackageInfo.getPackageName(), "appPackageName name is not defined");

                    final AppPackage appPackage = findOrCreatePackages(appPackageInfo, appPackageHist -> savePackageHist(createPackageHist(appPackageHist)));

                    final Boolean isNewSetting = StmtProcessor.isNull(appPackage.getPackageId());

                    getPackageDao().savePackage(appPackage);

                    final String finalMessage = String.format("Package is %s (PackageId=%d)",
                            isNewSetting ? "created" : "updated",
                            appPackage.getPackageId());

                    createdPackage.setCreatedPackageId(appPackage.getPackageId());

                    log.debug(finalMessage);

                    responseBody.setCode(OC_OK);
                    responseBody.setMessage(finalMessage);
                    responseBody.complete();
                }, errorInfos -> {
                    responseBody.setCode(OC_INVALID_ENTITY_ATTRS);
                    responseBody.setMessage(errorInfos.stream().findFirst().orElseThrow().getErrorMsg().toUpperCase());
                }))));
    }

    public AppPackage findOrCreatePackages(PackageInfo appPackageInfo, PackagesService.PackagesHistBuilder appPackagesHistBuilder) {
        return assignPackagesInfo.apply(appPackageInfo, appPackagesHistBuilder);
    }

    private AppPackageHist createPackageHist(AppPackage appPackage) {
        return StmtProcessor.create(AppPackageHist.class, appPackageHist -> {

            appPackageHist.setPackageId(appPackage.getPackageId());
            appPackageHist.setPackageName(appPackage.getPackageName());
            appPackageHist.setProvider(appPackage.getProvider());
            appPackageHist.setActualDate(appPackage.getActualDate());
            appPackageHist.setPackageNote(appPackage.getPackageNote());
            appPackageHist.setIsActual(appPackage.getIsActual());

        });
    }

    private void savePackageHist(AppPackageHist appPackageHist) {
        getPackageDao().savePackageHist(appPackageHist);
    }

    public AppPackage findPackage(Integer packageId) {
        return getPackageDao().findPackage(packageId);
    }
}
