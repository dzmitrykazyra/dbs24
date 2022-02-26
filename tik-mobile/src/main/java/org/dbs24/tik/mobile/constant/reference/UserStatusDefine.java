package org.dbs24.tik.mobile.constant.reference;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.dbs24.stmt.StmtProcessor;
import org.dbs24.tik.mobile.entity.domain.UserStatus;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
public enum UserStatusDefine {
    US_ACTIVE(1, "US.ACTIVE");

    private final Integer userStatusId;
    private final String userStatusName;

    public static List<UserStatus> getAll() {
        return Arrays.stream(UserStatusDefine.values()).map(
                userStatusEnum -> StmtProcessor.create(
                        UserStatus.class,
                        userStatus -> {
                            userStatus.setUserStatusId(userStatusEnum.getUserStatusId());
                            userStatus.setUserStatusName(userStatusEnum.getUserStatusName());
                        }
                )
        ).collect(Collectors.toList());
    }

}
