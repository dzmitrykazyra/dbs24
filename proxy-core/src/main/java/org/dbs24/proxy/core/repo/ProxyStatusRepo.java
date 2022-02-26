/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.proxy.core.repo;

import org.dbs24.proxy.core.entity.domain.ProxyStatus;
import org.dbs24.spring.core.data.ApplicationJpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface ProxyStatusRepo extends ApplicationJpaRepository<ProxyStatus, Integer>, JpaSpecificationExecutor<ProxyStatus>, PagingAndSortingRepository<ProxyStatus, Integer> {
    
}
