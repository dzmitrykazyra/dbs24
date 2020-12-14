/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.repository;

import org.dbs24.entity.type.EntityType;
import org.dbs24.spring.core.data.ApplicationJpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional(readOnly = true)
public interface EntityTypesRepository extends ApplicationJpaRepository<EntityType, Integer> {

    //EntityType findByEntityTypeId(Integer entityTypeId);
}
