/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.persistence.core;

import org.dbs24.persistence.api.PersistenceEntity;

/**
 *
 * @author Козыро Дмитрий
 */
public interface PersisntanceEntityCreator<T extends PersistenceEntity> {

    void create(T persistenceEntity);
}
