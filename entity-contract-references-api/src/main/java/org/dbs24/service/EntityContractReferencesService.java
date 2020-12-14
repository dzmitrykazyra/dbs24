/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.service;

import java.util.Collection;
import lombok.Data;
import org.springframework.stereotype.Service;
import org.dbs24.entity.*;
import org.dbs24.component.PersistenceEntityManager;
import org.dbs24.references.api.AbstractRefRecord;
import org.dbs24.references.core.CachedReferencesClasses;
import org.springframework.beans.factory.annotation.Autowired;
import static org.dbs24.consts.EntityContractConsts.*;

@Data
@Service
@CachedReferencesClasses(classes = {ContractSubject.class})
public class EntityContractReferencesService extends AbstractReferencesService {

    final PersistenceEntityManager persistenceEntityManager;

    @Autowired
    public EntityContractReferencesService(PersistenceEntityManager persistenceEntityManager) {
        this.persistenceEntityManager = persistenceEntityManager;
    }

//    //==========================================================================
//    @Deprecated
//    public final void createContractSubject(Integer contractSubjectId,
//            final String contractSubjectName) {
//
//        persistenceEntityManager.<ContractSubject>mergePersistenceEntity(ContractSubject.class,
//                contractSubject -> {
//
//                    contractSubject.setContractSubjectId(contractSubjectId);
//                    contractSubject.setContractSubjectName(contractSubjectName);
//                });
//    }

    //==========================================================================
    public static final ContractSubject findContractSubject(Integer liasContractSubjectId) {
        return AbstractRefRecord.<ContractSubject>getRefeenceRecord(ContractSubject.class,
                record -> record.getContractSubjectId().equals(liasContractSubjectId));
    }
    //==========================================================================
    public static final Collection<ContractSubject> getContractSubjectCollection() {

        return AbstractReferencesService.<ContractSubject>getGenericCollection(
                CONTRACT_SUBJECT_CLASS, new String[][]{
                    {"1", "Предмет договора -1"},
                    {"2", "Предмет договора -2"},
                    {"210001", "Предмет договора -210001"}
                },
                (record, stringRow) -> {
                    record.setContractSubjectId(Integer.valueOf(stringRow[0]));
                    record.setContractSubjectName(stringRow[1]);
                });
    }
}
