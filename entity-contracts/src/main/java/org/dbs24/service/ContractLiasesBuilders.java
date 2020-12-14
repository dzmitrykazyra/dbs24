/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collection;
import java.util.Optional;
import org.dbs24.application.core.nullsafe.NullSafe;
import org.dbs24.application.core.service.funcs.FilterComparator;
import org.dbs24.application.core.service.funcs.ServiceFuncs;
import org.dbs24.entity.Currency;
import org.dbs24.entity.AbstractEntityContract;
import org.dbs24.entity.debts.LiasDebt;
import org.dbs24.lias.opers.api.LiasOpersConst;
import org.dbs24.lias.opers.attrs.DEBT_STATE_ID;
import org.dbs24.lias.opers.attrs.LIAS_BASE_ASSET_TYPE_ID;
import org.dbs24.lias.opers.attrs.LIAS_CURRENCY_ID;
import org.dbs24.lias.opers.attrs.LIAS_FINAL_DATE;
import org.dbs24.lias.opers.attrs.LIAS_KIND_ID;
import org.dbs24.lias.opers.attrs.LIAS_START_DATE;
import org.dbs24.lias.opers.attrs.LIAS_TYPE_ID;
import org.dbs24.lias.opers.attrs.ROW_NUM;
import org.dbs24.lias.opers.napi.LiasFinanceOper;
import org.dbs24.references.liases.baseassettype.LiasBaseAssetType;
import org.dbs24.references.liases.debtstate.LiasDebtState;
import org.dbs24.references.liases.kind.LiasKind;
import org.dbs24.references.liases.type.LiasType;
import org.dbs24.references.tariffs.kind.TariffAccretionHisory;
import org.dbs24.spring.core.api.AbstractApplicationBean;
import org.springframework.stereotype.Service;
import org.dbs24.service.ApplicationReferencesService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * Построитель обязательств и задолженностей
 */
@Service
public class ContractLiasesBuilders extends AbstractApplicationBean {

    final ApplicationReferencesService applicationReferencesService;

    @Autowired
    public ContractLiasesBuilders(ApplicationReferencesService applicationReferencesService) {
        this.applicationReferencesService = applicationReferencesService;
    }

    //==========================================================================
    private LiasDebt createLiasDebt(
            AbstractEntityContract abstractEntityContract,
            LiasFinanceOper liasFinanceOper) {

        return NullSafe.<LiasDebt>createObject(LiasDebt.class, newLiasDebt -> {

            newLiasDebt.setCurrency(applicationReferencesService.findCurrency(liasFinanceOper.<Integer>attr(LIAS_CURRENCY_ID.class)));

            newLiasDebt.setLiasBaseAssetType(LiasBaseAssetType.findLiasBaseAssetType(liasFinanceOper.<Integer>attr(LIAS_BASE_ASSET_TYPE_ID.class)));
            newLiasDebt.setCounterparty(abstractEntityContract.getCounterparty());
            newLiasDebt.setDebtContract(abstractEntityContract);
            newLiasDebt.setLiasDebtState(LiasDebtState.findLiasDebtState(liasFinanceOper.<Integer>attr(DEBT_STATE_ID.class)));
            newLiasDebt.setLiasKind(LiasKind.findLiasKind(liasFinanceOper.<Integer>attr(LIAS_KIND_ID.class)));
            newLiasDebt.setLiasType(LiasType.findLiasType(liasFinanceOper.<Integer>attr(LIAS_TYPE_ID.class)));
            newLiasDebt.setDebtStartDate(liasFinanceOper.<LocalDate>attr(LIAS_START_DATE.class));
            newLiasDebt.setDebtFinalDate(liasFinanceOper.<LocalDate>attr(LIAS_FINAL_DATE.class));

        });
    }

