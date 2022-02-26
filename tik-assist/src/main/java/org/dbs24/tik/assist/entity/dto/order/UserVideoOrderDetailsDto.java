package org.dbs24.tik.assist.entity.dto.order;

import lombok.Data;
import org.dbs24.stmt.StmtProcessor;
import org.dbs24.tik.assist.entity.domain.UserOrder;
import org.dbs24.tik.assist.entity.dto.tiktok.TiktokUserPostDto;

import java.math.BigDecimal;

@Data
public class UserVideoOrderDetailsDto {

    private Integer orderId;
    private Integer orderActionTypeId;
    private BigDecimal orderSum;
    private Integer commentsAmount;
    private Integer viewsAmount;
    private Integer sharesAmount;
    private String coverLink;

    public static UserVideoOrderDetailsDto of(UserOrder userOrder) {

        return StmtProcessor.create(
                UserVideoOrderDetailsDto.class,
                userVideoOrderDetailsDto -> {
                    userVideoOrderDetailsDto.setOrderId(userOrder.getOrderId());
                    userVideoOrderDetailsDto.setOrderActionTypeId(userOrder.getActionType().getActionTypeId());
                    userVideoOrderDetailsDto.setOrderSum(userOrder.getOrderSum());
                }
        );
    }

    public void setTiktokPostData(TiktokUserPostDto tiktokUserPostDto) {

        this.setCommentsAmount(0);
        this.setViewsAmount(0);
        this.setSharesAmount(0);
        this.setCoverLink(tiktokUserPostDto.getCover());
    }
}
