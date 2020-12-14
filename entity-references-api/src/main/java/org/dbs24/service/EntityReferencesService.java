/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.service;

import java.util.Collection;
import org.dbs24.component.PersistenceEntityManager;
import org.springframework.stereotype.Service;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.dbs24.entity.type.EntityType;
import org.dbs24.entity.status.EntityStatus;
import org.dbs24.entity.action.ActionCode;
import org.dbs24.entity.kind.EntityKind;
import org.dbs24.entity.marks.Mark;
import org.dbs24.entity.marks.MarkValue;
import org.dbs24.references.api.AbstractRefRecord;
import org.dbs24.references.core.CachedReferencesClasses;
import static org.dbs24.consts.EntityReferenceConst.*;

@Log4j2
@Service
@CachedReferencesClasses(classes = {Mark.class, MarkValue.class})
public class EntityReferencesService extends AbstractReferencesService {

    final PersistenceEntityManager persistenceEntityManager;

    //==========================================================================
    @Autowired
    public EntityReferencesService(PersistenceEntityManager persistenceEntityManager) {
        this.persistenceEntityManager = persistenceEntityManager;
    }
    //==========================================================================

    public static final EntityType findEntityType(Integer typeId) {
        return AbstractRefRecord.<EntityType>getRefeenceRecord(EntityType.class,
                record -> record.getEntityTypeId().equals(typeId));
    }

    public static final ActionCode findActionCode(Integer actionCodeId) {
        return AbstractRefRecord.<ActionCode>getRefeenceRecord(ActionCode.class,
                record -> record.getActionCode().equals(actionCodeId));
    }

    //==========================================================================
    public static final EntityStatus findEntityStatus(Integer entityTypeId, Integer entityStatusId) {
        return AbstractRefRecord.<EntityStatus>getRefeenceRecord(EntityStatus.class,
                record -> record.getEntityStatusId().equals(entityStatusId)
                && record.getEntityTypeId().equals(entityTypeId));
    }

    //==========================================================================
    public final EntityKind findEntityKind(Integer entityKindId) {
        return AbstractRefRecord.<EntityKind>getRefeenceRecord(EntityKind.class,
                record -> record.getEntityKindId().equals(entityKindId));
    }

    //==========================================================================
    public static final Collection<Mark> getMarkCollection() {

        return AbstractReferencesService.<Mark>getGenericCollection(
                MARK_CLASS, new String[][]{
                    {String.valueOf(MR_AUTHORIZE_ENTITY), "1", "Авторизация энтити"}
                }, (record, stringRow) -> {
                    record.setMarkId(Integer.valueOf(stringRow[0]));
                    record.setMarkGroup(stringRow[1]);
                    record.setMarkName(stringRow[2]);
                });
    }

    //==========================================================================
    public static final Collection<MarkValue> getMarkValueCollection() {

        return AbstractReferencesService.<MarkValue>getGenericCollection(
                MARK_VALUE_CLASS, new String[][]{
                    {String.valueOf(MR_AUTHORIZE_ENTITY), String.valueOf(MR_AUTHORIZE_ENTITY_AUTH), "Авторизация энтити"},
                    {String.valueOf(MR_AUTHORIZE_ENTITY), String.valueOf(MR_AUTHORIZE_ENTITY_NOT_AUTH), "Отмена авторизация энтити"}
                }, (record, stringRow) -> {
                    record.setMarkId(Integer.valueOf(stringRow[0]));
                    record.setMarkValueId(Integer.valueOf(stringRow[1]));
                    record.setMarkValueName(stringRow[2]);
                });
    }
}