    //==========================================================================
    // изменение задолженностей или обязательств через операции над контрактом
    public void applyNewLiasOpers(
            AbstractEntityContract abstractEntityContract,
            Collection<LiasFinanceOper> opers) {

        NullSafe.create(abstractEntityContract.getContractDebts())
                .whenIsNull(() -> {

                    abstractEntityContract.setContractDebts(ServiceFuncs.<LiasDebt>getOrCreateCollection(ServiceFuncs.COLLECTION_NULL));
                })
                .safeExecute(() -> {
                    opers
                            .stream()
                            //.filter(s -> (s.<BigDecimal>attr(LiasOpersConst.LIAS_SUMM_CLASS)).compareTo(BigDecimal.ZERO) != 0)
                            .sorted((ld1, ld2) -> {
                                // сортировка по номеру операции в списке
                                return (ld1.<Integer>attr(ROW_NUM.class)).compareTo(ld2.<Integer>attr(ROW_NUM.class));
                            })
                            .forEach(operation -> {

                                // ищем нужную задолженность по типу задолженности
                                // задолженность не найдена, создаем новую задолженность
                                final int operDirection = operation.<BigDecimal>attr(LiasOpersConst.LIAS_SUMM_CLASS).signum();

                                final LiasDebt liasDebt = NullSafe.create(this.findLiasDebt(abstractEntityContract, operation, operDirection < 0))
                                        .whenIsNull(() -> {
                                            final LiasDebt newLiasDebt = this.createLiasDebt(abstractEntityContract, operation);
                                            // добавили новую задолженность
                                            abstractEntityContract.getContractDebts()
                                                    .add(newLiasDebt);
                                            return newLiasDebt;
                                            // добавление
                                        })
                                        .<LiasDebt>getObject();
                                // создали или увеличили/уменьшили обязательство
                                liasDebt.createOrUpdateLiases(operation);

                                // обновляем остатки по обязательству
                                liasDebt.createOrUpdateDebtRests(operation);
                            });
                }).throwException();

        // изменить остатки по задолженностям и обязательствам
        //this.updateRests();
    }

    //==========================================================================
    private LiasDebt findLiasDebt(
            AbstractEntityContract abstractEntityContract,
            LiasFinanceOper liasFinanceOper, 
            Boolean throwExcWhenNotFound) {

        final FilterComparator<LiasDebt> ldCmp = d -> ((d.getLiasKind().getLiasKindId().equals(liasFinanceOper.<Integer>attr(LIAS_KIND_ID.class)))
                && (d.getCurrency().getCurrencyId().equals(liasFinanceOper.<Integer>attr(LIAS_CURRENCY_ID.class)))
                && ((NullSafe.isNull(liasFinanceOper.<Integer>attr(LIAS_TYPE_ID.class))) || d.getLiasType().getLiasTypeId().equals(liasFinanceOper.<Integer>attr(LIAS_TYPE_ID.class)))
                && ((NullSafe.isNull(liasFinanceOper.<Integer>attr(LIAS_BASE_ASSET_TYPE_ID.class))) || d.getLiasBaseAssetType().getBaseAssetTypeId().equals(liasFinanceOper.<Integer>attr(LIAS_BASE_ASSET_TYPE_ID.class)))
                && ((NullSafe.isNull(liasFinanceOper.<Integer>attr(DEBT_STATE_ID.class))) || d.getLiasDebtState().getDebtStateId().equals(liasFinanceOper.<Integer>attr(DEBT_STATE_ID.class)))
                && ((NullSafe.isNull(liasFinanceOper.<LocalDate>attr(LiasOpersConst.LIAS_START_DATE_CLASS))) || d.getDebtStartDate().equals(liasFinanceOper.<LocalDate>attr(LiasOpersConst.LIAS_START_DATE_CLASS)))
                && ((NullSafe.isNull(liasFinanceOper.<LocalDate>attr(LiasOpersConst.LIAS_FINAL_DATE_CLASS))) || d.getDebtFinalDate().equals(liasFinanceOper.<LocalDate>attr(LiasOpersConst.LIAS_FINAL_DATE_CLASS))));

        final LiasDebt ld;

        if (throwExcWhenNotFound) {
            ld = ServiceFuncs.<LiasDebt>getCollectionElement(abstractEntityContract.getContractDebts(), ldCmp)
                    .orElseThrow(() -> new RuntimeException(String.format("Задолженность не существует(liasType=%s, LiasSumm=%f, lsd=%s, lfd=%s)",
                    liasFinanceOper.<Integer>attr(LIAS_TYPE_ID.class),
                    liasFinanceOper.<BigDecimal>attr(LiasOpersConst.LIAS_SUMM_CLASS),
                    liasFinanceOper.<LocalDate>attr(LiasOpersConst.LIAS_START_DATE_CLASS),
                    liasFinanceOper.<LocalDate>attr(LiasOpersConst.LIAS_FINAL_DATE_CLASS))));
        } else {
            ld = ServiceFuncs.<LiasDebt>getCollectionElement(abstractEntityContract.getContractDebts(), ldCmp)
                    .orElse(null);
        }

        return ld;

    }
    //==========================================================================

