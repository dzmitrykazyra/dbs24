package org.dbs24.tik.assist.entity.dto.plan;

import lombok.Data;
import org.dbs24.stmt.StmtProcessor;
import org.dbs24.tik.assist.entity.domain.PlanTemplate;
import org.dbs24.tik.assist.entity.domain.UserPlan;

@Data
public class CustomPlanConstraint {

    private Integer likesAmount;
    private Integer followersAmount;
    private Integer viewsAmount;
    private Integer commentsAmount;
    private Integer repostsAmount;

    public static CustomPlanConstraint toDto(UserPlan userPlan) {

        return StmtProcessor.create(
                CustomPlanConstraint.class,
                constraint -> {
                    if (userPlan.getPlanTemplate() != null) {
                        PlanTemplate planTemplate = userPlan.getPlanTemplate();

                        constraint.setLikesAmount(planTemplate.getLikesAmount());
                        constraint.setFollowersAmount(planTemplate.getFollowersAmount());
                        constraint.setViewsAmount(planTemplate.getViewsAmount());
                        constraint.setCommentsAmount(planTemplate.getCommentsAmount());
                        constraint.setRepostsAmount(planTemplate.getRepostsAmount());
                    } else {
                        constraint.setLikesAmount(userPlan.getLikesAmount());
                        constraint.setFollowersAmount(userPlan.getFollowersAmount());
                        constraint.setViewsAmount(userPlan.getViewsAmount());
                        constraint.setCommentsAmount(userPlan.getCommentsAmount());
                        constraint.setRepostsAmount(userPlan.getRepostsAmount());
                    }
                }
        );
    }
}
