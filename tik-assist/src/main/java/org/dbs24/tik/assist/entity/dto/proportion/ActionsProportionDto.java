package org.dbs24.tik.assist.entity.dto.proportion;

import lombok.Data;
import org.dbs24.stmt.StmtProcessor;
import org.dbs24.tik.assist.entity.domain.SumToActionsQuantity;

import java.math.BigDecimal;

@Data
public class ActionsProportionDto {

    private Integer sumId;
    private String actionTypeName;
    private Integer upToActionQuantity;
    private BigDecimal sum;

    public static ActionsProportionDto toActionsProportionDto(SumToActionsQuantity sumToActionsQuantity) {

        return StmtProcessor.create(
                ActionsProportionDto.class,
                actionsProportionDto -> {
                    actionsProportionDto.setSumId(sumToActionsQuantity.getSumId());
                    actionsProportionDto.setActionTypeName(sumToActionsQuantity.getActionType().getActionTypeName());
                    actionsProportionDto.setUpToActionQuantity(sumToActionsQuantity.getUpToActionQuantity());
                    actionsProportionDto.setSum(sumToActionsQuantity.getSum());
                }
        );
    }
    /**
     * Method allows creating sumToActionsQuantity from DTO with null fields:
     *      - action type
     */
    public static SumToActionsQuantity defaultSumToActionsQuantityFromDto(ActionsProportionDto actionsProportionDto) {

        return SumToActionsQuantity.builder()
                .sumId(actionsProportionDto.getSumId())
                .upToActionQuantity(actionsProportionDto.getUpToActionQuantity())
                .sum(actionsProportionDto.getSum())
                .build();
    }
}
