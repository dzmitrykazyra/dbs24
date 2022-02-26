package org.dbs24.tik.mobile.entity.dto.user;

import lombok.Data;
import org.dbs24.stmt.StmtProcessor;
import org.dbs24.tik.mobile.entity.domain.User;

@Data
public class UserIdDto {

    private Integer userId;

    public static UserIdDto of(User user) {

        return StmtProcessor.create(
                UserIdDto.class,
                userIdDto -> userIdDto.setUserId(user.getId())
        );
    }
}
