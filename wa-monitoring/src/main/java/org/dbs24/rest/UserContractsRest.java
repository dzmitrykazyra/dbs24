/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.rest;

import lombok.EqualsAndHashCode;
import lombok.extern.log4j.Log4j2;
import org.dbs24.application.core.locale.NLS;
import org.dbs24.component.RefsService;
import org.dbs24.component.UserContractsService;
import org.dbs24.entity.dto.ModifyContractBySupportInfo;
import org.dbs24.entity.dto.ModifyContractEndDateInfo;
import org.dbs24.entity.dto.ModifyContractInfo;
import org.dbs24.rest.api.*;
import org.dbs24.stmt.StmtProcessor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

import static org.dbs24.consts.WaConsts.Classes.*;
import static org.dbs24.consts.WaConsts.RestQueryParams.QP_CONTRACT_STATUS;
import static org.dbs24.consts.WaConsts.RestQueryParams.QP_LOGIN_TOKEN;
import static org.dbs24.stmt.StmtProcessor.ifNull;

@Log4j2
@Component
@ConditionalOnProperty(name = "config.restfull.profile.name", havingValue = "wa-monitoring")
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
public class UserContractsRest extends ReactiveRestProcessor {

    final UserContractsService userContractsService;
    final RefsService refsService;

    public UserContractsRest(UserContractsService userContractsService, RefsService refsService) {
        this.userContractsService = userContractsService;
        this.refsService = refsService;
    }

    //==========================================================================
    public Mono<ServerResponse> createOrUpdateContract(ServerRequest request) {

        return this.<UserContractInfo, CreatedUserContract>createResponse(request, USER_CONTRACT_INFO_CLASS, CreatedUserContract.class,
                userContractInfo -> userContractsService.createOrUpdateContract(userContractInfo));
    }

    //==========================================================================
    public Mono<ServerResponse> createOrUpdateFutureTrialContract(ServerRequest request) {

        return this.<FutureTrialUserContractsInfo, CreatedUserContract>createResponse(request, FutureTrialUserContractsInfo.class, CreatedUserContract.class,
                futureContractInfo -> userContractsService.createOrUpdateFutureTrialContract(futureContractInfo));
    }

    //==========================================================================
    @Transactional(readOnly = true)
    public Mono<ServerResponse> getUserContracts(ServerRequest request) {

        return this.<UserContractInfoCollection>createResponse(request, UserContractInfoCollection.class,
                () -> StmtProcessor.create(USER_CONTRACT_INFO_COLLECTION_CLASS, userContracts -> {

                    final String loginToken = this.getStringFromParam(request, QP_LOGIN_TOKEN);
                    final String constractStatusString = this.getOptionalStringFromParam(request, QP_CONTRACT_STATUS);

                    final Byte actualconstractStatus = !constractStatusString.isEmpty() ? Byte.valueOf(constractStatusString) : null;

                    log.info("getUserContracts: find login token {}, contractStatus = {}", loginToken, actualconstractStatus);

                    userContractsService.findAllUserContracts(loginToken)
                            .stream()
                            .filter(c -> c.getContractStatus().getContractStatusId().equals(actualconstractStatus) || StmtProcessor.isNull(actualconstractStatus))
                            .sorted((a, b) -> StmtProcessor.nvl(b.getEndDate(), LocalDateTime.MAX).compareTo(StmtProcessor.nvl(a.getEndDate(), LocalDateTime.MAX)))
                            .forEach(uc -> userContracts.getUserContracts().add(StmtProcessor.create(USER_CONTRACT_INFO_CLASS, uci -> uci.assign(uc))));

                    log.info("getUserContracts: loginToken {}, records {}", loginToken, userContracts.getUserContracts().size());

                }));
    }

