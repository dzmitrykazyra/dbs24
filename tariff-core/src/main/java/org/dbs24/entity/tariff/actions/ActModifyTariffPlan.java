/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.entity.tariff.actions;

import org.dbs24.entity.core.AbstractAction;
import org.dbs24.entity.core.api.ActionCodeId;
import org.dbs24.entity.core.api.AllowedMethod;
import org.dbs24.entity.core.api.EntityWarningsList;
//import org.dbs24.exception.api.EntityWarningsException;
//import org.dbs24.application.core.exception.api.InternalAppException;
import org.dbs24.consts.TariffConst;
import static org.dbs24.application.core.sysconst.SysConst.*;
import org.dbs24.entity.tariff.AbstractTariffPlan;
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

        return (AbstractAction.isAllowed(entity, ES_VALID));
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
