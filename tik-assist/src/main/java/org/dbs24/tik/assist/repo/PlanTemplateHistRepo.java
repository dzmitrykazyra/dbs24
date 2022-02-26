package org.dbs24.tik.assist.repo;

import org.dbs24.spring.core.data.ApplicationJpaRepository;
import org.dbs24.tik.assist.entity.domain.PlanTemplateHist;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface PlanTemplateHistRepo extends ApplicationJpaRepository<PlanTemplateHist, Integer>, JpaSpecificationExecutor<PlanTemplateHist>, PagingAndSortingRepository<PlanTemplateHist, Integer> {

}