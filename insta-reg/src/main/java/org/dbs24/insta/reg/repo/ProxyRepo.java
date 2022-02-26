/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.insta.reg.repo;

import org.dbs24.insta.reg.entity.Proxy;
import org.dbs24.insta.reg.entity.ProxyStatus;
import org.dbs24.spring.core.data.ApplicationJpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import java.util.Optional;
import java.util.Collection;

public interface ProxyRepo extends ApplicationJpaRepository<Proxy, Integer>, JpaSpecificationExecutor<Proxy>, PagingAndSortingRepository<Proxy, Integer> {

public Optional<Proxy> findByAddress(String address);

public Collection<Proxy> findByProxyStatus(ProxyStatus proxyStatus);

}
