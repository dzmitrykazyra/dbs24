/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.repository;

import org.dbs24.entity.Agent;
import org.dbs24.entity.AgentMessage;
import org.dbs24.entity.dto.AgentLatestMessage;
import org.dbs24.spring.core.data.ApplicationJpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;

public interface AgentMessageRepository extends ApplicationJpaRepository<AgentMessage, Integer> {

    Collection<AgentMessage> findByAgentAndPhoneNum(Agent agent, String phoneNum);

    @Query(value = "SELECT count(am.*) FROM wa_agent_messages am, wa_agents a  WHERE am.phone_num = :P and am.agent_id = a.agent_id and a.agent_status_id=6 ", nativeQuery = true)
    Integer getActualMessagesCount(@Param("P") String phoneNum);

    @Query(value = "WITH c_SubsNums AS (SELECT DISTINCT am.phone_num\n" +
            "  FROM wa_agent_messages am, wa_agents a\n" +
            "  WHERE am.agent_id = a.agent_id and a.agent_id = :A )\n" +
            "SELECT DISTINCT us.phone_num FROM wa_users_subscriptions us, c_SubsNums cSN\n" +
            "  WHERE us.subscription_status_id in (0,1) and us.phone_num = cSN.phone_num ", nativeQuery = true)
    Collection<String> getMessagingSubscriptions(@Param("A") Integer agentId);


    @Query(value = "SELECT phone_num agentPhone, agentCreateDate, max(create_date) createDate\n" +
            "            FROM ( SELECT a.phone_num, a.created agentCreateDate, am.create_date\n" +
            "                     FROM wa_agents a LEFT JOIN wa_agent_messages am ON (am.agent_id = a.agent_id)\n" +
            "                     WHERE a.agent_status_id <> 4\n" +
            "                 ) c GROUP BY phone_num, agentCreateDate ORDER BY agentPhone\n", nativeQuery = true)
    Collection<AgentLatestMessage> getMessagingLastMessageCache();

}
