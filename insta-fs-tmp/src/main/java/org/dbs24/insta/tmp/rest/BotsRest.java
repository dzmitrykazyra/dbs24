/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.insta.tmp.rest;

import lombok.extern.log4j.Log4j2;
import org.dbs24.insta.tmp.component.BotsService;
import static org.dbs24.insta.tmp.consts.IfsConst.RestQueryParams.QP_BOT_ID;
import org.dbs24.insta.tmp.rest.api.BotInfo;
import org.dbs24.insta.tmp.rest.api.CreatedBot;
import org.dbs24.rest.api.ReactiveRestProcessor;
import static org.dbs24.rest.api.ReactiveRestProcessor.httpOk;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;
import java.util.Collection;
import static org.dbs24.insta.tmp.consts.IfsConst.RestQueryParams.QP_BOTS_LIMIT;
import static org.dbs24.insta.tmp.consts.IfsConst.RestQueryParams.QP_BOTS_STATUS_ID;
import static org.dbs24.insta.tmp.consts.IfsConst.References.BotStatuses.BS_ACTUAL;

@Log4j2
@Component
@ConditionalOnProperty(name = "config.restfull.profile.name", havingValue = "insta-tmp")
public class BotsRest extends ReactiveRestProcessor {

    final BotsService botsService;

    public BotsRest(BotsService botsService) {
        this.botsService = botsService;
    }

    //==========================================================================
    public Mono<ServerResponse> createOrUpdateBot(ServerRequest request) {

        return this.<BotInfo, CreatedBot>processServerRequest(request, BotInfo.class,
                botInfo -> botsService.createOrUpdateBot(botInfo));
    }

    //==========================================================================
    public Mono<ServerResponse> getBot(ServerRequest request) {

        final Integer botId = getIntegerFromParam(request, QP_BOT_ID);

        return this.<BotInfo>processServerRequest(request, () -> botsService.getBot(botId));

    }

    //==========================================================================
    public Mono<ServerResponse> getBotsList(ServerRequest request) {

        final Integer botsLimit = getIntegerFromParam(request, QP_BOTS_LIMIT);
        final String botStatusString = getOptionalStringFromParam(request, QP_BOTS_STATUS_ID);

        final Integer botStatus = !botStatusString.isEmpty() ? Integer.valueOf(botStatusString) : BS_ACTUAL;

        return this.<Collection<BotInfo>>processServerRequest(request, () -> botsService.getBotsList(botsLimit, botStatus));

    }
}
