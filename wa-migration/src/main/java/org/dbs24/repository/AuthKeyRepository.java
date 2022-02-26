/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.repository;

import org.dbs24.entity.AppUser;
import org.dbs24.entity.AuthKey;
import org.dbs24.spring.core.data.ApplicationJpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;


public interface AuthKeyRepository extends ApplicationJpaRepository<AuthKey, Integer>, JpaSpecificationExecutor<AuthKey> {

    @Query(value = "select a.* from AuthKey a where id = (select max(id) from AuthKey)", nativeQuery = true)
    public AuthKey findLastAuthKey();

}
