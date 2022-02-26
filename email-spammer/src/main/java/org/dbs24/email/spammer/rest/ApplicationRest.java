package org.dbs24.email.spammer.rest;

import lombok.extern.log4j.Log4j2;
import org.dbs24.email.spammer.dao.ReferenceDao;
import org.dbs24.email.spammer.entity.dto.ApplicationListDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Log4j2
@Component
public class ApplicationRest {

    private final ReferenceDao referenceDao;

    public ApplicationRest(ReferenceDao referenceDao) {

        this.referenceDao = referenceDao;
    }

    public Mono<ServerResponse> getAllApplications(ServerRequest request) {
        return ServerResponse
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(
                        Mono.just(referenceDao.findAllApplications()),
                        ApplicationListDto.class
                );
    }
}
