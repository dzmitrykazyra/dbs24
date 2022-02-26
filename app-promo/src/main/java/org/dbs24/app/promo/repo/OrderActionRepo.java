/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.app.promo.repo;

import org.dbs24.app.promo.entity.ActionResult;
import org.dbs24.app.promo.entity.Order;
import org.dbs24.app.promo.entity.OrderAction;
import org.dbs24.spring.core.data.ApplicationJpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Collection;

public interface OrderActionRepo extends ApplicationJpaRepository<OrderAction, Integer>, PagingAndSortingRepository<OrderAction, Integer>, JpaSpecificationExecutor<OrderAction> {

    Collection<OrderAction> findByOrder(Order order);

    Collection<OrderAction> findByOrderAndActionResult(Order order, ActionResult actionResult);

}
