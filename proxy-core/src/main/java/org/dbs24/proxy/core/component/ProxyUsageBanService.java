package org.dbs24.proxy.core.component;


import lombok.Data;
import lombok.extern.log4j.Log4j2;
import org.dbs24.proxy.core.dao.ProxyUsageBanDao;
import org.dbs24.proxy.core.dao.ProxyUsageErrorDao;
import org.dbs24.proxy.core.entity.domain.ProxyUsageBan;
import org.dbs24.proxy.core.entity.domain.ProxyUsageError;
import org.dbs24.proxy.core.entity.dto.request.CreateProxyUsageBanRequest;
import org.dbs24.proxy.core.entity.dto.response.CreateProxyUsageBanResponse;
import org.dbs24.proxy.core.entity.dto.response.body.CreatedProxyUsageBan;
import org.dbs24.proxy.core.validator.ProxyUsageBanValidator;
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
public class ProxyUsageBanService extends AbstractRestApplicationService {

    final ProxyUsageBanDao proxyUsageBanDao;
    final ProxyUsageErrorDao proxyUsageErrorDao;

    final ProxyUsageBanValidator proxyUsageBanValidator;

    public ProxyUsageBanService(ProxyUsageBanDao proxyUsageBanDao, ProxyUsageErrorDao proxyUsageErrorDao, ProxyUsageBanValidator proxyUsageBanValidator) {

        this.proxyUsageBanDao = proxyUsageBanDao;
        this.proxyUsageErrorDao = proxyUsageErrorDao;
        this.proxyUsageBanValidator = proxyUsageBanValidator;
    }

    @Transactional
    public CreateProxyUsageBanResponse createProxyUsageBan(Mono<CreateProxyUsageBanRequest> monoRequest) {

        return this.<CreatedProxyUsageBan, CreateProxyUsageBanResponse>createAnswer(
                CreateProxyUsageBanResponse.class,
                (responseBody, createdProxyUsageBan) -> processRequest(
                        monoRequest,
                        responseBody,
                        request -> responseBody.setErrors(getProxyUsageBanValidator().validateConditional(
                                request.getEntityInfo(),
                                proxyUsageBanDto -> {

                                    log.info("DTO {}", proxyUsageBanDto);
                                    final ProxyUsageError proxyUsageErrorToSave = proxyUsageBanDao.createBanTemplateUsageError(proxyUsageBanDto);
                                    log.info("ENTITY OT SAVE {}", proxyUsageErrorToSave);
                                    final ProxyUsageError savedUsageError = proxyUsageErrorDao.save(proxyUsageErrorToSave);
                                    log.info("SAVED ERROR ENTITY {}", savedUsageError);

                                    final ProxyUsageBan proxyUsageBanToSave = proxyUsageBanDto.toProxyUsageBan(savedUsageError);
                                    log.info("ENTITY BAN OT SAVE {}", proxyUsageBanToSave);
                                    final ProxyUsageBan savedUsageBan = proxyUsageBanDao.save(proxyUsageBanToSave);
                                    log.info("SAVED BAN {}", savedUsageBan);

                                    createdProxyUsageBan.setBanId(savedUsageBan.getProxyUsageErrorId());

                                    responseBody.setCreatedEntity(createdProxyUsageBan);
                                    responseBody.setCode(OC_OK);
                                    responseBody.setMessage(String.format("Usage ban with id %s was stored",
                                            savedUsageBan.getProxyUsageErrorId().toString()));

                                    responseBody.complete();
                                }, errorInfos -> {
                                    responseBody.setCode(OC_INVALID_ENTITY_ATTRS);
                                    responseBody.setMessage(errorInfos.stream().findFirst().orElseThrow().getErrorMsg().toUpperCase());

                                    responseBody.complete();
                                }
                        ))
                )
        );
    }
}
