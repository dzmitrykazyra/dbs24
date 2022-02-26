package org.dbs24.proxy.core.repo;

import org.dbs24.proxy.core.entity.domain.ProxyUsageError;
import org.dbs24.spring.core.data.ApplicationJpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface ProxyUsageErrorRepo extends ApplicationJpaRepository<ProxyUsageError, Integer>, JpaSpecificationExecutor<ProxyUsageError>, PagingAndSortingRepository<ProxyUsageError, Integer> {

}
