package org.dbs24.tik.assist.entity.dto.user;

import lombok.Data;
import org.dbs24.stmt.StmtProcessor;
import org.dbs24.tik.assist.entity.domain.User;

@Data
public class UserIdDto {

    private Integer userId;

    public static UserIdDto of(User user) {

        return StmtProcessor.create(
                UserIdDto.class,
                userIdDto -> userIdDto.setUserId(user.getUserId())
        );
    }

    public static UserIdDto toDtoById(Integer userId) {

        return StmtProcessor.create(
                UserIdDto.class,
                userIdDto -> userIdDto.setUserId(userId)
        );
    }
}
