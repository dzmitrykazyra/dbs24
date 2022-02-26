/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.auth.server.wa;

import lombok.EqualsAndHashCode;
import lombok.extern.log4j.Log4j2;
import org.dbs24.application.core.service.funcs.ServiceFuncs;
import org.dbs24.auth.server.api.WaUserSessionInfo;
import org.dbs24.auth.server.component.JwtService;
import org.dbs24.component.AuthenticationManager;
import org.dbs24.component.JwtSecurityService;
import org.dbs24.component.LoginReactor;
import org.dbs24.component.SecurityRest;
import org.dbs24.rest.api.LoginResult;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.Collection;
import java.util.Map;

import static java.lang.String.format;
import static java.time.LocalDateTime.now;
import static org.dbs24.auth.server.consts.AuthConsts.Applications.WA_MONITORING;
import static org.dbs24.consts.SysConst.EMPTY_STRING;
import static org.dbs24.consts.SysConst.STRING_SPACE;
import static org.dbs24.rest.api.consts.RestApiConst.OperCode.*;
import static org.dbs24.stmt.StmtProcessor.*;

@Log4j2
@Component
@ConditionalOnProperty(name = "config.restfull.security.wa-monitoring.enabled", havingValue = "true")
@EqualsAndHashCode(callSuper = true)
public class WaSecurityRest extends SecurityRest {

    final JwtService jwtService;
    final WaManagedServers waManagedServers;

    @Value("${config.restfull.security.wa-monitoring.jwt.min-key-length:15}")
    protected Integer minKeyLength;

    @Value("${config.restfull.security.wa-monitoring.jwt.secret-key:ThisIsSecretForJWTHS512SignatureAlgorithmThatMUSTHave64ByteLength")
    protected String secretKey;

    @Value("${config.restfull.security.wa-monitoring.jwt.expiration-time:28800}")
    protected Long expirationTime;

    @Value("${config.restfull.security.wa-monitoring.allowed-packages:not-defined}")
    protected String allowedPackages;
    private String workAllowedPackages;

    @Value("${config.restfull.security.wa-monitoring.return-detailed-errors:false}")
    protected boolean returnDetailedErrors;

    public WaSecurityRest(GenericApplicationContext genericApplicationContext, JwtSecurityService jwtGenerator, LoginReactor loginReactor, JwtService jwtService, AuthenticationManager authenticationManager, WaManagedServers waManagedServers) {
        super(genericApplicationContext, jwtGenerator, loginReactor, authenticationManager);
        this.jwtService = jwtService;
        this.waManagedServers = waManagedServers;
    }

    // registered user devices
    final Collection<SessionInfo> userDevices = ServiceFuncs.<SessionInfo>createConcurencyCollection();

    //==========================================================================
    @Override
    public Mono<ServerResponse> login(ServerRequest request) {
        return processServerRequest(request, WaUserSessionInfo.class, this::clientLogin);
    }

    //==========================================================================
    private Collection<String> getErrors4UserSessionInfo(WaUserSessionInfo waUserSessionInfo) {

        var errors = ServiceFuncs.<String>createCollection();

        var packageName = nvl(waUserSessionInfo.getPackageName(), waUserSessionInfo.getAppName());

        ifNotNull(packageName, lAppName -> {

            if (!workAllowedPackages.contains("," + packageName + ",")) {
                errors.add(format("Illegal package '%s' (legal packages list: '%s')", packageName, allowedPackages));
            }
        }, () -> errors.add("packageName not defined"));

        return errors;
    }

    protected LoginResult clientLogin(WaUserSessionInfo waUserSessionInfo) {

        return create(LoginResult.class, loginResult -> {

            log.info("login user: '{}'", waUserSessionInfo);

            userDevices.add(create(SessionInfo.class, sessionInfo -> sessionInfo.assign(waUserSessionInfo, Boolean.FALSE)));

            var errors = getErrors4UserSessionInfo(waUserSessionInfo);

            // errors
            ifTrue(!errors.isEmpty(), () -> {

                loginResult.setNote(returnDetailedErrors ? errors.stream().findFirst().orElseThrow() : OC_GENERAL_ERROR_STR);
                loginResult.setAnswerCode(OC_GENEARL_ERROR);

                log.error("There are errors in UserSessionInfo: {}, waUserSessionInfo: {}", errors, waUserSessionInfo);

            }, () -> {

                //==========================================================================================================

                var sbToken = new StringBuilder(128);

                //gsfId
                ifNotNull(waUserSessionInfo.getGsfId(), gsfId -> sbToken.append(gsfId.concat(";")));

                //appleId
                ifNotNull(waUserSessionInfo.getAppleId(), appleId -> sbToken.append(appleId.concat(";")));

                // LoginToken
                ifNotNull(waUserSessionInfo.getLoginToken(), loginToken -> sbToken.append(loginToken.concat(";")));

                if (sbToken.length() <= minKeyLength) {

                    //identifierForVendor
                    ifNotNull(waUserSessionInfo.getIdentifierForVendor(), identifierForVendor -> sbToken.append(identifierForVendor.concat(";")));

                    //fcmToken
                    ifNotNull(waUserSessionInfo.getFcmToken(), fcmToken -> sbToken.append(fcmToken.concat(";")));

                    // deviceId
                    ifNotNull(waUserSessionInfo.getDeviceId(), deviceId -> sbToken.append(deviceId.toString().concat(";")));

                    // appName
                    ifNotNull(waUserSessionInfo.getAppName(), appName -> sbToken.append(appName.concat(";")));

                    // appVersion
                    ifNotNull(waUserSessionInfo.getAppVersion(), appVersion -> sbToken.append(appVersion.concat(";")));

                }

                if (sbToken.isEmpty()) {
                    sbToken.append(String.valueOf(waUserSessionInfo.hashCode()).concat(";"));
                    sbToken.append(String.valueOf(now().toString()).concat(";"));
                }

                var validUntil = now().plusSeconds(expirationTime);

                // final key
                var tokenKey = sbToken.toString();

                log.info("create tokenKey = '{}', {}", tokenKey, waUserSessionInfo);

                var token = jwtService.findToken(tokenKey,
                        validUntil,
                        WA_MONITORING,
                        nvl(waUserSessionInfo.getLoginToken(), "new user"),
                        () -> {

                            final Map<String, String> claims = ServiceFuncs.createMap();

                            claims.put("userId:", tokenKey);

                            return this.getJwtSecurityService().generateToken(tokenKey, claims, expirationTime);
                        });
                loginResult.setToken(token.getTokenCard());
                loginResult.setServerAddress(waManagedServers.getAvailableServer());
                loginResult.setAnswerCode(OC_OK);

            });
        });
    }

    @Override
    public void initialize() {

        super.initialize();
        workAllowedPackages = ",".concat(allowedPackages)
                .replaceAll(STRING_SPACE, EMPTY_STRING)
                .replaceAll(";", ",")
                .concat(",");

    }
}
