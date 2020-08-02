/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kdg.fs24.entity.contracts;

import com.kdg.fs24.application.core.nullsafe.NullSafe;
import com.kdg.fs24.application.core.service.funcs.ServiceFuncs;
import com.kdg.fs24.entity.debts.LiasDebt;
import com.kdg.fs24.lias.opers.api.LiasOpersConst;
import com.kdg.fs24.lias.opers.napi.LiasFinanceOper;
import java.math.BigDecimal;
import java.util.Collection;
import com.kdg.fs24.references.tariffs.kind.TariffAccretionHisory;
import com.kdg.fs24.lias.opers.attrs.*;
import java.time.LocalDate;
import lombok.Data;
import java.util.Optional;
import com.kdg.fs24.application.core.service.funcs.FilterComparator;
import com.kdg.fs24.entity.bondschedule.PmtSchedule;
import com.kdg.fs24.references.application.currency.Currency;
import com.kdg.fs24.references.liases.baseassettype.LiasBaseAssetType;
import com.kdg.fs24.references.liases.debtstate.LiasDebtState;
import com.kdg.fs24.references.liases.kind.LiasKind;
import com.kdg.fs24.references.liases.type.LiasType;

/**
 *
 * @author Козыро Дмитрий
 */
@Data
public abstract class AbstractEntityServiceContract extends AbstractEntityContract {

    private Collection<TariffAccretionHisory> accretionHistory;

