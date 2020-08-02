/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.repository;

import org.dbs24.spring.core.api.ApplicationReactiveRepository;
import org.dbs24.spring.core.api.ApplicationJpaRepository;
import org.dbs24.entity.retail.loan.contracts.RetailLoanContract;

/**
 *
 * @author Козыро Дмитрий
 */
//public interface LoanContractRepository extends ApplicationReactiveRepository<RetailLoanContract, Long> {
public interface LoanContractRepository extends ApplicationJpaRepository<RetailLoanContract, Long> {

}
