/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kdg.fs24.entity.service;

import com.kdg.fs24.entity.core.AbstractAction;
import com.kdg.fs24.entity.core.api.ActionCodeId;
import com.kdg.fs24.entity.core.api.AlwaysAllowedAction;
import com.kdg.fs24.entity.core.api.EntityContractConst;
//import com.kdg.fs24.services.api.ServiceLocator;
//import com.kdg.fs24.services.FS24JdbcService;
import com.kdg.fs24.application.core.nullsafe.NullSafe;

/**
 *
 * @author kazyra_d
 */
@ActionCodeId(action_code = EntityContractConst.ACT_RESET_DEFAULT_ENTITY_FIELDS,
        action_name = "Сбросить значение поля по умолчанию")
@AlwaysAllowedAction
public class ActResetDefaultEntityFields extends AbstractAction {

//    @Override
//    protected void doUpdate() {
//        NullSafe.create()
//                .execute(() -> {
//                    ServiceLocator
//                            .find(FS24JdbcService.class)
//                            .createCallQuery("{call core_reset_appfieldscaptions(:U, :A)} ")
//                            .setParamByName("U", ((ServiceEntity) this.getEntity()).getUserId())
//                            .setParamByName("A", ((ServiceEntity) this.getEntity()).getAppId())
//                            .execCallStmt();
//                }).throwException();
//    }
}
