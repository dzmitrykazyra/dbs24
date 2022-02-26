/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.insta.tmp.config;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import lombok.extern.log4j.Log4j2;
import org.dbs24.config.AbstractWebSecurityConfig;

import static org.dbs24.consts.RestHttpConsts.HTTP_200_STRING;
import static org.dbs24.insta.tmp.consts.IfsConst.RestQueryParams.*;

import org.dbs24.insta.tmp.rest.*;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.dbs24.insta.tmp.consts.IfsConst.UriConsts.*;

import org.springdoc.core.annotations.RouterOperation;
import org.springdoc.core.annotations.RouterOperations;

import static org.springframework.web.bind.annotation.RequestMethod.POST;

import org.dbs24.insta.tmp.rest.api.*;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
@Log4j2
@Configuration
@EnableCaching
@ConditionalOnProperty(name = "config.restfull.profile.name", havingValue = "insta-tmp")
public class InstafFsRestConfig extends AbstractWebSecurityConfig {

    @RouterOperations({
            @RouterOperation(path = URI_CREATE_OR_UPDATE_ACCOUNT, method = POST, operation = @Operation(operationId = URI_CREATE_OR_UPDATE_ACCOUNT, requestBody = @RequestBody(description = "Account details", content = @Content(schema = @Schema(implementation = AccountInfo.class))), responses = @ApiResponse(responseCode = HTTP_200_STRING, description = "Created account", content = @Content(schema = @Schema(implementation = CreatedAccount.class))))),
            @RouterOperation(path = URI_VALIDATE_ACCOUNT, method = GET, operation = @Operation(operationId = URI_VALIDATE_ACCOUNT, responses = @ApiResponse(responseCode = HTTP_200_STRING, description = "Validate insta account", content = @Content(schema = @Schema(implementation = InstaAccountInfo.class))), parameters = {
                    @Parameter(name = QP_INSTA_ID, in = ParameterIn.QUERY, schema = @Schema(type = "long"), description = "insta account id", example = "1234567890123")})),
            @RouterOperation(path = URI_GET_ACCOUNT, method = GET, operation = @Operation(operationId = URI_GET_ACCOUNT, responses = @ApiResponse(responseCode = HTTP_200_STRING, description = "Get insta account", content = @Content(schema = @Schema(implementation = InstaAccountInfo.class))), parameters = {
                    @Parameter(name = QP_INSTA_ID, in = ParameterIn.QUERY, schema = @Schema(type = "long"), description = "insta account id", example = "1234567890123")})),
            @RouterOperation(path = URI_CREATE_OR_UPDATE_POST, method = POST, operation = @Operation(operationId = URI_CREATE_OR_UPDATE_POST, requestBody = @RequestBody(description = "Post details", content = @Content(schema = @Schema(implementation = PostInfo.class))), responses = @ApiResponse(responseCode = HTTP_200_STRING, description = "Created post", content = @Content(schema = @Schema(implementation = CreatedPost.class))))),
            @RouterOperation(path = URI_CREATE_OR_UPDATE_SOURCE, method = POST, operation = @Operation(operationId = URI_CREATE_OR_UPDATE_SOURCE, requestBody = @RequestBody(description = "Source details", content = @Content(schema = @Schema(implementation = SourceInfo.class))), responses = @ApiResponse(responseCode = HTTP_200_STRING, description = "Created post", content = @Content(schema = @Schema(implementation = CreatedSource.class))))),
            @RouterOperation(path = URI_CREATE_OR_UPDATE_FACE, method = POST, operation = @Operation(operationId = URI_CREATE_OR_UPDATE_FACE, requestBody = @RequestBody(description = "face details", content = @Content(schema = @Schema(implementation = FaceInfo.class))), responses = @ApiResponse(responseCode = HTTP_200_STRING, description = "Created post", content = @Content(schema = @Schema(implementation = CreatedFace.class))))),
            @RouterOperation(path = URI_CREATE_OR_UPDATE_BOT, method = POST, operation = @Operation(operationId = URI_CREATE_OR_UPDATE_BOT, requestBody = @RequestBody(description = "Bot details", content = @Content(schema = @Schema(implementation = BotInfo.class))), responses = @ApiResponse(responseCode = HTTP_200_STRING, description = "Created bot", content = @Content(schema = @Schema(implementation = CreatedBot.class))))),
            @RouterOperation(path = URI_GET_BOT, method = GET, operation = @Operation(operationId = URI_GET_BOT, responses = @ApiResponse(responseCode = HTTP_200_STRING, description = "Get bot details", content = @Content(schema = @Schema(implementation = BotInfo.class))), parameters = {
                    @Parameter(name = QP_BOT_ID, in = ParameterIn.QUERY, schema = @Schema(type = "integer"), description = "bot id", example = "1234567890123")})),
            @RouterOperation(path = URI_GET_BOTS_LIST, method = GET, operation = @Operation(operationId = URI_GET_BOTS_LIST, responses = @ApiResponse(responseCode = HTTP_200_STRING, description = "Get bots list", content = @Content(schema = @Schema(implementation = BotsList.class))), parameters = {
                    @Parameter(name = QP_BOTS_LIMIT, in = ParameterIn.QUERY, schema = @Schema(type = "integer"), description = "amount of bots", example = "1"),
                    @Parameter(name = QP_BOTS_STATUS_ID, in = ParameterIn.QUERY, schema = @Schema(type = "integer"), description = "status of bots (1-Actual, 2-Closed, 3-Quarantine)", example = "1")})),
            @RouterOperation(path = URI_CREATE_OR_UPDATE_TASK, method = POST, operation = @Operation(operationId = URI_CREATE_OR_UPDATE_TASK, requestBody = @RequestBody(description = "Create task", content = @Content(schema = @Schema(implementation = TaskInfo.class))), responses = @ApiResponse(responseCode = HTTP_200_STRING, description = "Created task", content = @Content(schema = @Schema(implementation = CreatedTask.class))))),
            @RouterOperation(path = URI_GET_TASKS, method = GET, operation = @Operation(operationId = URI_GET_TASKS, responses = @ApiResponse(responseCode = HTTP_200_STRING, description = "Get all tasks", content = @Content(schema = @Schema(implementation = AllTasks.class))), parameters = {
                    @Parameter(name = QP_TASK_RESULT_ID, in = ParameterIn.QUERY, schema = @Schema(type = "integer"), description = "allowed task result statuses {1 - Incomplete, 2 - Done, 3 - Failed }", example = "1")})),
            @RouterOperation(path = URI_GET_ACCOUNTS, method = GET, operation = @Operation(operationId = URI_GET_ACCOUNTS, responses = @ApiResponse(responseCode = HTTP_200_STRING, description = "Get insta accounts", content = @Content(schema = @Schema(implementation = AllAccounts.class))),
                    parameters = {@Parameter(name = QP_INSTA_ID, in = ParameterIn.QUERY, schema = @Schema(type = "long"), description = "insta account id", example = "1234567890123")}))
    })

