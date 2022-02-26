/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.repository;

import java.util.Collection;
import org.dbs24.entity.Agent;
import org.dbs24.entity.AgentHist;
import org.dbs24.spring.core.data.ApplicationJpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface AgentHistRepository extends ApplicationJpaRepository<AgentHist, Integer> {

    @Query(value = "select a.agent_id, a.actual_date, a.agent_status_id, a.phone_num, a.payload, a.created, a.agent_note from wa_agents a where agent_id= :ID\n"
            + "union all\n"
            + "select h.agent_id, h.actual_date, h.agent_status_id, h.phone_num, h.payload, h.created, h.agent_note from wa_agents_hist h where agent_id= :ID\n"
            + "order by actual_date desc", nativeQuery = true)
    public Collection<AgentHist> findAgentHistory(@Param("ID") Integer agentId);

}
