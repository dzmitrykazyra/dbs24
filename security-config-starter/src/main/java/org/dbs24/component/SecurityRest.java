/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.component;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import org.dbs24.application.core.locale.NLS;
import org.dbs24.application.core.service.funcs.ServiceFuncs;
import org.dbs24.rest.api.*;
import org.dbs24.stmt.StmtProcessor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.Map;

import static org.dbs24.consts.SecurityConst.LOGIN_DETAILS_CLASS;
import static org.dbs24.consts.SysConst.LONG_ZERO;
import static org.dbs24.stmt.StmtProcessor.assertNotNull;

@Getter
@Log4j2
@Component
@ConditionalOnProperty(name = "config.restfull.security.profile.name", havingValue = "standard", matchIfMissing = true)
@EqualsAndHashCode(callSuper = true)
public class SecurityRest extends ReactiveRestProcessor {

    final GenericApplicationContext genericApplicationContext;
    final JwtSecurityService jwtSecurityService;
    final LoginReactor loginReactor;
    final AuthenticationManager authenticationManager;

    //==========================================================================
    public SecurityRest(GenericApplicationContext genericApplicationContext, JwtSecurityService jwtSecurityService, LoginReactor loginReactor, AuthenticationManager authenticationManager) {
        this.genericApplicationContext = genericApplicationContext;
        this.jwtSecurityService = jwtSecurityService;
        this.loginReactor = loginReactor;
        this.authenticationManager = authenticationManager;
    }

    //==========================================================================
    public Mono<ServerResponse> login(ServerRequest request) {
        return processServerRequest(request, LOGIN_DETAILS_CLASS, this::doLogin);
    }

    //==========================================================================
    protected LoginResult doLogin(LoginDetails loginDetails) {

        if (true == true) {
            throw new RuntimeException("Illegal call method");
        }

        return StmtProcessor.create(LoginResult.class, lr -> {

            assertNotNull(LoginDetails.class, loginDetails, "login details");
            assertNotNull(LoginDetails.class, loginDetails.getUid(), "login uid");
            assertNotNull(LoginDetails.class, loginDetails.getPwd(), "login pwd");

            log.debug("login user '{}'", loginDetails.getUid());

            final String jwt;

            // using auth server
            //checking users
            // create new token
            final Map<String, String> claims = ServiceFuncs.createMap();

            claims.put("userId", loginDetails.getUid());

            //final String jwt = jwtGenerator.createJwtForClaims(loginDetails.getUid(), claims);
            jwt = jwtSecurityService.generateToken(loginDetails.getUid(), claims,  LONG_ZERO);

            lr.setToken(jwt);

            loginReactor.emitEvent(lr);
        });
    }

    //==========================================================================
    protected PostResult doRegisterToken(TokenInfo tokenInfo) {

        return StmtProcessor.create(PostResult.class, pr -> {

            pr.setCode("0.0");

            authenticationManager.addToken(tokenInfo.getTokenCard(), NLS.long2LocalDateTime(tokenInfo.getValidUntil()));

            //throw new RuntimeException("Not implemented");
        });
    }

    //==========================================================================
    public Mono<ServerResponse> registerToken(ServerRequest request) {
        return processServerRequest(request, TokenInfo.class, this::doRegisterToken);
    }
}
