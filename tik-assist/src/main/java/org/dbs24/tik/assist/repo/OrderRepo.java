/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.tik.assist.repo;

import org.dbs24.spring.core.data.ApplicationJpaRepository;
import org.dbs24.tik.assist.entity.domain.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface OrderRepo extends ApplicationJpaRepository<UserOrder, Integer>, JpaSpecificationExecutor<UserOrder>, PagingAndSortingRepository<UserOrder, Integer> {
    
    
    List<UserOrder> findByOrderStatus(OrderStatus orderStatus);

    List<UserOrder> findByOrderStatusAndUserPlan(OrderStatus orderStatus, UserPlan userPlan);

    List<UserOrder> findByUser(User user);
    
    List<UserOrder> findByUserAndOrderStatusAndUserPlan(User user, OrderStatus orderStatus, UserPlan userPlan);

    List<UserOrder> findByOrderStatusAndEndDateBefore(OrderStatus closedOrderStatus, LocalDateTime currentDate);

    List<UserOrder> findByOrderStatusAndUserPlanAndTiktokAccount(OrderStatus closedOrderStatus, UserPlan userPlan, TiktokAccount tiktokAccount);

    Page<UserOrder> findByOrderStatusAndUserPlanAndTiktokAccount(OrderStatus orderStatus, UserPlan userPlan, TiktokAccount tiktokAccount,  Pageable pageable);

    List<UserOrder> findByTiktokAccountAndOrderStatusAndUserPlan(TiktokAccount tiktokAccount, OrderStatus actualOrderStatus, UserPlan userPlan);
}
