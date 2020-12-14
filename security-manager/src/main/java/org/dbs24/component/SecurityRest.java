/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.component;

import java.time.LocalDateTime;
import lombok.extern.log4j.Log4j2;
import org.dbs24.application.core.nullsafe.NullSafe;
import org.dbs24.application.core.service.funcs.SysEnvFuncs;
import static org.dbs24.consts.RestHttpConsts.*;
import static org.dbs24.consts.SecurityConst.SYSTEM_INFO_CLASS;
import static org.dbs24.consts.SysConst.VOID_CLASS;
import org.dbs24.rest.api.ReactiveRestProcessor;
import static org.dbs24.rest.api.ReactiveRestProcessor.httpOk;
import org.dbs24.rest.api.SystemInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Log4j2
@Component
public class SecurityRest extends ReactiveRestProcessor {

    @Value("${remote.auth.create.users:false}")
    private Boolean allowCreateUsers;

    final GenericApplicationContext genericApplicationContext;

    //==========================================================================    
    @Autowired
    public SecurityRest(GenericApplicationContext genericApplicationContext) {
        this.genericApplicationContext = genericApplicationContext;
    }

    //==========================================================================
    public Mono<ServerResponse> authCreateUser(ServerRequest request) {

        return this.<Void, SystemInfo>processServerRequest(request, VOID_CLASS,
                noEntity -> {

                    final String responseString = String.format("%s: I am fine, now is %s \n"
                            + "%s\n",
                            URI_LIVENESS,
                            LocalDateTime.now().toString(),
                            SysEnvFuncs.getMemoryStatistics()
                    );

                    log.info(responseString);

                    return NullSafe.createObject(SYSTEM_INFO_CLASS,
                            object -> object.setSysInfo(responseString));
                }, httpOk);
    }
    //==========================================================================
    public Mono<ServerResponse> authCreateRole(ServerRequest request) {

        return this.<Void, SystemInfo>processServerRequest(request, VOID_CLASS,
                noEntity -> {

                    final String responseString = String.format("%s: I am fine, now is %s \n"
                            + "%s\n",
                            URI_LIVENESS,
                            LocalDateTime.now().toString(),
                            SysEnvFuncs.getMemoryStatistics()
                    );

                    log.info(responseString);

                    return NullSafe.createObject(SYSTEM_INFO_CLASS,
                            object -> object.setSysInfo(responseString));
                }, httpOk);
    }
}
