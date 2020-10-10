/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.consts;

import org.dbs24.entity.RetailLoanContract;

/**
 *
 * @author Козыро Дмитрий
 */
public final class RetailLoanContractConst {

    public static final int LOAN2INDIVIDUAL = 2100; // кредит физическому лицу
    public static final int LOAN2LEGAL_ENTITY = 2200; // кредит юридическому лицу
    public static final int LOAN2BANK = 2300; // кредит межбанковский

    public static final int LOAN2INDIVIDUAL_CARD = 21000010; // Кредит ФЛ с выдачей на карточку
    public static final int LOAN2INDIVIDUAL_OVER = 21000020; // Кредит ФЛ в форме овердрафта
    public static final int LOAN2INDIVIDUAL_EMPL = 21000030; // Кредит сотруднику банка
    //==========================================================================
    public static final int MODIFY_INDIVIDUAL_LOAN_CONTRACT = 101100001; // 
    public static final int ACT_ISSUE_LOAN = 101200002; // выдача кредита
    public static final int ACT_CREATE_ACCRETION = 101200001; // начисление
    public static final int ACT_REPAYMENT_LOAN = 101200010; // погашение кредита
    public static final int ACT_ADJUST_LIASES = 101300001; // 
    public static final int MODIFY_INTERBANK_PLACED_LOAN_CONTRACT = 101400001;
    //==========================================================================
    public static final int LS_OWN_MONEY = 100;
    public static final int LS_OWN_MONEY_NEW = 101;

    public static final String REST_RETAIL_LOAN_REPAYMENT = "/retailLoanRepeyment";
    
    public static final Class<RetailLoanContract> RETAIL_LOAN_CONTRACT_CLASS = RetailLoanContract.class;
}
