package org.dbs24.tik.assist.entity.dto.user;

import lombok.Data;

@Data
public class ChangeForgottenPasswordDto {

    private String userKey;
    private String expirationKey;
    private String newPassword;
}
