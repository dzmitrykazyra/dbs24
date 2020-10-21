/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.tariff;

import org.dbs24.entity.tariff.AbstractTariffPlan;
import org.dbs24.entity.core.api.EntityKindId;
import org.dbs24.consts.TariffConst;
/**
 *
 * @author Козыро Дмитрий
 */
@EntityKindId(entity_kind_id = TariffConst.EK_TP_FOR_RETAIL_LOAN_CONTRACT,
        entity_type_id = TariffConst.ENTITY_TARIFF_PLAN,
        entity_kind_name = "Тарифный план кредитных договоров с карточкой")
public class LoanRetailTariff extends AbstractTariffPlan {

}
