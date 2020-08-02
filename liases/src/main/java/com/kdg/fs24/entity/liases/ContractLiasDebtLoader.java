/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kdg.fs24.entity.liases;

import com.kdg.fs24.application.core.service.funcs.ServiceFuncs;
//import com.kdg.fs24.entity.liases.references.LiasesReferencesService;
import com.kdg.fs24.application.core.api.ObjectRoot;
//import com.kdg.fs24.references.common.AppReferencesListService;
//import com.kdg.fs24.services.api.ServiceLocator;
import java.util.ArrayList;
import java.util.Collection;
import lombok.Data;

/**
 *
 * @author kazyra_d
 */
@Data
public class ContractLiasDebtLoader extends ObjectRoot {
//
//    //==========================================================================
//    public Collection<T> loadLiasDebts(final Long entity_id) {
//
//        //List<T> liasDebts = new ArrayList<>();
//        final Collection<T> liasDebts = ServiceFuncs.<T>createCollection();
//
//        // список задолженностей по договору
//        this.getDbService()
//                .createRsCall("{call liases_get_debts(:ENT)}")
//                .setParamByName("ENT", entity_id)
//                .execRsCallStmt((qryLiasDebts) -> {
//
//                    // заполняем задолженности
//                    while (qryLiasDebts.next()) {
//
//                        liasDebts.add((T) new LiasDebt(
//                                qryLiasDebts.getInteger("debt_id"),
//                                ServiceLocator.find(AppReferencesListService.class).getCurrency(qryLiasDebts.getInteger("currency_id")),
//                                ServiceLocator.find(LiasesReferencesService.class).getLiasDebtStateById(qryLiasDebts.getInteger("debt_state_id")),
//                                ServiceLocator.find(LiasesReferencesService.class).getLiasKindById(qryLiasDebts.getInteger("lias_kind_id")),
//                                ServiceLocator.find(LiasesReferencesService.class).getLiasTypeById(qryLiasDebts.getInteger("lias_type_id")),
//                                ServiceLocator.find(LiasesReferencesService.class).getBaseAssetTypeById(qryLiasDebts.getInteger("base_asset_type_id")),
//                                qryLiasDebts.getLocalDate("debt_start_date"),
//                                qryLiasDebts.getLocalDate("debt_final_date")
//                        ));
//                    }
//                });
//
//        return liasDebts;
//    }
}
