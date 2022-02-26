package org.dbs24.tik.mobile.rest;

import org.dbs24.tik.mobile.entity.dto.firebase.FireBaseApplicationIdDto;
import org.dbs24.tik.mobile.entity.dto.firebase.FireBaseApplicationListDto;
import org.dbs24.tik.mobile.entity.dto.firebase.FireBaseApplicationRequestDto;
import org.dbs24.tik.mobile.service.FireBaseApplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
public class FireBaseApplicationRest {

    private final FireBaseApplicationService fireBaseApplicationService;

    @Autowired
    public FireBaseApplicationRest(FireBaseApplicationService fireBaseApplicationService) {
        this.fireBaseApplicationService = fireBaseApplicationService;
    }

    public Mono<ServerResponse> getAllFireBaseApplications(ServerRequest request) {

        return ServerResponse
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(
                        fireBaseApplicationService.getAllApplications(),
                        FireBaseApplicationListDto.class
                );
    }

    public Mono<ServerResponse> createOrUpdateFireBaseApplications(ServerRequest request) {

        return ServerResponse
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(
                        fireBaseApplicationService.createOrUpdateFireBaseApp(request.bodyToMono(FireBaseApplicationRequestDto.class)),
                        FireBaseApplicationIdDto.class
                );
    }
}
