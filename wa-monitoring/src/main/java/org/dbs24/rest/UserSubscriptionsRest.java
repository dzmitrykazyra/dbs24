/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.rest;

import lombok.EqualsAndHashCode;
import lombok.extern.log4j.Log4j2;
import org.dbs24.component.*;
import org.dbs24.consts.SysConst;
import org.dbs24.entity.Agent;
import org.dbs24.entity.UserDevice;
import org.dbs24.entity.UserSubscription;
import org.dbs24.entity.dto.AgentPayloadInfo;
import org.dbs24.entity.dto.RebalanceAgentsResult;
import org.dbs24.rest.api.*;
import org.dbs24.stmt.StmtProcessor;
import org.modelmapper.ModelMapper;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.Collection;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.lang.String.format;
import static java.time.LocalDateTime.now;
import static org.dbs24.application.core.locale.NLS.localDateTime2long;
import static org.dbs24.consts.SysConst.BOOLEAN_TRUE;
import static org.dbs24.consts.WaConsts.Classes.*;
import static org.dbs24.consts.WaConsts.References.*;
import static org.dbs24.consts.WaConsts.RestQueryParams.*;
import static org.dbs24.stmt.StmtProcessor.*;

@Log4j2
@Component
@ConditionalOnProperty(name = "config.restfull.profile.name", havingValue = "wa-monitoring")
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
public class UserSubscriptionsRest extends ReactiveRestProcessor {

    final UserBuilderService userBuilderService;
    final UserSubscriptionsService userSubscriptionsService;
    final UserDevicesService userDevicesService;
    final AgentsService agentsService;
    final UsersService usersService;
    final UserContractsService userContractsService;
    final RefsService refsService;
    final ModelMapper modelMapper;

    public UserSubscriptionsRest(RefsService refsService, UserSubscriptionsService userSubscriptionsService, AgentsService agentsService, UsersService usersService, UserContractsService userContractsService, UserDevicesService userDevicesService, UserBuilderService userBuilderService, ModelMapper modelMapper) {
        this.userSubscriptionsService = userSubscriptionsService;
        this.agentsService = agentsService;
        this.usersService = usersService;
        this.userContractsService = userContractsService;
        this.refsService = refsService;
        this.userDevicesService = userDevicesService;
        this.userBuilderService = userBuilderService;
        this.modelMapper = modelMapper;
    }

    //==========================================================================
    public Mono<ServerResponse> createOrUpdateSubscription(ServerRequest request) {

        final String loginToken = getStringFromParam(request, QP_LOGIN_TOKEN);

        assertNotNull(String.class, loginToken, "loginToken");

        return this.<UserSubscriptionInfo, CreatedUserSubscription>createResponse(
                request,
                USER_SUBSCRIPTION_INFO_CLASS,
                CreatedUserSubscription.class,
                userSubscriptionInfo -> userSubscriptionsService.createOrUpdateSubscription(userSubscriptionInfo, loginToken));
    }

    //==========================================================================
    @Transactional
    public Mono<ServerResponse> updateSubscriptionStatus(ServerRequest request) {

        final Integer subscriptionId = getIntegerFromParam(request, QP_SUBSCRIPTION_ID);
        final String stringSubscriptionStatus = getStringFromParam(request, QP_SUBSCRIPTION_STATUS);

        assertNotNull(String.class, subscriptionId, QP_SUBSCRIPTION_ID + " not passed");
        assertNotNull(String.class, stringSubscriptionStatus, QP_SUBSCRIPTION_STATUS + " not passed");

        return this.<ActionResult>createResponse(request, ActionResult.class,
                () -> userSubscriptionsService.updateSubscriptionStatus(subscriptionId, stringSubscriptionStatus));
    }

    //==========================================================================
    @Transactional(readOnly = true)
    public Mono<ServerResponse> getSubscription(ServerRequest request) {

        final String phoneNum = getStringFromParam(request, QP_PHONE).replaceAll("[^\\d.]", "");
        final String loginToken = getStringFromParam(request, QP_LOGIN_TOKEN);

        log.info("find phone num {}, {}", phoneNum, loginToken);

        final var subscription = userSubscriptionsService.findUserSubscription(loginToken, phoneNum);

        assertNotNull(UserSubscription.class, subscription, format("userSubscription (loginToken=%s, phoneNum=%s)", loginToken, phoneNum));
        assertNotNull(UserSubscription.class, subscription.getSubscriptionId(), format("userSubscription (loginToken=%s, phoneNum=%s)", loginToken, phoneNum));

        return this.<UserSubscriptionInfo>createResponse(request, UserSubscriptionInfo.class,
                () -> modelMapper.map(subscription, UserSubscriptionInfo.class));
    }

