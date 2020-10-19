/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.test.core;

import java.time.LocalDateTime;
import org.dbs24.application.core.locale.NLS;
import org.dbs24.application.core.log.LogService;
import org.dbs24.application.core.nullsafe.NullSafe;
import org.dbs24.application.core.nullsafe.StopWatcher;
import org.dbs24.entity.core.AbstractPersistenceEntity;
import org.dbs24.persistence.core.PersistenceEntityManager;
import java.util.Collection;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.dbs24.service.WebClientMgmt;
import org.springframework.core.env.Environment;

/**
 *
 * @author kazyra_d
 */
@Data
public abstract class Utils4test {

    @LocalServerPort
    private int port;

    @Autowired
    private PersistenceEntityManager PersistenceEntityManager;

    @Autowired
    private WebClientMgmt webClientMgmt;
    
    @Autowired
    private Environment environment;    

    //==========================================================================
    public static final String generateTestName(final Class clazz) {
        return String
                .format("test_%s_%s", clazz.getCanonicalName(), NLS.getStringDateTime(LocalDateTime.now()))
                .replace(" ", "_");
    }

    //==========================================================================
    @Transactional(readOnly = true, propagation = Propagation.NEVER)
    public <T extends AbstractPersistenceEntity> T loadLastEntity(final Class<T> entClass) {

        final String className = entClass.getSimpleName();
        final StopWatcher stopWatcher = StopWatcher.create(className);
        final String query = String.format("Select e from %s e where e.entity_id = (select max(entity_id) from %s )",
                className,
                className);

        LogService.LogInfo(this.getClass(), () -> String.format("Reload last entity (%s)", className));

        final Collection<T> collection = this.getPersistenceEntityManager()
                .getEntityManager()
                .createQuery(query)
                .getResultList();

        if (collection.isEmpty()) {
            throw new RuntimeException(String.format("Entity not found", className));
        }

        final T entity = collection.iterator().next();

        LogService.LogInfo(this.getClass(), () -> String.format("Entity is loaded (%s, entity_id=%d)",
                stopWatcher.getStringExecutionTime(),
                entity.entityId()
        ));

        return entity;

    }

    //==========================================================================
    @Transactional(readOnly = true, propagation = Propagation.NEVER)
    public <T extends AbstractPersistenceEntity> Collection<T> loadAllEntities(final Class<T> entClass) {

        final String className = entClass.getSimpleName();
        final StopWatcher stopWatcher = StopWatcher.create(className);
        final String query = String.format("Select e from %s e ", className);

        LogService.LogInfo(this.getClass(), () -> String.format("Reload all entities (%s)", className));

        final Collection<T> collection = this.getPersistenceEntityManager()
                .getEntityManager()
                .createQuery(query)
                .getResultList();

        LogService.LogInfo(this.getClass(), () -> String.format("%s : %d entities is loaded (%s)",
                className,
                collection.size(),
                stopWatcher.getStringExecutionTime()
        ));

        return collection;

    }

    //==========================================================================
    public String getTestRestAddress() {
        return String.format("http://localhost:%d", port);
    }
}
