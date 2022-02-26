package org.dbs24.tik.assist.entity.dto.plan.template;

import lombok.Data;
import org.dbs24.stmt.StmtProcessor;
import org.dbs24.tik.assist.entity.domain.PlanTemplate;
import org.dbs24.tik.assist.entity.domain.SumToActionsQuantity;
import org.dbs24.tik.assist.entity.dto.proportion.AllActionProportionDto;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Data
public class PlanTemplateListDto {

    private List<PlanTemplateDto> planTemplateDtoList;
    private AllActionProportionDto customProportions;

    public static PlanTemplateListDto toPlanTemplateListDto(List<PlanTemplate> planTemplates, Map<String, SumToActionsQuantity> sumToActionsQuantityMap) {

        return StmtProcessor.create(
                PlanTemplateListDto.class,
                planTemplateListDto -> {
                    planTemplateListDto
                            .setPlanTemplateDtoList(
                                    planTemplates
                                            .stream()
                                            .map(
                                                    PlanTemplateDto::toPlanTemplateDto
                                            )
                                            .collect(Collectors.toList()));
                    planTemplateListDto.setCustomProportions(AllActionProportionDto.of(sumToActionsQuantityMap));
                }
        );
    }
}
