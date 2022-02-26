/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.auth.server.repo;

import org.dbs24.auth.server.entity.Application;
import org.dbs24.spring.core.data.ApplicationJpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 *
 * @author kdg
 */
public interface ApplicationRepo extends ApplicationJpaRepository<Application, Integer>, JpaSpecificationExecutor<Application>, PagingAndSortingRepository<Application, Integer> {
    
}
