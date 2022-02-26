/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.card.pmt.repo;

import org.dbs24.card.pmt.entity.PaymentService;
import org.dbs24.spring.core.data.ApplicationJpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface PaymentServiceRepo  extends ApplicationJpaRepository<PaymentService, Integer>, JpaSpecificationExecutor<PaymentService>, PagingAndSortingRepository<PaymentService, Integer> {
    
}