    private LiasDebt findLiasDebt(
            AbstractEntityContract abstractEntityContract,
            Integer lias_type_id,
            Integer lias_kind_id,
            Integer base_asset_type_id,
            Integer debt_state_id,
            Integer currency_id,
            LocalDate debtStartDate,
            LocalDate debtFinalDate,
            Boolean throwExcWhenNotFound) {

        final FilterComparator<LiasDebt> filterComparator = d -> (d.getLiasKind().getLiasKindId().equals(lias_kind_id)
                && d.getLiasType().getLiasTypeId().equals(lias_type_id)
                && d.getLiasBaseAssetType().getBaseAssetTypeId().equals(base_asset_type_id)
                && d.getLiasDebtState().getDebtStateId().equals(debt_state_id)
                && d.getCurrency().getCurrencyId().equals(currency_id)
                && d.getDebtStartDate().equals(debtStartDate)
                && d.getDebtFinalDate().equals(debtFinalDate));

        final Optional<LiasDebt> ld = ServiceFuncs.<LiasDebt>getCollectionElement(abstractEntityContract.getContractDebts(), filterComparator);

        if (!ld.isPresent() && throwExcWhenNotFound) {
            throw new RuntimeException(String.format("Задолженность не существует(liasType=%d)", lias_type_id));
        }

        return ld.get();

    }

    //==========================================================================
//    public Collection<TariffAccretionHisory> getAccretionHistory(
//            AbstractEntityContract abstractEntityContract) {
//        NullSafe.create(abstractEntityContract.accretionHistory)
//                .whenIsNull(() -> {
//
//                    this.accretionHistory = ServiceFuncs.<TariffAccretionHisory>createCollection();
//
//                    //this.refreshAccretionHistory();
//                    return this.accretionHistory;
//                })
//                .<LiasDebt>getObject();
//        return this.accretionHistory;
//    }

    //==========================================================================
//    public void mergeAccretionHistory(
//            Collection<TariffAccretionHisory> collection) {
//        collection
//                .stream()
//                .unordered()
//                .forEach(newAccretion -> {
//
//                    // находим 
//                    final TariffAccretionHisory exists = ServiceFuncs.<TariffAccretionHisory>getCollectionElement_silent(this.getAccretionHistory(),
//                            (l -> (l.getTariff_serv_id().equals(newAccretion.getTariff_serv_id()))));
//
//                    // не найдено, необходимо добавить
//                    if (null != exists) {
//                        this.getAccretionHistory().add(newAccretion);
//                    } else {
//
//                        // заменили последнюю дату начисления
//                        final LocalDate newAccretionDate = newAccretion.getAccretion_date();
//                        final Long newLias_action_id = newAccretion.getLias_action_id();
//
//                        exists.setAccretion_date(newAccretionDate);
//                        exists.setLias_action_id(newLias_action_id);
//                    }
//
//                });
//
//        collection.clear();
//
//    }

//    public LocalDate getLastAccretionDate(Integer tariff_serv_id, Integer tariff_kind_id) {
//
//        final LocalDate lastAccretionDate;
//
//        synchronized (this) {
//
//            lastAccretionDate = ServiceFuncs.<TariffAccretionHisory>getCollectionElement_silent(this.accretionHistory,
//                    a -> a.getTariff_serv_id().equals(tariff_serv_id) && a.getTariff_kind_id().equals(tariff_kind_id))
//                    .getAccretion_date();
//        }
//
//        return lastAccretionDate;
//    }

}
