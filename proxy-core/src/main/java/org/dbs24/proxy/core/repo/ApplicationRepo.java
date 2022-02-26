package org.dbs24.proxy.core.repo;

import org.dbs24.proxy.core.entity.domain.Application;
import org.dbs24.proxy.core.entity.domain.ApplicationNetwork;
import org.dbs24.proxy.core.entity.domain.ApplicationStatus;
import org.dbs24.spring.core.data.ApplicationJpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface ApplicationRepo extends ApplicationJpaRepository<Application, Integer>, JpaSpecificationExecutor<Application>, PagingAndSortingRepository<Application, Integer> {

    List<Application> findByApplicationStatus(ApplicationStatus applicationStatus);

    List<Application> findByName(String name);

    List<Application> findByDescription(String description);

    List<Application> findByApplicationNetwork(ApplicationNetwork applicationNetwork);
}