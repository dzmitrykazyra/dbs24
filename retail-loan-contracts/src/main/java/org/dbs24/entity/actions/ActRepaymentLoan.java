/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.entity.actions;

import org.dbs24.stmt.StmtProcessor;
import static org.dbs24.consts.BondScheduleConst.*;
import java.math.BigDecimal;
import org.dbs24.entity.core.api.ActionCodeId;
import org.dbs24.entity.AbstractRetailLoanContract;
import org.dbs24.lias.opers.attrs.*;
import org.dbs24.consts.RetailLoanContractConst;
import org.dbs24.lias.opers.napi.LiasFinanceOper;
import static org.dbs24.references.api.LiasesConst.*;
import lombok.Data;

/**
 *
 * @author Козыро Дмитрий
 */
@Data
@ActionCodeId(action_code = RetailLoanContractConst.ACT_REPAYMENT_LOAN,
        action_name = "Погашение кредита")
public class ActRepaymentLoan extends AbstractLiasContractOper<AbstractRetailLoanContract> {

    // сумма выдачи кредита
    private BigDecimal liasRepaymentSum;

    //==========================================================================
    @Override
    protected void preCalculation() {
        this.addNewLiasOper(StmtProcessor.create(LiasFinanceOper.class, oper -> oper
                .<LIAS_SUMM>addAttr(() -> this.getLiasRepaymentSum())
                .<LIAS_CURRENCY_ID>addAttr(() -> this.getContractEntity().getCurrency().getCurrencyId())
                .<COUNTERPARTY_ID>addAttr(() -> this.getContractEntity().entityId())
                .<LIAS_DATE>addAttr(() -> this.getLiasDate())
                .<DEBT_STATE_ID>addAttr(() -> LDS_NORMAL_DEBTS)
                .<LIAS_FINOPER_CODE>addAttr(() -> FOC_MAIN_PLACEMENT)
                .<LIAS_ACTION_TYPE_ID>addAttr(() -> LAT_GET_PRIMARY_LIASES)
                .<LIAS_KIND_ID>addAttr(() -> LKI_RETURN_MAIN_DEBT)
                .<LIAS_TYPE_ID>addAttr(() -> LTI_CURRENT_LIASES)
                .<LIAS_BASE_ASSET_TYPE_ID>addAttr(() -> LBAT_MONEYS)
                .<LIAS_START_DATE>addAttr(() -> this.getContractEntity().getBeginDate())
                .<LIAS_FINAL_DATE>addAttr(() -> this.getContractEntity().getEndDate())
                .<PMT_SCHEDULE>addAttr(() -> EK_BONDSCHEDULE_MAIN_DEBT)
                .<OPER_NOTES>addAttr(() -> "Repayment loann")
        ));
    }

}
