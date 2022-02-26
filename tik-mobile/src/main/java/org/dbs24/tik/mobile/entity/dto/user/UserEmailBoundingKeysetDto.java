package org.dbs24.tik.mobile.entity.dto.user;

import lombok.Data;
import org.dbs24.stmt.StmtProcessor;

@Data
public class UserEmailBoundingKeysetDto {

    private String key;

    public static UserEmailBoundingKeysetDto of(String key) {

        return StmtProcessor.create(
                UserEmailBoundingKeysetDto.class,
                userEmailBoundingKeysetDto -> userEmailBoundingKeysetDto.setKey(key)
        );
    }
}
