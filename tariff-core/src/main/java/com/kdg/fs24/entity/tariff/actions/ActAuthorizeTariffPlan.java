/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kdg.fs24.entity.tariff.actions;

import com.kdg.fs24.entity.core.AbstractAction;
import com.kdg.fs24.entity.core.api.ActionCodeId;
import com.kdg.fs24.entity.core.api.AllowedMethod;
import com.kdg.fs24.entity.core.api.EntityConst;
import com.kdg.fs24.entity.core.api.PreViewDialog;
import com.kdg.fs24.entity.core.api.ViewAction;
import com.kdg.fs24.references.tariffs.api.TariffConst;
import com.kdg.fs24.application.core.sysconst.SysConst;
import com.kdg.fs24.entity.tariff.AbstractTariffPlan;
import com.kdg.fs24.application.core.nullsafe.NullSafe;
import javax.persistence.Entity;

/**
 *
 * @author Козыро Дмитрий
 */
@ActionCodeId(action_code = TariffConst.ACT_AUTHORIZE_TARIFF_PLAN,
        action_name = "Утвердить тарифный план")
@ViewAction
@PreViewDialog
@AllowedMethod(action = ActAuthorizeTariffPlan.class, entity = AbstractTariffPlan.class)
public class ActAuthorizeTariffPlan extends AbstractAction<AbstractTariffPlan> {

    @Override
    protected void doUpdate() {
//        this.getPersistenceObjects().add(this.getEntity());
//        NullSafe.create()
//                .execute(() -> {
//                    super.doUpdate();
//                    this.getEntity().saveEntityInstance();
//                }).throwException();
//        this.getEntity().persistEntity();
    }
    //==========================================================================
//    public static Boolean isAllowed(AbstractTariffPlan entity) {
//
//        return (NullSafe.create()
//                .execute2result(() -> {
//                    return !entity.getIsAuthorized() && (entity.getEntityStatus().getEntity_status_id().equals(SysConst.ES_VALID));
//                }))
//                .catchException2result((e) -> {
//                    return SysConst.IS_NOT_ALLOWED_ACTION;
//                })
//                .<Boolean>getObject();
//
//    }

    //==========================================================================
//    @Override
//    protected void doUpdate() {
//        NullSafe.create()
//                .execute(() -> {
//                    this.getEntity()
//                            .getEntityMarks()
//                            .saveEntityMark(
//                                    this.getEntity().getEntity_id(),
//                                    this.actionId,
//                                    EntityConst.MR_AUTHORIZE_ENTITY,
//                                    EntityConst.MR_AUTHORIZE_ENTITY_AUTH);
//                }).throwException();
//    }
    //==========================================================================    
//    @Override
//    protected final void afterCommit() {
//        // добавили отметку в коллекцию отметок
//        this.getEntity()
//                .getEntityMarks()
//                .addEntityMark(EntityConst.MR_AUTHORIZE_ENTITY,
//                        EntityConst.MR_AUTHORIZE_ENTITY_NOT_AUTH);
//        this.getEntity()
//                .setIsAuthorized(EntityConst.IS_AUTHORIZED);
//    }
}
