/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kdg.fs24.entity.tariff.actions;

import com.kdg.fs24.entity.core.AbstractAction;
import com.kdg.fs24.entity.core.api.ActionCodeId;
import com.kdg.fs24.entity.core.api.AllowedMethod;
import com.kdg.fs24.entity.core.api.EntityWarningsList;
//import com.kdg.fs24.exception.api.EntityWarningsException;
//import com.kdg.fs24.application.core.exception.api.InternalAppException;
import com.kdg.fs24.references.tariffs.api.TariffConst;
import com.kdg.fs24.application.core.sysconst.SysConst;
import com.kdg.fs24.entity.tariff.AbstractTariffPlan;
import lombok.Data;

/**
 *
 * @author Козыро Дмитрий
 */
//@Entity
//@Table(name = "tariffplans")
@ActionCodeId(action_code = TariffConst.ACT_MODIFY_TARIFF_PLAN,
        action_name = "Редактировать тарифный план")
@Data
//@AllowedMethod(action = ActModifyTariffPlan.class, entity = AbstractTariffPlan.class)
public class ActModifyTariffPlan extends AbstractAction<AbstractTariffPlan> {

    //==========================================================================
    public static Boolean isAllowed(AbstractTariffPlan entity) {

        return (AbstractAction.isAllowed(entity, SysConst.ES_VALID));
    }

    //==========================================================================
    @Override
    public void doUpdate() {
        // this.getPersistenceObjects().add(this.getEntity());
//        NullSafe.create()
//                .execute(() -> {
//                    super.doUpdate();
//                    this.getEntity().saveEntityInstance();
//                }).throwException();
//        this.getEntity().persistEntity();
    }
}
