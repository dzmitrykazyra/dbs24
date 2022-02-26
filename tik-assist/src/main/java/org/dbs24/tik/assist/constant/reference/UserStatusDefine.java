package org.dbs24.tik.assist.constant.reference;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.dbs24.stmt.StmtProcessor;
import org.dbs24.tik.assist.entity.domain.UserStatus;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
public enum UserStatusDefine {

    US_NOT_ACTIVE(1, "US.NOT-ACTIVE"),
    US_BANNED(2, "US.BANNED"),
    US_ACTIVE(3, "US.ACTIVE"),
    US_ACTIVE_FACEBOOK(4, "US.ACTIVE-FACEBOOK"),
    US_ACTIVE_GOOGLE(5, "US.ACTIVE-GOOGLE");

    private final Integer id;
    private final String statusValue;

    public static List<UserStatus> getAll() {
        return Arrays.stream(UserStatusDefine.values()).map(
                userStatusEnum -> StmtProcessor.create(
                        UserStatus.class,
                        userStatus -> {
                            userStatus.setUserStatusId(userStatusEnum.getId());
                            userStatus.setUserStatusName(userStatusEnum.getStatusValue());
                        }
                )
        ).collect(Collectors.toList());
    }
}