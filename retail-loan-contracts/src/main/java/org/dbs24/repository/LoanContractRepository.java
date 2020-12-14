/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.repository;


import org.dbs24.spring.core.data.ApplicationJpaRepository;
import org.dbs24.entity.RetailLoanContract;


//public interface LoanContractRepository extends ApplicationReactiveRepository<RetailLoanContract, Long> {
public interface LoanContractRepository extends ApplicationJpaRepository<RetailLoanContract, Long> {

}
