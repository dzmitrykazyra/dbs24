package org.dbs24.tik.assist.entity.dto.tiktok;

import lombok.Builder;
import lombok.Data;
import org.dbs24.tik.assist.entity.domain.PlanTemplate;
import org.dbs24.tik.assist.entity.domain.UserPlan;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;

@Data
@Builder
public class TiktokPlanInfoDto {

    private String name;
    private BigDecimal price;
    private Integer subscriptionId;
    private Long dateBilling;
    private String card;
    private Info info;


    public static TiktokPlanInfoDto of(UserPlan userPlan){
        if(userPlan.getPlanTemplate() == null){
            return TiktokPlanInfoDto
                    .builder()
                    .name("Custom Plan")
                    .price(userPlan.getPlanSum())
                    .subscriptionId(userPlan.getUserSubscription().getUserSubscriptionId())
                    .dateBilling(getTimestamp(userPlan.getEndDate()))
                    .info(Info.builder()
                            .countLikes(userPlan.getLikesAmount())
                            .countFollowers(userPlan.getFollowersAmount())
                            .countViews(userPlan.getViewsAmount())
                            .countComments(userPlan.getCommentsAmount())
                            .countReposts(userPlan.getRepostsAmount())
                            .build())
                    .build();
        }else {
            PlanTemplate planTemplate = userPlan.getPlanTemplate();
            return TiktokPlanInfoDto
                    .builder()
                    .name(planTemplate.getPlanTemplateName())
                    .price(planTemplate.getPlanTemplateSum())
                    .subscriptionId(userPlan.getUserSubscription().getUserSubscriptionId())
                    .dateBilling(getTimestamp(userPlan.getEndDate()))
                    .info(Info.builder()
                            .countLikes(planTemplate.getLikesAmount())
                            .countFollowers(planTemplate.getFollowersAmount())
                            .countViews(planTemplate.getViewsAmount())
                            .countComments(planTemplate.getCommentsAmount())
                            .countReposts(planTemplate.getRepostsAmount()).build())
                    .build();
        }
    }

    private static Long getTimestamp(LocalDateTime localDateTime){
        Timestamp timestamp = Timestamp.valueOf(localDateTime);
        return timestamp.getTime();
    }

    @Data
    @Builder
    public static class Info{
        private Integer countLikes;
        private Integer countFollowers;
        private Integer countViews;
        private Integer countComments;
        private Integer countReposts;
    }
}