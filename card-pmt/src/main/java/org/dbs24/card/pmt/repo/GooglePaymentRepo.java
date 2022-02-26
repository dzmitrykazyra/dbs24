/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.card.pmt.repo;

import org.dbs24.card.pmt.entity.GooglePayment;
import org.dbs24.spring.core.data.ApplicationJpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Collection;


public interface GooglePaymentRepo extends ApplicationJpaRepository<GooglePayment, Integer>, JpaSpecificationExecutor<GooglePayment> {
    Collection<GooglePayment> findByGooglePurchaseToken(String googlePurchaseToken);

    Collection<GooglePayment> findByGoogleOrderId(String googleOrderId);
}
