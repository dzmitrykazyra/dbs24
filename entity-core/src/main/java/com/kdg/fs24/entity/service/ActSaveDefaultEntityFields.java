/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kdg.fs24.entity.service;

import com.kdg.fs24.entity.core.AbstractAction;
import com.kdg.fs24.entity.core.api.ActionCodeId;
import com.kdg.fs24.entity.core.api.AlwaysAllowedAction;
//import com.kdg.fs24.fields.desc.FieldDescriptionImpl;
import com.kdg.fs24.entity.core.api.EntityContractConst;
//import com.kdg.fs24.services.api.ServiceLocator;
//import com.kdg.fs24.services.FS24JdbcService;
//import com.kdg.fs24.application.core.nullsafe.NullSafe;

/**
 *
 * @author kazyra_d
 */
@ActionCodeId(action_code = EntityContractConst.ACT_SAVE_DEFAULT_ENTITY_FIELDS,
        action_name = "Сохранить настроки полей сущности")
@AlwaysAllowedAction
public class ActSaveDefaultEntityFields extends AbstractAction {

//    @Override
//    protected void doUpdate() {
//        //this.getEntity().saveEntityInstance();
//        // сохраняем измененные поля
//        //ent_save_appfieldscaptions(p_user_id tidcode, p_app_id tidcode, p_field_name tstr100, p_field_caption tstr100, p_field_tooltip tstr100)
//        NullSafe.create()
//                .execute(() -> {
//                    ServiceEntity serviceEntity = (ServiceEntity) this.getEntity();
//
//                    ServiceLocator
//                            .find(FS24JdbcService.class)
//                            .createCallBath("{call core_save_appfields_captions(:U,:A,:N,:C,:T)}")
//                            .execBatch(stmt -> {
//
//                                for (FieldDescriptionImpl fd : serviceEntity.getSettings()) {
//                                    stmt.setParamByName("U", serviceEntity.getUserId());
//                                    stmt.setParamByName("A", serviceEntity.getAppId());
//                                    stmt.setParamByName("N", fd.getField_name());
//                                    stmt.setParamByName("C", fd.getField_caption());
//                                    stmt.setParamByName("T", fd.getField_tooltip());
//                                    stmt.addBatch();
//                                }
//                            });
//                }).throwException();
//    }
}
