/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.service;

import lombok.Data;
import org.springframework.stereotype.Service;
import org.dbs24.entity.contract.subjects.ContractSubject;
import org.dbs24.persistence.core.PersistenceEntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.dbs24.spring.core.api.ApplicationService;

/**
 *
 * @author Козыро Дмитрий
 */
@Data
@Service
public class EntityContractReferencesService implements ApplicationService {

    @Autowired
    private PersistenceEntityManager PersistenceEntityManager;

    //==========================================================================
    public final void createContractSubject(final Integer contractSubjectId,
            final String contractSubjectName) {

        PersistenceEntityManager.<ContractSubject>mergePersistenceEntity(ContractSubject.class,
                contractSubject -> {

                    contractSubject.setContractSubjectId(contractSubjectId);
                    contractSubject.setContractSubjectName(contractSubjectName);

                });
    }
}
