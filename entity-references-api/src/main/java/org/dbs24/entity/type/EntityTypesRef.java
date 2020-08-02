/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.entity.type;

import org.dbs24.references.core.AbstractReference;
import org.dbs24.exception.references.ReferenceNotFound;

/**
 *
 * @author kazyra_d
 */
@Deprecated
public class EntityTypesRef<T extends EntityType> extends AbstractReference<EntityType> {

    //==========================================================================
    public String getEntityTypeNameById(final Integer type_id) throws ReferenceNotFound {

        return this.findEntityTypeById(type_id).getEntityTypeName();
    }

    //==========================================================================
    public T getEntityTypeById(final Integer type_id) throws ReferenceNotFound {

        return (T) this.<T>findReference(() -> (this.findEntityTypeById(type_id)),
                String.format("Неизвестный тип сущности (EntityTypesRef.app_id=%d)", type_id));

    }

    //==========================================================================
    private T findEntityTypeById(final Integer type_id) {

        return (T) this.findCachedRecords((object) -> ((T) object).getEntityTypeId().equals(type_id));

    }
}
