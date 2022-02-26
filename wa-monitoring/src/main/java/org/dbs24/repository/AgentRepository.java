/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.repository;

import org.dbs24.entity.Agent;
import org.dbs24.entity.AgentStatus;
import org.dbs24.spring.core.data.ApplicationJpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Optional;

import static org.dbs24.consts.SysConst.INTEGER_ONE;

public interface AgentRepository extends ApplicationJpaRepository<Agent, Integer>, PagingAndSortingRepository<Agent, Integer>, JpaSpecificationExecutor<Agent> {

    String HARDLESS_AGENTS_LIST =
            "with c_Group as (select us.agent_id, count(*) cnt\n" +
                    "                                     from wa_agents a,\n" +
                    "                                          wa_users_subscriptions us\n" +
                    "                                     where us.agent_id = a.agent_id\n" +
                    "                                       and us.subscription_status_id = 1\n" +
                    "                                       and a.agent_status_id = 3\n" +
                    "                                     group by us.agent_id),\n" +
                    "                         c_Agents3 as (select a.agent_id, c_Group.cnt\n" +
                    "                                       from wa_agents a\n" +
                    "                                                left join c_Group on (a.agent_id = c_Group.agent_id)\n" +
                    "                                       where a.agent_status_id = 3)\n" +
                    "                    select *\n" +
                    "                    from wa_agents a, (select agent_id from c_Agents3 order by cnt nulls first limit :L) l\n" +
                    "                    where a.agent_id = l.agent_id";

    @Query(value = "select a.* from wa_Agents a where agent_id = (select max(agent_id) from wa_Agents)", nativeQuery = true)
    Agent findLastAgent();

    @Query(value = HARDLESS_AGENTS_LIST, nativeQuery = true)
    default Optional<Agent> findLeastLoadedAgent() {
        return findLeastLoadedAgents(INTEGER_ONE).stream().findFirst();
    }

    @Query(value = HARDLESS_AGENTS_LIST, nativeQuery = true)
    Collection<Agent> findLeastLoadedAgents(@Param("L") Integer limit);

    @Query(value = "select a.agent_id, agent_status_id, agent_os_type_id, phone_num, payload, created, actual_date, agent_note\n" +
            "            from (\n" +
            "                select a.*, row_number() over ( ORDER BY agent_status_id, actual_date asc ) rn\n" +
            "                  from wa_agents a\n" +
            "                  where a.agent_status_id in (1, 5) and a.agent_os_type_id = :OS \n" +
            "                union all\n" +
            "              -- insuranse bot \n" +
            "                select i.*, 1 rn from wa_agents i\n" +
            "                  where i.agent_status_id = 0\n" +
            "                ) a\n" +
            "            where rn = 1\n" +
            "            order by agent_status_id desc limit 1", nativeQuery = true)
    Optional<Agent> findAvailableAgent(@Param("OS") Byte agentOsType);

    Optional<Agent> findAgentByPhoneNum(String phoneNum);

    Collection<Agent> findAgentByAgentStatus(AgentStatus agentStatus);

    @Query(value = "with c_Agents as (\n"
            + "    select a.*\n"
            + "    from wa_agents a\n"
            + "    where :AD > created\n"
            + "      and (a.actual_date + INTERVAL '30 days') > :AD\n"
            + "),\n"
            + "     c_Union as (\n"
            + "         select c_Agents.agent_id,\n"
            + "                c_Agents.actual_date,\n"
            + "                c_Agents.agent_status_id,\n"
            + "                c_Agents.agent_os_type_id,\n"
            + "                c_Agents.phone_num,\n"
            + "                c_Agents.payload,\n"
            + "                c_Agents.created,\n"
            + "                c_Agents.agent_note --, 0 hist\n"
            + "         from c_Agents\n"
            + "         union all\n"
            + "         select h.agent_id, h.agent_os_type_id, h.actual_date, h.agent_status_id, h.phone_num, h.payload, h.created, h.agent_note --, 1 hist\n"
            + "         from wa_agents_hist h,\n"
            + "              c_Agents a\n"
            + "         where a.agent_id = h.agent_id\n"
            + "           and a.actual_date > h.actual_date\n"
            + "     ),\n"
            + "     c_All as (\n"
            + "         select c.*, row_number() OVER (PARTITION BY agent_id ORDER BY actual_date DESC ) rn\n"
            + "         from c_Union c\n"
            + "         where c.actual_date < :AD\n"
            + "     )\n"
            + "select agent_id, agent_os_type_id, actual_date, agent_status_id, phone_num, payload, created, agent_note\n"
            + "from c_All\n"
            + "where rn = 1\n"
            + "order by agent_id, actual_date desc;", nativeQuery = true)
    Collection<Agent> findAgentsListByActualDate(@Param("AD") LocalDateTime actualDate);

}
