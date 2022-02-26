package org.dbs24.tik.assist.constant.reference;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.dbs24.stmt.StmtProcessor;
import org.dbs24.tik.assist.entity.domain.UserSubscriptionStatus;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
public enum UserSubscriptionStatusDefine {

    USS_ACTIVE(1, "USS.ACTIVE"),
    USS_WAITING_FOR_PAYMENT(2, "USS.WAITING.FOR.PAYMENT"),
    USS_NON_ACTIVE(3, "USS.NONACTIVE"),
    USS_CHANGED(4, "USS.CHANGED");

    private final Integer id;
    private final String userSubscriptionStatusValue;

    public static List<UserSubscriptionStatus> getAll() {
        return Arrays.stream(UserSubscriptionStatusDefine.values()).map(
                userSubscriptionStatusEnum -> StmtProcessor.create(
                        UserSubscriptionStatus.class,
                        userSubscriptionStatus -> {
                            userSubscriptionStatus.setUserSubscriptionStatusId(userSubscriptionStatusEnum.getId());
                            userSubscriptionStatus.setUserSubscriptionStatusName(userSubscriptionStatusEnum.getUserSubscriptionStatusValue());
                        }
                )
        ).collect(Collectors.toList());
    }
}