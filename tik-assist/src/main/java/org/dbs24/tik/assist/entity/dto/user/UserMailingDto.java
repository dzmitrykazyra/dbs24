package org.dbs24.tik.assist.entity.dto.user;

import lombok.Data;
import org.dbs24.stmt.StmtProcessor;

@Data
public class UserMailingDto {

    private boolean isMailingCreated;

    public static UserMailingDto success() {

        return StmtProcessor.create(
                UserMailingDto.class,
                userMailingDto -> userMailingDto.setMailingCreated(true)
        );
    }
}
