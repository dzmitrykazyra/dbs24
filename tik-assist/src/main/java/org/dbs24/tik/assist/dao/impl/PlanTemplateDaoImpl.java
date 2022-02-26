package org.dbs24.tik.assist.dao.impl;

import lombok.Data;
import lombok.extern.log4j.Log4j2;
import org.dbs24.tik.assist.dao.PlanTemplateDao;
import org.dbs24.tik.assist.dao.ReferenceDao;
import org.dbs24.tik.assist.entity.domain.PlanTemplate;
import org.dbs24.tik.assist.repo.PlanTemplateRepo;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Data
@Log4j2
@Component
public class PlanTemplateDaoImpl implements PlanTemplateDao {

    final PlanTemplateRepo planTemplateRepo;

    final ReferenceDao referenceDao;

    public PlanTemplateDaoImpl(PlanTemplateRepo planTemplateRepo, ReferenceDao referenceDao) {

        this.planTemplateRepo = planTemplateRepo;
        this.referenceDao = referenceDao;
    }

    @Override
    public PlanTemplate findPlanTemplateById(Integer planTemplateId) {

        return findPlanTemplateOptionalById(planTemplateId)
                .orElseThrow(() -> new RuntimeException("Cannot find plan template by id in db"));
    }

    @Override
    public Optional<PlanTemplate> findPlanTemplateOptionalById(Integer planTemplateId) {

        return planTemplateRepo.findById(planTemplateId);
    }

    @Override
    public PlanTemplate save(PlanTemplate planTemplateToSave) {

        return planTemplateRepo.save(planTemplateToSave);
    }

    @Override
    public Optional<PlanTemplate> findActivePlanTemplatesByName(String planTemplateName) {


        return planTemplateRepo.findByPlanTemplateNameAndPlanTemplateStatus(
                planTemplateName,
                referenceDao.findActivePlanTemplateStatus()
        );
    }

    @Override
    public List<PlanTemplate> findAllActivePlanTemplates() {

        return planTemplateRepo.findByPlanTemplateStatus(
                referenceDao.findActivePlanTemplateStatus()
        );
    }

    @Override
    public PlanTemplate changePlanTemplateStatusToNotActive(Integer planTemplateId) {

        Optional<PlanTemplate> planTemplateFound = findPlanTemplateOptionalById(planTemplateId);

        planTemplateFound.ifPresent(
                planTemplate -> {
                    planTemplate.setPlanTemplateStatus(referenceDao.findNotActivePlanTemplateStatus());
                    planTemplateRepo.save(planTemplate);
                }
        );

        return planTemplateFound.orElseThrow(() -> new RuntimeException("No plan template with such id"));
    }
}
