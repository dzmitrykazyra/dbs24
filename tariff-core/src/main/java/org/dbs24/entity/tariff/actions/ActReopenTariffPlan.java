/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.entity.tariff.actions;

import org.dbs24.entity.actions.ActAbstractReopenEntity;
//import org.dbs24.entity.core.AbstractEntityImpl;
import org.dbs24.entity.core.api.ActionCodeId;
import org.dbs24.entity.core.api.AllowedMethod;
import org.dbs24.entity.core.api.PreViewDialog;
import org.dbs24.entity.core.api.ViewAction;
import org.dbs24.consts.TariffConst;
import org.dbs24.entity.tariff.AbstractTariffPlan;
import javax.persistence.Entity;

/**
 *
 * @author Козыро Дмитрий
 */
//@Entity
@ActionCodeId(action_code = TariffConst.ACT_REOPEN_TARIFF_PLAN,
        action_name = "Открытие закрытого тарифного плана")
//@ViewAction
//@PreViewDialog
//@AllowedMethod(action = ActAbstractReopenEntity.class, entity = AbstractEntityImpl.class)
public class ActReopenTariffPlan extends ActAbstractReopenEntity<AbstractTariffPlan> {

}
