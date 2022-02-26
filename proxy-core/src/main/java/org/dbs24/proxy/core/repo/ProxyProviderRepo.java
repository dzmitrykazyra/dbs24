/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.proxy.core.repo;

import org.dbs24.proxy.core.entity.domain.ProxyProvider;
import org.dbs24.spring.core.data.ApplicationJpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ProxyProviderRepo extends ApplicationJpaRepository<ProxyProvider, Integer>, JpaSpecificationExecutor<ProxyProvider>, PagingAndSortingRepository<ProxyProvider, Integer> {

    Optional<ProxyProvider> findByProviderNameIgnoreCase(String providerName);
}
