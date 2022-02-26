/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.service;

import org.dbs24.component.PersistenceService;
import org.springframework.stereotype.Service;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.dbs24.entity.marks.Mark;
import org.dbs24.entity.marks.MarkValue;
import org.dbs24.references.core.CachedReferencesClasses;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;

@Log4j2
@Service
@CachedReferencesClasses(classes = {Mark.class, MarkValue.class})
@ConditionalOnProperty(name = "config.entity.core.enabled", havingValue = "true", matchIfMissing = true)
public class EntityReferencesService extends AbstractReferencesService {

    final PersistenceService persistenceEntityManager;

    //==========================================================================
    @Autowired
    public EntityReferencesService(PersistenceService persistenceEntityManager) {
        this.persistenceEntityManager = persistenceEntityManager;
    }
    //==========================================================================
}
