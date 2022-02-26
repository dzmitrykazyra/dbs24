/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.app.promo.rest;

import lombok.extern.log4j.Log4j2;
import org.dbs24.app.promo.component.*;
import org.dbs24.app.promo.rest.dto.action.CreateOrderActionRequest;
import org.dbs24.app.promo.rest.dto.apppackage.CreatePackageRequest;
import org.dbs24.app.promo.rest.dto.batchsetup.CreateBatchSetupRequest;
import org.dbs24.app.promo.rest.dto.batchtemplate.CreateBatchTemplateRequest;
import org.dbs24.app.promo.rest.dto.bot.CreateBotRequest;
import org.dbs24.app.promo.rest.dto.comment.CreateCommentRequest;
import org.dbs24.app.promo.rest.dto.order.CreateOrderRequest;
import org.dbs24.rest.api.NewReactiveRestProcessor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Log4j2
@Component
@ConditionalOnProperty(name = "config.restfull.profile.name", havingValue = "app-promo")
public class AppPromotionRest extends NewReactiveRestProcessor {

    final BotsService botsService;
    final CommentsService commentsService;
    final PackagesService packagesService;
    final BatchSetupService batchSetupService;
    final RestOrdersService restOrdersService;
    final RestOrderActionsService restOrderActionsService;
    final BatchTemplateService batchTemplateService;

    public AppPromotionRest(BotsService botsService, CommentsService commentsService, PackagesService packagesService, BatchSetupService batchSetupService, RestOrdersService restOrdersService, RestOrderActionsService restOrderActionsService, BatchTemplateService batchTemplateService) {

        this.botsService = botsService;
        this.commentsService = commentsService;
        this.packagesService = packagesService;
        this.batchSetupService = batchSetupService;
        this.restOrdersService = restOrdersService;
        this.restOrderActionsService = restOrderActionsService;
        this.batchTemplateService = batchTemplateService;
    }

    //==========================================================================
    public Mono<ServerResponse> createOrUpdateBot(ServerRequest serverRequest) {

        return buildPostRequest(serverRequest, CreateBotRequest.class, botsService::createOrUpdateBot);
    }

    public Mono<ServerResponse> createOrUpdateComment(ServerRequest serverRequest) {

        return buildPostRequest(serverRequest, CreateCommentRequest.class, commentsService::createOrUpdateComment);
    }

    public Mono<ServerResponse> createOrUpdatePackage(ServerRequest serverRequest) {

        return buildPostRequest(serverRequest, CreatePackageRequest.class, packagesService::createOrUpdatePackage);
    }

    public Mono<ServerResponse> createOrUpdateBatchSetup(ServerRequest serverRequest) {

        return buildPostRequest(serverRequest, CreateBatchSetupRequest.class, batchSetupService::createOrUpdateBatchSetup);
    }

    public Mono<ServerResponse> createOrUpdateOrder(ServerRequest serverRequest) {

        return buildPostRequest(serverRequest, CreateOrderRequest.class, restOrdersService::createOrUpdateOrder);
    }

    public Mono<ServerResponse> createOrUpdateOrderAction(ServerRequest serverRequest) {

        return buildPostRequest(serverRequest, CreateOrderActionRequest.class, restOrderActionsService::createOrUpdateOrderAction);
    }

    public Mono<ServerResponse> createOrUpdateBatchTemplate(ServerRequest serverRequest) {

        return buildPostRequest(serverRequest, CreateBatchTemplateRequest.class, batchTemplateService::createOrUpdateBatchTemplate);
    }

    //==========================================================================
//    public Mono<ServerResponse> createOrUpdateWaBot(ServerRequest request) {
//
//        return buildPostRequest(request, CreateWaBotRequest.class, waBotsService::createOrUpdateWaBot);
//    }
}
