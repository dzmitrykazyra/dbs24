package org.dbs24.tik.assist.entity.dto.plan.template;

import lombok.Data;
import org.dbs24.stmt.StmtProcessor;
import org.dbs24.tik.assist.entity.domain.PlanTemplate;
import org.dbs24.tik.assist.entity.dto.tiktok.TiktokPlanInfoDto.Info;

import java.math.BigDecimal;

@Data
public class PlanTemplateDto {

    private Integer planId;
    private String name;
    private BigDecimal price;
    private Info info;

    public static PlanTemplate toPlanTemplate(PlanTemplateDto planTemplateDto) {

        return PlanTemplate.builder()
                .planTemplateId(planTemplateDto.getPlanId())
                .planTemplateName(planTemplateDto.getName())
                .planTemplateSum(planTemplateDto.getPrice())
                .viewsAmount(planTemplateDto.info.getCountViews())
                .likesAmount(planTemplateDto.info.getCountLikes())
                .followersAmount(planTemplateDto.info.getCountFollowers())
                .commentsAmount(planTemplateDto.info.getCountComments())
                .repostsAmount(planTemplateDto.info.getCountReposts())
                .build();
    }

    public static PlanTemplateDto toPlanTemplateDto(PlanTemplate planTemplate) {

        return StmtProcessor.create(
                PlanTemplateDto.class,
                planTemplateDto -> {
                    planTemplateDto.setPlanId(planTemplate.getPlanTemplateId());
                    planTemplateDto.setName(planTemplate.getPlanTemplateName());
                    planTemplateDto.setPrice(planTemplate.getPlanTemplateSum());
                    planTemplateDto.setInfo(Info.builder()
                            .countViews(planTemplate.getViewsAmount())
                            .countLikes(planTemplate.getLikesAmount())
                            .countFollowers(planTemplate.getFollowersAmount())
                            .countComments(planTemplate.getCommentsAmount())
                            .countReposts(planTemplate.getRepostsAmount())
                            .build());
                }
        );
    }
}
