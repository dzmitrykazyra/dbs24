/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.component;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import org.dbs24.application.core.nullsafe.StopWatcher;
import org.dbs24.entity.*;
import org.dbs24.entity.dto.*;
import org.dbs24.repository.AgentHistRepository;
import org.dbs24.repository.AgentMessageHistRepository;
import org.dbs24.repository.AgentMessageRepository;
import org.dbs24.repository.AgentRepository;
import org.dbs24.rest.api.AgentInfoCollection;
import org.dbs24.spring.core.api.AbstractApplicationService;
import org.dbs24.spring.core.data.PageLoader;
import org.dbs24.stmt.StmtProcessor;
import org.modelmapper.ModelMapper;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.web.reactive.function.server.ServerRequest;
import reactor.core.publisher.Mono;

import javax.persistence.criteria.Predicate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Comparator;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiFunction;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import static java.lang.Byte.valueOf;
import static java.lang.String.format;
import static java.time.LocalDateTime.now;
import static java.util.Optional.ofNullable;
import static org.dbs24.application.core.locale.NLS.localDateTime2String;
import static org.dbs24.application.core.locale.NLS.localDateTime2long;
import static org.dbs24.application.core.service.funcs.ServiceFuncs.createCollection;
import static org.dbs24.application.core.service.funcs.ServiceFuncs.createConcurencyCollection;
import static org.dbs24.consts.SysConst.BOOLEAN_FALSE;
import static org.dbs24.consts.SysConst.BOOLEAN_TRUE;
import static org.dbs24.consts.WaConsts.Classes.*;
import static org.dbs24.consts.WaConsts.References.*;
import static org.dbs24.consts.WaConsts.RestQueryParams.QP_AGENT_PHONE;
import static org.dbs24.consts.WaConsts.RestQueryParams.QP_PHONE;
import static org.dbs24.rest.api.ReactiveRestProcessor.getOptionalStringFromParam;
import static org.dbs24.rest.api.consts.RestApiConst.OperCode.*;
import static org.dbs24.stmt.StmtProcessor.*;
import static reactor.core.publisher.Mono.just;

@Getter
@Log4j2
@Component
@ConditionalOnProperty(name = "config.restfull.profile.name", havingValue = "wa-monitoring")
@EqualsAndHashCode(callSuper = true)
public class AgentsService extends AbstractApplicationService {

    final RefsService refsService;
    final AgentRepository agentRepository;
    final AgentHistRepository agentHistRepository;
    final UserSubscriptionsService userSubscriptionsService;
    final ModelMapper modelMapper;
    final AgentMessageRepository agentMessageRepository;
    final AgentMessageHistRepository agentMessageHistRepository;


    public AgentsService(AgentRepository agentRepository, AgentHistRepository agentHistRepository, RefsService refsService, UserSubscriptionsService userSubscriptionsService, ModelMapper modelMapper, AgentMessageRepository agentMessageRepository, AgentMessageHistRepository agentMessageHistRepository) {
        this.agentRepository = agentRepository;
        this.agentHistRepository = agentHistRepository;
        this.refsService = refsService;
        this.userSubscriptionsService = userSubscriptionsService;
        this.modelMapper = modelMapper;
        this.agentMessageRepository = agentMessageRepository;
        this.agentMessageHistRepository = agentMessageHistRepository;

    }

    public static final java.util.function.Predicate<Agent> isTrackingAgent = agent -> agent.getAgentStatus().getAgentStatusId().equals(AS_TRACKNG);

    public Agent createAgent() {
        return create(AGENT_CLASS, a -> a.setCreated(now()));
    }

    public Agent findAgent(Integer agentId) {

        return agentRepository
                .findById(agentId)
                .orElseThrow(() -> new RuntimeException(format("agentId not found (%d)", agentId)));
    }

    //@Cacheable(CACHE_AGENT_BY_PHONE_NUM)
    public Agent findAgentByPhoneNum(String phoneNum) {

        return agentRepository
                .findAgentByPhoneNum(phoneNum)
                .orElseThrow(() -> new RuntimeException(format("agent not found (phoneNum = %s)", phoneNum)));
    }

    public Collection<Agent> findAgentsList(AgentStatus agentStatus) {

        return agentRepository
                .findAgentByAgentStatus(agentStatus);
    }

