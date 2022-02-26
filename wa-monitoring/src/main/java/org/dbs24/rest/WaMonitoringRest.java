/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.rest;

import lombok.EqualsAndHashCode;
import lombok.extern.log4j.Log4j2;
import org.dbs24.component.*;
import org.dbs24.entity.*;
import org.dbs24.entity.dto.*;
import org.dbs24.rest.api.*;
import org.dbs24.stmt.StmtProcessor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.dbs24.application.core.locale.NLS.localDateTime2long;
import static org.dbs24.application.core.locale.NLS.long2LocalDateTime;
import static org.dbs24.consts.WaConsts.Classes.*;
import static org.dbs24.consts.WaConsts.RestQueryParams.*;

@Log4j2
@Component
@ConditionalOnProperty(name = "config.restfull.profile.name", havingValue = "wa-monitoring")
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
public class WaMonitoringRest extends CommonRest {

    final ActivityReactor activityReactor;
    final UserContractsService userContractsService;
    final AgentsService agentsService;
    final UserSubscriptionsService userSubscriptionsService;
    final UsersService usersService;
    final RefsService refsService;
    final UserDevicesService userDevicesService;
    final DeviceSessionsService deviceSessionsService;

    public WaMonitoringRest(
            ActivityReactor activityReactor,
            AgentsService agentsService,
            UserContractsService userContractsService,
            UserSubscriptionsService userSubscriptionsService,
            UsersService usersService,
            UserDevicesService userDevicesService,
            RefsService refsService,
            DeviceSessionsService deviceSessionsService,
            GenericApplicationContext genericApplicationContext) {

        super(genericApplicationContext);

        this.agentsService = agentsService;
        this.userContractsService = userContractsService;
        this.activityReactor = activityReactor;
        this.userSubscriptionsService = userSubscriptionsService;
        this.usersService = usersService;
        this.refsService = refsService;
        this.userDevicesService = userDevicesService;
        this.deviceSessionsService = deviceSessionsService;
    }

    @Scheduled(fixedRateString = "${config.kafka.processing-interval:5000}", cron = "${config.kafka.processing-cron:}")
    public void fltrExec() {
        getQueryMetrics().filterExecuted();
    }

    //==========================================================================
    @Transactional
    public Mono<ServerResponse> createOrUpdateAgent(ServerRequest request) {

        return this.<AgentInfo, CreatedAgent>createResponse(request, AGENT_INFO_CLASS, CreatedAgent.class,
                agentInfo -> StmtProcessor.create(CREATED_AGENT_CLASS, createdAgent -> {

                    log.debug("import agentInfo: {}", agentInfo);

                    final Agent agent = agentsService.findOrCreateAgent(agentInfo.getAgent_id());

                    // copy 2 history
                    agentsService.saveAgentHist(agent);

                    agent.setPayload(agentInfo.getPayload());
                    agent.setPhoneNum(agentInfo.getPhone_num());
                    agent.setActualDate(long2LocalDateTime(agentInfo.getActual_date()));
                    agent.setAgentOsType(refsService.findAgentOsType(agentInfo.getAgent_os_type_id()));
                    agent.setAgentStatus(refsService.findAgentStatus(agentInfo.getAgent_status_id()));

                    log.debug("try 2 creating/update agent: {}", agent);

                    agentsService.saveAgent(agent);
                    createdAgent.setAgentId(agent.getAgentId());
                    log.debug("created/update agent: {}", createdAgent);

                }));
    }

