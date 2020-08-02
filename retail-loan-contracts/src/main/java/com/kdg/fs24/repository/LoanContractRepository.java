/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kdg.fs24.repository;

import com.kdg.fs24.spring.core.api.ApplicationReactiveRepository;
import com.kdg.fs24.spring.core.api.ApplicationJpaRepository;
import com.kdg.fs24.entity.retail.loan.contracts.RetailLoanContract;

/**
 *
 * @author Козыро Дмитрий
 */
//public interface LoanContractRepository extends ApplicationReactiveRepository<RetailLoanContract, Long> {
public interface LoanContractRepository extends ApplicationJpaRepository<RetailLoanContract, Long> {

}
