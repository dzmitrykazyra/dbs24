/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.entity.core.list;

import org.dbs24.entity.core.api.ActionEntity;

// список сущностей
public interface EntitiesList<T extends ActionEntity> {

    T getEntity(int entityId);
    void addEntity(T entity);

}
