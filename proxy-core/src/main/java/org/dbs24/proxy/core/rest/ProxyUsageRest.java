package org.dbs24.proxy.core.rest;

import lombok.extern.log4j.Log4j2;
import org.dbs24.proxy.core.component.RequestService;
import org.dbs24.proxy.core.entity.dto.request.FinalizeRequestByApplicationId;
import org.dbs24.proxy.core.entity.dto.request.FinalizeRequestById;
import org.dbs24.rest.api.NewReactiveRestProcessor;
import org.dbs24.stmt.StmtProcessor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static org.dbs24.proxy.core.rest.api.consts.ProxyHttpConsts.RestQueryParams.QP_APPLICATION_ID;
import static org.dbs24.proxy.core.rest.api.consts.ProxyHttpConsts.RestQueryParams.QP_REQUEST_ID;


@Log4j2
@Component
@ConditionalOnProperty(name = "config.restfull.profile.name", havingValue = "proxy-core")
public class ProxyUsageRest extends NewReactiveRestProcessor {

    final RequestService requestService;

    public ProxyUsageRest(RequestService requestService) {
        this.requestService = requestService;
    }

    public Mono<ServerResponse> finalizeByRequestId(ServerRequest request) {

        final Mono<FinalizeRequestById> monoRequest = Mono.just(
            StmtProcessor.create(
                    FinalizeRequestById.class,
                    req -> req.setRequestId(getIntegerFromParam(request, QP_REQUEST_ID))
            )
        );

        return buildGetRequest(request, () -> requestService.finalizeByRequestId(monoRequest));
    }

    public Mono<ServerResponse> finalizeByApplicationId(ServerRequest request) {

        final Mono<FinalizeRequestByApplicationId> monoRequest = Mono.just(
                StmtProcessor.create(
                        FinalizeRequestByApplicationId.class,
                        req -> req.setApplicationId(getIntegerFromParam(request, QP_APPLICATION_ID))
                )
        );

        return buildGetRequest(request, () -> requestService.finalizeByApplicationId(monoRequest));
    }
}
