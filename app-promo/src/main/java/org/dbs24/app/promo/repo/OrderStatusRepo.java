/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.app.promo.repo;

import org.dbs24.app.promo.entity.OrderStatus;
import org.dbs24.spring.core.data.ApplicationJpaRepository;

public interface OrderStatusRepo extends ApplicationJpaRepository<OrderStatus, Integer> {
    
}
