package org.dbs24.tik.assist.service.hierarchy;

import lombok.extern.log4j.Log4j2;
import org.dbs24.spring.core.api.AbstractApplicationService;
import org.dbs24.stmt.StmtProcessor;
import org.dbs24.tik.assist.constant.reference.ActionTypeDefine;
import org.dbs24.tik.assist.dao.PlanTemplateDao;
import org.dbs24.tik.assist.dao.ReferenceDao;
import org.dbs24.tik.assist.dao.SumToActionsQuantityDao;
import org.dbs24.tik.assist.entity.domain.ActionType;
import org.dbs24.tik.assist.entity.domain.PlanTemplate;
import org.dbs24.tik.assist.entity.domain.SumToActionsQuantity;
import org.dbs24.tik.assist.entity.dto.plan.template.PlanTemplateActionDto;
import org.dbs24.tik.assist.entity.dto.plan.template.PlanTemplateDto;
import org.dbs24.tik.assist.entity.dto.plan.template.PlanTemplateListDto;
import org.dbs24.tik.assist.service.exception.NoSuchDataInDaoException;
import org.dbs24.tik.assist.service.exception.PasswordMismatchException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Log4j2
@Service
public class PlanTemplateService extends AbstractApplicationService {

    final PlanTemplateDao planTemplateDao;
    final ReferenceDao referenceDao;
    final SumToActionsQuantityDao sumToActionsQuantityDao;

    @Value("${config.security.staff-secret}")
    private String staffSecretKey;

    public PlanTemplateService(PlanTemplateDao planTemplateDao, ReferenceDao referenceDao, SumToActionsQuantityDao sumToActionsQuantityDao) {

        this.planTemplateDao = planTemplateDao;
        this.referenceDao = referenceDao;
        this.sumToActionsQuantityDao = sumToActionsQuantityDao;
    }

    public PlanTemplateActionDto createOrUpdatePlanTemplate(PlanTemplateDto planTemplateDto, String staffSecretRequest) {

        if (!isSecretMatch(staffSecretRequest)) {
            throw new PasswordMismatchException(HttpStatus.BAD_REQUEST);
        }

        return StmtProcessor.create(
                PlanTemplateActionDto.class,
                planTemplateActionDto -> {
                    PlanTemplate planTemplate = PlanTemplateDto.toPlanTemplate(planTemplateDto);

                    planTemplate.setActualDate(LocalDateTime.now());
                    planTemplate.setPlanTemplateStatus(referenceDao.findActivePlanTemplateStatus());

                    planTemplateActionDto.setPlanTemplateId(planTemplateDao.save(planTemplate).getPlanTemplateId());
                }
        );
    }

    public PlanTemplateDto getActivePlanTemplateByName(String planTemplateName) {

        return PlanTemplateDto.toPlanTemplateDto(
                planTemplateDao
                        .findActivePlanTemplatesByName(planTemplateName)
                        .orElseThrow(() -> new NoSuchDataInDaoException(HttpStatus.BAD_REQUEST))
        );
    }

    public PlanTemplateListDto getAllActivePlanTemplates() {

        return PlanTemplateListDto.toPlanTemplateListDto(planTemplateDao.findAllActivePlanTemplates(), getMapSumToActionsQuantity());
    }

    public PlanTemplateDto invalidatePlanTemplate(Integer planTemplateId, String staffSecretRequest) {

        if (!isSecretMatch(staffSecretRequest)) {
            throw new PasswordMismatchException(HttpStatus.BAD_REQUEST);
        }

        return PlanTemplateDto.toPlanTemplateDto(
                planTemplateDao
                        .changePlanTemplateStatusToNotActive(planTemplateId)
        );
    }

    private boolean isSecretMatch(String requestKey) {

        return staffSecretKey.equals(requestKey);
    }

    private Map<String, SumToActionsQuantity> getMapSumToActionsQuantity(){
        List<ActionType> allActionType = ActionTypeDefine.getAll();
        Map<String, SumToActionsQuantity> sumToActionsQuantityMap = new HashMap<>();
        for(int i = 0; i < allActionType.size(); i++){
            sumToActionsQuantityMap.put(allActionType.get(i).getActionTypeName(), sumToActionsQuantityDao.findMaxQuantityByActionType(allActionType.get(i)));
        }
        return sumToActionsQuantityMap;
    }
}
