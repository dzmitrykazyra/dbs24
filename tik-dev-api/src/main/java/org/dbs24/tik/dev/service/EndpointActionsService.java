/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.tik.dev.service;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import org.dbs24.rest.api.action.SimpleActionInfo;
import org.dbs24.rest.api.service.AbstractRestApplicationService;
import org.dbs24.tik.dev.dao.EndpointActionDao;
import org.dbs24.tik.dev.entity.EndpointAction;
import org.dbs24.tik.dev.rest.dto.endpoint.CreateEndpointActionRequest;
import org.dbs24.tik.dev.rest.dto.endpoint.CreatedEndpointAction;
import org.dbs24.tik.dev.rest.dto.endpoint.CreatedEndpointActionResponse;
import org.dbs24.tik.dev.rest.dto.endpoint.EndpointActionInfo;
import org.dbs24.tik.dev.rest.dto.endpoint.validator.EndpointActionInfoValidator;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import java.util.function.BiFunction;
import java.util.function.Function;

import static java.time.LocalDateTime.now;
import static org.dbs24.application.core.locale.NLS.long2LocalDateTime;
import static org.dbs24.rest.api.consts.RestApiConst.RestOperCode.OC_INVALID_ENTITY_ATTRS;
import static org.dbs24.rest.api.consts.RestApiConst.RestOperCode.OC_OK;
import static org.dbs24.stmt.StmtProcessor.*;

@Getter
@Log4j2
@Component
@EqualsAndHashCode
public class EndpointActionsService extends AbstractRestApplicationService {

    final EndpointActionDao endpointActionDao;
    final RefsService refsService;
    final DevelopersService developersService;
    final DevicesService devicesService;
    final TikAccountService tikAccountService;
    final ContractsService contractsService;
    final EndpointActionInfoValidator endpointActionInfoValidator;

    public EndpointActionsService(RefsService refsService, EndpointActionDao endpointActionDao, EndpointActionInfoValidator endpointActionInfoValidator, DevelopersService developersService, ContractsService contractsService, DevicesService devicesService, TikAccountService tikAccountService) {

        this.refsService = refsService;
        this.endpointActionDao = endpointActionDao;
        this.endpointActionInfoValidator = endpointActionInfoValidator;
        this.developersService = developersService;
        this.contractsService = contractsService;
        this.devicesService = devicesService;
        this.tikAccountService = tikAccountService;
    }

    final BiFunction<EndpointActionInfo, EndpointAction, EndpointAction> assignDto = (endpointActionInfo, endpointAction) -> {

        endpointAction.setExecutionDate(long2LocalDateTime(endpointActionInfo.getExecutionDate()));
        ifNull(endpointAction.getExecutionDate(), () -> endpointAction.setExecutionDate(now()));
        endpointAction.setEndpoint(getRefsService().findEndpoint(endpointActionInfo.getEndpointId()));
        endpointAction.setEndpointResult(getRefsService().findEndpointResult(endpointActionInfo.getEndpointResultId()));
        endpointAction.setContract(getContractsService().findContract(endpointActionInfo.getContractId()));
        endpointAction.setEndpointResponse(endpointActionInfo.getEndpointResponse());
        endpointAction.setBody(endpointActionInfo.getBody());
        endpointAction.setDevice(getDevicesService().findDevice(endpointActionInfo.getDeviceId()));
        endpointAction.setErrLog(endpointActionInfo.getErrLog());
        endpointAction.setIpAddress(endpointActionInfo.getIpAddress());
        endpointAction.setHeaders(endpointActionInfo.getHeaders());
        endpointAction.setTikAccount(getTikAccountService().findTikAccount(endpointActionInfo.getTikAccountId()));
        endpointAction.setQueryParams(endpointActionInfo.getQueryParams());
        endpointAction.setUsedBytes(endpointActionInfo.getUsedBytes());

        return endpointAction;
    };

    final Function<EndpointActionInfo, EndpointAction> assignEndpointActionsInfo = endpointActionInfo -> {

        final EndpointAction endpointAction = create(EndpointAction.class);

        assignDto.apply(endpointActionInfo, endpointAction);

        return endpointAction;
    };

    //==========================================================================
    @Transactional
    public CreatedEndpointActionResponse createEndpointAction(Mono<CreateEndpointActionRequest> monoRequest) {

        return this.<CreatedEndpointAction, CreatedEndpointActionResponse>createAnswer(CreatedEndpointActionResponse.class,
                (responseBody, createdEndpointAction) -> processRequest(monoRequest, responseBody, request
                        -> responseBody.setErrors(endpointActionInfoValidator.validateConditional(request.getEntityInfo(), endpointActionInfo
                        -> {

                    final SimpleActionInfo simpleActionInfo = request.getEntityAction();

                    //log.info("simpleActionInfo =  {}", simpleActionInfo);

                    log.debug("create endpointAction: {}", endpointActionInfo);

                    //StmtProcessor.assertNotNull(String.class, endpointActionInfo.getPackageName(), "packageName name is not defined");

                    final EndpointAction endpointAction = createEndpointActions(endpointActionInfo);

                    final Boolean isNewSetting = isNull(endpointAction.getEndpointActionId());

                    getEndpointActionDao().saveEndpointAction(endpointAction);

                    final String finalMessage = String.format("EndpointAction is %s (EndpointActionId=%d)",
                            isNewSetting ? "created" : "updated",
                            endpointAction.getEndpointActionId());

                    createdEndpointAction.setCreatedEndpointActionId(endpointAction.getEndpointActionId());
                    responseBody.complete();

                    log.debug(finalMessage);

                    responseBody.setCode(OC_OK);
                    responseBody.setMessage(finalMessage);
                }, errorInfos -> {
                    responseBody.setCode(OC_INVALID_ENTITY_ATTRS);
                    responseBody.setMessage(errorInfos.stream().findFirst().orElseThrow().getErrorMsg().toUpperCase());
                }))));
    }

    public EndpointAction createEndpointActions(EndpointActionInfo endpointActionInfo) {
        return assignEndpointActionsInfo.apply(endpointActionInfo);
    }
}
