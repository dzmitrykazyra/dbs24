package org.dbs24.tik.assist.dao;

import org.dbs24.tik.assist.entity.domain.PlanTemplate;

import java.util.List;
import java.util.Optional;

public interface PlanTemplateDao {

    PlanTemplate save(PlanTemplate planTemplateToSave);

    PlanTemplate findPlanTemplateById(Integer planTemplateId);
    Optional<PlanTemplate> findPlanTemplateOptionalById(Integer planTemplateId);
    Optional<PlanTemplate> findActivePlanTemplatesByName(String planTemplateName);
    List<PlanTemplate> findAllActivePlanTemplates();

    PlanTemplate changePlanTemplateStatusToNotActive(Integer planTemplateId);
}
