package org.dbs24.proxy.core.repo;

import org.dbs24.proxy.core.entity.domain.ApplicationStatus;
import org.dbs24.proxy.core.entity.domain.ProxyStatus;
import org.dbs24.spring.core.data.ApplicationJpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface ApplicationStatusRepo extends ApplicationJpaRepository<ApplicationStatus, Integer>, JpaSpecificationExecutor<ProxyStatus>, PagingAndSortingRepository<ApplicationStatus, Integer> {

    List<ApplicationStatus> findByApplicationStatusName(String applicationStatusName);
}
