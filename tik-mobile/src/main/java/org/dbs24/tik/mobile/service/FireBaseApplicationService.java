package org.dbs24.tik.mobile.service;

import org.dbs24.tik.mobile.entity.dto.firebase.FireBaseApplicationIdDto;
import org.dbs24.tik.mobile.entity.dto.firebase.FireBaseApplicationListDto;
import org.dbs24.tik.mobile.entity.dto.firebase.FireBaseApplicationRequestDto;
import reactor.core.publisher.Mono;

public interface FireBaseApplicationService {

    Mono<FireBaseApplicationListDto> getAllApplications();

    Mono<FireBaseApplicationIdDto> createOrUpdateFireBaseApp(Mono<FireBaseApplicationRequestDto> fireBaseAppDtoMono);

}
