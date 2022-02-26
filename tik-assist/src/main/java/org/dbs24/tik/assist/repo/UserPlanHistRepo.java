package org.dbs24.tik.assist.repo;

import org.dbs24.spring.core.data.ApplicationJpaRepository;
import org.dbs24.tik.assist.entity.domain.PlanHist;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface UserPlanHistRepo extends ApplicationJpaRepository<PlanHist, Integer>, JpaSpecificationExecutor<PlanHist>, PagingAndSortingRepository<PlanHist, Integer> {

}