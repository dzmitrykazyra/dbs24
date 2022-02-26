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
import org.dbs24.tik.dev.dao.ContractDao;
import org.dbs24.tik.dev.entity.Contract;
import org.dbs24.tik.dev.entity.ContractHist;
import org.dbs24.tik.dev.rest.dto.contract.ContractInfo;
import org.dbs24.tik.dev.rest.dto.contract.CreateContractRequest;
import org.dbs24.tik.dev.rest.dto.contract.CreatedContract;
import org.dbs24.tik.dev.rest.dto.contract.CreatedContractResponse;
import org.dbs24.tik.dev.rest.dto.contract.validator.ContractInfoValidator;
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
public class ContractsService extends AbstractRestApplicationService {

    final ContractDao contractDao;
    final RefsService refsService;
    final DevelopersService developersService;
    final TariffPlanService tariffPlanService;
    final ContractInfoValidator contractInfoValidator;

    public ContractsService(RefsService refsService, ContractDao contractDao, ContractInfoValidator contractInfoValidator, DevelopersService developersService, TariffPlanService tariffPlanService) {

        this.refsService = refsService;
        this.contractDao = contractDao;
        this.contractInfoValidator = contractInfoValidator;
        this.developersService = developersService;
        this.tariffPlanService = tariffPlanService;
    }

    @FunctionalInterface
    interface ContractsHistBuilder {
        void buildContractsHist(Contract contract);
    }

    final Supplier<Contract> createNewContract = () -> create(Contract.class);

    final BiFunction<ContractInfo, Contract, Contract> assignDto = (contractInfo, contract) -> {

        contract.setActualDate(long2LocalDateTime(contractInfo.getActualDate()));
        ifNull(contract.getActualDate(), () -> contract.setActualDate(now()));
        contract.setDeveloper(getDevelopersService().findDeveloper(contractInfo.getDeveloperId()));
        contract.setContractStatus(getRefsService().findContractStatus(contractInfo.getContractStatusId()));
        contract.setTariffPlan(getTariffPlanService().findTariffPlan(contractInfo.getTariffPlanId()));
        contract.setBeginDate(long2LocalDateTime(contractInfo.getBeginDate()));
        contract.setEndDate(long2LocalDateTime(contractInfo.getEndDate()));
        contract.setCancelDate(long2LocalDateTime(contractInfo.getCancelDate()));

        return contract;
    };

    final BiFunction<ContractInfo, ContractsHistBuilder, Contract> assignContractsInfo = (contractInfo, contractsHistBuilder) -> {

        final Contract contract = ofNullable(contractInfo.getContractId())
                .map(this::findContract)
                .orElseGet(createNewContract);

        // store history
        ofNullable(contract.getContractId()).ifPresent(borId -> contractsHistBuilder.buildContractsHist(contract));

        assignDto.apply(contractInfo, contract);

        return contract;
    };

    //==========================================================================
    @Transactional
    public CreatedContractResponse createOrUpdateContract(Mono<CreateContractRequest> monoRequest) {

        return this.<CreatedContract, CreatedContractResponse>createAnswer(CreatedContractResponse.class,
                (responseBody, createdContract) -> processRequest(monoRequest, responseBody, request
                        -> responseBody.setErrors(contractInfoValidator.validateConditional(request.getEntityInfo(), contractInfo
                        -> {

                    final SimpleActionInfo simpleActionInfo = request.getEntityAction();

                    //log.info("simpleActionInfo =  {}", simpleActionInfo);

                    log.debug("create/update contract: {}", contractInfo);

                    //StmtProcessor.assertNotNull(String.class, contractInfo.getPackageName(), "packageName name is not defined");

                    final Contract contract = findOrCreateContracts(contractInfo, contractHist -> saveContractHist(createContractHist(contractHist)));

                    final Boolean isNewSetting = isNull(contract.getContractId());

                    getContractDao().saveContract(contract);

                    final String finalMessage = String.format("Contract is %s (ContractId=%d)",
                            isNewSetting ? "created" : "updated",
                            contract.getContractId());

                    createdContract.setCreatedContractId(contract.getContractId());
                    responseBody.complete();

                    log.debug(finalMessage);

                    responseBody.setCode(OC_OK);
                    responseBody.setMessage(finalMessage);
                }, errorInfos -> {
                    responseBody.setCode(OC_INVALID_ENTITY_ATTRS);
                    responseBody.setMessage(errorInfos.stream().findFirst().orElseThrow().getErrorMsg().toUpperCase());
                }))));
    }

    public Contract findOrCreateContracts(ContractInfo contractInfo, ContractsService.ContractsHistBuilder contractsHistBuilder) {
        return assignContractsInfo.apply(contractInfo, contractsHistBuilder);
    }

    private ContractHist createContractHist(Contract contract) {
        return create(ContractHist.class, contractHist -> {
            contractHist.setContractId(contract.getContractId());
            contractHist.setActualDate(contract.getActualDate());
            contractHist.setContractStatus(contract.getContractStatus());
            contractHist.setEndDate(contract.getEndDate());
            contractHist.setBeginDate(contract.getBeginDate());
            contractHist.setCancelDate(contract.getCancelDate());
            contractHist.setDeveloper(contract.getDeveloper());
            contractHist.setTariffPlan(contract.getTariffPlan());
        });
    }

    private void saveContractHist(ContractHist contractHist) {
        getContractDao().saveContractHist(contractHist);
    }

    public Contract findContract(Long contractId) {
        return getContractDao().findContract(contractId);
    }
}
