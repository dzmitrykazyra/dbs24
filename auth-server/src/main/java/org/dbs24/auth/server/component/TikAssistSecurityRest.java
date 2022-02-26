/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.auth.server.component;

import lombok.extern.log4j.Log4j2;
import org.dbs24.auth.server.entity.tik_assist.FacebookTikAssistUser;
import org.dbs24.auth.server.entity.tik_assist.FacebookVerificationResponse;
import org.dbs24.auth.server.entity.tik_assist.GoogleTikAssistUser;
import org.dbs24.auth.server.entity.tik_assist.GoogleVerificationResponse;
import org.dbs24.component.AuthenticationManager;
import org.dbs24.component.JwtSecurityService;
import org.dbs24.component.LoginReactor;
import org.dbs24.component.SecurityRest;
import org.dbs24.stmt.StmtProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Log4j2
@Component
@ConditionalOnProperty(name = "config.restfull.security.tik-assist.enabled", havingValue = "true")
public class TikAssistSecurityRest extends SecurityRest {

    @Value("${config.restfull.security.tik-assist.jwt.expiration-time:28800}")
    protected Long expirationTime;

    final OAuth2Service oAuth2Service;

    @Autowired
    public TikAssistSecurityRest(GenericApplicationContext genericApplicationContext, JwtSecurityService jwtUtils, LoginReactor loginReactor, AuthenticationManager authenticationManager, OAuth2Service oAuth2Service) {

        super(genericApplicationContext, jwtUtils, loginReactor, authenticationManager);

        this.oAuth2Service = oAuth2Service;
    }

    public Mono<ServerResponse> getTikUserJwtByGoogle(ServerRequest request) {

        return processServerRequest(
                request,
                GoogleTikAssistUser.class,
                GoogleVerificationResponse.class,
                oAuth2Service::getGoogleVerificationResponse
        );
    }

    public Mono<ServerResponse> getTikUserJwtFacebook(ServerRequest request) {

        return this.<FacebookTikAssistUser, FacebookVerificationResponse>createResponse(
                request,
                FacebookTikAssistUser.class,
                FacebookVerificationResponse.class,
                user -> StmtProcessor.create(
                        FacebookVerificationResponse.class,
                        response -> oAuth2Service.getFacebookVerificationResponse(user)
                ));
    }
}
