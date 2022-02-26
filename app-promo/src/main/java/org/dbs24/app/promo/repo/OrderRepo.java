/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.app.promo.repo;

import org.dbs24.app.promo.entity.Order;
import org.dbs24.spring.core.data.ApplicationJpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Collection;

public interface OrderRepo extends ApplicationJpaRepository<Order, Integer>, PagingAndSortingRepository<Order, Integer>, JpaSpecificationExecutor<Order> {

    @Query(value = "with c_Odrers as (select o.order_id\n" +
            "                  from pr_package_orders o\n" +
            "                  where o.order_status_id in (1, 10)),\n" +
            "     c_Acts as (select distinct a.order_id\n" +
            "                from pr_orders_actions a,\n" +
            "                     c_Odrers\n" +
            "                where a.order_id = c_Odrers.order_id)\n" +
            "select o.*\n" +
            "from pr_package_orders o,\n" +
            "     c_Odrers\n" +
            "where o.order_id = c_Odrers.order_id\n" +
            "  and o.order_id not in (select order_id from c_Acts)", nativeQuery = true)
    Collection<Order> findInvalidOrders();

}
