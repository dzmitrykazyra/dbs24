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
import org.dbs24.tik.dev.dao.TikAccountScopeDao;
import org.dbs24.tik.dev.entity.TikAccountScope;
import org.dbs24.tik.dev.entity.TikAccountScopeHist;
import org.dbs24.tik.dev.rest.dto.tik.account.scope.CreateTikAccountScopeRequest;
import org.dbs24.tik.dev.rest.dto.tik.account.scope.CreatedTikAccountScope;
import org.dbs24.tik.dev.rest.dto.tik.account.scope.CreatedTikAccountScopeResponse;
import org.dbs24.tik.dev.rest.dto.tik.account.scope.TikAccountScopeInfo;
import org.dbs24.tik.dev.rest.dto.tik.account.scope.validator.TikAccountScopeInfoValidator;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import java.util.function.BiFunction;
import java.util.function.Supplier;

import static java.time.LocalDateTime.now;
import static java.util.Optional.ofNullable;
import static org.dbs24.rest.api.consts.RestApiConst.RestOperCode.OC_INVALID_ENTITY_ATTRS;
import static org.dbs24.rest.api.consts.RestApiConst.RestOperCode.OC_OK;
import static org.dbs24.stmt.StmtProcessor.*;

@Getter
@Log4j2
@Component
@EqualsAndHashCode
public class TikAccountScopeService extends AbstractRestApplicationService {

    final TikAccountScopeDao tikAccountScopeDao;
    final RefsService refsService;
    final DevelopersService developersService;
    final TikAccountService tikAccountService;
    final TikAccountScopeInfoValidator tikAccountScopeInfoValidator;

    public TikAccountScopeService(RefsService refsService, TikAccountScopeDao tikAccountScopeDao, TikAccountScopeInfoValidator tikAccountScopeInfoValidator, DevelopersService developersService, TikAccountService tikAccountService) {

        this.refsService = refsService;
        this.tikAccountScopeDao = tikAccountScopeDao;
        this.tikAccountScopeInfoValidator = tikAccountScopeInfoValidator;
        this.developersService = developersService;
        this.tikAccountService = tikAccountService;
    }

    @FunctionalInterface
    interface TikAccountScopesHistBuilder {
        void buildTikAccountScopesHist(TikAccountScope tikAccountScope);
    }

    final Supplier<TikAccountScope> createNewTikAccountScope = () -> create(TikAccountScope.class, tikAccountScope -> {
        tikAccountScope.setGrantDate(now());
    });


    final BiFunction<TikAccountScopeInfo, TikAccountScope, TikAccountScope> assignDto = (tikAccountScopeInfo, tikAccountScope) -> {

        ifNull(tikAccountScope.getGrantDate(), () -> tikAccountScope.setGrantDate(now()));
        tikAccountScope.setEndpointScope(getRefsService().findEndpointScope(tikAccountScopeInfo.getEndpointScopeId()));
        tikAccountScope.setTikAccount(getTikAccountService().findTikAccount(tikAccountScopeInfo.getTikAccountId()));
        tikAccountScope.setIsGranted(tikAccountScopeInfo.getIsGranted());

        return tikAccountScope;
    };

    final BiFunction<TikAccountScopeInfo, TikAccountScopesHistBuilder, TikAccountScope> assignTikAccountScopesInfo = (tikAccountScopeInfo, tikAccountScopesHistBuilder) -> {

        final TikAccountScope tikAccountScope = ofNullable(tikAccountScopeInfo.getGrantId())
                .map(this::findTikAccountScope)
                .orElseGet(createNewTikAccountScope);

        // store history
        ofNullable(tikAccountScope.getGrantId()).ifPresent(borId -> tikAccountScopesHistBuilder.buildTikAccountScopesHist(tikAccountScope));

        assignDto.apply(tikAccountScopeInfo, tikAccountScope);

        return tikAccountScope;
    };

    //==========================================================================
    @Transactional
    public CreatedTikAccountScopeResponse createOrUpdateTikAccountScope(Mono<CreateTikAccountScopeRequest> monoRequest) {

        return this.<CreatedTikAccountScope, CreatedTikAccountScopeResponse>createAnswer(CreatedTikAccountScopeResponse.class,
                (responseBody, createdTikAccountScope) -> processRequest(monoRequest, responseBody, request
                        -> responseBody.setErrors(tikAccountScopeInfoValidator.validateConditional(request.getEntityInfo(), tikAccountScopeInfo
                        -> {

                    final SimpleActionInfo simpleActionInfo = request.getEntityAction();

                    //log.info("simpleActionInfo =  {}", simpleActionInfo);

                    log.debug("create/update tikAccountScope: {}", tikAccountScopeInfo);

                    //StmtProcessor.assertNotNull(String.class, tikAccountScopeInfo.getPackageName(), "packageName name is not defined");

                    final TikAccountScope tikAccountScope = findOrCreateTikAccountScopes(tikAccountScopeInfo, tikAccountScopeHist -> saveTikAccountScopeHist(createTikAccountScopeHist(tikAccountScopeHist)));

                    final Boolean isNewSetting = isNull(tikAccountScope.getGrantId());

                    getTikAccountScopeDao().saveTikAccountScope(tikAccountScope);

                    final String finalMessage = String.format("TikAccountScope is %s (TikAccountScopeId=%d)",
                            isNewSetting ? "created" : "updated",
                            tikAccountScope.getGrantId());

                    createdTikAccountScope.setCreatedGrantId(tikAccountScope.getGrantId());
                    responseBody.complete();

                    log.debug(finalMessage);

                    responseBody.setCode(OC_OK);
                    responseBody.setMessage(finalMessage);
                }, errorInfos -> {
                    responseBody.setCode(OC_INVALID_ENTITY_ATTRS);
                    responseBody.setMessage(errorInfos.stream().findFirst().orElseThrow().getErrorMsg().toUpperCase());
                }))));
    }

    public TikAccountScope findOrCreateTikAccountScopes(TikAccountScopeInfo tikAccountScopeInfo, TikAccountScopeService.TikAccountScopesHistBuilder tikAccountScopesHistBuilder) {
        return assignTikAccountScopesInfo.apply(tikAccountScopeInfo, tikAccountScopesHistBuilder);
    }

    private TikAccountScopeHist createTikAccountScopeHist(TikAccountScope tikAccountScope) {
        return create(TikAccountScopeHist.class, tikAccountScopeHist -> {
            tikAccountScopeHist.setGrantDate(tikAccountScope.getGrantDate());
            tikAccountScopeHist.setGrantId(tikAccountScope.getGrantId());
            tikAccountScopeHist.setTikAccount(tikAccountScope.getTikAccount());
            tikAccountScopeHist.setEndpointScope(tikAccountScope.getEndpointScope());
            tikAccountScopeHist.setIsGranted(tikAccountScope.getIsGranted());
        });
    }

    private void saveTikAccountScopeHist(TikAccountScopeHist tikAccountScopeHist) {
        getTikAccountScopeDao().saveTikAccountScopeHist(tikAccountScopeHist);
    }

    public TikAccountScope findTikAccountScope(Long tikAccountScopeId) {
        return getTikAccountScopeDao().findTikAccountScope(tikAccountScopeId);
    }
}
