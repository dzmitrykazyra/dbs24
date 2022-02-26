package org.dbs24.tik.mobile.entity.dto.order.actual;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.dbs24.tik.mobile.entity.domain.Order;
import org.dbs24.tik.mobile.entity.domain.UserProfileDetail;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(setterPrefix = "with")
public class ActualOrderDto {
    private Integer orderId;
    private Integer cost;
    private Integer actionTypeId;
    private String tiktokUsername;
    private String tiktokUri;
    private String tiktokFullName;
    private Integer followersQuantity;
    private Integer followingQuantity;
    private Integer likesQuantity;
    private String avatarUrl;
    private String cover;

    public static ActualOrderDto of(Order order, UserProfileDetail userProfileDetail, Integer cost, String cover) {
        return ActualOrderDto.builder()
                .withOrderId(order.getOrderId())
                .withActionTypeId(order.getOrderActionType().getOrderActionTypeId())
                .withAvatarUrl(userProfileDetail.getAvatarUrl())
                .withFollowersQuantity(userProfileDetail.getFollowersQuantity())
                .withFollowingQuantity(userProfileDetail.getFollowingQuantity())
                .withTiktokFullName(userProfileDetail.getTiktokFullName())
                .withTiktokUsername(userProfileDetail.getTiktokUsername())
                .withLikesQuantity(userProfileDetail.getLikesQuantity())
                .withCost(cost)
                .withTiktokUri(order.getTiktokUri())
                .withCover(cover)
                .build();

    }
}
