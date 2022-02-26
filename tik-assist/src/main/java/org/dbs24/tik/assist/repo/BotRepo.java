/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.tik.assist.repo;

import org.dbs24.spring.core.data.ApplicationJpaRepository;
import org.dbs24.tik.assist.entity.domain.Bot;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import java.util.Collection;
import java.util.List;

import org.dbs24.tik.assist.entity.domain.BotStatus;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface BotRepo extends ApplicationJpaRepository<Bot, Integer>, JpaSpecificationExecutor<Bot>, PagingAndSortingRepository<Bot, Integer> {

    List<Bot> findByBotStatus(BotStatus botStatus);

    Page<Bot> findByBotStatus(BotStatus botStatus, Pageable pageable);

    /**
     * Method allows choosing bots never used for doing previous required specific tasks such as:
     *      one bot can be used for FOLLOWING by one tiktok account
     */
    @Query(
            value = "select distinct * from tik_bots where bot_status_id = :activeBotStatusId " +
                    "    and bot_id not in ( " +
                    "        select bot_id " +
                    "        from tik_order_actions " +
                    "        where order_id in (select order_id " +
                    "                               from tik_user_orders " +
                    "                               where action_type_id = :followersActionTypeId " +
                    "                               and account_id = :accountId)" +
                    ") limit :botsQuantity",
            nativeQuery = true
    )
    List<Bot> findFollowersAvailableBotsByBotStatusAndUserIdAndAwemeIdLimit(
            @Param("activeBotStatusId") Integer activeBotStatusId,
            @Param("followersActionTypeId") Integer followersActionTypeId,
            @Param("accountId") Integer accountId,
            @Param("botsQuantity") Integer botsQuantity
    );

    /**
     * Method allows choosing bots never used for doing previous required specific tasks such as:
     *      one bot can LIKE N account videos of N existing account videos
     */
    @Query(
            value = "select distinct * from tik_bots where bot_status_id = :activeBotStatusId " +
                    "    and bot_id not in ( " +
                    "        select bot_id " +
                    "        from tik_order_actions " +
                    "        where order_id in (select order_id " +
                    "                               from tik_user_orders " +
                    "                               where action_type_id = :likesActionTypeId " +
                    "                               and account_id = :accountId " +
                    "                               and aweme_id = :awemeId) " +
                    ") limit :botsQuantity",
            nativeQuery = true
    )
    List<Bot> findLikesAvailableBotsByBotStatusAndUserIdAndAwemeIdLimit(
            @Param("activeBotStatusId") Integer activeBotStatusId,
            @Param("likesActionTypeId") Integer likesActionTypeId,
            @Param("accountId") Integer accountId,
            @Param("awemeId") String awemeId,
            @Param("botsQuantity") Integer botsQuantity
    );

    @Query(value =
            "select count(*) " +
            "from tik_bots " +
            "where bot_status_id = :botStatusId " +
            "and bot_id not in (" +
            "           select distinct bot_id used " +
            "           from tik_order_actions " +
            "           where order_id in (" +
            "                 select order_id " +
            "                 from tik_user_orders " +
            "                 where aweme_id = :awemeId " +
            "                 and action_type_id = :actionTypeId))", nativeQuery = true)
    Integer getNotUsedBotsCountByBotStatusIdAndAwemeIdAndActionTypeId(
            @Param("botStatusId") Integer botStatusId,
            @Param("awemeId") String awemeId,
            @Param("actionTypeId") Integer actionTypeId
    );

    @Query(value =
           "select count(*) " +
           "from tik_bots " +
           "where bot_status_id = :botStatusId " +
           "and bot_id not in (" +
           "           select distinct bot_id used " +
           "           from tik_order_actions " +
           "           where order_id in (" +
           "                 select order_id " +
           "                 from tik_user_orders " +
           "                 where account_id = :accountId " +
           "                 and action_type_id = :actionTypeId))", nativeQuery = true)
    Integer getNotUsedBotsCountByBotStatusIdAndAccountIdAndActionTypeId(
            @Param("botStatusId") Integer botStatusId,
            @Param("accountId") Integer accountId,
            @Param("actionTypeId") Integer actionTypeId
    );

    @Query(value =
            "select distinct bot_id " +
            "       from tik_order_actions " +
            "       where order_id in ( " +
            "             select order_id " +
            "             from tik_user_orders " +
            "             where aweme_id = :awemeId)", nativeQuery = true)
    Collection<Integer> getUsedBotsByAwemeId(@Param("awemeId") String awemeId);
}
