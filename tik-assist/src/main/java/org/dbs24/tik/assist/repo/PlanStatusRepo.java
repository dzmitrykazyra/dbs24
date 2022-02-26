package org.dbs24.tik.assist.repo;

import org.dbs24.spring.core.data.ApplicationJpaRepository;
import org.dbs24.tik.assist.entity.domain.PlanStatus;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Optional;

public interface PlanStatusRepo extends ApplicationJpaRepository<PlanStatus, Integer>, JpaSpecificationExecutor<PlanStatus>, PagingAndSortingRepository<PlanStatus, Integer> {

    Optional<PlanStatus> findByPlanStatusName(String planStatusName);
}