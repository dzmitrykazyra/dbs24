/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.component;

import lombok.extern.log4j.Log4j2;
import org.dbs24.entity.Agent;
import org.dbs24.entity.AgentHist;
import org.dbs24.entity.AgentStatus;
import org.dbs24.entity.dto.AgentPayloadInfo;
import org.dbs24.entity.dto.RebalanceAgentsResult;
import org.dbs24.repository.AgentHistRepository;
import org.dbs24.repository.AgentRepository;
import org.dbs24.rest.api.AgentInfo;
import org.dbs24.rest.api.AgentInfoCollection;
import org.dbs24.spring.core.api.AbstractApplicationService;
import org.dbs24.spring.core.data.PageLoader;
import org.dbs24.stmt.StmtProcessor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.Predicate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Optional;

import static java.time.LocalDateTime.now;
import static org.dbs24.application.core.locale.NLS.localDateTime2String;
import static org.dbs24.application.core.locale.NLS.localDateTime2long;
import static org.dbs24.application.core.service.funcs.ServiceFuncs.createConcurencyCollection;
import static org.dbs24.consts.SysConst.BOOLEAN_TRUE;
import static org.dbs24.consts.WaConsts.References.AS_TRACKNG;
import static org.dbs24.stmt.StmtProcessor.create;

@Log4j2
@Component
@ConditionalOnProperty(name = "config.restfull.profile.name", havingValue = "wa-monitoring")
public class AgentsService extends AbstractApplicationService {

    final RefsService refsService;
    final AgentRepository agentRepository;
    final AgentHistRepository agentHistRepository;

    public AgentsService(AgentRepository agentRepository, AgentHistRepository agentHistRepository, RefsService refsService) {
        this.agentRepository = agentRepository;
        this.agentHistRepository = agentHistRepository;
        this.refsService = refsService;
    }

    public static final java.util.function.Predicate<Agent> isTrackingAgent = agent -> agent.getAgentStatus().getAgentStatusId().equals(AS_TRACKNG);

    public Agent createAgent() {
        return create(Agent.class, a -> a.setCreated(now()));
    }

    public Agent findAgent(Integer agentId) {

        return agentRepository
                .findById(agentId)
                .orElseThrow(() -> new RuntimeException(String.format("agentId not found (%d)", agentId)));
    }

    //@Cacheable(CACHE_AGENT_BY_PHONE_NUM)
    public Agent findAgentByPhoneNum(String phoneNum) {

        return agentRepository
                .findAgentByPhoneNum(phoneNum)
                .orElseThrow(() -> new RuntimeException(String.format("agent not found (phoneNum = %s)", phoneNum)));
    }

    public Collection<Agent> findAgentsList(AgentStatus agentStatus) {

        return agentRepository
                .findAgentByAgentStatus(agentStatus);
    }

    public Agent findLeastLoadedAgent() {

        log.info("try 2 findLeastLoadedAgent()");

        return agentRepository.findLeastLoadedAgent()
                .orElseThrow(() -> new RuntimeException("findHardLessAgent: No hardless agents founds!!!!"));
    }

    @Transactional(readOnly = true)
    public Agent validateOrReplaceAgent(Agent existAgent) {

        StmtProcessor.ifNotNull(existAgent, () -> log.debug("validate existing agent: (agentId={})", existAgent.getAgentId()));

        Boolean needReplaceAgent = BOOLEAN_TRUE;

        if (StmtProcessor.notNull(existAgent)) {
            needReplaceAgent = !isTrackingAgent.test(existAgent);
        }

        return needReplaceAgent ? agentRepository.findLeastLoadedAgent()
                .orElseThrow(() -> new RuntimeException("findHardLessAgent: No hardless agents founds!!!!"))
                : existAgent;

    }

    public Agent findAvailableAgent() {

        return agentRepository.findAvaiableAgent()
                .orElseGet(() -> {
                    log.warn("no available agents founds (1,5)");
                    return findLeastLoadedAgent();
                });
    }

