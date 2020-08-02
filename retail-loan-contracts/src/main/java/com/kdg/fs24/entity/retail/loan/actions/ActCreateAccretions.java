/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kdg.fs24.entity.retail.loan.actions;

/**
 *
 * @author Козыро Дмитрий
 */
import com.kdg.fs24.entity.contracts.actions.ActCreateOrUpdateContract;
import com.kdg.fs24.entity.core.api.ActionCodeId;
import com.kdg.fs24.entity.retail.loan.contracts.AbstractRetailLoanContract;
import com.kdg.fs24.consts.RetailLoanContractConst;
import lombok.Data;

@Data
@ActionCodeId(action_code = RetailLoanContractConst.ACT_CREATE_ACCRETION,
        action_name = "Начисление процентных и комиссионных доходов")
public class ActCreateAccretions extends ActCreateOrUpdateContract<AbstractRetailLoanContract> {
    
}
