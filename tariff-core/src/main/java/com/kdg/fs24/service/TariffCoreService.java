/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kdg.fs24.service;

import com.kdg.fs24.application.core.log.LogService;
import com.kdg.fs24.application.core.nullsafe.NullSafe;
import com.kdg.fs24.application.core.service.funcs.ReflectionFuncs;
import com.kdg.fs24.application.core.sysconst.SysConst;
import com.kdg.fs24.entity.core.api.EntityClassesPackages;
import com.kdg.fs24.entity.kind.EntityKind;
import lombok.Data;
import org.springframework.stereotype.Service;
import com.kdg.fs24.entity.tariff.AbstractTariffPlan;
import java.time.LocalDate;
import java.time.LocalDateTime;
import com.kdg.fs24.references.tariffs.api.TariffConst;
import com.kdg.fs24.entity.status.EntityStatus;
import com.kdg.fs24.references.api.AbstractRefRecord;
import com.kdg.fs24.entity.tariff.TariffPlanProcessor;
import com.kdg.fs24.references.tariffs.kind.TariffKind;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Profile;

/**
 *
 * @author Козыро Дмитрий
 */
@Data
@Service
@ComponentScan(basePackages = SysConst.SERVICE_PACKAGE)
//@EntityClassesPackages(pkgList = {"com.kdg.fs24.entity.tariff"})
@EntityClassesPackages(pkgList = {SysConst.ENTITY_PACKAGE + ".tariff"})
public class TariffCoreService extends ActionExecutionService {

//    @Value("${tariff.debug:false}")
//    private Boolean tariffDebug = SysConst.BOOLEAN_FALSE;
    @Bean
    @Profile("dev")
    public TariffStdRates tariffStrRatesDev() {
        return NullSafe.createObject(TariffStdRates.class);
    }

    @Bean
    @Profile("production")
    public TariffStdRates tariffStrRatesProd() {
        return NullSafe.createObject(TariffStdRates.class);
    }

    @Override
    public void postActionExecutionService() {
        //    TariffConst.tariffDebug = this.tariffDebug;
        // делаем бинами объекты-тарифы по расчету тарифицируемых услуг
        ReflectionFuncs.processPkgClassesCollection(SysConst.TARIFF_PACKAGE, TariffKind.class, null,
                (tariffClazz) -> {

                    final String tariffClazzName = tariffClazz.getCanonicalName();

                    if (!this.getGenericApplicationContext().containsBean(tariffClazzName)) {

                        if (this.getEntityCoreDebug()) {
                            LogService.LogInfo(this.getClass(), () -> String.format("Registry tariff bean: '%s'",
                            tariffClazzName));
                        }

                        this.getGenericApplicationContext().registerBean(tariffClazz, bd -> {
                            bd.setBeanClassName(tariffClazzName);
                            bd.setScope(ConfigurableBeanFactory.SCOPE_SINGLETON);
                            bd.setAutowireCandidate(true);
                        });
                    }

                });

        // родительский метод
        super.postActionExecutionService();

    }

    public AbstractTariffPlan createTariffPlan(final String tariffPlanName,
            final String tariffPlanCode,
            final EntityKind entityKind,
            final LocalDate actualDate,
            final LocalDate finishDate,
            final TariffPlanProcessor tariffPlanProcessor) {

        final AbstractTariffPlan abstractTariffPlan = this.<AbstractTariffPlan>createActionEntity(AbstractTariffPlan.class,
                (tariffPlan) -> {
                    tariffPlan.setPlanKind(entityKind);
                    tariffPlan.setTariffPlanCode(tariffPlanCode);
                    tariffPlan.setTariffPlanName(tariffPlanName);
                    tariffPlan.setActualDate(actualDate);
                    tariffPlan.setFinishDate(finishDate);
                    tariffPlan.setCreation_date(LocalDateTime.now());
                    tariffPlan.setEntityStatus(AbstractRefRecord.<EntityStatus>getRefeenceRecord(
                            EntityStatus.class,
                            record -> record.getEntityStatusId().equals(0)
                            && record.getEntityTypeId().equals(TariffConst.ENTITY_TARIFF_PLAN)));
                });

        tariffPlanProcessor.processTariffPlan(abstractTariffPlan);

        return abstractTariffPlan;
    }
    //==========================================================================

}
