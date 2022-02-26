/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.tik.assist.rest;

import lombok.extern.log4j.Log4j2;
import org.dbs24.rest.api.ReactiveRestProcessor;
import org.dbs24.tik.assist.constant.RequestQueryParam;
import org.dbs24.tik.assist.entity.dto.action.ActionTask;
import org.dbs24.tik.assist.entity.dto.action.ActionTaskConfirm;
import org.dbs24.tik.assist.entity.dto.action.ActionTaskResult;
import org.dbs24.tik.assist.entity.dto.bot.RepairBotTask;
import org.dbs24.tik.assist.entity.dto.bot.RepairBotTaskConfirm;
import org.dbs24.tik.assist.entity.dto.bot.RepairBotTaskResult;
import org.dbs24.tik.assist.service.bot.TikConnectorEmuService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Log4j2
@Component
@ConditionalOnProperty(name = "config.restfull.profile.name", havingValue = "tik-assist")
public class TikConnectorEmuRest extends ReactiveRestProcessor {

    final TikConnectorEmuService tikConnectorEmuService;

    public TikConnectorEmuRest(TikConnectorEmuService tikConnectorEmuService) {
        this.tikConnectorEmuService = tikConnectorEmuService;
    }

    //==========================================================================
    public Mono<ServerResponse> createTikAction(ServerRequest request) {

        return this.<ActionTask, ActionTaskConfirm>createResponse(
                request,
                ActionTask.class,
                ActionTaskConfirm.class,
                actionTask -> tikConnectorEmuService.createTikAction(actionTask));
    }

    //==========================================================================
    public Mono<ServerResponse> getTikActionStatus(ServerRequest request) {

        return this.<ActionTaskResult>createResponse(
                request,
                ActionTaskResult.class,
                () -> tikConnectorEmuService.getTikActionStatus(getLongFromParam(request, RequestQueryParam.QP_ACTION_ID))
        );
    }
    
    //==========================================================================
    public Mono<ServerResponse> createRepairTaskAction(ServerRequest request) {

        return this.<RepairBotTask, RepairBotTaskConfirm>createResponse(
                request,
                RepairBotTask.class,
                RepairBotTaskConfirm.class,
                tikConnectorEmuService::createRepairTaskAction
        );
    }

    //==========================================================================
    public Mono<ServerResponse> getAgentRepairActionStatus(ServerRequest request) {

        return this.<RepairBotTaskResult>createResponse(
                request,
                RepairBotTaskResult.class,
                () -> tikConnectorEmuService.getAgentRepairActionStatus(getLongFromParam(request, RequestQueryParam.QP_TASK_ID))
        );
    }    
}
