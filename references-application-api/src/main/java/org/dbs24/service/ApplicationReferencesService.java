/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.service;

import lombok.Data;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.dbs24.component.PersistenceService;
import org.dbs24.entity.*;
import org.dbs24.references.core.CachedReferencesClasses;

@Data
@Service
@CachedReferencesClasses(classes = {Currency.class})
public class ApplicationReferencesService extends AbstractReferencesService {

    final PersistenceService persistenceEntityManager;

    @Autowired
    public ApplicationReferencesService(PersistenceService persistenceEntityManager) {
        this.persistenceEntityManager = persistenceEntityManager;
    }
}
