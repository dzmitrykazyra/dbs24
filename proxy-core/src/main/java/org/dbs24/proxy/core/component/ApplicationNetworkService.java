package org.dbs24.proxy.core.component;

import lombok.Data;
import lombok.extern.log4j.Log4j2;
import org.dbs24.proxy.core.dao.ApplicationNetworkDao;
import org.dbs24.proxy.core.entity.domain.ApplicationNetwork;
import org.dbs24.proxy.core.entity.dto.ApplicationNetworkDto;
import org.dbs24.proxy.core.entity.dto.SingleValueListDto;
import org.dbs24.proxy.core.entity.dto.request.CreateApplicationNetworkRequest;
import org.dbs24.proxy.core.entity.dto.response.CreateApplicationNetworkResponse;
import org.dbs24.proxy.core.entity.dto.response.SingleValuesDtoResponse;
import org.dbs24.proxy.core.entity.dto.response.body.CreatedApplicationNetwork;
import org.dbs24.proxy.core.validator.ApplicationNetworkDtoValidator;
import org.dbs24.rest.api.consts.RestApiConst;
import org.dbs24.rest.api.service.AbstractRestApplicationService;
import org.dbs24.stmt.StmtProcessor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import java.util.Optional;
import java.util.stream.Collectors;

import static org.dbs24.rest.api.consts.RestApiConst.RestOperCode.OC_INVALID_ENTITY_ATTRS;

@Data
@Log4j2
@Component
@ConditionalOnProperty(name = "config.restfull.profile.name", havingValue = "proxy-core")
public class ApplicationNetworkService extends AbstractRestApplicationService {

    final ApplicationNetworkDao applicationNetworkDao;

    final ApplicationNetworkDtoValidator networkDtoValidator;

    public ApplicationNetworkService(ApplicationNetworkDao applicationNetworkDao,
                                     ApplicationNetworkDtoValidator applicationNetworkDtoValidator) {
        this.applicationNetworkDao = applicationNetworkDao;
        this.networkDtoValidator = applicationNetworkDtoValidator;
    }

    @Transactional
    public CreateApplicationNetworkResponse createOrUpdateApplicationNetwork(Mono<CreateApplicationNetworkRequest> monoRequest) {

        return this.<CreatedApplicationNetwork, CreateApplicationNetworkResponse>createAnswer(
                CreateApplicationNetworkResponse.class,
                (responseBody, createdApplicationNetwork) -> processRequest(
                        monoRequest,
                        responseBody,
                        request -> responseBody.setErrors(getNetworkDtoValidator().validateConditional(
                                request.getEntityInfo(),
                                applicationNetworkDto -> {

                                    final ApplicationNetwork applicationNetwork = findOrCreateApplicationNetwork(applicationNetworkDto);

                                    if (null == applicationNetwork.getApplicationNetworkId()) {

                                        applicationNetworkDao.save(applicationNetwork);
                                        responseBody.setMessage(String.format("Application network %s was successfully created ", applicationNetworkDto.getName()));
                                    } else {
                                        applicationNetworkDao.save(applicationNetwork);
                                        responseBody.setMessage(String.format("Application network %s was successfully updated ", applicationNetworkDto.getName()));
                                    }
                                    responseBody.complete();

                                }, errorInfos -> {
                                    responseBody.setCode(OC_INVALID_ENTITY_ATTRS);
                                    responseBody.setMessage(errorInfos.stream().findFirst().orElseThrow().getErrorMsg().toUpperCase());

                                    responseBody.complete();
                                }))));
    }

    private ApplicationNetwork findOrCreateApplicationNetwork(ApplicationNetworkDto applicationNetworkDto) {
        String networkName = applicationNetworkDto.getName();
        Optional<ApplicationNetwork> applicationOptionalFound = applicationNetworkDao.findApplicationNetworkOptionalByName(networkName);

        return applicationOptionalFound.isEmpty()
                ? StmtProcessor.create(
                ApplicationNetwork.class,
                applicationNetwork -> {
                    applicationNetwork.setApplicationNetworkName(networkName);
                })
                : applicationOptionalFound.get();
    }

    public SingleValuesDtoResponse getApplicationNetworks() {

        return StmtProcessor.create(
                SingleValuesDtoResponse.class,
                singleValuesDtoResponse -> {
                    setSuccessAttributesToResponse(singleValuesDtoResponse);
                    singleValuesDtoResponse.setCreatedEntity(
                            StmtProcessor.create(
                                    SingleValueListDto.class,
                                    singleValueListDto -> singleValueListDto
                                            .setReferenceValues(
                                                    applicationNetworkDao
                                                            .findAllApplicationNetworks()
                                                            .stream()
                                                            .map(ApplicationNetwork::getApplicationNetworkName)
                                                            .collect(Collectors.toList())
                                            )
                            )
                    );
                }
        );
    }

    private void setSuccessAttributesToResponse(SingleValuesDtoResponse response) {

        response.setCode(RestApiConst.RestOperCode.OC_OK);
        response.setMessage("Successfully founded required application names");
        response.complete();
    }
}
