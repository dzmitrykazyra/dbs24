package org.dbs24.impl.service;

import lombok.extern.log4j.Log4j2;
import org.dbs24.application.core.nullsafe.StopWatcher;
import org.dbs24.impl.api.ServerState;
import org.dbs24.kafka.RegistryApplication;
import org.dbs24.kafka.ShutDownApplication;
import org.dbs24.spring.core.api.AbstractApplicationService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Comparator;
import java.util.function.Function;
import java.util.function.Predicate;

import static java.time.LocalDateTime.now;
import static org.dbs24.application.core.locale.NLS.localDateTime2long;
import static org.dbs24.application.core.service.funcs.ServiceFuncs.createConcurencyCollection;
import static org.dbs24.consts.RestHttpConsts.URI_READINESS;
import static org.dbs24.consts.SysConst.*;
import static org.dbs24.impl.api.MultiApiConsts.Consumers.ATTEMPTS_RETRY;
import static org.dbs24.impl.api.MultiApiConsts.Consumers.PRODUCER_GROUP_ID;
import static org.dbs24.impl.api.MultiApiConsts.KafkaTopics.KAFKA_REGISTRY_APPLICATION;
import static org.dbs24.impl.api.MultiApiConsts.KafkaTopics.KAFKA_SHUTDOWN_APPLICATION;
import static org.dbs24.stmt.StmtProcessor.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;

@Log4j2
public abstract class ApplicationProcessor extends AbstractApplicationService {

    final Collection<ServerState> servers = createConcurencyCollection();
    final Collection<RegistryApplication> serverStatesUpdates = createConcurencyCollection();

    final Predicate<ServerState> isReady = ServerState::getIsReady;
    final Predicate<ServerState> passed4limit = server -> server.getUsersProceed() < server.getUserCapacity();
    final Predicate<ServerState> legalDeadLine = server -> localDateTime2long(now()) < server.getRebootDeadLine();
    final Comparator<ServerState> proceed = (a, b) -> a.getLastProceed().compareTo(b.getLastProceed());
    final Function<ServerState, String> state2server = serverState -> {
        serverState.setUsersProceed(serverState.getUsersProceed() + 1);
        serverState.setLastProceed(now());
        log.info("{}: assign new user ( total assigned: {})", serverState.getAddress(), serverState.getUsersProceed());
        return serverState.getAddress();
    };

    public abstract void registerServer(ServerState serverState);

    public abstract void registerServer(ServerState serverState, String note);

    public abstract Collection<ServerState> initServersList();

    @KafkaListener(id = KAFKA_REGISTRY_APPLICATION, groupId = PRODUCER_GROUP_ID, topics = KAFKA_REGISTRY_APPLICATION)
    public void registryServer(Collection<RegistryApplication> servers) {

        serverStatesUpdates.addAll(servers);
    }

    @KafkaListener(id = KAFKA_SHUTDOWN_APPLICATION, groupId = PRODUCER_GROUP_ID, topics = KAFKA_SHUTDOWN_APPLICATION)
    public void stopServer(Collection<ShutDownApplication> shoutDownServers) {

        shoutDownServers.forEach(server ->
                servers.stream()
                        .filter(existServ -> existServ.getAddress().equals(server.getAddress()) &&
                                existServ.getPid().equals(server.getPid()))
                        .findFirst()
                        .ifPresent(es -> {
                            es.setIsReady(BOOLEAN_FALSE);
                            es.setIsShoutDown(BOOLEAN_TRUE);
                            es.setNote(server.getNote());
                        })
        );
    }

    @Scheduled(fixedRateString = "${config.kafka.processing-interval:3000}", cron = "${config.kafka.processing-cron:}")
    public void registryNewServer() {

        ifTrue(!serverStatesUpdates.isEmpty(),
                () -> serverStatesUpdates.forEach(reg -> {

                    log.info("{}: process server: {}", KAFKA_REGISTRY_APPLICATION, reg);

                    servers
                            .stream()
                            .filter(server -> server.getAddress().equals(reg.getAddress())
                                    && server.getPid().equals(reg.getPid()))
                            .findFirst()
                            .ifPresentOrElse(serverState -> {
                                // update exist
                                log.warn("update existing server: {}", reg);

                                ifNotNull(reg.getApplicationName(), () -> serverState.setApplicationName(reg.getApplicationName()));
                                ifNotNull(reg.getCountryCode(), () -> serverState.setCountryCode(reg.getCountryCode()));

                                serverState.setPid(reg.getPid());
                                serverState.setIsReady(BOOLEAN_FALSE);
                                serverState.setNextReboot(reg.getNextReboot());
                                serverState.setRebootDeadLine(reg.getRebootDeadLine());

                            }, () -> servers.add(create(ServerState.class, serverState -> {

                                log.info("registry new server: {}", reg);

                                serverState.setPid(reg.getPid());
                                serverState.setAddress(reg.getAddress());
                                serverState.setApplicationName(reg.getApplicationName());
                                serverState.setLastProceed(LocalDateTime.MIN);
                                serverState.setUserCapacity(reg.getUserCapacity());
                                serverState.setUsersProceed(INTEGER_ZERO);
                                serverState.setAttemptRetry(ATTEMPTS_RETRY);
                                serverState.setIsShoutDown(BOOLEAN_FALSE);
                                serverState.setNextReboot(reg.getNextReboot());
                                serverState.setRebootDeadLine(reg.getRebootDeadLine());
                                serverState.setCountryCode(reg.getCountryCode());

                                registerServer(serverState);

                            })));

                    serverStatesUpdates.remove(reg);

                }));
    }

