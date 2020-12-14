/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.entity.actions;

import org.dbs24.application.core.locale.NLS;
import org.dbs24.application.core.nullsafe.NullSafe;
import org.dbs24.entity.AbstractRetailLoanContract;
import org.dbs24.consts.RetailLoanContractConst;
import org.dbs24.entity.core.api.ActionCodeId;
import org.dbs24.lias.opers.attrs.*;
import org.dbs24.lias.opers.napi.LiasFinanceOper;
import java.math.BigDecimal;
import lombok.Data;
import org.dbs24.references.api.LiasesConst;
import static org.dbs24.consts.BondScheduleConst.*;
import org.dbs24.repository.LoanContractRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author Козыро Дмитрий
 */
@Data
@ActionCodeId(action_code = RetailLoanContractConst.ACT_ISSUE_LOAN,
        action_name = "Выдача кредита")
//@Component
public class ActIssueLoan extends AbstractLiasContractOper<AbstractRetailLoanContract> {

    @Autowired
    private LoanContractRepository loanContractRepository;    
    
    // сумма выдачи кредита
    private BigDecimal liasIssueSum;

    @Override
    public void initialize() {
        // запрос даты расчета операции        
        //this.setAccretionDate(null);
        super.initialize();
        // параметры из запроса
        this.setLiasDate(NLS.string2LocalDate(this.getMvmParam(LIAS_DATE.class)));
        this.setLiasIssueSum(new BigDecimal((this.getMvmParam(LIAS_SUMM.class))));
        
    }

    //==========================================================================
    @Override
    protected void preCalculation() {
        this.addNewLiasOper(NullSafe.createObject(LiasFinanceOper.class)
                .<LIAS_SUMM>addAttr(() -> this.getLiasIssueSum())
                .<LIAS_CURRENCY_ID>addAttr(() -> this.getContractEntity().getCurrency().getCurrencyId())
                .<COUNTERPARTY_ID>addAttr(() -> this.getContractEntity().entityId())
                .<LIAS_DATE>addAttr(() -> this.getLiasDate())
                .<DEBT_STATE_ID>addAttr(() -> LiasesConst.LDS_NORMAL_DEBTS)
                .<LIAS_FINOPER_CODE>addAttr(() -> LiasesConst.FOC_MAIN_PLACEMENT)
                .<LIAS_ACTION_TYPE_ID>addAttr(() -> LiasesConst.LAT_GET_PRIMARY_LIASES)
                .<LIAS_KIND_ID>addAttr(() -> LiasesConst.LKI_RETURN_MAIN_DEBT)
                .<LIAS_TYPE_ID>addAttr(() -> LiasesConst.LTI_CURRENT_LIASES)
                .<LIAS_BASE_ASSET_TYPE_ID>addAttr(() -> LiasesConst.LBAT_MONEYS)
                .<LIAS_START_DATE>addAttr(() -> this.getContractEntity().getBeginDate())
                .<LIAS_FINAL_DATE>addAttr(() -> this.getContractEntity().getEndDate())
                .<PMT_SCHEDULE>addAttr(() -> EK_BONDSCHEDULE_MAIN_DEBT)
                .<OPER_NOTES>addAttr(() -> "Issue loann")
        );

        // ещё какая-то задолженность
//        this.addNewLiasOper(new NewLiasOperImpl()
//                .<LIAS_SUMM>add(() -> BigDecimal.valueOf(100))
//                .<LIAS_CURRENCY_ID>add(() -> this.getContractEntity().getContractCurrency_id())
//                .<COUNTERPARTY_ID>add(() -> this.getContractEntity().getCounterparty_id())
//                .<LIAS_DATE>add(() -> this.getLiasDate())
//                .<DEBT_STATE_ID>add(() -> LiasesConst.LDS_NORMAL_DEBTS)
//                .<LIAS_FINOPER_CODE>add(() -> LiasesConst.FOC_1)
//                .<LIAS_ACTION_TYPE_ID>add(() -> LiasesConst.LAT_GET_PRIMARY_LIASES)
//                .<LIAS_KIND_ID>add(() -> LiasesConst.LKI_2)
//                .<LIAS_TYPE_ID>add(() -> LiasesConst.LTI_CURRENT_LIASES)
//                .<LIAS_BASE_ASSET_TYPE_ID>add(() -> LiasesConst.LBAT_MONEYS)
//                .<LIAS_START_DATE>add(() -> this.getContractEntity().getContract_date())
//                .<LIAS_FINAL_DATE>add(() -> this.getContractEntity().getEnd_date())
//                .<OPER_NOTES>add(() -> "Создание Х задолженности")
//        );
        // создание платежного документа
        //this.addDocument(document);        
    }

}
