/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.service;

import lombok.Data;
import lombok.extern.log4j.Log4j2;
import org.dbs24.component.PersistenceEntityManager;
import org.dbs24.references.api.AbstractRefRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Collection;
import org.dbs24.application.core.nullsafe.NullSafe;
import org.dbs24.application.core.nullsafe.StopWatcher;

@Data
@Log4j2
@Service
public class ReferenceRecordsManager<T extends AbstractRefRecord> extends AbstractApplicationService {

    final PersistenceEntityManager persistenceEntityManager;

    @Autowired
    public ReferenceRecordsManager(PersistenceEntityManager persistenceEntityManager) {
        this.persistenceEntityManager = persistenceEntityManager;
    }

    //==========================================================================
    public void registerReference(
            Collection<T> collection,
            Class<T> clazz,
            String serviceName) {
        log.debug("{} is registered ({} recs, by {}) ",
                clazz.getCanonicalName(),
                collection.size(),
                serviceName
        );

        //final StopWatcher stopWatcher = StopWatcher.create("registerReference");
        // сохранение в бд
        getPersistenceEntityManager()
                .executeTransaction(em
                        -> collection
                        .stream()
                        .forEach(record -> em.merge(record)));
        //log.debug(stopWatcher.getStringExecutionTime());
    }

    //==========================================================================
    public void registerReferenceAndRefresh(
            Collection<T> collection,
            Class<T> clazz,
            String serviceName) {
        registerReference(collection, clazz, serviceName);
        refreshCacheReference(clazz);
    }

    //==========================================================================    
    public void refreshCacheReference(Class<T> clazz) {

        // перечитываем справочники
        NullSafe.create()
                .execute(stmt -> {
                    final Collection<? extends AbstractRefRecord> collection = getPersistenceEntityManager()
                            .getEntityManager()
                            .createQuery("Select t from " + clazz.getSimpleName() + " t")
                            .getResultList();

                    AbstractRefRecord.REF_CACHE.put(clazz, collection);
                    log.debug(String.format("%s is loaded ",
                            clazz.getCanonicalName()));
                })
                .catchException(e -> log.error("Can't load refernce '{}' ({}) ", clazz.getCanonicalName(), e.getMessage()))
                .throwException();
    }
}
