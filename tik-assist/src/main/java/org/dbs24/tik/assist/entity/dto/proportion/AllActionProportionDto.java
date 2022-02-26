package org.dbs24.tik.assist.entity.dto.proportion;

import lombok.Builder;
import lombok.Data;
import org.dbs24.tik.assist.constant.reference.ActionTypeDefine;
import org.dbs24.tik.assist.entity.domain.SumToActionsQuantity;

import java.math.BigDecimal;
import java.util.Map;

@Data
@Builder
public class AllActionProportionDto {
    private String name;
    private ActionInfo info;

    public static AllActionProportionDto of(Map<String, SumToActionsQuantity> stringSumToActionsQuantityMap){
        return AllActionProportionDto.builder()
                .name("Custom Plan")
                .info(ActionInfo.builder()
                        .countLikes(ActionInfo.Info.builder()
                                .maxCount(stringSumToActionsQuantityMap.get(ActionTypeDefine.AT_GET_LIKES.getActionTypeValue()).getUpToActionQuantity())
                                .ratio(stringSumToActionsQuantityMap.get(ActionTypeDefine.AT_GET_LIKES.getActionTypeValue()).getSum())
                                .build())
                        .countViews(ActionInfo.Info.builder()
                                .maxCount(stringSumToActionsQuantityMap.get(ActionTypeDefine.AT_GET_VIEWS.getActionTypeValue()).getUpToActionQuantity())
                                .ratio(stringSumToActionsQuantityMap.get(ActionTypeDefine.AT_GET_VIEWS.getActionTypeValue()).getSum())
                                .build())
                        .countComments(ActionInfo.Info.builder()
                                .maxCount(stringSumToActionsQuantityMap.get(ActionTypeDefine.AT_GET_COMMENTS.getActionTypeValue()).getUpToActionQuantity())
                                .ratio(stringSumToActionsQuantityMap.get(ActionTypeDefine.AT_GET_COMMENTS.getActionTypeValue()).getSum())
                                .build())
                        .countReposts(ActionInfo.Info.builder()
                                .maxCount(stringSumToActionsQuantityMap.get(ActionTypeDefine.AT_GET_REPOSTS.getActionTypeValue()).getUpToActionQuantity())
                                .ratio(stringSumToActionsQuantityMap.get(ActionTypeDefine.AT_GET_REPOSTS.getActionTypeValue()).getSum())
                                .build())
                        .countFollowers(ActionInfo.Info.builder()
                                .maxCount(stringSumToActionsQuantityMap.get(ActionTypeDefine.AT_GET_FOLLOWERS.getActionTypeValue()).getUpToActionQuantity())
                                .ratio(stringSumToActionsQuantityMap.get(ActionTypeDefine.AT_GET_FOLLOWERS.getActionTypeValue()).getSum())
                                .build())
                        .build())
                .build();
    }

    @Data
    @Builder
    static class ActionInfo{
        private Info countLikes;
        private Info countViews;
        private Info countComments;
        private Info countReposts;
        private Info countFollowers;

        @Data
        @Builder
        static class Info{
            private Integer maxCount;
            private BigDecimal ratio;
        }
    }
}