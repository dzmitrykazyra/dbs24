/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kdg.fs24.retail.tariff;

import com.kdg.fs24.entity.tariff.AbstractTariffPlan;
import com.kdg.fs24.entity.core.api.EntityKindId;
import com.kdg.fs24.references.tariffs.api.TariffConst;
/**
 *
 * @author Козыро Дмитрий
 */
@EntityKindId(entity_kind_id = TariffConst.EK_TP_FOR_RETAIL_LOAN_CONTRACT,
        entity_type_id = TariffConst.ENTITY_TARIFF_PLAN,
        entity_kind_name = "Тарифный план кредитных договоров с карточкой")
public class LoanRetailTariff extends AbstractTariffPlan {

}
