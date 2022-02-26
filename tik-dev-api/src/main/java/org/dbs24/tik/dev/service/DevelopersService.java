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
import org.dbs24.tik.dev.dao.DeveloperDao;
import org.dbs24.tik.dev.entity.Developer;
import org.dbs24.tik.dev.entity.DeveloperHist;
import org.dbs24.tik.dev.rest.dto.developer.CreateDeveloperRequest;
import org.dbs24.tik.dev.rest.dto.developer.CreatedDeveloper;
import org.dbs24.tik.dev.rest.dto.developer.CreatedDeveloperResponse;
import org.dbs24.tik.dev.rest.dto.developer.DeveloperInfo;
import org.dbs24.tik.dev.rest.dto.developer.validator.DeveloperInfoValidator;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.function.BiFunction;
import java.util.function.Supplier;

import static java.util.Optional.ofNullable;
import static org.dbs24.application.core.locale.NLS.long2LocalDateTime;
import static org.dbs24.rest.api.consts.RestApiConst.RestOperCode.OC_INVALID_ENTITY_ATTRS;
import static org.dbs24.rest.api.consts.RestApiConst.RestOperCode.OC_OK;
import static org.dbs24.stmt.StmtProcessor.create;
import static org.dbs24.stmt.StmtProcessor.isNull;

@Getter
@Log4j2
@Component
@EqualsAndHashCode
public class DevelopersService extends AbstractRestApplicationService {

    final DeveloperDao developerDao;
    final RefsService refsService;
    final DeveloperInfoValidator developerInfoValidator;
//    final DeveloperInfoValidator developerInfoValidator;
//    private final DeveloperDetailDao developerDetailDao;

    public DevelopersService(RefsService refsService, DeveloperDao developerDao, DeveloperInfoValidator developerInfoValidator) {

        this.refsService = refsService;
        this.developerDao = developerDao;
        this.developerInfoValidator = developerInfoValidator;
//        this.developerDao = developerDao;
//        this.developerInfoValidator = developerInfoValidator;
//        this.developerDetailDao = developerDetailDao;
    }

    @FunctionalInterface
    interface DevelopersHistBuilder {
        void buildDevelopersHist(Developer developer);
    }

    final Supplier<Developer> createNewDeveloper = () -> create(Developer.class);


    final BiFunction<DeveloperInfo, Developer, Developer> assignDto = (developerInfo, developer) -> {

        developer.setActualDate(long2LocalDateTime(developerInfo.getActualDate()));
        developer.setDeveloperStatus(getRefsService().findDeveloperStatus(developerInfo.getDeveloperStatusId()));
        developer.setApiKey(developerInfo.getApiKey());
        developer.setEmail(developerInfo.getEmail());
        developer.setCountryCode(developerInfo.getCountryCode());
        developer.setOauthClientId(developerInfo.getOauthClientId());
        developer.setWebsite(developerInfo.getWebsite());

        return developer;
    };

    final BiFunction<DeveloperInfo, DevelopersHistBuilder, Developer> assignDevelopersInfo = (developerInfo, developersHistBuilder) -> {

        final Developer developer = ofNullable(developerInfo.getDeveloperId())
                .map(this::findDeveloper)
                .orElseGet(createNewDeveloper);

        // store history
        ofNullable(developer.getDeveloperId()).ifPresent(borId -> developersHistBuilder.buildDevelopersHist(developer));

        assignDto.apply(developerInfo, developer);

        return developer;
    };

    //==========================================================================
    @Transactional
    public CreatedDeveloperResponse createOrUpdateDeveloper(Mono<CreateDeveloperRequest> monoRequest) {

        return this.<CreatedDeveloper, CreatedDeveloperResponse>createAnswer(CreatedDeveloperResponse.class,
                (responseBody, createdDeveloper) -> processRequest(monoRequest, responseBody, request
                        -> responseBody.setErrors(developerInfoValidator.validateConditional(request.getEntityInfo(), developerInfo
                        -> {

                    final SimpleActionInfo simpleActionInfo = request.getEntityAction();

                    //log.info("simpleActionInfo =  {}", simpleActionInfo);

                    log.debug("create/update developer: {}", developerInfo);

                    //StmtProcessor.assertNotNull(String.class, developerInfo.getPackageName(), "packageName name is not defined");

                    final Developer developer = findOrCreateDevelopers(developerInfo, developerHist -> saveDeveloperHist(createDeveloperHist(developerHist)));

                    final Boolean isNewSetting = isNull(developer.getDeveloperId());

                    getDeveloperDao().saveDeveloper(developer);

                    final String finalMessage = String.format("Developer is %s (DeveloperId=%d)",
                            isNewSetting ? "created" : "updated",
                            developer.getDeveloperId());

                    createdDeveloper.setCreatedDeveloperId(developer.getDeveloperId());
                    responseBody.complete();

                    log.debug(finalMessage);

                    responseBody.setCode(OC_OK);
                    responseBody.setMessage(finalMessage);
                }, errorInfos -> {
                    responseBody.setCode(OC_INVALID_ENTITY_ATTRS);
                    responseBody.setMessage(errorInfos.stream().findFirst().orElseThrow().getErrorMsg().toUpperCase());
                }))));
    }

    public Developer findOrCreateDevelopers(DeveloperInfo developerInfo, DevelopersService.DevelopersHistBuilder developersHistBuilder) {
        return assignDevelopersInfo.apply(developerInfo, developersHistBuilder);
    }

    private DeveloperHist createDeveloperHist(Developer developer) {
        return create(DeveloperHist.class, developerHist -> {
            developerHist.setDeveloperId(developer.getDeveloperId());
            developerHist.setActualDate(LocalDateTime.now());
            developerHist.setDeveloperStatus(developer.getDeveloperStatus());
            developerHist.setCountryCode(developer.getCountryCode());
            developerHist.setApiKey(developer.getApiKey());
            developerHist.setWebsite(developer.getWebsite());
            developerHist.setOauthClientId(developer.getOauthClientId());
        });
    }

    private void saveDeveloperHist(DeveloperHist developerHist) {
        getDeveloperDao().saveDeveloperHist(developerHist);
    }

    public Developer findDeveloper(Long developerId) {
        return getDeveloperDao().findDeveloper(developerId);
    }


}
