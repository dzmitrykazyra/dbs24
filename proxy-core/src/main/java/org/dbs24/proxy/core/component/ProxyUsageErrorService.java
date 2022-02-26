package org.dbs24.proxy.core.component;

import lombok.Data;
import lombok.extern.log4j.Log4j2;
import org.dbs24.proxy.core.dao.ApplicationDao;
import org.dbs24.proxy.core.dao.ProxyUsageDao;
import org.dbs24.proxy.core.dao.ProxyUsageErrorDao;
import org.dbs24.proxy.core.entity.domain.ProxyUsageError;
import org.dbs24.proxy.core.entity.dto.request.CreateProxyUsageErrorRequest;
import org.dbs24.proxy.core.entity.dto.response.CreateProxyUsageErrorResponse;
import org.dbs24.proxy.core.entity.dto.response.body.CreatedProxyUsageError;
import org.dbs24.proxy.core.validator.ProxyUsageErrorValidator;
import org.dbs24.rest.api.service.AbstractRestApplicationService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import static org.dbs24.rest.api.consts.RestApiConst.RestOperCode.OC_INVALID_ENTITY_ATTRS;
import static org.dbs24.rest.api.consts.RestApiConst.RestOperCode.OC_OK;

@Data
@Log4j2
@Component
@ConditionalOnProperty(name = "config.restfull.profile.name", havingValue = "proxy-core")
public class ProxyUsageErrorService extends AbstractRestApplicationService {

    final ProxyUsageErrorDao proxyUsageErrorDao;
    final ProxyUsageDao proxyUsageDao;
    final ApplicationDao applicationDao;

    final ProxyUsageErrorValidator proxyUsageErrorValidator;

    public ProxyUsageErrorService(ProxyUsageErrorDao proxyUsageErrorDao, ProxyUsageDao proxyUsageDao, ApplicationDao applicationDao, ProxyUsageErrorValidator proxyUsageErrorValidator) {
        this.proxyUsageErrorDao = proxyUsageErrorDao;
        this.proxyUsageDao = proxyUsageDao;
        this.applicationDao = applicationDao;
        this.proxyUsageErrorValidator = proxyUsageErrorValidator;
    }

    @Transactional
    public CreateProxyUsageErrorResponse createProxyUsageError(Mono<CreateProxyUsageErrorRequest> monoRequest) {

        return this.<CreatedProxyUsageError, CreateProxyUsageErrorResponse>createAnswer(
                CreateProxyUsageErrorResponse.class,
                (responseBody, createdProxyUsageError) -> processRequest(
                        monoRequest,
                        responseBody,
                        request -> responseBody.setErrors(getProxyUsageErrorValidator().validateConditional(
                                request.getEntityInfo(),
                                proxyUsageErrorDto -> {

                                    ProxyUsageError fromDto = proxyUsageErrorDao.toProxyUsageError(proxyUsageErrorDto);
                                    ProxyUsageError savedUsageError = proxyUsageErrorDao.save(fromDto);

                                    createdProxyUsageError.assign(savedUsageError);

                                    responseBody.setCreatedEntity(createdProxyUsageError);
                                    responseBody.setCode(OC_OK);
                                    responseBody.setMessage(String.format("Your usage error %s was stored", savedUsageError.toString()));

                                    responseBody.complete();
                                }, errorInfos -> {
                                    responseBody.setCode(OC_INVALID_ENTITY_ATTRS);
                                    responseBody.setMessage(errorInfos.stream().findFirst().orElseThrow().getErrorMsg().toUpperCase());

                                    responseBody.complete();
                                })))
        );
    }
}