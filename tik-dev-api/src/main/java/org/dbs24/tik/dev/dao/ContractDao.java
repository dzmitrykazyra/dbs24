/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.tik.dev.dao;

import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import org.dbs24.spring.core.api.DaoAbstractApplicationService;
import org.dbs24.tik.dev.entity.Contract;
import org.dbs24.tik.dev.entity.ContractHist;
import org.dbs24.tik.dev.repo.ContractHistRepo;
import org.dbs24.tik.dev.repo.ContractRepo;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Getter
@Log4j2
@Component
public class ContractDao extends DaoAbstractApplicationService {

    final ContractRepo contractRepo;
    final ContractHistRepo contractHistRepo;

    public ContractDao(ContractRepo contractRepo, ContractHistRepo contractHistRepo) {
        this.contractRepo = contractRepo;
        this.contractHistRepo = contractHistRepo;
    }

    //==========================================================================
    public Optional<Contract> findOptionalContract(Long contractId) {
        return contractRepo.findById(contractId);
    }

    public Contract findContract(Long contractId) {
        return findOptionalContract(contractId).orElseThrow();
    }

    public void saveContractHist(ContractHist contractHist) {
        contractHistRepo.save(contractHist);
    }

    public void saveContract(Contract contract) {
        contractRepo.save(contract);
    }

}
