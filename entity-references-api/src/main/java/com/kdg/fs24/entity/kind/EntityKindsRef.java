/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kdg.fs24.entity.kind;

import com.kdg.fs24.exception.references.ReferenceNotFound;
import com.kdg.fs24.references.core.AbstractReference;

/**
 *
 * @author kazyra_d
 */
@Deprecated
public class EntityKindsRef<T extends EntityKind> extends AbstractReference<EntityKind> {

    //==========================================================================
    public String getEntityKindTitleById(final Integer entity_kind_id) throws ReferenceNotFound {

        return this.getEntityKindById(entity_kind_id).getEntityKindName();
    }

    //==========================================================================
    public T getEntityKindById(final Integer entity_kind_id) throws ReferenceNotFound {

        return (T) this.<T>findReference(() -> (this.findEntityKindById(entity_kind_id)),
                String.format("Неизвестный вид сущности (EntityKindRef.kind_id=%d)", entity_kind_id));
    }

    //==========================================================================
    private T findEntityKindById(final Integer entity_kind_id) {

        return (T) this.findCachedRecords((object) -> ((T) object).getEntityKindId().equals(entity_kind_id));
    }
}
