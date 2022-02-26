package org.dbs24.tik.mobile.entity.dto.user;

import lombok.Data;
import org.dbs24.stmt.StmtProcessor;

@Data
public class UserVerificationDto {

    private Boolean isVerified;

    public static UserVerificationDto of(Boolean isVerified) {

        return StmtProcessor.create(
                UserVerificationDto.class,
                userVerificationDto -> userVerificationDto.setIsVerified(isVerified)
        );
    }
}
