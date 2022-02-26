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
import org.dbs24.tik.dev.dao.TikAccountDao;
import org.dbs24.tik.dev.entity.TikAccount;
import org.dbs24.tik.dev.entity.TikAccountHist;
import org.dbs24.tik.dev.rest.dto.tik.account.CreateTikAccountRequest;
import org.dbs24.tik.dev.rest.dto.tik.account.CreatedTikAccount;
import org.dbs24.tik.dev.rest.dto.tik.account.CreatedTikAccountResponse;
import org.dbs24.tik.dev.rest.dto.tik.account.TikAccountInfo;
import org.dbs24.tik.dev.rest.dto.tik.account.validator.TikAccountInfoValidator;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import java.util.function.BiFunction;
import java.util.function.Supplier;

import static java.time.LocalDateTime.now;
import static java.util.Optional.ofNullable;
import static org.dbs24.application.core.locale.NLS.long2LocalDateTime;
import static org.dbs24.rest.api.consts.RestApiConst.RestOperCode.OC_INVALID_ENTITY_ATTRS;
import static org.dbs24.rest.api.consts.RestApiConst.RestOperCode.OC_OK;
import static org.dbs24.stmt.StmtProcessor.*;

@Getter
@Log4j2
@Component
@EqualsAndHashCode
public class TikAccountService extends AbstractRestApplicationService {

    final TikAccountDao tikAccountDao;
    final RefsService refsService;
    final DevelopersService developersService;
    final TikAccountInfoValidator tikAccountInfoValidator;

    public TikAccountService(RefsService refsService, TikAccountDao tikAccountDao, TikAccountInfoValidator tikAccountInfoValidator, DevelopersService developersService) {

        this.refsService = refsService;
        this.tikAccountDao = tikAccountDao;
        this.tikAccountInfoValidator = tikAccountInfoValidator;
        this.developersService = developersService;
    }

    @FunctionalInterface
    interface TikAccountsHistBuilder {
        void buildTikAccountsHist(TikAccount tikAccount);
    }

    final Supplier<TikAccount> createNewTikAccount = () -> create(TikAccount.class, tikAccount -> {
        tikAccount.setCreateDate(now());
        tikAccount.setActualDate(tikAccount.getCreateDate());
    });


    final BiFunction<TikAccountInfo, TikAccount, TikAccount> assignDto = (tikAccountInfo, tikAccount) -> {

        tikAccount.setActualDate(long2LocalDateTime(tikAccountInfo.getActualDate()));
        ifNull(tikAccount.getActualDate(), () -> tikAccount.setActualDate(now()));
        tikAccount.setTikAccountStatus(getRefsService().findTikAccountStatus(tikAccountInfo.getTikAccountStatusId()));
        tikAccount.setDeveloper(getDevelopersService().findDeveloper(tikAccountInfo.getDeveloperId()));
        tikAccount.setTikAuthKey(tikAccountInfo.getTikAuthKey());
        tikAccount.setTikEmail(tikAccountInfo.getTikEmail());
        tikAccount.setTikLogin(tikAccountInfo.getTikLogin());
        tikAccount.setTikPwd(tikAccountInfo.getTikPwd());

        return tikAccount;
    };

    final BiFunction<TikAccountInfo, TikAccountsHistBuilder, TikAccount> assignTikAccountsInfo = (tikAccountInfo, tikAccountsHistBuilder) -> {

        final TikAccount tikAccount = ofNullable(tikAccountInfo.getTikAccountId())
                .map(this::findTikAccount)
                .orElseGet(createNewTikAccount);

        // store history
        ofNullable(tikAccount.getTikAccountId()).ifPresent(borId -> tikAccountsHistBuilder.buildTikAccountsHist(tikAccount));

        assignDto.apply(tikAccountInfo, tikAccount);

        return tikAccount;
    };

    //==========================================================================
    @Transactional
    public CreatedTikAccountResponse createOrUpdateTikAccount(Mono<CreateTikAccountRequest> monoRequest) {

        return this.<CreatedTikAccount, CreatedTikAccountResponse>createAnswer(CreatedTikAccountResponse.class,
                (responseBody, createdTikAccount) -> processRequest(monoRequest, responseBody, request
                        -> responseBody.setErrors(tikAccountInfoValidator.validateConditional(request.getEntityInfo(), tikAccountInfo
                        -> {

                    final SimpleActionInfo simpleActionInfo = request.getEntityAction();

                    //log.info("simpleActionInfo =  {}", simpleActionInfo);

                    log.debug("create/update tikAccount: {}", tikAccountInfo);

                    //StmtProcessor.assertNotNull(String.class, tikAccountInfo.getPackageName(), "packageName name is not defined");

                    final TikAccount tikAccount = findOrCreateTikAccounts(tikAccountInfo, tikAccountHist -> saveTikAccountHist(createTikAccountHist(tikAccountHist)));

                    final Boolean isNewSetting = isNull(tikAccount.getTikAccountId());

                    getTikAccountDao().saveTikAccount(tikAccount);

                    final String finalMessage = String.format("TikAccount is %s (TikAccountId=%d)",
                            isNewSetting ? "created" : "updated",
                            tikAccount.getTikAccountId());

                    createdTikAccount.setCreatedTikAccountId(tikAccount.getTikAccountId());
                    responseBody.complete();

                    log.debug(finalMessage);

                    responseBody.setCode(OC_OK);
                    responseBody.setMessage(finalMessage);
                }, errorInfos -> {
                    responseBody.setCode(OC_INVALID_ENTITY_ATTRS);
                    responseBody.setMessage(errorInfos.stream().findFirst().orElseThrow().getErrorMsg().toUpperCase());
                }))));
    }

    public TikAccount findOrCreateTikAccounts(TikAccountInfo tikAccountInfo, TikAccountService.TikAccountsHistBuilder tikAccountsHistBuilder) {
        return assignTikAccountsInfo.apply(tikAccountInfo, tikAccountsHistBuilder);
    }

    private TikAccountHist createTikAccountHist(TikAccount tikAccount) {
        return create(TikAccountHist.class, tikAccountHist -> {
            tikAccountHist.setTikAccountId(tikAccount.getTikAccountId());
            tikAccountHist.setActualDate(tikAccount.getActualDate());
            tikAccountHist.setDeveloper(tikAccount.getDeveloper());
            tikAccountHist.setTikAccountStatus(tikAccount.getTikAccountStatus());
            tikAccountHist.setTikAuthKey(tikAccount.getTikAuthKey());
            tikAccountHist.setTikEmail(tikAccount.getTikEmail());
            tikAccountHist.setTikLogin(tikAccount.getTikLogin());
            tikAccountHist.setTikPwd(tikAccount.getTikPwd());

        });
    }

    private void saveTikAccountHist(TikAccountHist tikAccountHist) {
        getTikAccountDao().saveTikAccountHist(tikAccountHist);
    }

    public TikAccount findTikAccount(Long tikAccountId) {
        return getTikAccountDao().findTikAccount(tikAccountId);
    }


}