    //==========================================================================
    @Transactional(readOnly = true)
    public Mono<ServerResponse> getAllActualSubscriptions(ServerRequest request) {

        return this.<UserSubscriptionInfoCollection>createResponse(request, UserSubscriptionInfoCollection.class,
                () -> create(USER_SUBSCRIPTION_INFO_COLLECTION_CLASS, userSubscriptionInfoCollection -> {

                    final String loginToken = getStringFromParam(request, QP_LOGIN_TOKEN);

                    log.info("find login token {}", loginToken);

                    userSubscriptionInfoCollection.setUserSubscriptionInfoCollection(
                            userSubscriptionsService.findActualUserSubscriptions(loginToken)
                                    .stream()
                                    .map(subscription -> modelMapper.map(subscription, UserSubscriptionInfo.class))
                                    .collect(Collectors.toList()));

                    log.info("loginToken {}, return {} record(s)", loginToken, userSubscriptionInfoCollection.getUserSubscriptionInfoCollection().size());
                }));
    }

    //==========================================================================
    public synchronized Mono<ServerResponse> updateAgentStatus(ServerRequest request) {

        final String agentPhoneNum = getStringFromParam(request, QP_AGENT_PHONE); //"phoneNum");
        final String stringAgentStatus = getStringFromParam(request, QP_AGENT_STATUS); //"agentStatusId");
        final String stringAgentNote = getOptionalNullStringFromParam(request, QP_AGENT_NOTE); //"agentNote");


        assertNotNull(String.class, agentPhoneNum, "agentPhoneNum");
        assertNotNull(String.class, stringAgentStatus, "stringAgentStatus");

        return this.<AgentPayloadInfo>createResponse(request, AgentPayloadInfo.class, () -> agentsService.updateAgentStatus(agentPhoneNum.replaceAll("[^\\d.]", ""), stringAgentStatus, stringAgentNote));
    }

    //==========================================================================
    public Mono<ServerResponse> rebalanceAgents(ServerRequest request) {

        return this.<RebalanceAgentsResult>createResponse(request, RebalanceAgentsResult.class, agentsService::rebalanceAgents);
    }

    //==========================================================================
    @Transactional(readOnly = true)
    public Mono<ServerResponse> getAgentSubscriptions(ServerRequest request) {

        return this.<UserSubscriptionInfoCollection>createResponse(request, UserSubscriptionInfoCollection.class,
                () -> create(USER_SUBSCRIPTION_INFO_COLLECTION_CLASS, userSubscriptionInfoCollection -> {

                    final String agentPhoneNum = getStringFromParam(request, QP_AGENT_PHONE);

                    final Boolean actualOnly = getOptionalBooleanFromParam(request, QP_ACTUAL_ONLY);

                    assertNotNull(String.class, agentPhoneNum, QP_AGENT_PHONE);

                    final Agent agent = agentsService.findAgentByPhoneNum(agentPhoneNum.replaceAll("[^\\d.]", ""));

                    log.info("try 2 load agent subscriptions (agentId = {}, actualOnly = {})", agent.getAgentId(), actualOnly);

                    userSubscriptionInfoCollection.setUserSubscriptionInfoCollection(
                            userSubscriptionsService.findAgentSubscriptions(agent, actualOnly)
                                    .stream()
                                    .filter(subscription -> !subscription.getUserContract().getContractStatus().getContractStatusId().equals(CS_FUTURED))
                                    .map(subscription -> modelMapper.map(subscription, UserSubscriptionInfo.class))
                                    .collect(Collectors.toList()));

                    log.info("found {} agent subscription(s) 4 agent (agentId = {})",
                            userSubscriptionInfoCollection.getUserSubscriptionInfoCollection().size(),
                            agent.getAgentId());

                }));
    }

    //==========================================================================

    public Mono<ServerResponse> getAllUserDevices(ServerRequest request) {

        return this.<UserDevicesInfoCollection>createResponse(request, UserDevicesInfoCollection.class,
                () -> create(USER_DEVICES_INFO_COLLECTION_CLASS, userDevicesInfoCollection -> {

                    final Integer userId = getOptionalIntegerFromParam(request, QP_USER_ID);
                    final String loginToken = getOptionalNullStringFromParam(request, QP_LOGIN_TOKEN);

                    log.info("try 2 find all user devices for user_id = {} or loginToken = {})", userId, loginToken);
                    // find by userId
                    StmtProcessor.ifNotNull(userId, id -> {

                        fillUserDevices(userDevicesService.findAllUserDevices(usersService.findUser(id)).stream(), userDevicesInfoCollection);

                        log.info("found {} user device(s) for user_id =  {}",
                                userDevicesInfoCollection.getUserDevices().size(), id);

                    });

                    // find by loginToken
                    StmtProcessor.ifNotNull(loginToken, id -> {
                        fillUserDevices(userDevicesService.findAllUserDevices(usersService.findUser(id)).stream(), userDevicesInfoCollection);

                        log.info("found {} user device(s) for loginToken =  {}",
                                userDevicesInfoCollection.getUserDevices().size(), id);

                    });

                }));
    }

