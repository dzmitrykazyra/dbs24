/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.entity.actions;

import org.dbs24.entity.core.api.ActionCodeId;
import static org.dbs24.consts.EntityReferenceConst.*;
import org.dbs24.entity.core.api.PreViewDialog;
import org.dbs24.entity.core.api.ViewAction;
import org.dbs24.entity.AbstractEntityContract;
import lombok.Data;
import java.time.LocalDate;
import java.time.Period;
import org.dbs24.application.core.nullsafe.NullSafe;
import org.dbs24.application.core.service.funcs.ServiceFuncs;
import org.dbs24.entity.tariff.TariffCalcRecord;
import org.dbs24.entity.tariff.TariffRate;
import org.dbs24.references.tariffs.serv.TariffServ;
import java.util.Collection;
import org.dbs24.entity.tariff.TariffKindService;
import org.dbs24.exception.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.GenericApplicationContext;

@ActionCodeId(action_code = ACT_CALCULATE_TARIFFS,
        action_name = "Пересчет тарифицируемых сумм",
        en_action_name = "Recalculate tariff records")
@Data
@ViewAction
@PreViewDialog
public class ActCalculateTariffSums<T extends AbstractEntityContract> extends AbstractContractAction<T> {

    final GenericApplicationContext genericApplicationContext;
    final Collection<TariffCalcRecord> newTariffCalcRecords = ServiceFuncs.<TariffCalcRecord>createCollection();

    private LocalDate D1, D2;

    @Autowired
    public ActCalculateTariffSums(GenericApplicationContext genericApplicationContext) {
        this.genericApplicationContext = genericApplicationContext;
    }

    //==========================================================================
    @Override
    protected void doCalculation() {

        this.getContractEntity().getTariffPlan().getTariffServs()
                .stream()
                .findFirst()
                .orElseThrow(() -> new TariffPlanIsEmptyException("TariffPlan is empty"));

        this.D1 = this.getContractEntity().getBeginDate();
        this.D2 = this.getContractEntity().getEndDate();

        // строим калькуляции на основании услуг из тарифного плана
        this.getContractEntity()
                .getTariffPlan()
                .getTariffServs()
                .stream()
                .forEach(serv -> {
                    // тарифицируемая услуга на плане
                    final TariffServ tariffServ = serv.getTariffServ();
                    //serv.getTariffRates()
                    // ищем расчет с тарифицируемой услугой

                    final TariffCalcRecord tariffCalcRecord = NullSafe.createObject(TariffCalcRecord.class);

                    // актуальные ставки
                    final TariffRate tariffRate = serv
                            .getTariffRates()
                            .stream()
                            .filter(rate -> rate.getTariffKind().getTariffServ().getTariffServId().equals(tariffServ.getTariffServId()))
                            .findFirst()
                            .orElseThrow(() -> new RateNotFoundException(String.format("Rate not found for servId=%d", tariffServ.getTariffServId())));
                    // ищем расчет
                    tariffCalcRecord.setTariffRate(tariffRate);
                    tariffCalcRecord.setEntity(this.getContractEntity());

                    final Class tariffClass = serv.getTariffKind().getClass();

                    final TariffKindService tariffKind = genericApplicationContext.<TariffKindService>getBean(tariffClass);

                    // суммы калькуляции за период
                    tariffCalcRecord.setTariffSums(tariffKind.calculateTariff(this.getContractEntity(), tariffRate, D1, D2));

                    newTariffCalcRecords.add(tariffCalcRecord);
                });
    }

    @Override
    protected Integer getJdbcBatchSize() {
        //return (Period.between(D2, D1).getDays() / 2);
        return 100;
    }

    //==========================================================================
    @Override
    protected void doUpdate() {

        super.doUpdate();
        // синхронизация с основными рассчетами
        this.newTariffCalcRecords
                .stream()
                .forEach(record -> {
                    // находим существующий расчет по услуге
                    final TariffCalcRecord tariffCalcRecord
                            = this.getContractEntity()
                                    .getTariffCalcRecords()
                                    .stream()
                                    .filter(existsRecord
                                            -> existsRecord
                                            .getTariffRate()
                                            .getTariffPlan2Serv()
                                            .getTariffServ()
                                            .getTariffServId()
                                            .equals(record.getTariffRate().getTariffPlan2Serv().getTariffServ().getTariffServId()))
                                    .findFirst()
                                    .orElse(NullSafe.createObject(TariffCalcRecord.class));

                    tariffCalcRecord.mergeRecord(record, D1, D2);

                    if (NullSafe.isNull(tariffCalcRecord.getTariffCalcId())) {
                        this.getContractEntity()
                                .getTariffCalcRecords()
                                .add(tariffCalcRecord);
                    }
                });
    }
}
