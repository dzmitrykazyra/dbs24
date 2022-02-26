/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.app.notifier.rest;

import lombok.EqualsAndHashCode;
import lombok.extern.log4j.Log4j2;
import org.dbs24.app.notifier.component.MessagesService;
import org.dbs24.app.notifier.entity.dto.CreatedMessage;
import org.dbs24.app.notifier.entity.dto.MessageDto;
import org.dbs24.app.notifier.entity.dto.MessagesList;
import org.dbs24.rest.api.ReactiveRestProcessor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static org.dbs24.app.notifier.consts.AppNotifierConsts.RestQueryParams.*;

@Log4j2
@Component
@EqualsAndHashCode(callSuper = true)
public class AppNotifierRest extends ReactiveRestProcessor {

    final MessagesService messagesService;

    public AppNotifierRest(MessagesService messagesService) {
        this.messagesService = messagesService;
    }

    //==========================================================================
    public Mono<ServerResponse> createOrUpdateMessage(ServerRequest serverRequest) {

        return processServerRequest(
                serverRequest,
                MessageDto.class,
                CreatedMessage.class,
                messagesService::createOrUpdateMessage);
    }

    //==========================================================================
    public Mono<ServerResponse> getMessages(ServerRequest serverRequest) {

        final Long startDate = getOptionalLongFromParam(serverRequest, QP_START_DATE);
        final String loginToken = getOptionalStringFromParam(serverRequest, QP_LOGIN_TOKEN);
        final String appPackage = getOptionalStringFromParam(serverRequest, QP_PACKAGE);
        final String appVersion = getOptionalStringFromParam(serverRequest, QP_VERSION);

        return createResponse(serverRequest, MessagesList.class, () -> messagesService.getMessages(startDate, loginToken, appPackage, appVersion));

    }
}
