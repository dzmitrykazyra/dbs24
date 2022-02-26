package org.dbs24.email.spammer.rest;

import lombok.extern.log4j.Log4j2;
import org.dbs24.email.spammer.entity.dto.SpammerDto;
import org.dbs24.email.spammer.entity.dto.SpammerList;
import org.dbs24.email.spammer.service.SpammerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Log4j2
@Component
public class SpammerRest {

    private final SpammerService spammerService;

    public SpammerRest(SpammerService spammerService) {

        this.spammerService = spammerService;
    }

    public Mono<ServerResponse> getAllSpammers(ServerRequest serverRequest) {

        return ServerResponse
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(
                        spammerService.getAllSpammers(),
                        SpammerList.class
                );
    }

    public Mono<ServerResponse> getSpammerByEmail(ServerRequest request) {

        return Mono.empty();
    }

    public Mono<ServerResponse> createSpammer(ServerRequest request) {

        return ServerResponse
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(
                        spammerService.createSpammer(request.bodyToMono(SpammerDto.class)),
                        SpammerList.class
                );
    }
}
