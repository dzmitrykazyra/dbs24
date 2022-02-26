package org.dbs24.proxy.core.repo;

import org.dbs24.proxy.core.entity.domain.ApplicationNetwork;
import org.dbs24.spring.core.data.ApplicationJpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Optional;

public interface ApplicationNetworkRepo extends ApplicationJpaRepository<ApplicationNetwork, Integer>, JpaSpecificationExecutor<ApplicationNetwork>, PagingAndSortingRepository<ApplicationNetwork, Integer> {

    Optional<ApplicationNetwork> findByApplicationNetworkName(String applicationNetworkName);
}