/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.service;

import org.dbs24.application.core.nullsafe.NullSafe;
import org.dbs24.persistence.core.PersistanceEntityManager;
import org.springframework.stereotype.Service;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.dbs24.entity.type.EntityType;
import org.dbs24.entity.status.EntityStatus;
import org.dbs24.entity.action.ActionCode;
import org.dbs24.entity.kind.EntityKind;
import org.dbs24.spring.core.api.ApplicationService;

/**
 *
 * @author Козыро Дмитрий
 */
@Data
@Service
public class EntityReferencesService implements ApplicationService {

    @Autowired
    private PersistanceEntityManager persistanceEntityManager;

    //==========================================================================
    public final void createNewEntityType(final Integer entityTypeId,
            final String entityTypeName,
            final String entityAppName) {

        persistanceEntityManager.<EntityType>mergePersistenceEntity(EntityType.class,
                entityType -> {

                    entityType.setEntityAppName(entityAppName);
                    entityType.setEntityTypeId(entityTypeId);
                    entityType.setEntityTypeName(entityTypeName);

                });

    }

    //==========================================================================
    public final void createNewEntityKind(final Integer entityKindId,
            final Integer entityTypeId,
            final String entityKindName) {

        persistanceEntityManager.<EntityKind>mergePersistenceEntity(EntityKind.class,
                entity -> {

                    entity.setEntityKindId(entityKindId);
                    entity.setEntityTypeId(entityTypeId);
                    entity.setEntityKindName(entityKindName);

                });
    }

    //==========================================================================
    public final void createNewEntityStatus(final Integer entityStatusId,
            final Integer entityTypeId,
            final String entityStatusName) {

        persistanceEntityManager.<EntityStatus>mergePersistenceEntity(EntityStatus.class,
                entity -> {

                    entity.setEntityStatusId(entityStatusId);
                    entity.setEntityStatusName(entityStatusName);
                    entity.setEntityTypeId(entityTypeId);

                });
    }
    //==========================================================================

    public final void createNewActionCode(final Integer actionCode,
            final String actionName,
            final String appName,
            final Boolean isClosed) {

        persistanceEntityManager.<ActionCode>mergePersistenceEntity(ActionCode.class,
                entity -> {
                    entity.setActionCode(actionCode);
                    entity.setActionName(actionName);
                    entity.setAppName(appName);
                    entity.setIsClosed(isClosed);
                });
    }
}
