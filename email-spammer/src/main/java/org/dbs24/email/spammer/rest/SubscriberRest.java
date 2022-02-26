package org.dbs24.email.spammer.rest;

import lombok.extern.log4j.Log4j2;
import org.dbs24.email.spammer.entity.dto.SpammerDto;
import org.dbs24.email.spammer.entity.dto.SpammerList;
import org.dbs24.email.spammer.entity.dto.SubscriberDto;
import org.dbs24.email.spammer.service.SubscriberService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Log4j2
@Component
public class SubscriberRest {

    private final SubscriberService subscriberService;

    public SubscriberRest(SubscriberService subscriberService) {

        this.subscriberService = subscriberService;
    }

    public Mono<ServerResponse> createSubscriber(ServerRequest request) {

        return ServerResponse
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(
                        subscriberService.createSubscriber(request.bodyToMono(SubscriberDto.class)),
                        SpammerList.class
                );
    }
}
