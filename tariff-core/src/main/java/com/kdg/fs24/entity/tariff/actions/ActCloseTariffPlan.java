/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kdg.fs24.entity.tariff.actions;

import com.kdg.fs24.entity.actions.ActAbstractCloseEntity;
//import com.kdg.fs24.entity.core.AbstractEntityImpl;
import com.kdg.fs24.entity.core.api.ActionCodeId;
import com.kdg.fs24.entity.core.api.AllowedMethod;
import com.kdg.fs24.entity.core.api.PreViewDialog;
import com.kdg.fs24.entity.core.api.ViewAction;
import com.kdg.fs24.references.tariffs.api.TariffConst;
import com.kdg.fs24.entity.tariff.AbstractTariffPlan;
import javax.persistence.Entity;

/**
 *
 * @author Козыро Дмитрий
 */
//@Entity
@ActionCodeId(action_code = TariffConst.ACT_CLOSE_TARIFF_PLAN,
        action_name = "Закрыть тарифный план")
//@ViewAction
//@PreViewDialog
//@AllowedMethod(action = ActAbstractCloseEntity.class, entity = AbstractEntityImpl.class)
public class ActCloseTariffPlan extends ActAbstractCloseEntity<AbstractTariffPlan> {

}