    @Scheduled(fixedRateString = "${config.kafka.processing-interval:5000}", cron = "${config.kafka.processing-cron:}")
    public void balanceServers() {

        ifTrue(!servers.isEmpty(),
                () -> {

                    // stopped servers
                    servers
                            .stream()
                            .filter(ServerState::getIsShoutDown)
                            .forEach(server -> {

                                log.warn("{}, {}: server {} is shutdown ({})",
                                        server.getPid(),
                                        server.getAddress(),
                                        server.getApplicationName(),
                                        server.getNote());
                                servers.remove(server);

                                registerServer(server);

                            });

                    servers
                            .stream()
                            .filter(server -> server.getAttemptRetry() > 0)
                            .forEach(server ->

                                    ifNull(server.getWebClient(), () -> {

                                        // init webClient
                                        log.info("{}: initialize webClient", server.getAddress());
                                        server.setWebClient(setupSSL(WebClient.builder(), server.getAddress())
                                                .baseUrl(server.getAddress())
                                                .exchangeStrategies(ExchangeStrategies.builder().build())
                                                .build());

                                    }, () -> {

                                        final StopWatcher stopWatcher = StopWatcher.create("Server uptime");
                                        final String fullAddress = server.getAddress() + URI_READINESS;

                                        // do test ping
                                        server.getWebClient()
                                                .get()
                                                .uri(uriBuilder
                                                        -> uriBuilder
                                                        .path(URI_READINESS)
                                                        .build())
                                                .accept(APPLICATION_JSON)
                                                .retrieve()
                                                .bodyToMono(Object.class)
                                                .doOnError(throwable -> {

                                                    // Flag - server not ready
                                                    server.setIsReady(BOOLEAN_FALSE);
                                                    server.setAttemptRetry(server.getAttemptRetry() - 1);

                                                    // register server state
                                                    registerServer(server, throwable.getMessage());

                                                    log.error("{}: {}, attemptsRemain = {}",
                                                            fullAddress,
                                                            throwable.getMessage(),
                                                            server.getAttemptRetry());

                                                }).subscribe(o -> {

                                                    //final Long execTime = stopWatcher.getExecutionTime();
                                                    server.setAttemptRetry(ATTEMPTS_RETRY);

                                                    log.debug("{}, {}: exec ping time: {}", server.getPid(), fullAddress, stopWatcher.getStringExecutionTime());

                                                    server.setIsReady(BOOLEAN_TRUE);

                                                });
                                    })
                            );

                    // invalid servers
                    servers
                            .stream()
                            .filter(server -> server.getAttemptRetry() <= 0)
                            .forEach(server -> {

                                log.warn("{}, {}: stop processing", server.getPid(), server.getAddress());
                                servers.remove(server);

                                registerServer(server);

                            });
                });
    }

    public String getAvailableServer() {

        log.info("servers: {}", servers);

        return servers.stream()
                .filter(isReady)
                .filter(passed4limit)
                .filter(legalDeadLine)
                .sorted(proceed)
                .findFirst()
                .map(state2server)
                .orElseGet(this::getDeadlineServer);
    }

    public String getDeadlineServer() {

        log.warn("find deadline server: {}", servers);

        final String deadLineServer = servers.stream()
                .filter(isReady)
                .sorted(proceed)
                .findFirst()
                .map(state2server)
                .orElse(EMPTY_STRING);

        log.warn("found deadline server: '{}'", deadLineServer);

        return deadLineServer;
    }

    @Override
    public void initialize() {

        servers.addAll(initServersList());

        super.initialize();
    }
}
