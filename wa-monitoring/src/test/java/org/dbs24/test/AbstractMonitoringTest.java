/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.test;

import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import org.dbs24.component.AgentsService;
import org.dbs24.component.UserContractsService;
import org.dbs24.component.UsersService;
import org.dbs24.repository.*;
import org.dbs24.test.core.AbstractWebTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.TestPropertySource;

import java.util.Optional;

@Log4j2
@TestPropertySource(properties = {
        "config.security.profile.webfilter.chain=development",
        "config.wa.contract.deprecated.update=2147483648",
        "springdoc.api-docs.enabled=false",
        "springdoc.swagger-ui.enabled=false",
        "spring.datasource.url=jdbc:postgresql://193.178.170.145:5432/wa_monitoring",
        "spring.datasource.username=wa_admin",
        "spring.datasource.password=$wa$m4n0t1r9ng$"})
public abstract class AbstractMonitoringTest extends AbstractWebTest {

    @Autowired
    protected UsersService usersService;

    @Autowired
    protected UserContractsService userContractsService;

    @Autowired
    protected UserSubscriptionRepository userSubscriptionRepository;

    @Autowired
    protected AgentRepository agentRepository;

    @Autowired
    protected UserContractRepository userContractRepository;

    @Autowired
    protected UserDeviceRepository userDeviceRepository;

    @Autowired
    protected UserRepository userRepository;

    @Autowired
    @Getter
    protected AgentsService agentsService;

    private Integer lastAgentId;
    private Integer lastContractId;
    private Integer lastSubsriptionId;
    private Integer lastDeviceId;
    private Integer lastUserId;
    private String lastUseLoginToken;

    protected Integer getLastSubsriptionId() {
        return Optional.ofNullable(lastSubsriptionId)
                .orElseGet(() -> {
                    lastSubsriptionId = userSubscriptionRepository.findLastUserSubscription().getSubscriptionId();
                    log.info("using lastSubsriptionId {}", lastSubsriptionId);
                    return lastSubsriptionId;
                });
    }

    protected Integer getLastAgentId() {

        return Optional.ofNullable(lastAgentId)
                .orElseGet(() -> {
                    lastAgentId = agentRepository.findLastAgent().getAgentId();
                    log.info("using agentId {}", lastAgentId);
                    return lastAgentId;
                });
    }

    protected Integer getLastConractId() {

        return Optional.ofNullable(lastContractId)
                .orElseGet(() -> {
                    lastContractId = userContractRepository.findLastContract().getContractId();
                    log.info("using contractId {}", lastContractId);
                    return lastContractId;
                });
    }

    protected Integer getLastDeviceId() {

        return Optional.ofNullable(lastDeviceId)
                .orElseGet(() -> {
                    lastDeviceId = userDeviceRepository.findLastDevice().getDeviceId();
                    log.info("using DeviceId {}", lastDeviceId);
                    return lastDeviceId;
                });
    }

    @Autowired
    protected UserRepository appUserRepository;

    protected Integer getLastUserId() {

        return Optional.ofNullable(lastUserId)
                .orElseGet(() -> {

                    lastUserId = usersService.findLastUser().getUserId();
                    log.info("using userId {}", lastUserId);
                    return lastUserId;
                });
    }

    protected String getLastUserLoginToken() {

        return Optional.ofNullable(lastUseLoginToken)
                .orElseGet(() -> {
                    lastUseLoginToken = usersService.findLastUser().getLoginToken();
                    log.info("using userId {}", lastUseLoginToken);
                    return lastUseLoginToken;
                });
    }
}
