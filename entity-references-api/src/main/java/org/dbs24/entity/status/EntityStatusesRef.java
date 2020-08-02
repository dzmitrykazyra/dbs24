/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.entity.status;

import org.dbs24.exception.references.ReferenceNotFound;
import org.dbs24.references.core.AbstractReference;

/**
 *
 * @author kazyra_d
 */
@Deprecated
public class EntityStatusesRef<T extends EntityStatus> extends AbstractReference<EntityStatus> {

    //==========================================================================
    public String getEntityStatusNameById(final Integer type_id, final Integer status_id) throws ReferenceNotFound {

        return this.getEntityStatusById(type_id, status_id).getEntityStatusName();
    }

    //==========================================================================
    public T getEntityStatusById(final Integer type_id, final Integer status_id) throws ReferenceNotFound {

        return (T) this.<T>findReference(() -> (this.findEntityStatusById(type_id, status_id)),
                String.format("Неизвестный тип сущности (EntityStatusesRef.app_id=%d)", type_id));

    }

    //==========================================================================
    private T findEntityStatusById(final Integer type_id, final Integer status_id) {

        return (T) this.findCachedRecords((
                (object) -> (((T) object).getEntityTypeId().equals(type_id)
                && ((T) object).getEntityStatusId().equals(status_id))));

    }
}
