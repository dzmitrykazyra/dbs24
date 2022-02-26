package org.dbs24.tik.assist.repo;

import org.dbs24.spring.core.data.ApplicationJpaRepository;
import org.dbs24.tik.assist.entity.domain.PlanTemplate;
import org.dbs24.tik.assist.entity.domain.PlanTemplateStatus;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;
import java.util.Optional;

public interface PlanTemplateRepo extends ApplicationJpaRepository<PlanTemplate, Integer>, JpaSpecificationExecutor<PlanTemplate>, PagingAndSortingRepository<PlanTemplate, Integer> {

    Optional<PlanTemplate> findByPlanTemplateName(String planTemplateName);

    List<PlanTemplate> findByPlanTemplateStatus(PlanTemplateStatus planTemplateStatus);

    Optional<PlanTemplate> findByPlanTemplateNameAndPlanTemplateStatus(String planTemplateName, PlanTemplateStatus planTemplateStatus);
}