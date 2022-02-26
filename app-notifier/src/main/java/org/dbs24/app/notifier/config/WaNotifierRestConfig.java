/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.app.notifier.config;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.EqualsAndHashCode;
import lombok.extern.log4j.Log4j2;
import org.dbs24.app.notifier.entity.dto.CreatedMessage;
import org.dbs24.app.notifier.entity.dto.MessageDto;
import org.dbs24.app.notifier.entity.dto.MessagesList;
import org.dbs24.app.notifier.rest.AppNotifierRest;
import org.dbs24.config.AbstractWebSecurityConfig;
import org.springdoc.core.annotations.RouterOperation;
import org.springdoc.core.annotations.RouterOperations;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.dbs24.app.notifier.consts.AppNotifierConsts.RestQueryParams.*;
import static org.dbs24.app.notifier.consts.AppNotifierConsts.Uri.URI_CREATE_OR_UPDATE_MESSAGE;
import static org.dbs24.app.notifier.consts.AppNotifierConsts.Uri.URI_GET_MESSAGE;
import static org.dbs24.app.notifier.wa.WaNotifierConsts.*;
import static org.dbs24.consts.RestHttpConsts.HTTP_200_STRING;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
@Log4j2
@Configuration
@EnableCaching
@EqualsAndHashCode(callSuper = true)
public class WaNotifierRestConfig extends AbstractWebSecurityConfig {

    final AppNotifierRest appNotifierRest;

    @Override
    public String getAppUriPrefix() {
        return URI_WA_CONST;
    }

    public WaNotifierRestConfig(AppNotifierRest appNotifierRest) {
        this.appNotifierRest = appNotifierRest;
    }

    @RouterOperations({
            @RouterOperation(path = URI_WA_CREATE_OR_UPDATE_MESSAGE, method = POST, operation = @Operation(operationId = URI_WA_CREATE_OR_UPDATE_MESSAGE, requestBody = @RequestBody(description = "message details", content = @Content(schema = @Schema(implementation = MessageDto.class))), responses = @ApiResponse(responseCode = HTTP_200_STRING, description = "Create/update bot", content = @Content(schema = @Schema(implementation = CreatedMessage.class))))),
            @RouterOperation(path = URI_WA_GET_MESSAGES, method = GET, operation = @Operation(operationId = URI_WA_GET_MESSAGES, responses = @ApiResponse(responseCode = HTTP_200_STRING, description = "get messages", content = @Content(schema = @Schema(implementation = MessagesList.class))), parameters = {
                    @Parameter(name = QP_START_DATE, in = ParameterIn.QUERY, schema = @Schema(type = "long"), description = "Start DateTime", example = "123430947839873"),
                    @Parameter(name = QP_LOGIN_TOKEN, in = ParameterIn.QUERY, schema = @Schema(type = "string"), description = "message key (loginToken)", example = "ST_B44E76C740834F3B9D9CF5B4028"),
                    @Parameter(name = QP_PACKAGE, in = ParameterIn.QUERY, schema = @Schema(type = "string"), description = "application package", example = "com.zetreex.waparenttool"),
                    @Parameter(name = QP_VERSION, in = ParameterIn.QUERY, schema = @Schema(type = "string"), description = "application version", example = "1.0.0")
            })),
    })
    @Bean
    public RouterFunction<ServerResponse> routerWaNotifierRest() {
        return addCommonRoutes()
                .andRoute(postRoute(URI_CREATE_OR_UPDATE_MESSAGE), appNotifierRest::createOrUpdateMessage)
                .andRoute(getRoute(URI_GET_MESSAGE), appNotifierRest::getMessages);
    }
}
