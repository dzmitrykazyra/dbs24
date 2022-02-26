package org.dbs24.tik.assist.rest;

import lombok.extern.log4j.Log4j2;
import org.dbs24.tik.assist.entity.dto.bot.BotIdList;
import org.dbs24.tik.assist.service.bot.BotService;

import org.dbs24.tik.assist.constant.RequestQueryParam;
import org.dbs24.tik.assist.entity.dto.bot.BotDto;
import org.dbs24.tik.assist.entity.dto.bot.response.AvailableBotsByAwemeIdResponse;
import org.dbs24.tik.assist.entity.dto.bot.response.AvailableBotsBySecUserIdResponse;
import org.dbs24.tik.assist.entity.dto.bot.CreatedBotDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Log4j2
@Component
public class BotRest {

    private final BotService botService;

    public BotRest(BotService botService) {

        this.botService = botService;
    }

    public Mono<ServerResponse> createOrUpdateBot(ServerRequest request) {

        return ServerResponse
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(
                        botService.createOrUpdateBot(request.bodyToMono(BotDto.class)),
                        CreatedBotDto.class
                );
    }

    public Mono<ServerResponse> getBotById(ServerRequest request) {

        return ServerResponse
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(
                        botService.getBotById(Integer.valueOf(request.queryParam(RequestQueryParam.QP_BOT_ID).orElse("0"))),
                        BotDto.class
                );
    }

    public Mono<ServerResponse> getAvailableBotsAmountByAwemeId(ServerRequest request) {

        return ServerResponse
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(
                        botService.getAvailableBotsAmountByAwemeId(request.queryParam(RequestQueryParam.QP_AWEME_ID).orElse("0")),
                        AvailableBotsByAwemeIdResponse.class
                );
    }

    public Mono<ServerResponse> getAvailableBotsAmountBySecUserId(ServerRequest request) {

        return ServerResponse
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(
                        botService.getAvailableBotsAmountBySecUserId(request.queryParam(RequestQueryParam.QP_SEC_USER_ID).orElse("0")),
                        AvailableBotsBySecUserIdResponse.class
                );
    }

    public Mono<ServerResponse> getActiveBotsIds(ServerRequest request) {

        return ServerResponse
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(
                        botService.getActiveBotsIds(),
                        BotIdList.class
                );
    }
}
