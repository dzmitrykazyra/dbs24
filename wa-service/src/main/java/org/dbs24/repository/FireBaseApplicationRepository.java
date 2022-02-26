/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.repository;

import org.dbs24.entity.FireBaseApplication;
import org.dbs24.spring.core.data.ApplicationJpaRepository;

import java.util.Collection;

public interface FireBaseApplicationRepository extends ApplicationJpaRepository<FireBaseApplication, Integer> {

    Collection<FireBaseApplication> findByIsActual(Boolean isActual);
}
