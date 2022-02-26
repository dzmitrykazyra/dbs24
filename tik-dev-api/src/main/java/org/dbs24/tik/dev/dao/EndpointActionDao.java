/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.tik.dev.dao;

import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import org.dbs24.spring.core.api.DaoAbstractApplicationService;
import org.dbs24.tik.dev.entity.EndpointAction;
import org.dbs24.tik.dev.repo.EndpointActionRepo;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Getter
@Log4j2
@Component
public class EndpointActionDao extends DaoAbstractApplicationService {

    final EndpointActionRepo contractRepo;

    public EndpointActionDao(EndpointActionRepo contractRepo) {
        this.contractRepo = contractRepo;
    }

    //==========================================================================
    public Optional<EndpointAction> findOptionalEndpointAction(Long contractId) {
        return contractRepo.findById(contractId);
    }

    public EndpointAction findEndpointAction(Long contractId) {
        return findOptionalEndpointAction(contractId).orElseThrow();
    }

    public void saveEndpointAction(EndpointAction contract) {
        contractRepo.save(contract);
    }

}
