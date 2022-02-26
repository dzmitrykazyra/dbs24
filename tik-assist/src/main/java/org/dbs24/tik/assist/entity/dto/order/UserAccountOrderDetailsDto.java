package org.dbs24.tik.assist.entity.dto.order;

import lombok.Data;
import org.dbs24.stmt.StmtProcessor;
import org.dbs24.tik.assist.entity.domain.UserOrder;
import org.dbs24.tik.assist.entity.dto.tiktok.TiktokUserDto;

import java.math.BigDecimal;

@Data
public class UserAccountOrderDetailsDto {

    private Integer orderId;
    private Integer orderActionTypeId;
    private BigDecimal orderSum;
    private Integer followersAmount;
    private String avatarLink;

    public static UserAccountOrderDetailsDto of(UserOrder userOrder) {

        return StmtProcessor.create(
                UserAccountOrderDetailsDto.class,
                userAccountOrderDetailsDto -> {
                    userAccountOrderDetailsDto.setOrderId(userOrder.getOrderId());
                    userAccountOrderDetailsDto.setOrderActionTypeId(userOrder.getActionType().getActionTypeId());
                    userAccountOrderDetailsDto.setOrderSum(userOrder.getOrderSum());
                }
        );
    }

    public void setAccountData(TiktokUserDto tiktokUserDto) {

        this.setFollowersAmount(tiktokUserDto.getFollowers());
        this.setAvatarLink(tiktokUserDto.getAvatar());
    }
}

