package org.dbs24.tik.mobile.entity.dto.user;

import lombok.Data;
import org.dbs24.stmt.StmtProcessor;

@Data
public class UserEmailDto {

    private String userEmail;

    public static UserEmailDto of(String email) {

        return StmtProcessor.create(
                UserEmailDto.class,
                userEmailDto -> userEmailDto.setUserEmail(email)
        );
    }
}