    @Bean
    public RouterFunction<ServerResponse> routerInstaRegRest(
            AccountsRest accountsRest, PostsRest postsRest, SourcesRest sourcesRest, FacesRest facesRest, BotsRest botsRest, TasksRests tasksRests) {

        return addCommonRoutes()
                // Accounts
                .andRoute(postRoute(URI_CREATE_OR_UPDATE_ACCOUNT), accountsRest::createOrUpdateAccount)
                .andRoute(getRoute(URI_VALIDATE_ACCOUNT), accountsRest::validateInstaAccount)
                .andRoute(getRoute(URI_GET_ACCOUNT), accountsRest::getAccount)
                .andRoute(getRoute(URI_GET_ACCOUNTS), accountsRest::getAccounts)
                .andRoute(getRoute(URI_DELETE_ALL), accountsRest::deleteAll)
                // Posts
                .andRoute(postRoute(URI_CREATE_OR_UPDATE_POST), postsRest::createOrUpdatePost)
                // Sources
                .andRoute(postRoute(URI_CREATE_OR_UPDATE_SOURCE), sourcesRest::createOrUpdateSource)
                // Faces
                .andRoute(postRoute(URI_CREATE_OR_UPDATE_FACE), facesRest::createOrUpdateFace)
                // Bots
                .andRoute(postRoute(URI_CREATE_OR_UPDATE_BOT), botsRest::createOrUpdateBot)
                .andRoute(getRoute(URI_GET_BOT), botsRest::getBot)
                .andRoute(getRoute(URI_GET_BOTS_LIST), botsRest::getBotsList)
                // Tasks
                .andRoute(postRoute(URI_CREATE_OR_UPDATE_TASK), tasksRests::createOrUpdateTask)
                .andRoute(getRoute(URI_GET_TASKS), tasksRests::getTasksList);

    }
}