    //==========================================================================
    public Mono<ServerResponse> getActualUserContracts(ServerRequest request) {

        final String loginToken = getStringFromParam(request, QP_LOGIN_TOKEN);

        return this.<UserContractInfoCollection>createResponse(request, UserContractInfoCollection.class,
                () -> userContractsService.getActualUserContractsInfo(loginToken));
    }

    //==========================================================================
    @Transactional(readOnly = true)
    public Mono<ServerResponse> checkUserContractValidity(ServerRequest request) {

        return this.<ContractExpiryInfo>createResponse(request, ContractExpiryInfo.class,
                () -> StmtProcessor.create(ContractExpiryInfo.class, contractExpiryInfo -> {

                    final String loginToken = this.getStringFromParam(request, QP_LOGIN_TOKEN);

                    userContractsService.findAllUserContracts(loginToken)
                            .stream()
                            .sorted((a, b) -> StmtProcessor.nvl(b.getEndDate(), LocalDateTime.MAX).compareTo(StmtProcessor.nvl(a.getEndDate(), LocalDateTime.MAX)))
                            .limit(1)
                            .forEach(ci -> contractExpiryInfo.setExpiryTime(NLS.localDateTime2long(ci.getEndDate())));

                    ifNull(contractExpiryInfo.getExpiryTime(), () -> contractExpiryInfo.setExpiryTime(NLS.localDateTime2long(LocalDateTime.now())));

                    log.info("checkUserContractValidity: loginToken {}, expiryInfo {}", loginToken, contractExpiryInfo);
                }));
    }

    //==========================================================================
    @Transactional(readOnly = true)
    public Mono<ServerResponse> checkAllUserContractValidity(ServerRequest request) {

        return this.<ShortUserContractInfoCollection>createResponse(request, ShortUserContractInfoCollection.class,
                () -> StmtProcessor.create(SHORT_USER_CONTRACT_INFO_COLLECTION_CLASS, shortContractsInfo -> {

                    final String loginToken = this.getStringFromParam(request, QP_LOGIN_TOKEN);

                    userContractsService.findActualUserContracts(loginToken)
                            .stream()
                            .forEach(uc -> shortContractsInfo.getShortUserContracts().add(
                                    StmtProcessor.create(SHORT_USER_CONTRACT_INFO_CLASS,
                                            uci -> {
                                                uci.setContractId(uc.getContractId());
                                                uci.setContractFinishDate(NLS.localDateTime2long(uc.getEndDate()));
                                                uci.setContractFinishDateLdt(uc.getEndDate());
                                            })));

                    log.info("checkAllUserContractValidity: loginToken {}, records {}", loginToken, shortContractsInfo.getShortUserContracts().size());
                }));
    }

    //==========================================================================
    public Mono<ServerResponse> createContractFromPayment(ServerRequest request) {

        return this.<UserContractFromPaymentInfo, CreatedUserContract>createResponse(request, UserContractFromPaymentInfo.class, CreatedUserContract.class,
                userContractsService::createContractFromPayment);
    }

    //==========================================================================
    public Mono<ServerResponse> modifyContract(ServerRequest request) {

        return this.<ModifyContractInfo, CreatedUserContract>createResponse(request, ModifyContractInfo.class, CreatedUserContract.class,
                userContractsService::modifyContract);
    }

    //==========================================================================
    public Mono<ServerResponse> modifyContractBySupport(ServerRequest request) {

        final String loginToken = getOptionalStringFromParam(request, QP_LOGIN_TOKEN);

        return this.<ModifyContractBySupportInfo, CreatedUserContract>createResponse(request, ModifyContractBySupportInfo.class, CreatedUserContract.class,
                newContractInfo -> userContractsService.modifyContractBySupport(newContractInfo, loginToken));
    }


    //==========================================================================
    public Mono<ServerResponse> modifyContractEndDate(ServerRequest request) {

        return this.<ModifyContractEndDateInfo, CreatedUserContract>createResponse(request, ModifyContractEndDateInfo.class, CreatedUserContract.class,
                userContractsService::modifyContractEndDate);
    }
}
