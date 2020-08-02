/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.persistence.api;

import javax.persistence.EntityManager;

/**
 *
 * @author Козыро Дмитрий
 */
public interface PersistenceAction {

    void execute(EntityManager entityManager);
}
