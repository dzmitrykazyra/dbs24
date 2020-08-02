/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kdg.fs24.entity.retail.loan.actions;

import com.kdg.fs24.entity.contracts.actions.ActCreateOrUpdateContract;
import com.kdg.fs24.entity.core.api.ActionCodeId;
import com.kdg.fs24.entity.retail.loan.contracts.AbstractRetailLoanContract;
import com.kdg.fs24.consts.RetailLoanContractConst;
import com.kdg.fs24.entity.core.api.RefreshEntity;
import lombok.Data;

/**
 *
 * @author Козыро Дмитрий
 */
@Data
@ActionCodeId(action_code = RetailLoanContractConst.MODIFY_INDIVIDUAL_LOAN_CONTRACT,
        action_name = "Редактировать кредитную сделку с физическим лицом")
@RefreshEntity
public class ActCreateOrUpdateLoanContract extends ActCreateOrUpdateContract<AbstractRetailLoanContract> {

}