    public Agent findOrCreateAgent(Integer agentId) {
        return (Optional.ofNullable(agentId)
                .orElseGet(() -> 0) > 0)
                ? findAgent(agentId)
                : createAgent();
    }

    public void saveAgentHist(Agent agent) {
        Optional.ofNullable(agent.getAgentId())
                .ifPresent(id -> agentHistRepository.save(create(AgentHist.class, agentHist -> {

                    log.info("store agent rec 2 history ({}, {}, statusId = {})", agent.getAgentId(), agent.getPhoneNum(), agent.getAgentStatus().getAgentStatusId());

                    agentHist.setAgentId(id);
                    agentHist.setActualDate(agent.getActualDate());
                    agentHist.setAgentStatus(agent.getAgentStatus());
                    agentHist.setCreated(agent.getCreated());
                    agentHist.setPayload(agent.getPayload());
                    agentHist.setPhoneNum(agent.getPhoneNum());
                    agentHist.setAgentNote(agent.getAgentNote());
                })));
    }

    public void saveAgent(Agent agent) {
        agentRepository.saveAndFlush(agent);
    }

    public AgentInfoCollection getAgentsList(LocalDateTime actualDate) {

        return create(AgentInfoCollection.class, aic
                -> agentRepository
                .findAgentsListByActualDate(actualDate)
                .stream()
                .forEach(agent -> aic.getAgents().add(
                        create(AgentInfo.class, agentInfo -> {
                            agentInfo.setActual_date(localDateTime2long(agent.getActualDate()));
                            agentInfo.setAgent_id(agent.getAgentId());
                            agentInfo.setStr_actual_date(localDateTime2String(agent.getActualDate()));
                            agentInfo.setAgent_status_id(agent.getAgentStatus().getAgentStatusId());
                            agentInfo.setPayload("{...}");
                            agentInfo.setPhone_num(agent.getPhoneNum());
                        })))
        );
    }

    public AgentInfoCollection getAgentHistory(Integer agentId) {

        return create(AgentInfoCollection.class, aic
                -> agentHistRepository
                .findAgentHistory(agentId)
                .stream()
                .forEach(agent -> aic.getAgents().add(
                        create(AgentInfo.class, agentInfo -> {
                            agentInfo.setActual_date(localDateTime2long(agent.getActualDate()));
                            agentInfo.setAgent_id(agent.getAgentId());
                            agentInfo.setStr_actual_date(localDateTime2String(agent.getActualDate()));
                            agentInfo.setAgent_status_id(agent.getAgentStatus().getAgentStatusId());
                            agentInfo.setPayload("{...}");
                            agentInfo.setPhone_num(agent.getPhoneNum());
                        })))
        );
    }

    //==========================================================================
    public Collection<Agent> loadAgents(AgentStatus agentStatus) {

        final Collection<Agent> agents = createConcurencyCollection();

        (new PageLoader<Agent>() {
            @Override
            public void addPage(Collection<Agent> pageAgents) {
                log.debug("load agents ({}): {} records(s)", agentStatus.getAgentStatusId(), pageAgents.size());
                agents.addAll(pageAgents);
            }
        }).loadRecords(agentRepository, () -> (r, cq, cb) -> {

            final Predicate predicate = cb.conjunction();

            predicate.getExpressions().add(cb.equal(r.get("agentStatus"), agentStatus.getAgentStatusId()));

            return predicate;

        }, 1000);

        return agents;
    }

    //==========================================================================
    @Transactional
    public RebalanceAgentsResult rebalanceAgents() {

        return create(RebalanceAgentsResult.class, rebalanceAgentsResult -> {

        });

    }

    //==========================================================================
    @Transactional
    public AgentPayloadInfo updateAgentStatus(String agentPhoneNum, String stringAgentStatus) {

        return create(AgentPayloadInfo.class, payLoadInfo -> {
        });
    }
}
