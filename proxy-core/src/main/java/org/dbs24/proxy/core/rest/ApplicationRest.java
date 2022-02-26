package org.dbs24.proxy.core.rest;

import lombok.extern.log4j.Log4j2;
import org.dbs24.proxy.core.component.ApplicationService;
import org.dbs24.proxy.core.entity.dto.request.CreateApplicationRequest;
import org.dbs24.proxy.core.entity.dto.request.GetApplicationByNetworkRequest;
import org.dbs24.rest.api.NewReactiveRestProcessor;
import org.dbs24.stmt.StmtProcessor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static org.dbs24.proxy.core.rest.api.consts.ProxyHttpConsts.RestQueryParams.QP_APPLICATION_NETWORK_NAME;

@Log4j2
@Component
@ConditionalOnProperty(name = "config.restfull.profile.name", havingValue = "proxy-core")
public class ApplicationRest extends NewReactiveRestProcessor {

    final ApplicationService applicationService;

    public ApplicationRest(ApplicationService applicationService) {
        this.applicationService = applicationService;
    }

    public Mono<ServerResponse> createOrUpdateApplication(ServerRequest request) {
        return buildPostRequest(request, CreateApplicationRequest.class, applicationService::createOrUpdateApplication);
    }

    public Mono<ServerResponse> getAllApplications(ServerRequest request) {

        final Mono<GetApplicationByNetworkRequest> monoRequest
                = Mono.just(
                        StmtProcessor.create(
                                GetApplicationByNetworkRequest.class,
                                gpr -> gpr.setApplicationNetworkName(
                                        request.queryParam(QP_APPLICATION_NETWORK_NAME).orElse(null)
                                )
                        )
        );

        return buildGetRequest(request, () -> applicationService.getApplicationsByNetworkName(monoRequest));
    }
}