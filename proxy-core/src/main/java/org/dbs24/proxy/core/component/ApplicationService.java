package org.dbs24.proxy.core.component;


import lombok.Data;
import lombok.extern.log4j.Log4j2;
import org.dbs24.proxy.core.dao.ApplicationDao;
import org.dbs24.proxy.core.dao.ApplicationNetworkDao;
import org.dbs24.proxy.core.entity.domain.Application;
import org.dbs24.proxy.core.entity.dto.ApplicationDto;
import org.dbs24.proxy.core.entity.dto.request.CreateApplicationRequest;
import org.dbs24.proxy.core.entity.dto.request.GetApplicationByNetworkRequest;
import org.dbs24.proxy.core.entity.dto.response.CreateApplicationResponse;
import org.dbs24.proxy.core.entity.dto.response.GetApplicationsResponse;
import org.dbs24.proxy.core.entity.dto.response.body.CreatedApplication;
import org.dbs24.proxy.core.validator.ApplicationDtoValidator;
import org.dbs24.rest.api.ResponseBody;
import org.dbs24.rest.api.service.AbstractRestApplicationService;
import org.dbs24.stmt.StmtProcessor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.stream.Collectors;

import static org.dbs24.rest.api.consts.RestApiConst.RestOperCode.OC_INVALID_ENTITY_ATTRS;
import static org.dbs24.rest.api.consts.RestApiConst.RestOperCode.OC_OK;

@Data
@Log4j2
@Component
@ConditionalOnProperty(name = "config.restfull.profile.name", havingValue = "proxy-core")
public class ApplicationService extends AbstractRestApplicationService {

    private final ApplicationDtoValidator applicationDtoValidator;
    private final ApplicationNetworkDao applicationNetworkDao;
    private final ApplicationDao applicationDao;

    public ApplicationService(ApplicationDtoValidator applicationDtoValidator, ApplicationNetworkDao applicationNetworkDao, ApplicationDao applicationDao) {
        this.applicationDtoValidator = applicationDtoValidator;
        this.applicationNetworkDao = applicationNetworkDao;
        this.applicationDao = applicationDao;
    }

    @Transactional
    public CreateApplicationResponse createOrUpdateApplication(Mono<CreateApplicationRequest> monoRequest) {

        return this.<CreatedApplication, CreateApplicationResponse>createAnswer(
                CreateApplicationResponse.class,
                (responseBody, createdApplication) -> processRequest(
                        monoRequest,
                        responseBody,
                        request -> responseBody.setErrors(applicationDtoValidator.validateConditional(request.getEntityInfo(),
                                applicationDto -> {

                                    Application application = applicationDto.getApplicationId() == null
                                            ? createApplication(applicationDto, responseBody)
                                            : updateApplication(applicationDto, responseBody);

                                    createdApplication.setCreatedApplicationId(application.getApplicationId());
                                    responseBody.complete();

                                }, errorInfos -> {
                                    responseBody.setCode(OC_INVALID_ENTITY_ATTRS);
                                    responseBody.setMessage(errorInfos.stream().findFirst().orElseThrow().getErrorMsg().toUpperCase());
                                    responseBody.complete();
                                }))));
    }

    public GetApplicationsResponse getApplicationsByNetworkName(Mono<GetApplicationByNetworkRequest> monoRequest) {

        return createAnswer(
                GetApplicationsResponse.class,
                (responseBody, applicationDtoList) ->
                        processRequest(
                                monoRequest,
                                responseBody,
                                request -> {
                                    String applicationNetworkName = request.getApplicationNetworkName();

                                    StmtProcessor.ifNotNull(
                                            applicationNetworkName,
                                            () -> applicationDtoList.setApplicationDtoList(getApplicationDtosByNetworkName(applicationNetworkName)),
                                            () -> applicationDtoList.setApplicationDtoList(getAllApplicationDtos())
                                    );

                                    responseBody.setCreatedEntity(applicationDtoList);
                                    responseBody.complete();
                                }
                        )
        );
    }

    private List<ApplicationDto> getApplicationDtosByNetworkName(String applicationNetworkName) {

        return applicationDao
                .findApplicationsByApplicationNetworkName(applicationNetworkName)
                .stream()
                .map(ApplicationDto::of)
                .collect(Collectors.toList());
    }

    private List<ApplicationDto> getAllApplicationDtos() {

        return applicationDao
                .findAllActiveApplications()
                .stream()
                .map(ApplicationDto::of)
                .collect(Collectors.toList());
    }

    private Application createApplication(ApplicationDto applicationDto, ResponseBody<CreatedApplication> responseBody) {

        Application application = applicationDao.save(StmtProcessor.create(Application.class,
                createdApp -> fillApplicationFromDto(createdApp, applicationDto))
        );

        responseBody.setCode(OC_OK);
        responseBody.setMessage(String.format("Application %s successfully created", application.getName()));

        return application;
    }

    private Application updateApplication(ApplicationDto applicationDto, ResponseBody<CreatedApplication> responseBody) {
        Application application = applicationDao.findByApplicationId(applicationDto.getApplicationId());

        fillApplicationFromDto(application, applicationDto);

        applicationDao.save(application);

        responseBody.setCode(OC_OK);
        responseBody.setMessage(String.format("Application %s was successfully updated ", application.getName()));

        return application;
    }

    private void fillApplicationFromDto(Application application, ApplicationDto applicationDto) {
        application.setName(applicationDto.getName());
        application.setApplicationNetwork(applicationNetworkDao.findApplicationNetworkByName(applicationDto.getApplicationNetworkName()));
        application.setDescription(applicationDto.getDescription());

        Integer applicationStatusId = applicationDto.getApplicationStatusId();

        application.setApplicationStatus(
                applicationStatusId == null
                        ? applicationDao.getActiveApplicationStatus()
                        : applicationDao.findApplicationStatusById(applicationStatusId)
        );
    }

}
