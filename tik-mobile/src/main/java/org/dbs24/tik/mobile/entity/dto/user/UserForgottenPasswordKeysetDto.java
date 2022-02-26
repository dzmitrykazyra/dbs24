package org.dbs24.tik.mobile.entity.dto.user;

import lombok.Data;

@Data
public class UserForgottenPasswordKeysetDto {

    private String userKey;
    private String expirationKey;
    private String rawNewPassword;
}
