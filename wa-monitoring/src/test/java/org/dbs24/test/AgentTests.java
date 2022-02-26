/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.test;

import lombok.extern.log4j.Log4j2;
import org.dbs24.WaMonitoring;
import org.dbs24.application.core.locale.NLS;
import org.dbs24.application.core.service.funcs.TestFuncs;
import org.dbs24.config.WaServerConfig;
import org.dbs24.entity.Agent;
import org.dbs24.entity.AgentOsType;
import org.dbs24.entity.dto.AgentPayloadInfo;
import org.dbs24.rest.api.AgentInfo;
import org.dbs24.rest.api.AgentInfoCollection;
import org.dbs24.stmt.StmtProcessor;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

import static org.dbs24.consts.WaConsts.Classes.AGENT_INFO_CLASS;
import static org.dbs24.consts.WaConsts.Classes.CREATED_AGENT_CLASS;
import static org.dbs24.consts.WaConsts.References.*;
import static org.dbs24.consts.WaConsts.RestQueryParams.*;
import static org.dbs24.consts.WaConsts.Uri.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;

@Log4j2
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = {WaMonitoring.class})
@Import({WaServerConfig.class})
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AgentTests extends AbstractMonitoringTest {

    private Integer lastAgentId;

    @Order(100)
    @Test
    @DisplayName("test1")
    @RepeatedTest(10)
    public void createAgents() {

        final Mono<AgentInfo> monoAgent = Mono.just(StmtProcessor.create(AGENT_INFO_CLASS, agent -> {
            agent.setActual_date(NLS.localDateTime2long(LocalDateTime.now()));
            agent.setAgent_status_id(AS_RESERVE);
            agent.setPayload(TestFuncs.generateTestString15());
            agent.setPhone_num(TestFuncs.generateTestString15());
            //user.set
        }));
        runTest(() -> {

            log.info("testing {}", URI_CREATE_AGENT);

            lastAgentId = webTestClient
                    //                .mutate().filter(basicAuthentication(this.uid, this.pwd)).build()
                    .post()
                    .uri(uriBuilder
                            -> uriBuilder
                            .path(URI_CREATE_AGENT)
                            .build())
                    .contentType(APPLICATION_JSON)
                    .accept(APPLICATION_JSON)
                    .body(monoAgent, AGENT_INFO_CLASS)
                    .exchange()
                    .expectStatus()
                    .isOk()
                    .expectBody(CREATED_AGENT_CLASS)
                    .returnResult()
                    .getResponseBody()
                    .getAgentId();
        });
    }

    @Order(200)
    @Test
    @DisplayName("test2")
    public void updateAgents() {

        log.info("testing {}, update agentId = {}", URI_CREATE_AGENT, lastAgentId);

        final Mono<AgentInfo> monoAgent = Mono.just(StmtProcessor.create(AGENT_INFO_CLASS, agent -> {
            agent.setAgent_id(lastAgentId);
            agent.setActual_date(NLS.localDateTime2long(LocalDateTime.now()));
            agent.setAgent_status_id(AS_TRACKNG);
            agent.setPayload(TestFuncs.generateTestString15());
            agent.setPhone_num(TestFuncs.generateTestString15());
        }));
        runTest(() -> {

            log.info("testing {}", URI_CREATE_AGENT);

            webTestClient
                    .post()
                    .uri(uriBuilder
                            -> uriBuilder
                            .path(URI_CREATE_AGENT)
                            .build())
                    .contentType(APPLICATION_JSON)
                    .accept(APPLICATION_JSON)
                    .body(monoAgent, AGENT_INFO_CLASS)
                    .exchange()
                    .expectStatus()
                    .isOk();
        });
    }

    @Order(210)
    @Test
    @DisplayName("test2")
    public void updateAgentsStatusReplacement() {

        log.info("testing {}, update agentId = {} with replacement (5)", URI_UPDATE_AGENT_STATUS, lastAgentId);

        runTest(() -> {

            log.info("testing {}", URI_UPDATE_AGENT_STATUS);

            final Agent agent = agentsService.findAgent(lastAgentId);

            final AgentPayloadInfo agentPayloadInfo
                    = webTestClient
                            .get()
                            .uri(uriBuilder
                                    -> uriBuilder
                                    .path(URI_UPDATE_AGENT_STATUS)
                                    .queryParam(QP_AGENT_PHONE, agent.getPhoneNum())
                                    .queryParam(QP_AGENT_STATUS, AS_QUARANTINE)
                                    .build())
                            .accept(APPLICATION_JSON)
                            .exchange()
                            .expectStatus()
                            .isOk()
                            .expectBody(AgentPayloadInfo.class)
                            .returnResult()
                            .getResponseBody();

            log.info("{}: {}", URI_UPDATE_AGENT_STATUS, agentPayloadInfo);

        });
    }

    @Order(220)
    @Test
    @DisplayName("test2")
    public void updateAgentsStatus2Ban() {

        log.info("testing {}, update agentId = {} -> ban (4)", URI_UPDATE_AGENT_STATUS, lastAgentId);

        runTest(() -> {

            log.info("testing {}", URI_UPDATE_AGENT_STATUS);

            final Agent agent = agentsService.findAgent(lastAgentId);

            final AgentPayloadInfo agentPayloadInfo
                    = webTestClient
                            .get()
                            .uri(uriBuilder
                                    -> uriBuilder
                                    .path(URI_UPDATE_AGENT_STATUS)
                                    .queryParam(QP_AGENT_PHONE, agent.getPhoneNum())
                                    .queryParam(QP_AGENT_STATUS, AS_BANNED)
                                    .build())
                            .accept(APPLICATION_JSON)
                            .exchange()
                            .expectStatus()
                            .isOk()
                            .expectBody(AgentPayloadInfo.class)
                            .returnResult()
                            .getResponseBody();

            log.info("{}: {}", URI_UPDATE_AGENT_STATUS, agentPayloadInfo);

        });
    }

    @Order(300)
    @Test
    @DisplayName("test2")
    public void getAgentList1day() {

        log.info("testing {}", URI_GET_AGENTS_LIST_BY_ACTUAL_DATE);

        runTest(() -> {

            log.info("testing {}", URI_GET_AGENTS_LIST_BY_ACTUAL_DATE);

            final Long actualDate = NLS.localDateTime2long(LocalDateTime.now().minusDays(2));

            final AgentInfoCollection agentInfoCollection
                    = webTestClient
                            .get()
                            .uri(uriBuilder
                                    -> uriBuilder
                                    .path(URI_GET_AGENTS_LIST_BY_ACTUAL_DATE)
                                    .queryParam(QP_ACTUAL_DATE, actualDate)
                                    .build())
                            .accept(APPLICATION_JSON)
                            .exchange()
                            .expectStatus()
                            .isOk()
                            .expectBody(AgentInfoCollection.class)
                            .returnResult()
                            .getResponseBody();

            log.info("{}: found {} record(s), actualDate = '{}'",
                    URI_GET_AGENTS_LIST_BY_ACTUAL_DATE,
                    agentInfoCollection.getAgents().size(),
                    NLS.getStringDateTimeMS(NLS.long2LocalDateTime(actualDate)));

            agentInfoCollection
                    .getAgents()
                    .stream()
                    .forEach(ai -> log.info("agentId = {}, status = {}, ad = {}",
                    ai.getAgent_id(),
                    ai.getAgent_status_id(),
                    ai.getStr_actual_date()));
        });
    }

    @Order(400)
    @Test
    @DisplayName("test2")
    public void getAgentList30days() {

        log.info("testing {}", URI_GET_AGENTS_LIST_BY_ACTUAL_DATE);

        runTest(() -> {

            log.info("testing {}", URI_GET_AGENTS_LIST_BY_ACTUAL_DATE);

            final Long actualDate = NLS.localDateTime2long(LocalDateTime.now().minusDays(30));

            final AgentInfoCollection agentInfoCollection
                    = webTestClient
                            .get()
                            .uri(uriBuilder
                                    -> uriBuilder
                                    .path(URI_GET_AGENTS_LIST_BY_ACTUAL_DATE)
                                    .queryParam(QP_ACTUAL_DATE, actualDate)
                                    .build())
                            .accept(APPLICATION_JSON)
                            .exchange()
                            .expectStatus()
                            .isOk()
                            .expectBody(AgentInfoCollection.class)
                            .returnResult()
                            .getResponseBody();

            log.info("{}: found {} record(s), actualDate = '{}'",
                    URI_GET_AGENTS_LIST_BY_ACTUAL_DATE,
                    agentInfoCollection.getAgents().size(),
                    NLS.getStringDateTimeMS(NLS.long2LocalDateTime(actualDate)));

            agentInfoCollection
                    .getAgents()
                    .stream()
                    .forEach(ai -> log.info("agentId = {}, status = {}, ad = {}",
                    ai.getAgent_id(),
                    ai.getAgent_status_id(),
                    ai.getStr_actual_date()));
        });
    }

    @Order(500)
    @Transactional(readOnly = true)
    @Test
    public void getAvailableAgent(AgentOsType agentOsType) {

        log.info("testing findAvaialableAgent() service");

        runTest(() -> {

            final Agent agent = this.getAgentsService().findAvailableAgent(agentOsType);

            log.info("found agent: {}", agent);

        });
    }

}