    @Transactional(readOnly = true)
    public Agent findLeastLoadedAgent() {

        log.info("try 2 find LeastLoadedAgent()");

        return agentRepository.findLeastLoadedAgent()
                .orElseThrow(() -> new RuntimeException("findHardLessAgent: No hardless agents founds!!!!"));
    }

    @Transactional(readOnly = true)
    public Collection<Agent> findLeastLoadedAgents(Integer size) {

        log.info("try 2 find LeastLoadedAgent({})", size);

        return agentRepository.findLeastLoadedAgents(size);
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

    public Agent findAvailableAgent(AgentOsType agentOsType) {

        return agentRepository.findAvailableAgent(agentOsType.getAgentOsTypeId())
                .orElseGet(() -> {
                    log.warn("no available agents founds (1,5)");
                    return findLeastLoadedAgent();
                });
    }

    public Agent findOrCreateAgent(Integer agentId) {
        return ofNullable(agentId)
                .map(this::findAgent)
                .orElseGet(this::createAgent);
    }

    public void saveAgentHist(Agent agent) {
        ofNullable(agent.getAgentId())
                .ifPresent(id -> agentHistRepository.save((create(AGENT_HIST_CLASS, agentHist -> {

                    log.info("store agent rec 2 history ({}, {}, statusId = {})", agent.getAgentId(), agent.getPhoneNum(), agent.getAgentStatus().getAgentStatusId());

                    agentHist.setAgentId(id);
                    agentHist.setActualDate(agent.getActualDate());
                    agentHist.setAgentStatus(agent.getAgentStatus());
                    agentHist.setCreated(agent.getCreated());
                    agentHist.setPayload(agent.getPayload());
                    agentHist.setPhoneNum(agent.getPhoneNum());
                    agentHist.setAgentNote(agent.getAgentNote());
                    agentHist.setAgentOsType(agent.getAgentOsType());
                }))));
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
                        create(AGENT_INFO_CLASS, agentInfo -> {
                            agentInfo.setActual_date(localDateTime2long(agent.getActualDate()));
                            agentInfo.setAgent_id(agent.getAgentId());
                            agentInfo.setStr_actual_date(localDateTime2String(agent.getActualDate()));
                            agentInfo.setAgent_status_id(agent.getAgentStatus().getAgentStatusId());
                            agentInfo.setPayload("{...}");
                            agentInfo.setPhone_num(agent.getPhoneNum());
                            agentInfo.setAgent_os_type_id(agent.getAgentOsType().getAgentOsTypeId());
                        })))
        );
    }

    public AgentInfoCollection getAgentHistory(Integer agentId) {

        return create(AgentInfoCollection.class, aic
                -> agentHistRepository
                .findAgentHistory(agentId)
                .stream()
                .forEach(agent -> aic.getAgents().add(
                        create(AGENT_INFO_CLASS, agentInfo -> {
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

            final var agentStatus = refsService.findAgentStatus(AS_TRACKNG);
            final var subscriptionStatus = refsService.findSubscriptionStatus(SS_CONFIRMED);

            final var actualAgents = loadAgents(agentStatus);
            final var actualSubscriptions = userSubscriptionsService.loadAllSubscriptions(subscriptionStatus);
            final Collection<UserSubscriptionHist> histSubscriptions = createCollection();

            final int workLoad = actualSubscriptions.size() / actualAgents.size() + 1;
            final AtomicInteger agentCounter = new AtomicInteger();

            rebalanceAgentsResult.setAgentAmount(actualAgents.size());
            rebalanceAgentsResult.setSubscriptionAmount(actualSubscriptions.size());
            rebalanceAgentsResult.setWorkLoad(workLoad);

            agentCounter.set(0);

            actualAgents.forEach(agent ->
                    actualSubscriptions.stream()
                            .skip((long) agentCounter.getAndIncrement() * workLoad)
                            .limit(workLoad)
                            .forEach(userSubscription -> {

                                log.debug("replace agent ({}->{}) to userSubscription: {} ",
                                        userSubscription.getAgent().getAgentId(),
                                        agent.getAgentId(),
                                        userSubscription.getSubscriptionId());

                                histSubscriptions.add(modelMapper.map(userSubscription, UserSubscriptionHist.class));

                                userSubscription.setAgent(agent);
                                userSubscription.setActualDate(now());

                            })
            );

            userSubscriptionsService.saveUserSubscriptions(actualSubscriptions);
            userSubscriptionsService.saveUserSubscriptionsHist(histSubscriptions);
        });
    }

    //==========================================================================
    @Transactional
    public AgentPayloadInfo updateAgentStatus(String agentPhoneNum, String stringAgentStatus, String stringAgentNote) {

        return create(AGENT_PAYLOAD_INFO_CLASS, payLoadInfo -> {

            final StopWatcher stopWatcher = StopWatcher.create("replaceSubscriptionAgent");

            final AgentStatus newAgentStatus = refsService.findAgentStatus(valueOf(stringAgentStatus));

            assertNotNull(AgentStatus.class, newAgentStatus.getAgentStatusId(), format("Unknown AgentStatus (%s)", stringAgentStatus));

            final Agent agent = findAgentByPhoneNum(agentPhoneNum);

            final Boolean needReplaceAgent = newAgentStatus.getAgentStatusId().equals(AS_BANNED)
                    || newAgentStatus.getAgentStatusId().equals(AS_QUARANTINE);

            log.info("update agent ({}, phoneNum = {}), assign new status ({}->{}, need replace = {})",
                    agent.getAgentId(), agentPhoneNum, agent.getAgentStatus().getAgentStatusId(), newAgentStatus.getAgentStatusId(), needReplaceAgent);

            Assert.isTrue(agentPhoneNum.equals(agent.getPhoneNum()),
                    format("Invalid cache phone num usage (agentId = %d, phoneNum = %s, sphonenum = %s)",
                            agent.getAgentId(), agent.getPhoneNum(), agentPhoneNum));

            // update subscriptions
            ifTrue(needReplaceAgent, ()
                    -> //getting agents client (subscriptions)
            {

                // new agent 4 replace banned
                final Agent newAvailableAgent = findAvailableAgent(agent.getAgentOsType());

                // no available agents
                if (isTrackingAgent.test(newAvailableAgent)) {

                    log.error("no available agents found! ({})".toUpperCase(), localDateTime2long(now()));

                    //payLoadInfo.setAuthKey(null);
                } else {
                    payLoadInfo.setAuthKey(newAvailableAgent.getPhoneNum() + ":" + newAvailableAgent.getPayload());
                    payLoadInfo.setAgentId(newAvailableAgent.getAgentId());
                }

                log.debug("prepare agent replacement ({}->{})", agent.getAgentId(), newAvailableAgent.getAgentId());

                final Collection<UserSubscriptionHist> userSubscriptionHists = createCollection();

                // replace subscriptions
                StmtProcessor.<UserSubscription>processCollection(userSubscriptionsService.findAgentSubscriptions(agent, BOOLEAN_TRUE),
                        userSubscriptions -> {

                            log.debug("replace agents ({}) into {} subscription(s)", agent.getAgentId(), userSubscriptions.size());
                            log.debug("newAvailableAgent: agentId - {}, agentStatusId - {}", newAvailableAgent.getAgentId(), newAvailableAgent.getAgentStatus().getAgentStatusId());
                            log.debug("assign new agent to subscriptions ({}->{})", agent.getAgentId(), newAvailableAgent.getAgentId());

                            userSubscriptions.forEach(ss -> {

                                // history
                                userSubscriptionHists.add(modelMapper.map(ss, UserSubscriptionHist.class));

                                // set new agent
                                ss.setAgent(newAvailableAgent);
                                ss.setActualDate(now());
                            });

                            userSubscriptionsService.saveUserSubscriptions(userSubscriptions);
                            userSubscriptionsService.saveUserSubscriptionsHist(userSubscriptionHists);

                        }
                );

                log.info("update status in replacement agent (agent_id: {}, status: {} -> {})",
                        newAvailableAgent.getAgentId(), newAvailableAgent.getAgentStatus().getAgentStatusId(), agent.getAgentStatus().getAgentStatusId());

                // update history
                saveAgentHist(newAvailableAgent);

                newAvailableAgent.setAgentStatus(refsService.findAgentStatus(agent.getAgentStatus().getAgentStatusId()));
                newAvailableAgent.setActualDate(now());
                // save agent
                saveAgent(newAvailableAgent);

            });

            // check insurance subscriptions
            ifTrue(newAgentStatus.getAgentStatusId().equals(AS_TRACKNG), () ->
                    userSubscriptionsService
                            .findInsuranceSubscriptions(10)
                            .forEach(userSubscription -> {
                                userSubscriptionsService.saveUserSubscriptionHist(userSubscription);
                                userSubscription.setAgent(agent);
                                // save and notify kafka
                                userSubscriptionsService.saveUserSubscription(userSubscription, BOOLEAN_TRUE);
                            })
            );

            // update history
            saveAgentHist(agent);
            agent.setAgentStatus(newAgentStatus);
            agent.setAgentNote(stringAgentNote);
            agent.setActualDate(now());
            // save agent
            saveAgent(agent);

            // new status
            log.info("set new agent status {}, (agent_id = {}, {}, payloadInfo = {}, needReplace = {}, {})",
                    newAgentStatus.getAgentStatusId(),
                    agent.getAgentId(),
                    agent.getPhoneNum(),
                    payLoadInfo,
                    needReplaceAgent,
                    stopWatcher.getStringExecutionTime());

        });
    }

    //==================================================================================================================
    @Override
    public void initialize() {
        super.initialize();

        // create faked insurance agent
        ifTrue(agentRepository.findAgentByAgentStatus(refsService.findAgentStatus(AS_INSURANCE))
                .stream().findAny().isEmpty(), () ->
                agentRepository.saveAndFlush(create(Agent.class, agent -> {

                    final var insuranceStr = "Default insurance bot";

                    agent.setAgentNote(insuranceStr);
                    agent.setPayload(insuranceStr);
                    agent.setPhoneNum("911-911-911");
                    agent.setAgentStatus(refsService.findAgentStatus(AS_INSURANCE));
                    agent.setAgentOsType(refsService.findAgentOsType(OS_BASIC));
                    agent.setActualDate(now());
                    agent.setCreated(now());

                })));

        modelMapper.addConverter(ctx -> findAgentByPhoneNum(ctx.getSource().getAgentPhoneNum()), AgentMessageDto.class, Agent.class);
        modelMapper.addConverter(ctx -> findAgentByPhoneNum(ctx.getSource()), String.class, Agent.class);
        modelMapper.addConverter(ctx -> ctx.getSource().getPhoneNum(), Agent.class, String.class);

        modelMapper.emptyTypeMap(AgentMessageDto.class, AgentMessage.class)
                .addMapping(AgentMessageDto::getAgentPhoneNum, AgentMessage::setAgent)
                .addMapping(AgentMessageDto::getSubscriptionPhoneNum, AgentMessage::setPhoneNum)
                .addMapping(AgentMessageDto::getIsTrackingAllowed, AgentMessage::setIsTrackingAllowed)
                .addMapping(AgentMessageDto::getMessageNote, AgentMessage::setMessageNote);

    }

    @FunctionalInterface
    interface AgentMessageHistBuilder {
        void buildAgentMessageHist(AgentMessage agentMessage);
    }

    final Supplier<AgentMessage> createNewAgentMessage = () -> create(AgentMessage.class, agentMessage -> agentMessage.setCreateDate(now()));

    final BiFunction<AgentMessageDto, AgentMessageHistBuilder, AgentMessage> assignAgentMessageDto = (agentMessageDto, agentMessageHistBuilder) -> {

        final var agentMessage = ofNullable(agentMessageDto.getIsTrackingAllowed())
                .map(allowed -> findAgentMessages(agentMessageDto.getAgentPhoneNum(), agentMessageDto.getSubscriptionPhoneNum())
                        .stream()
                        .max(Comparator.comparing(AgentMessage::getCreateDate))
                        .orElseGet(createNewAgentMessage))
                .orElseGet(createNewAgentMessage);

        agentMessage.setActualDate(now());

        // store history
        ofNullable(agentMessage.getMessageId()).ifPresent(messageId -> agentMessageHistBuilder.buildAgentMessageHist(agentMessage));

        getModelMapper().map(agentMessageDto, agentMessage);

        return agentMessage;
    };

    //==========================================================================
    public Mono<CreatedAgentMessage> createOrUpdateAgentMessage(AgentMessageDto agentMessageDto) {

        return just(create(CreatedAgentMessage.class, CreatedAgentMessage ->

                just(CreatedAgentMessage).subscribe(amd -> {

                    log.debug("createOrUpdateAgentState: {}", agentMessageDto);

                    final var agentMessage = findOrCreateAgentMessage(agentMessageDto, this::createAgentMessageHist);
                    final var isNewSetting = isNull(agentMessage.getMessageId());

                    ifNull(agentMessage.getIsTrackingAllowed(), () -> agentMessage.setIsTrackingAllowed(BOOLEAN_FALSE));

                    agentMessageRepository.saveAndFlush(agentMessage);

                    final String finalAgentMessage = format("AgentMessage is %s (messageId=%d)",
                            isNewSetting ? "created" : "updated",
                            agentMessage.getMessageId());

                    log.debug(finalAgentMessage);

                    CreatedAgentMessage.setNote(finalAgentMessage);
                    CreatedAgentMessage.setAnswerCode(OC_OK);
                    CreatedAgentMessage.setAgentPhoneNum(agentMessageDto.getAgentPhoneNum());
                    CreatedAgentMessage.setSubscriptionPhoneNum(agentMessageDto.getSubscriptionPhoneNum());
                    CreatedAgentMessage.setIsTrackingAllowed(agentMessage.getIsTrackingAllowed());

                }, throwable -> {
                    CreatedAgentMessage.setNote(throwable.getMessage());
                    CreatedAgentMessage.setAnswerCode(OC_GENEARL_ERROR);

                    log.error("{}: {}", throwable.getClass(), CreatedAgentMessage.getNote());

                    throwable.printStackTrace();

                })
        ));
    }

    public AgentMessage findOrCreateAgentMessage(AgentMessageDto agentMessageDto, AgentMessageHistBuilder agentMessageHistBuilder) {
        return assignAgentMessageDto.apply(agentMessageDto, agentMessageHistBuilder);
    }

    public Collection<AgentMessage> findAgentMessages(String agentPhone, String phoneNum) {

        final var agent = agentRepository.findAgentByPhoneNum(agentPhone).orElseThrow(() -> new RuntimeException(format("agent not found(%s)", agentPhone)));

        return agentMessageRepository.findByAgentAndPhoneNum(agent, phoneNum);
    }

    private void createAgentMessageHist(AgentMessage agentMessage) {
        saveAgentMessageHist(getModelMapper().map(agentMessage, AgentMessageHist.class));
    }

    private void saveAgentMessageHist(AgentMessageHist adsSettingsHist) {
        agentMessageHistRepository.saveAndFlush(adsSettingsHist);
    }

    //------------------------------------------------------------------------------------------------------------------
    public Mono<AgentMessageDtoResponse> getAgentMessage(ServerRequest serverRequest) {

        return just(create(AgentMessageDtoResponse.class, agentMessageDtoResponse ->

                just(agentMessageDtoResponse).subscribe(amd -> {

                    agentMessageDtoResponse.setNote(OC_GENERAL_ERROR_STR);
                    agentMessageDtoResponse.setAnswerCode(OC_GENEARL_ERROR);

                    final var agentPhone = getOptionalStringFromParam(serverRequest, QP_AGENT_PHONE);
                    final var subscriptionPhone = getOptionalStringFromParam(serverRequest, QP_PHONE);
                    final var errorMsgBuilder = new StringBuilder(128);

                    ifTrue(agentPhone.isEmpty(), () -> errorMsgBuilder.append(format("%s: Query param '%s' not defined; ", serverRequest.path(), QP_AGENT_PHONE)));
                    ifTrue(subscriptionPhone.isEmpty(), () -> errorMsgBuilder.append(format("%s: Query param '%s' not defined; ", serverRequest.path(), QP_PHONE)));

                    ifTrue(!errorMsgBuilder.isEmpty(), () -> {
                        agentMessageDtoResponse.setNote(errorMsgBuilder.toString());
                        agentMessageDtoResponse.setAnswerCode(OC_GENEARL_ERROR);
                        log.error(errorMsgBuilder.toString());
                    }, () -> {

                        final var agentMessages = findAgentMessages(agentPhone, subscriptionPhone);
                        final Supplier<Optional<AgentMessage>> defSupplier = () -> agentMessages.stream().max(Comparator.comparing(AgentMessage::getCreateDate));

                        final var optionalAgentMessage = agentMessages
                                .stream()
                                .filter(AgentMessage::getIsTrackingAllowed)
                                .findFirst()
                                .or(defSupplier);

                        log.debug("getAgentMessage: agentPhone={}; subscriptionPhone={}; agentMessage: {}",
                                agentPhone, subscriptionPhone, optionalAgentMessage.isPresent() ? optionalAgentMessage.get().getMessageId() : null);

                        agentMessageDtoResponse.setAgentPhoneNum(agentPhone);
                        agentMessageDtoResponse.setSubscriptionPhoneNum(subscriptionPhone);

                        optionalAgentMessage.ifPresentOrElse(agentMessage -> {
                            agentMessageDtoResponse.setCreateDate(localDateTime2long(agentMessage.getCreateDate()));
                            agentMessageDtoResponse.setMessageNote(agentMessage.getMessageNote());
                            agentMessageDtoResponse.setNote(format("found messageId: %d", agentMessage.getMessageId()));
                            agentMessageDtoResponse.setIsTrackingAllowed(agentMessage.getIsTrackingAllowed());
                        }, () -> {
                            agentMessageDtoResponse.setNote("no message found".toUpperCase());
                            agentMessageDtoResponse.setMessageNote(agentMessageDtoResponse.getNote());
                            agentMessageDtoResponse.setIsTrackingAllowed(BOOLEAN_FALSE);
                        });

                        agentMessageDtoResponse.setAnswerCode(OC_OK);

                    });
                }, throwable -> {

                    agentMessageDtoResponse.setMessageNote(throwable.getMessage());
                    agentMessageDtoResponse.setNote(throwable.getMessage());
                    agentMessageDtoResponse.setAnswerCode(OC_GENEARL_ERROR);

                    log.error("{}: {}", throwable.getClass(), agentMessageDtoResponse.getMessageNote());

                    throwable.printStackTrace();

                })
        ));
    }

    //------------------------------------------------------------------------------------------------------------------
    public Mono<ActualMessagesCountDtoResponse> getActualMessagesCount(ServerRequest serverRequest) {

        return just(create(ActualMessagesCountDtoResponse.class, actualMessagesCountDtoResponse ->

                just(actualMessagesCountDtoResponse).subscribe(amd -> {

                    actualMessagesCountDtoResponse.setNote(OC_GENERAL_ERROR_STR);
                    actualMessagesCountDtoResponse.setAnswerCode(OC_GENEARL_ERROR);

                    final var subscriptionPhone = getOptionalStringFromParam(serverRequest, QP_PHONE);
                    final var errorMsgBuilder = new StringBuilder(128);

                    ifTrue(subscriptionPhone.isEmpty(), () -> errorMsgBuilder.append(format("%s: Query param '%s' not defined; ", serverRequest.path(), QP_PHONE)));

                    ifTrue(!errorMsgBuilder.isEmpty(), () -> {
                        actualMessagesCountDtoResponse.setNote(errorMsgBuilder.toString());
                        actualMessagesCountDtoResponse.setAnswerCode(OC_GENEARL_ERROR);
                        log.error(errorMsgBuilder.toString());
                    }, () -> {

                        final var actualMessagesCount = agentMessageRepository.getActualMessagesCount(subscriptionPhone);

                        log.debug("getActualMessagesCount: subscriptionPhone={}; actualMessagesCount= {}", subscriptionPhone, actualMessagesCount);

                        actualMessagesCountDtoResponse.setMessagesCount(actualMessagesCount);
                        actualMessagesCountDtoResponse.setNote(format("found %d messages", actualMessagesCount));
                        actualMessagesCountDtoResponse.setAnswerCode(OC_OK);

                    });
                }, throwable -> {

                    actualMessagesCountDtoResponse.setNote(throwable.getMessage());
                    actualMessagesCountDtoResponse.setAnswerCode(OC_GENEARL_ERROR);

                    log.error("{}: {}", throwable.getClass(), actualMessagesCountDtoResponse.getNote());

                    throwable.printStackTrace();

                })
        ));
    }

    //------------------------------------------------------------------------------------------------------------------
    public Mono<MessagesPhoneNumsDtoResponse> getMessagingSubscriptions(ServerRequest serverRequest) {

        return just(create(MessagesPhoneNumsDtoResponse.class, messagesPhoneNumsDtoResponse ->

                just(messagesPhoneNumsDtoResponse).subscribe(amd -> {

                    messagesPhoneNumsDtoResponse.setNote(OC_GENERAL_ERROR_STR);
                    messagesPhoneNumsDtoResponse.setAnswerCode(OC_GENEARL_ERROR);

                    final var agentPhone = getOptionalStringFromParam(serverRequest, QP_AGENT_PHONE);
                    final var errorMsgBuilder = new StringBuilder(128);

                    ifTrue(agentPhone.isEmpty(), () -> errorMsgBuilder.append(format("%s: Query param '%s' not defined; ", serverRequest.path(), QP_AGENT_PHONE)));

                    ifTrue(!errorMsgBuilder.isEmpty(), () -> {
                        messagesPhoneNumsDtoResponse.setNote(errorMsgBuilder.toString());
                        messagesPhoneNumsDtoResponse.setAnswerCode(OC_GENEARL_ERROR);
                        log.error(errorMsgBuilder.toString());
                    }, () -> {

                        final var agent = agentRepository.findAgentByPhoneNum(agentPhone).orElseThrow(() -> new RuntimeException(format("agent not found(%s)", agentPhone)));
                        final var phoneNums = agentMessageRepository.getMessagingSubscriptions(agent.getAgentId());

                        log.debug("getMessagingSubscriptions: agentPhone={}; actualMessagesCount= {}", agent.getPhoneNum(), phoneNums.size());

                        messagesPhoneNumsDtoResponse.setPhoneNums(phoneNums);
                        messagesPhoneNumsDtoResponse.setNote(format("found %d subscriptions", phoneNums.size()));
                        messagesPhoneNumsDtoResponse.setAnswerCode(OC_OK);

                    });
                }, throwable -> {

                    messagesPhoneNumsDtoResponse.setNote(throwable.getMessage());
                    messagesPhoneNumsDtoResponse.setAnswerCode(OC_GENEARL_ERROR);

                    log.error("{}: {}", throwable.getClass(), messagesPhoneNumsDtoResponse.getNote());

                    throwable.printStackTrace();

                })
        ));
    }

    //------------------------------------------------------------------------------------------------------------------
    public Mono<AgentLatestMessagesDtoResponse> getMessagingLastMessageCache(ServerRequest serverRequest) {

        return just(create(AgentLatestMessagesDtoResponse.class, agentLatestMessagesDtoResponse ->

                just(agentLatestMessagesDtoResponse).subscribe(amd -> {

                    agentLatestMessagesDtoResponse.setNote(OC_GENERAL_ERROR_STR);
                    agentLatestMessagesDtoResponse.setAnswerCode(OC_GENEARL_ERROR);

                    final var phoneNums = agentMessageRepository.getMessagingLastMessageCache()
                            .stream()
                            .map(pn -> create(AgentLatestMessageDto.class, messagesLastCache -> {
                                messagesLastCache.setAgentPhone(pn.getAgentPhone());
                                messagesLastCache.setAgentCreateDate(localDateTime2long(pn.getAgentCreateDate()));
                                messagesLastCache.setCreateDate(localDateTime2long(pn.getCreateDate()));
                            }))
                            .collect(Collectors.toList());

                    log.debug("getMessagingLastMessageCache: {} records", phoneNums.size());

                    agentLatestMessagesDtoResponse.setAgentLatestMessageDtos(phoneNums);
                    agentLatestMessagesDtoResponse.setNote(format("found %d records", phoneNums.size()));
                    agentLatestMessagesDtoResponse.setAnswerCode(OC_OK);

                }, throwable -> {

                    agentLatestMessagesDtoResponse.setNote(throwable.getMessage());
                    agentLatestMessagesDtoResponse.setAnswerCode(OC_GENEARL_ERROR);

                    log.error("{}: {}", throwable.getClass(), agentLatestMessagesDtoResponse.getNote());

                    throwable.printStackTrace();

                })
        ));
    }
}