    //==========================================================================
    private void fillUserDevices(Stream<UserDevice> stream, UserDevicesInfoCollection userDevicesInfoCollection) {

        stream.forEach(userDevice
                -> userDevicesInfoCollection
                .getUserDevices()
                .add(create(USER_DEVICE_SHORT_INFO_CLASS, userDeviceInfo -> {
                    userDeviceInfo.setActualDate(localDateTime2long(userDevice.getActualDate()));
                    userDeviceInfo.setUserId(userDevice.getUser().getUserId());
                    userDeviceInfo.setDeviceId(userDevice.getDeviceId());
                    userDeviceInfo.setAppName(userDevice.getAppName());
                    userDeviceInfo.setAppVersion(userDevice.getAppVersion());

                    ifTrue(userDevice.getDeviceType().getDeviceTypeId().equals(DT_ANDROID),
                            () -> userDeviceInfo.setGcmToken(userDevicesService.findUserDeviceAndroid(userDevice.getDeviceId()).getFcmToken()));

                    ifTrue(userDevice.getDeviceType().getDeviceTypeId().equals(DT_IOS),
                            () -> userDeviceInfo.setGcmToken(userDevicesService.findUserDeviceIos(userDevice.getDeviceId()).getIcmToken()));
                })));
    }

    //==========================================================================
    @Transactional(readOnly = true)
    public Mono<ServerResponse> getSubscriptionNotifyStatus(ServerRequest request) {

        return this.<UserSubscriptionNotifyStatusInfo>createResponse(request, UserSubscriptionNotifyStatusInfo.class,
                () -> create(USER_SUBSCRIPTION_NOTIFY_STATUS_INFO_CLASS, userSubscriptionNotifyStatus -> {

                    final Integer subscriptionId = getIntegerFromParam(request, QP_SUBSCRIPTION_ID);

                    final UserSubscription userSubscription = userSubscriptionsService.findUserSubscription(subscriptionId);

                    userSubscriptionNotifyStatus.setActualDate(localDateTime2long(userSubscription.getActualDate()));
                    userSubscriptionNotifyStatus.setPhoneNum(userSubscription.getPhoneNum());
                    userSubscriptionNotifyStatus.setOnlineNotify(userSubscription.getOnlineNotify());
                    userSubscriptionNotifyStatus.setSubscriptionStatusId(userSubscription.getSubscriptionStatus().getSubscriptionStatusId());
                    userSubscriptionNotifyStatus.setAssignedName(userSubscription.getSubscriptionName());
                    userSubscriptionNotifyStatus.setAvatarModifyDate(localDateTime2long(userSubscription.getAvatarModifyDate()));

                }));
    }

    //==========================================================================    
    public Mono<ServerResponse> getAllUserSubscriptionNotifyStatus(ServerRequest request) {

        return this.<UserSubscriptionShortInfoCollection>createResponse(request, UserSubscriptionShortInfoCollection.class,
                () -> create(UserSubscriptionShortInfoCollection.class, allUsersSubscriptions -> {

                    final Integer userId = getIntegerFromParam(request, QP_USER_ID);

                    // getting user
                    //final User user = usersService.findUser(userId);
                    userSubscriptionsService.findAllUserSubscriptions(userId, Boolean.TRUE)
                            .stream()
                            .forEach(ss
                                    -> allUsersSubscriptions.getUserSubscriptionCollection().add(
                                    create(UserSubscriptionShortInfo.class, si -> {
                                        si.setActualDate(localDateTime2long(ss.getActualDate()));
                                        si.setAssignedName(ss.getSubscriptionName());
                                        si.setOnlineNotify(ss.getOnlineNotify());
                                        si.setPhoneNum(ss.getPhoneNum());
                                        si.setSubscriptionId(ss.getSubscriptionId());
                                        si.setSubscriptionStatusId(ss.getSubscriptionStatus().getSubscriptionStatusId());
                                        si.setActualDate(localDateTime2long(ss.getAvatarModifyDate()));
                                    })));
                }));
    }

