/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.tik.assist.repo;

import org.dbs24.spring.core.data.ApplicationJpaRepository;
import java.util.Collection;

import org.dbs24.tik.assist.entity.domain.DepositPayment;
import org.dbs24.tik.assist.entity.domain.DepositPaymentType;
import org.dbs24.tik.assist.entity.domain.User;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;


public interface DepositPaymentRepo extends ApplicationJpaRepository<DepositPayment, Integer>, JpaSpecificationExecutor<DepositPayment>, PagingAndSortingRepository<DepositPayment, Integer> {
 
    Collection<DepositPayment> findByUserAndDepositPaymentType(User user, DepositPaymentType depositPaymentType);
    
    Collection<DepositPayment> findByUser(User user);
    
}