    //==========================================================================
    // изменение задолженностей или обязательств через операции над контрактом
    public void applyNewLiasOpers(final Collection<LiasFinanceOper> opers) {

        NullSafe.create(this.getContractDebts())
                .whenIsNull(() -> {

                    this.setContractDebts(ServiceFuncs.<LiasDebt>getOrCreateCollection(ServiceFuncs.COLLECTION_NULL));
                })
                .safeExecute(() -> {
                    opers
                            .stream()
                            //.filter(s -> (s.<BigDecimal>attr(LiasOpersConst.LIAS_SUMM_CLASS)).compareTo(BigDecimal.ZERO) != 0)
                            .sorted((ld1, ld2) -> {
                                // сортировка по номеру операции в списке
                                //return ld1.getRowNum().compareTo(ld2.getRowNum());
                                return (ld1.<Integer>attr(ROW_NUM.class)).compareTo(ld2.<Integer>attr(ROW_NUM.class));
                            })
                            .forEach(operation -> {

                                // ищем нужную задолженность по типу задолженности
                                // задолженность не найдена, создаем новую задолженность
                                final int operDirection = operation.<BigDecimal>attr(LiasOpersConst.LIAS_SUMM_CLASS).signum();

                                final LiasDebt liasDebt = NullSafe.create(this.findLiasDebt(operation, operDirection < 0))
                                        .whenIsNull(() -> {
                                            final LiasDebt newLiasDebt = this.createLiasDebt(operation);
                                            // добавили новую задолженность
                                            this.getContractDebts()
                                                    .add(newLiasDebt);
                                            return newLiasDebt;
                                            // добавление
                                            //this.getContactDebts().add(liasDebt);
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

//        public void store() {
//
//        //поочередное сохранение задолженности
//        NullSafe.create(this.contactDebts)
//                .safeExecute((ns_SafeContactDebts) -> {
//                    ((Collection<T>) ns_SafeContactDebts)
//                            .stream()
//                            .unordered()
//                            .forEach((liasDebt) -> {
//                                liasDebt.store(this.entity_id, this.counterparty_id);
//                                //throw new RuntimeException("");
//                            });
//                }).throwException();
//    }
//    public void load(final Long entity_id, final Long counterparty_id) {
//        LogService.LogInfo(this.getClass(), () -> LogService.getCurrentObjProcName(this));
//        this.entity_id = entity_id;
//        this.counterparty_id = counterparty_id;
//        this.setContactDebts((new ContractLiasDebtLoader()).loadLiasDebts(entity_id));
//    }
    //==========================================================================
//    public ContractDebtsImpl bind(final Collection<Document> docs) {
//
//        docs
//                .stream()
//                .unordered()
//                .forEach((document) -> {
//                    // найти и привязать документ к финансовой операции
//                    this.getContactDebts()
//                            .stream()
//                            .unordered()
//                            .forEach((debt) -> {
//
//                                final Collection<Lias> liases = debt.getLiases();
//                                liases
//                                        .stream()
//                                        .unordered()
//                                        .forEach((lias) -> {
//                                            lias.getLiasActions()
//                                                    .stream()
//                                                    .unordered()
//                                                    .filter(action -> action.getLiasOperHC() == document.getLiasOperHC())
//                                                    .forEach((action) -> {
//                                                        action.setDoc_id(document.getDocId());
//                                                    });
//                                        });
//                            });
//                });
//
//        return this;
//    }
    //==========================================================================
    private LiasDebt createLiasDebt(final LiasFinanceOper liasFinanceOper) {
        //LogService.LogInfo(this.getClass(), () -> LogService.getCurrentObjProcName(this));

        final LiasDebt newLiasDebt = new LiasDebt();

        newLiasDebt.setCurrency(Currency.findCurrency(liasFinanceOper.<Integer>attr(LIAS_CURRENCY_ID.class)));

        newLiasDebt.setLiasBaseAssetType(LiasBaseAssetType.findLiasBaseAssetType(liasFinanceOper.<Integer>attr(LIAS_BASE_ASSET_TYPE_ID.class)));
        newLiasDebt.setCounterparty(this.getCounterparty());
        newLiasDebt.setDebtContract(this);
        newLiasDebt.setLiasDebtState(LiasDebtState.findLiasDebtState(liasFinanceOper.<Integer>attr(DEBT_STATE_ID.class)));
        newLiasDebt.setLiasKind(LiasKind.findLiasKind(liasFinanceOper.<Integer>attr(LIAS_KIND_ID.class)));
        newLiasDebt.setLiasType(LiasType.findLiasType(liasFinanceOper.<Integer>attr(LIAS_TYPE_ID.class)));
        newLiasDebt.setDebtStartDate(liasFinanceOper.<LocalDate>attr(LIAS_START_DATE.class));
        newLiasDebt.setDebtFinalDate(liasFinanceOper.<LocalDate>attr(LIAS_FINAL_DATE.class));

        return newLiasDebt;
    }

    //==========================================================================
    private LiasDebt findLiasDebt(final LiasFinanceOper liasFinanceOper, final Boolean throwExcWhenNotFound) {

        final FilterComparator<LiasDebt> ldCmp = d -> ((d.getLiasKind().getLiasKindId().equals(liasFinanceOper.<Integer>attr(LIAS_KIND_ID.class)))
                && (d.getCurrency().getCurrencyId().equals(liasFinanceOper.<Integer>attr(LIAS_CURRENCY_ID.class)))
                && ((NullSafe.isNull(liasFinanceOper.<Integer>attr(LIAS_TYPE_ID.class))) || d.getLiasType().getLiasTypeId().equals(liasFinanceOper.<Integer>attr(LIAS_TYPE_ID.class)))
                && ((NullSafe.isNull(liasFinanceOper.<Integer>attr(LIAS_BASE_ASSET_TYPE_ID.class))) || d.getLiasBaseAssetType().getBaseAssetTypeId().equals(liasFinanceOper.<Integer>attr(LIAS_BASE_ASSET_TYPE_ID.class)))
                && ((NullSafe.isNull(liasFinanceOper.<Integer>attr(DEBT_STATE_ID.class))) || d.getLiasDebtState().getDebtStateId().equals(liasFinanceOper.<Integer>attr(DEBT_STATE_ID.class)))
                && ((NullSafe.isNull(liasFinanceOper.<LocalDate>attr(LiasOpersConst.LIAS_START_DATE_CLASS))) || d.getDebtStartDate().equals(liasFinanceOper.<LocalDate>attr(LiasOpersConst.LIAS_START_DATE_CLASS)))
                && ((NullSafe.isNull(liasFinanceOper.<LocalDate>attr(LiasOpersConst.LIAS_FINAL_DATE_CLASS))) || d.getDebtFinalDate().equals(liasFinanceOper.<LocalDate>attr(LiasOpersConst.LIAS_FINAL_DATE_CLASS))));

        final LiasDebt ld;

        if (throwExcWhenNotFound) {
            ld = ServiceFuncs.<LiasDebt>getCollectionElement(this.getContractDebts(), ldCmp)
                    .orElseThrow(() -> new RuntimeException(String.format("Задолженность не существует(liasType=%s, LiasSumm=%f, lsd=%s, lfd=%s)",
                    liasFinanceOper.<Integer>attr(LIAS_TYPE_ID.class),
                    liasFinanceOper.<BigDecimal>attr(LiasOpersConst.LIAS_SUMM_CLASS),
                    liasFinanceOper.<LocalDate>attr(LiasOpersConst.LIAS_START_DATE_CLASS),
                    liasFinanceOper.<LocalDate>attr(LiasOpersConst.LIAS_FINAL_DATE_CLASS))));
        } else {
            ld = ServiceFuncs.<LiasDebt>getCollectionElement(this.getContractDebts(), ldCmp)
                    .orElse(null);
        }

        return ld;

    }
    //==========================================================================

    private LiasDebt findLiasDebt(final Integer lias_type_id,
            final Integer lias_kind_id,
            final Integer base_asset_type_id,
            final Integer debt_state_id,
            final Integer currency_id,
            final LocalDate debtStartDate,
            final LocalDate debtFinalDate,
            final Boolean throwExcWhenNotFound) {

        final FilterComparator<LiasDebt> filterComparator = d -> (d.getLiasKind().getLiasKindId().equals(lias_kind_id)
                && d.getLiasType().getLiasTypeId().equals(lias_type_id)
                && d.getLiasBaseAssetType().getBaseAssetTypeId().equals(base_asset_type_id)
                && d.getLiasDebtState().getDebtStateId().equals(debt_state_id)
                && d.getCurrency().getCurrencyId().equals(currency_id)
                && d.getDebtStartDate().equals(debtStartDate)
                && d.getDebtFinalDate().equals(debtFinalDate));

        final Optional<LiasDebt> ld = ServiceFuncs.<LiasDebt>getCollectionElement(this.getContractDebts(), filterComparator);

        if (!ld.isPresent() && throwExcWhenNotFound) {
            throw new RuntimeException(String.format("Задолженность не существует(liasType=%d)", lias_type_id));
        }

        return ld.get();

    }

    //==========================================================================
    public Collection<TariffAccretionHisory> getAccretionHistory() {
        NullSafe.create(this.accretionHistory)
                .whenIsNull(() -> {

                    this.accretionHistory = ServiceFuncs.<TariffAccretionHisory>createCollection();

                    //this.refreshAccretionHistory();
                    return this.accretionHistory;
                })
                .<LiasDebt>getObject();
        return this.accretionHistory;
    }

    //==========================================================================
//    private void refreshAccretionHistory() {
//        // загрузить список коллекций
//
//        synchronized (this) {
//
//            //this.accretionHistory.clear();
//            this.getDbService()
//                    .createRsCall("{call tariff_get_accretion_history(:ENT)}")
//                    .setParamByName("ENT", this.entity_id)
//                    .<TariffAccretionHisory>fillCollection(this.accretionHistory, TariffAccretionHisory.class);
//        }
//    }
    //==========================================================================
    public void mergeAccretionHistory(final Collection<TariffAccretionHisory> collection) {
        collection
                .stream()
                .unordered()
                .forEach(newAccretion -> {

                    // находим 
                    final TariffAccretionHisory exists = ServiceFuncs.<TariffAccretionHisory>getCollectionElement_silent(this.getAccretionHistory(),
                            (l -> (l.getTariff_serv_id().equals(newAccretion.getTariff_serv_id()))));

                    // не найдено, необходимо добавить
                    if (null != exists) {
                        this.getAccretionHistory().add(newAccretion);
                    } else {

                        // заменили последнюю дату начисления
                        final LocalDate newAccretionDate = newAccretion.getAccretion_date();
                        final Long newLias_action_id = newAccretion.getLias_action_id();

                        exists.setAccretion_date(newAccretionDate);
                        exists.setLias_action_id(newLias_action_id);
                    }

                });

        collection.clear();

    }

    public LocalDate getLastAccretionDate(final Integer tariff_serv_id, final Integer tariff_kind_id) {

        final LocalDate lastAccretionDate;

        synchronized (this) {

            lastAccretionDate = ServiceFuncs.<TariffAccretionHisory>getCollectionElement_silent(this.accretionHistory,
                    a -> a.getTariff_serv_id().equals(tariff_serv_id) && a.getTariff_kind_id().equals(tariff_kind_id))
                    .getAccretion_date();
        }

        return lastAccretionDate;
    }

    //==========================================================================
//    public void insertAccretionDate(
//            final Integer tariff_serv_id,
//            final Integer tariff_kind_id,
//            final LocalDate accretion_date,
//            final Long lias_action_id) {
//
//        this.getDbService()
//                .createCallQuery("{call tariff_insertorupdate_tariff_accretion(:AD, :TS, :TK, :ENT, :LI)}")
//                .setParamByName("AD", accretion_date)
//                .setParamByName("TS", tariff_serv_id)
//                .setParamByName("TK", tariff_kind_id)
//                .setParamByName("ENT", entity_id)
//                .setParamByName("LI", lias_action_id)
//                .execCallStmt();
//    }
    //==========================================================================
    public void createBondschedules() {
        if (NullSafe.notNull(this.getPmtSchedules())) {
            this.getPmtSchedules().clear();
        } else {
            this.setPmtSchedules(ServiceFuncs.<PmtSchedule>getOrCreateCollection(ServiceFuncs.COLLECTION_NULL));
        }

    }
}
