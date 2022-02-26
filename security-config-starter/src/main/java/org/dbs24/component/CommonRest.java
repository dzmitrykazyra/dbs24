/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.component;

import lombok.EqualsAndHashCode;
import lombok.extern.log4j.Log4j2;
import org.dbs24.application.core.locale.NLS;
import org.dbs24.application.core.nullsafe.StopWatcher;
import org.dbs24.application.core.service.funcs.GetNetworkAddress;
import org.dbs24.application.core.service.funcs.SysEnvFuncs;
import org.dbs24.exception.RemoteShutdownIsNotAllowed;
import org.dbs24.rest.api.*;
import org.dbs24.stmt.StmtProcessor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.dbs24.consts.RestHttpConsts.*;
import static org.dbs24.consts.SecurityConst.SYSTEM_INFO_CLASS;
import static org.dbs24.consts.SysConst.*;

@Log4j2
@Component
@EnableScheduling
@EnableAsync
@ConditionalOnProperty(name = "config.restfull.common-routes.enabled", havingValue = "true", matchIfMissing = true)
@EqualsAndHashCode(callSuper = true)
public class CommonRest extends ReactiveRestProcessor {

    @Value("${remote.Shutdown.allowed:true}")
    private Boolean remoteShutDownAllowed;

    @Value("${remote.Shutdown.delay:3000}")
    private Integer remoteShutDelay;

    final GenericApplicationContext genericApplicationContext;

    public CommonRest(GenericApplicationContext genericApplicationContext) {
        this.genericApplicationContext = genericApplicationContext;
    }

    final StopWatcher stopWatcher = StopWatcher.create("Server uptime");

    //==========================================================================
    public Mono<ServerResponse> liveness(ServerRequest request) {

        return this.<Void, SystemInfo>createResponse(request, VOID_CLASS, SystemInfo.class,
                noEntity -> buildLiveNessRecord());
    }

    //==========================================================================
    public Mono<ServerResponse> startTime(ServerRequest request) {

        return this.<Void, ServerStartTimeInfo>createResponse(request, VOID_CLASS, ServerStartTimeInfo.class,
                noEntity -> buildServerStartTimeRecord());
    }

    //    stopWatcher.getStartDateTime(),
//                        stopWatcher.getStringExecutionTime("Server uptime"),
    //==========================================================================
    @Scheduled(fixedRateString = "${security.manager.liveness.interval:1000000}")
    protected String buildDefaultLiveNessRecord() {
        final String responseString = String.format("%s: Server start time: %s; i am fine, now is %s, "
                        + "%s, %s",
                URI_LIVENESS,
                stopWatcher.getStringStartDateTime(),
                NLS.localDateTime2String(LocalDateTime.now()),
                GetNetworkAddress.getAllAddresses(),
                SysEnvFuncs.getMemoryStatistics()
        );
        log.info(responseString);

        //monitoringRSocketService.emitEvent(ServiceFuncs.createMap(MONITORING_LIVENESS, responseString));
        return responseString;
    }

    //==========================================================================
    public Mono<ServerResponse> readiness(ServerRequest request) {

        return this.<Void, IamReady>createResponse(request, VOID_CLASS, IamReady.class,
                noEntity -> {

                    final String responseString = String.format("%s: I am ready",
                            URI_READINESS);

                    log.info(responseString);

                    return StmtProcessor.create(IamReady.class,
                            object -> object.setIsReady(BOOLEAN_TRUE));
                });
    }

    //==========================================================================
    public Mono<ServerResponse> shutdown(ServerRequest request) {

        return this.<Void, SystemExit>createResponse(request, VOID_CLASS, SystemExit.class,
                noEntity -> {

                    Optional.ofNullable(remoteShutDownAllowed ? BOOLEAN_TRUE : OBJECT_NULL)
                            .orElseThrow(() -> new RemoteShutdownIsNotAllowed("Remote Shutdown is not allowed"));

                    final ShutdownRequest sr = ready4ShutDown();

                    log.debug(sr);

                    Optional.ofNullable(sr.getCanShutDown() ? BOOLEAN_TRUE : OBJECT_NULL)
                            .orElseThrow(() -> new RemoteShutdownIsNotAllowed(sr.getStatus()));

                    final String responseString = "Shutting down, bye...";

                    final SystemExit systemExit = StmtProcessor.create(SystemExit.class,
                            object -> object.setExitMessage(responseString));

                    log.info(responseString);

                    StmtProcessor.runNewThread(() -> {
                        StmtProcessor.execute(() -> Thread.sleep(remoteShutDelay));
                        genericApplicationContext.clearResourceCaches();
                        genericApplicationContext.close();
                        //SpringApplication.exit(genericApplicationContext, () -> 0);
                        System.exit(0);
                    });

                    return systemExit;
                });
    }

    //==========================================================================
    protected ShutdownRequest ready4ShutDown() {
        return StmtProcessor.create(ShutdownRequest.class, sr -> {

            sr.setStatus("can Shutdown");
            sr.setCanShutDown(true);

        });
    }

    //==========================================================================
    public Mono<ServerResponse> canShutdown(ServerRequest request) {

        return this.<Void, ShutdownRequest>createResponse(request, VOID_CLASS, ShutdownRequest.class,
                noEntity -> remoteShutDownAllowed ? ready4ShutDown() : StmtProcessor.create(SHUTDOWN_REQUEST_CLASS, sr -> {

                    sr.setStatus("Remote Shutdown is not allowed");
                    sr.setCanShutDown(false);

                }));
    }

    //==========================================================================
    protected SystemInfo buildLiveNessRecord() {
        return StmtProcessor.create(SYSTEM_INFO_CLASS,
                object -> {
                    object.setSysInfo(buildDefaultLiveNessRecord());
                    object.setServerStringStartTime(NLS.localDateTime2String(stopWatcher.getStartDateTime()));
                    object.setServerLongStartTime(NLS.localDateTime2long(stopWatcher.getStartDateTime()));
                    object.setServerUpTime(stopWatcher.getStringExecutionTime("Server uptime"));
                    object.setServerLongCurrentTime(NLS.localDateTime2long(LocalDateTime.now()));
                });
    }

    //==========================================================================
    protected ServerStartTimeInfo buildServerStartTimeRecord() {
        return StmtProcessor.create(ServerStartTimeInfo.class,
                object -> {
                    object.setServerLongStartTime(NLS.localDateTime2long(stopWatcher.getStartDateTime()));
                    object.setServerStringStartTime(NLS.localDateTime2String(stopWatcher.getStartDateTime()));
                    object.setServerUpTime(stopWatcher.getStringExecutionTime("Server uptime"));
                });
    }
}