    //==========================================================================
    @Transactional(readOnly = true)
    public Mono<ServerResponse> getInvalidSubscriptions(ServerRequest request) {

        return this.<UserSubscriptionAgentInfoCollection>createResponse(request, UserSubscriptionAgentInfoCollection.class,
                () -> create(USER_SUBSCRIPTION_AGENT_INFO_COLLECTION_CLASS, allUsersSubscriptions -> {

                    final Collection<UserSubscription> invalidSubscriptions = userSubscriptionsService.findInvalidSubscriptions();

                    ifTrue(!invalidSubscriptions.isEmpty(),
                            () -> {
                                log.warn("There are {} invalid subscription(s)", invalidSubscriptions.size());

                                invalidSubscriptions
                                        .stream()
                                        .forEach(ss
                                                -> allUsersSubscriptions.getCollection().add(
                                                StmtProcessor.create(UserSubscriptionAgentInfo.class, si -> {
                                                    si.setActualDate(localDateTime2long(ss.getActualDate()));
                                                    si.setAvatarId(ss.getAvatarId());
                                                    si.setOnlineNotify(ss.getOnlineNotify());
                                                    si.setPhoneNum(ss.getPhoneNum());
                                                    si.setSubscriptionId(ss.getSubscriptionId());
                                                    si.setSubscriptionName(ss.getSubscriptionName());
                                                    si.setSubscriptionStatusId(ss.getSubscriptionStatus().getSubscriptionStatusId());
                                                    si.setUserId(ss.getUser().getUserId());

                                                    final AgentInfo agentInfo = StmtProcessor.create(AgentInfo.class, ai -> ai.assign(ss.getAgent()));

                                                    si.setAgentInfo(agentInfo);

                                                    agentInfo.setPayload(SysConst.STRING_NULL);

                                                })));
                            });

                }));
    }

    //==========================================================================
    @Transactional(readOnly = true)
    public Mono<ServerResponse> getInvalidActivitySubscriptions(ServerRequest request) {

        return this.<UserSubscriptionAgentInfoCollection>createResponse(request, UserSubscriptionAgentInfoCollection.class,
                () -> StmtProcessor.create(USER_SUBSCRIPTION_AGENT_INFO_COLLECTION_CLASS, allUsersSubscriptions -> {

                    final Integer hours = getIntegerFromParam(request, QP_HOURS);

                    final Collection<UserSubscription> invalidSubscriptions = userSubscriptionsService.findInvalidActivitySubscriptions(hours);

                    ifTrue(!invalidSubscriptions.isEmpty(),
                            () -> {
                                log.warn("There are {} invalid activity subscription(s) with empty activities list ({} hours)", invalidSubscriptions.size(), hours);

                                invalidSubscriptions
                                        .stream()
                                        .forEach(ss
                                                -> allUsersSubscriptions.getCollection().add(
                                                StmtProcessor.create(UserSubscriptionAgentInfo.class, si -> {
                                                    si.setActualDate(localDateTime2long(ss.getActualDate()));
                                                    si.setAvatarId(ss.getAvatarId());
                                                    si.setOnlineNotify(ss.getOnlineNotify());
                                                    si.setPhoneNum(ss.getPhoneNum());
                                                    si.setSubscriptionId(ss.getSubscriptionId());
                                                    si.setSubscriptionName(ss.getSubscriptionName());
                                                    si.setSubscriptionStatusId(ss.getSubscriptionStatus().getSubscriptionStatusId());
                                                    si.setUserId(ss.getUser().getUserId());

                                                    final AgentInfo agentInfo = StmtProcessor.create(AgentInfo.class, ai -> ai.assign(ss.getAgent()));

                                                    si.setAgentInfo(agentInfo);

                                                    agentInfo.setPayload(SysConst.STRING_NULL);

                                                })));
                            }, () -> log.info("There is no invalid activity subscription(s) ({} hours)", hours));

                }));
    }

