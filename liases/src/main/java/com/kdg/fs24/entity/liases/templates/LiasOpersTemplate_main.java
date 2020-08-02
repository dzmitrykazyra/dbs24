/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kdg.fs24.entity.liases.templates;

import com.kdg.fs24.lias.opers.api.LiasOperTemplateId;
import com.kdg.fs24.lias.opers.napi.LiasOper2Tariff;
import com.kdg.fs24.references.api.LiasesConst;
import com.kdg.fs24.references.tariffs.api.TariffConst;

/**
 *
 * @author kazyra_d
 */
@LiasOperTemplateId(template_id = 1)
@LiasOper2Tariff(tariff_serv_id = TariffConst.TS_MAIN_PERCENTS,
        debt_state_id = LiasesConst.LDS_NORMAL_DEBTS,
        fin_oper_code = LiasesConst.FOC_MAIN_ACCRETION,
        lias_action_type = LiasesConst.LAT_INCREMENT_LIASES,
        lias_kind_id = LiasesConst.LKI_PERC_PAY_REQUIREMENT,
        lias_type_id = LiasesConst.LTI_PAYMENT_LIASES,
        base_asset_type_id = LiasesConst.LBAT_MONEYS)
@LiasOper2Tariff(tariff_serv_id = TariffConst.TS_CASHBACK,
        debt_state_id = LiasesConst.LDS_NORMAL_DEBTS,
        fin_oper_code = LiasesConst.FOC_MAIN_ACCRETION,
        lias_action_type = LiasesConst.LAT_INCREMENT_LIASES,
        lias_kind_id = LiasesConst.LKI_PERC_PAY_REQUIREMENT,
        lias_type_id = LiasesConst.LTI_PAYMENT_LIASES,
        base_asset_type_id = LiasesConst.LBAT_MONEYS)

//@LiasOper2Tariff(debtStateID = 2)
//@LiasOper2Tariff(debtStateID = 3)
public abstract class LiasOpersTemplate_main extends AbstractLiasOpersTemplate {

}
