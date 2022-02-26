/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.tik.assist.repo;

import org.dbs24.spring.core.data.ApplicationJpaRepository;
import org.dbs24.tik.assist.entity.domain.Bot;
import org.dbs24.tik.assist.entity.domain.OrderAction;
import org.dbs24.tik.assist.entity.domain.OrderActionResult;
import org.dbs24.tik.assist.entity.domain.UserOrder;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrderActionRepo extends ApplicationJpaRepository<OrderAction, Long>, JpaSpecificationExecutor<OrderAction>, PagingAndSortingRepository<OrderAction, Long> {

    List<OrderAction> findByOrderActionResultAndUserOrder(OrderActionResult orderActionResult, UserOrder userOrder);

    List<OrderAction> findByOrderActionResult(OrderActionResult orderActionResult);

    List<OrderAction> findByBotAndOrderActionResult(Bot bot, OrderActionResult orderActionResult);

    @Query(value =
            "SELECT * " +
            "FROM tik_order_actions " +
            "WHERE order_id in (" +
            "   SELECT order_id " +
            "   FROM tik_user_orders " +
            "   WHERE order_status_id = :orderStatusId)",
            nativeQuery = true)
    List<OrderAction> findByOrderStatus(@Param("orderStatusId") Integer orderStatusId);

    @Query(value =
            "SELECT * " +
            "FROM tik_order_actions " +
            "WHERE order_id = :orderActionId " +
            "   AND order_action_result_id = :orderActionResultId " +
            "   LIMIT :orderActionsQuantity",
            nativeQuery = true)
    List<OrderAction> findByOrderActionIdLimit(
            @Param("orderActionId") Integer orderActionId,
            @Param("orderActionResultId") Integer orderActionResultId,
            @Param("orderActionsQuantity") Integer orderActionsQuantity
    );

    @Query(value =
            "SELECT * " +
            "FROM tik_order_actions " +
            "WHERE order_action_result_id = :orderActionResultId " +
            "AND order_id IN (" +
            "    SELECT tik_user_orders.order_id " +
            "    FROM tik_user_orders " +
            "    WHERE plan_id = :userPlanId )",
            nativeQuery = true)
    List<OrderAction> findByOrderActionResultIdAndUserPlanId(
            @Param("orderActionResultId") Integer orderActionResultId,
            @Param("userPlanId") Integer userPlanId
    );

    @Query(value =
            "SELECT * " +
            "FROM tik_order_actions " +
            "WHERE order_action_result_id = :orderActionResultId " +
            "AND order_id IN ( " +
            "    SELECT tik_user_orders.order_id " +
            "    FROM tik_user_orders " +
            "    WHERE plan_id in (" +
            "        SELECT plan_id " +
            "        FROM tik_user_subscriptions " +
            "        WHERE user_subscription_id = :userSubscriptionId ))",
            nativeQuery = true)
    List<OrderAction> findByOrderActionResultIdAndUserSubscriptionId(
            @Param("orderActionResultId") Integer orderActionResultId,
            @Param("userSubscriptionId") Integer userSubscriptionId
    );

    List<OrderAction> findByUserOrder(UserOrder userOrder);
}