    //==========================================================================
    @Transactional(readOnly = true)
    public Mono<ServerResponse> getAgentsList(ServerRequest request) {

        return this.<AgentInfoCollection>createResponse(request, AgentInfoCollection.class,
                () -> StmtProcessor.create(AGENT_INFO_COLLECTION_CLASS, agentsInfoCollection -> {

                    final Byte agentStatusId = getByteFromParam(request, QP_AGENT_STATUS);

                    log.info("find agents with status {}", agentStatusId);

                    final AgentStatus agentStatus = refsService.findAgentStatus(agentStatusId);

                    agentsService.findAgentsList(agentStatus)
                            .stream()
                            .forEach(agent
                                    -> agentsInfoCollection
                                    .getAgents()
                                    .add(StmtProcessor.create(AGENT_INFO_CLASS, agentInfo -> {
                                        agentInfo.setActual_date(localDateTime2long(agent.getActualDate()));
                                        agentInfo.setAgent_id(agent.getAgentId());
                                        agentInfo.setAgent_status_id(agentStatusId);
                                        agentInfo.setAgent_os_type_id(agent.getAgentOsType().getAgentOsTypeId());
                                        agentInfo.setPayload(agent.getPayload());
                                        agentInfo.setPhone_num(agent.getPhoneNum());
                                    })));

                    log.debug("getAgentsList: agentSatus {}, return {} record(s)", agentStatus, agentsInfoCollection.getAgents().size());
                }));
    }

    //==========================================================================
    public Mono<ServerResponse> getAgentsListByActualDate(ServerRequest request) {

        final LocalDateTime actualDate = getLocalDateTimeFromLongParam(request, QP_ACTUAL_DATE);

        return this.<AgentInfoCollection>createResponse(request, AgentInfoCollection.class, () -> agentsService.getAgentsList(actualDate));
    }

    //==========================================================================
    public Mono<ServerResponse> getAgentHistory(ServerRequest request) {

        final Integer agentId = getIntegerFromParam(request, QP_AGENT_ID);

        return this.<AgentInfoCollection>createResponse(request, AgentInfoCollection.class, () -> agentsService.getAgentHistory(agentId));
    }

    //==========================================================================
    @Deprecated
    public Mono<ServerResponse> createOrUpdateDevice(ServerRequest request) {

        return this.<UserDeviceInfo, CreatedUserDevice>createResponse(request, USER_DEVICE_INFO_CLASS, CreatedUserDevice.class,
                userDeviceInfo -> StmtProcessor.create(CREATED_USER_DEVICE_CLASS, createdUserSubscription -> {

                    final UserDevice userDevice = userDevicesService.findOrCreateUserDevice(userDeviceInfo.getDeviceId());

//                    userDevicesService.saveUserDeviceHist(userDevice);
                    userDevice.setActualDate(long2LocalDateTime(userDeviceInfo.getActualDate()));
//                    userDevice.setAppName(userDeviceInfo.getAppName());
//                    userDevice.setAppVersion(userDeviceInfo.getAppVersion());
//                    userDevice.setCpuId(userDeviceInfo.getCpuId());
                    userDevice.setDeviceType(refsService.findDeviceType(userDeviceInfo.getDeviceTypeId()));
//                    userDevice.setGcmToken(userDeviceInfo.getGcmToken());
//                    userDevice.setDeviceFingerprint(userDeviceInfo.getDeviceFingerprint());
//                    userDevice.setGsfId(userDeviceInfo.getGsfId());
//                    userDevice.setIosKey(userDeviceInfo.getIosKey());
//                    userDevice.setIpAddress(userDeviceInfo.getIpAddress());
//                    userDevice.setSecureId(userDeviceInfo.getSecureId());
                    userDevice.setUser(usersService.findUser(userDeviceInfo.getUserId()));

                    log.debug("try 2 creating/update device_id: {}", userDevice);

                    userDevicesService.saveUserDevice(userDevice);

                    createdUserSubscription.setDeviceId(userDevice.getDeviceId());

                    log.debug("created/update subscription: {}", createdUserSubscription);

                }));
    }

