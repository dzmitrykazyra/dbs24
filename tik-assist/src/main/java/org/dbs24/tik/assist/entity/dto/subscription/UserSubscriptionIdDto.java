package org.dbs24.tik.assist.entity.dto.subscription;

import lombok.Data;
import org.dbs24.stmt.StmtProcessor;
import org.dbs24.tik.assist.entity.domain.UserSubscription;

@Data
public class UserSubscriptionIdDto {

    private Integer userSubscriptionId;

    public static UserSubscriptionIdDto toDto(UserSubscription userSubscription) {

        return StmtProcessor.create(
                UserSubscriptionIdDto.class,
                createdUserSubscriptionDto -> createdUserSubscriptionDto.setUserSubscriptionId(userSubscription.getUserSubscriptionId())
        );
    }
}
