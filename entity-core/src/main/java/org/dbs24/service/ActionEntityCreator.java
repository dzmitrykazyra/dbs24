/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.service;

import org.dbs24.entity.core.AbstractActionEntity;

/**
 *
 * @author Козыро Дмитрий
 */
@Deprecated
public interface ActionEntityCreator<T extends AbstractActionEntity> {

    void createEntity(T entity);

}