    //==========================================================================
    public Mono<ServerResponse> createDeviceSession(ServerRequest request) {

        return this.<DeviceSessionInfo, CreatedDeviceSession>createResponse(request, DEVICE_SESSION_INFO_CLASS, CreatedDeviceSession.class,
                deviceSessionInfo -> StmtProcessor.create(CREATED_DEVICE_SESSION_CLASS, createdDeviceSession -> {

                    final DeviceSession deviceSession = deviceSessionsService.findOrCreateDeviceSession(deviceSessionInfo.getSessionId());

                    deviceSession.setActualDate(long2LocalDateTime(deviceSessionInfo.getActualDate()));
                    deviceSession.setIpAddress(deviceSessionInfo.getIpAddress());
                    deviceSession.setNote(deviceSessionInfo.getNote());
                    deviceSession.setUserDevice(userDevicesService.findUserDevice(deviceSessionInfo.getDeviceId()));

                    log.debug("try 2 creating/update device_sesiion: {}", deviceSession);
                    deviceSessionsService.saveDeviceSession(deviceSession);
                    createdDeviceSession.setSessionId(deviceSession.getSessionId());
                    log.debug("created/update subscription: {}", createdDeviceSession);

                }));
    }

    //==========================================================================
    public Mono<ServerResponse> createUserToken(ServerRequest request) {

        return this.<UserTokenInfo, CreatedUserToken>createResponse(request, USER_TOKEN_INFO_CLASS, CreatedUserToken.class,
                userTokenInfo -> StmtProcessor.create(CREATED_USER_TOKEN_CLASS, createdUserToken -> {

                    // cancel previous token
                    Optional.ofNullable(userTokenInfo.getTokenId())
                            .ifPresent(existTokenId -> {

                                final UserToken cancelToken
                                        = deviceSessionsService.findUserToken(userTokenInfo.getTokenId());

                                cancelToken.setIsValid(Boolean.FALSE);
                                deviceSessionsService.saveUserToken(cancelToken);

                                log.debug("cancel token: {}", cancelToken.getTokenId());

                            });

                    // always new token
                    final UserToken newUserToken = StmtProcessor.create(USER_TOKEN_CLASS, a -> {

                        a.setCreated(LocalDateTime.now());
                        a.setIsValid(userTokenInfo.getIsValid());
                        a.setToken("new toked body");
                        a.setUser(usersService.findUser(userTokenInfo.getUserId()));
                        a.setValidDate(LocalDateTime.now().plusHours(4));
                    });

                    deviceSessionsService.saveUserToken(newUserToken);
                    createdUserToken.setCreatedToken(newUserToken.getToken());
                    log.debug("created/update token: {}", createdUserToken);

                }));
    }

    //==========================================================================
    @Override
    protected ShutdownRequest ready4ShutDown() {
        return StmtProcessor.create(ShutdownRequest.class, sr -> {

            sr.setCanShutDown(!activityReactor.isBusy());
            sr.setStatus(sr.getCanShutDown() ? "is ready 4 Shutdown" : " Processor is busy");
        });
    }

    //==========================================================================
    public Mono<ServerResponse> createOrUpdateAgentMessage(ServerRequest request) {

        return processServerRequest(
                request,
                AgentMessageDto.class,
                CreatedAgentMessage.class,
                agentsService::createOrUpdateAgentMessage);
    }

    //==========================================================================
    public Mono<ServerResponse> getAgentMessage(ServerRequest request) {

        return processServerRequest(
                request,
                AgentMessageDtoResponse.class,
                agentsService::getAgentMessage);
    }

    //==========================================================================
    public Mono<ServerResponse> getActualMessagesCount(ServerRequest request) {

        return processServerRequest(
                request,
                ActualMessagesCountDtoResponse.class,
                agentsService::getActualMessagesCount);
    }

    //==========================================================================
    public Mono<ServerResponse> getMessagingSubscriptions(ServerRequest request) {

        return processServerRequest(
                request,
                MessagesPhoneNumsDtoResponse.class,
                agentsService::getMessagingSubscriptions);
    }

    //==========================================================================
    public Mono<ServerResponse> getMessagingLastMessageCache(ServerRequest request) {

        return processServerRequest(
                request,
                AgentLatestMessagesDtoResponse.class,
                agentsService::getMessagingLastMessageCache);
    }

}
