package org.dbs24.tik.assist.entity.dto.user;

import lombok.Data;

@Data
public class ChangePasswordDto {

    private String oldPassword;
    private String newPassword;
}
