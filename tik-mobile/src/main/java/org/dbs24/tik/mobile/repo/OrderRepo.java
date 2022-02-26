package org.dbs24.tik.mobile.repo;

import org.dbs24.tik.mobile.entity.domain.Order;
import org.dbs24.tik.mobile.entity.domain.OrderStatus;
import org.dbs24.tik.mobile.entity.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.persistence.Converter;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepo extends JpaRepository<Order, Integer> {

    @Query(value = "with user_actions as (SELECT DISTINCT order_id FROM tm_order_actions WHERE user_id = :userId) " +
            "SELECT * " +
            "FROM (SELECT * " +
            "      FROM tm_user_orders uo " +
            "               NATURAL JOIN tm_order_execution_progresses oep " +
            "      WHERE uo.order_status_id = :inProgressOrderStatusId) as user_orders " +
            "WHERE ((done_actions_quantity / user_orders.actions_amount) < " +
            "       ((extract(epoch from (:current_date - user_orders.start_date))) / " +
            "        (extract(epoch from (user_orders.end_date - user_orders.start_date)))) " +
            "    AND user_orders.order_id not in (SELECT order_id FROM user_actions)) ", nativeQuery = true)
    List<Order> findAllAvailableOrders(@Param("userId") Integer userId,
                                       @Param("current_date") LocalDateTime currentDate,
                                       @Param("inProgressOrderStatusId") Integer inProgressOrderStatusId);

    //TODO: REMOVE DOUBLE CAST

    @Query(value = "SELECT *" +
            "FROM tm_user_orders uo " +
            "WHERE (uo.user_id = :userId " +
            "    AND uo.order_status_id = :order_status_id " +
            "    AND (:type_id IS NULL OR (uo.action_type_id = CAST(CAST(:type_id AS TEXT) AS INTEGER))))", nativeQuery = true)
    List<Order> findAllOrdersByUserAndOrderStatus(@Param("userId") Integer userId,
                                                  @Param("order_status_id") Integer orderStatusId,
                                                  @Param("type_id") Integer actionTypeId,
                                                  Pageable pageable);

    Optional<Order> findByOrderId(Integer orderId);

    Integer countAllByUser(User user);

    List<Order> findByOrderStatus(OrderStatus orderStatus);
}