    //==========================================================================
    @Transactional
    public Mono<ServerResponse> updateInvalidSubscriptions(ServerRequest request) {

        return this.<UserSubscriptionAgentInfoCollection>createResponse(request, UserSubscriptionAgentInfoCollection.class,
                () -> StmtProcessor.create(USER_SUBSCRIPTION_AGENT_INFO_COLLECTION_CLASS, allUsersSubscriptions -> {

                    final Collection<UserSubscription> invalidSubscriptions = userSubscriptionsService.findInvalidSubscriptions();

                    ifTrue(!invalidSubscriptions.isEmpty(),
                            () -> {

                                final String mainMsg = format("There are %d invalid subscription(s) \n", invalidSubscriptions.size());

                                log.warn(mainMsg);

                                final StringBuilder sb = new StringBuilder(2048);

                                sb.append(mainMsg + "\n");

                                userSubscriptionsService.findInvalidSubscriptions()
                                        .stream()
                                        .forEach(ss -> {

                                            // agent 4 replace
                                            final Agent agent = agentsService.findLeastLoadedAgent();

                                            final UserSubscriptionAgentInfo usai
                                                    = StmtProcessor.create(UserSubscriptionAgentInfo.class, si -> {
                                                si.setActualDate(localDateTime2long(ss.getActualDate()));
                                                si.setAvatarId(ss.getAvatarId());
                                                si.setOnlineNotify(ss.getOnlineNotify());
                                                si.setPhoneNum(ss.getPhoneNum());
                                                si.setSubscriptionId(ss.getSubscriptionId());
                                                si.setSubscriptionName(ss.getSubscriptionName());
                                                si.setSubscriptionStatusId(ss.getSubscriptionStatus().getSubscriptionStatusId());
                                                si.setUserId(ss.getUser().getUserId());

                                                final AgentInfo agentInfo = StmtProcessor.create(AgentInfo.class, ai -> ai.assign(agent));

                                                si.setAgentInfo(agentInfo);

                                                agentInfo.setPayload(SysConst.STRING_NULL);

                                            });

                                            allUsersSubscriptions.getCollection().add(usai);

                                            sb.append(format("Update invalid subscription: %d, assign new agent ( %d -> %d ) \n",
                                                    ss.getSubscriptionId(), ss.getAgent().getAgentId(), agent.getAgentId()));

//                                             store ss 2 history
                                            userSubscriptionsService.saveUserSubscriptionHist(ss);
//
//                                            // update ss
                                            ss.setAgent(agent);
                                            ss.setActualDate(now());
                                            userSubscriptionsService.saveUserSubscription(ss, BOOLEAN_TRUE);
                                        });

                                log.warn(sb.toString());

                            }, () -> log.info("All subscriptions are valid"));
                }));
    }

    //==========================================================================
    public Mono<ServerResponse> createOrUpdateAvatar(ServerRequest request) {

        return this.<AvatarInfo, CreatedAvatar>createResponse(
                request,
                AvatarInfo.class,
                CreatedAvatar.class,
                userSubscriptionsService::createOrUpdateAvatar);
    }

    //==========================================================================
    public Mono<ServerResponse> createOrUpdateCustomAvatar(ServerRequest request) {

        final String loginToken = getStringFromParam(request, QP_LOGIN_TOKEN);
        final String phone = getStringFromParam(request, QP_PHONE).replaceAll("[^\\d.]", "");

        return this.<AvatarInfoBySubscriptionPhone, CreatedAvatar>createResponse(
                request,
                AvatarInfoBySubscriptionPhone.class,
                CreatedAvatar.class,
                avatarInfo -> userSubscriptionsService.createOrUpdateCustomAvatar(avatarInfo, loginToken, phone));
    }

    //==========================================================================
    public Mono<ServerResponse> getAvatar(ServerRequest request) {

        return this.<AvatarInfo>createResponse(
                request,
                AvatarInfo.class,
                () -> userSubscriptionsService.getAvatar(getIntegerFromParam(request, QP_SUBSCRIPTION_ID)));
    }

    //==========================================================================
    public Mono<ServerResponse> getCustomAvatar(ServerRequest request) {

        return this.<AvatarInfoBySubscriptionPhone>createResponse(
                request,
                AvatarInfoBySubscriptionPhone.class,
                () -> userSubscriptionsService.getCustomAvatar(getStringFromParam(request, QP_LOGIN_TOKEN), getStringFromParam(request, QP_PHONE).replaceAll("[^\\d.]", "")));
    }

    //==========================================================================
    @Transactional(readOnly = true)
    public Mono<ServerResponse> prepareActualSubscriptions(ServerRequest request) {

        final Integer pageSize = getOptionalIntegerFromParam(request, QP_PAGE_SIZE);

        return this.<PrepareSubscriptionAnswer>createResponse(
                request,
                PrepareSubscriptionAnswer.class,
                () -> userSubscriptionsService.prepareAllActualUserSubsriptions(pageSize));
    }

    //==========================================================================
//    public Mono<ServerResponse> createUserSubscriptions4test(ServerRequest request) {
//
//        return this.<TestMassSubscriptionsInfo, CreateTestSubscriptionResult>processServerRequest(
//                request,
//                TestMassSubscriptionsInfo.class,
//                testingService::createUserSubscriptions4test);
//    }
}
