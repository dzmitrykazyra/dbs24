/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.component;

import org.dbs24.stmt.StmtProcessor;
import java.time.LocalDateTime;
import java.util.Optional;
import lombok.extern.log4j.Log4j2;
import static org.dbs24.consts.RestHttpConsts.*;
import static org.dbs24.consts.SysConst.*;
import static org.dbs24.consts.SecurityConst.*;
import static org.dbs24.rest.api.ReactiveRestProcessor.httpOk;
import org.dbs24.rest.api.ReactiveRestProcessor;
import org.dbs24.rest.api.*;
import org.springframework.stereotype.Component;
import org.dbs24.application.core.service.funcs.SysEnvFuncs;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.support.GenericApplicationContext;
import org.dbs24.exception.RemoteShoutdownIsNotAllowed;
import static org.dbs24.rsocket.api.MessageType.MONITORING_LIVENESS;
import static org.dbs24.spring.boot.api.AbstractSpringBootApplication.monitoringRSocketService;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.annotation.EnableAsync;

@Log4j2
@Component
@EnableScheduling
@EnableAsync
public class CommonRest extends ReactiveRestProcessor {

    @Value("${remote.shoutdown.allowed:true}")
    private Boolean remoteShoutDownAllowed;

    @Value("${remote.shoutdown.delay:3000}")
    private Integer remoteShoutDelay;

    final GenericApplicationContext genericApplicationContext;

    //==========================================================================    
    @Autowired
    public CommonRest(GenericApplicationContext genericApplicationContext) {
        this.genericApplicationContext = genericApplicationContext;
    }

    //==========================================================================
    public Mono<ServerResponse> liveness(ServerRequest request) {

        return this.<Void, SystemInfo>processServerRequest(request, VOID_CLASS,
                noEntity
                -> StmtProcessor.create(SYSTEM_INFO_CLASS,
                        object -> object.setSysInfo(buildLiveNessRec())), httpOk);
    }

    //==========================================================================
    @Scheduled(fixedRate = 600000)
    private String buildLiveNessRec() {
        final String responseString = String.format("%s: I am fine, now is %s, "
                + "%s",
                URI_LIVENESS,
                LocalDateTime.now().toString(),
                SysEnvFuncs.getMemoryStatistics()
        );
        log.info(responseString);

        monitoringRSocketService.send(MONITORING_LIVENESS, responseString);

        return responseString;
    }

    //==========================================================================
    public Mono<ServerResponse> readiness(ServerRequest request) {

        return this.<Void, IamReady>processServerRequest(request, VOID_CLASS,
                noEntity -> {

                    final String responseString = String.format("%s: I am ready",
                            URI_READINESS);

                    log.info(responseString);

                    return StmtProcessor.create(I_AM_READY_CLASS,
                            object -> object.setIsReady(BOOLEAN_TRUE));
                }, httpOk);
    }

    //==========================================================================
    public Mono<ServerResponse> shoutdown(ServerRequest request) {

        return this.<Void, SystemExit>processServerRequest(request, VOID_CLASS,
                noEntity -> {

                    Optional.ofNullable(remoteShoutDownAllowed ? BOOLEAN_TRUE : OBJECT_NULL)
                            .orElseThrow(() -> new RemoteShoutdownIsNotAllowed("Remote shoutdown is not allowed"));

                    final String responseString = "Shutting down, bye...";

                    final SystemExit systemExit = StmtProcessor.create(SystemExit.class,
                            object -> object.setExitMessage(responseString));

                    log.info(responseString);

                    new Thread(() -> {
                        StmtProcessor.execute(() -> Thread.sleep(remoteShoutDelay));
                        genericApplicationContext.clearResourceCaches();
                        genericApplicationContext.close();
                        //SpringApplication.exit(genericApplicationContext, () -> 0);
                        System.exit(0);
                    }).start();

                    return systemExit;
                }, httpOk
        );
    }
}
