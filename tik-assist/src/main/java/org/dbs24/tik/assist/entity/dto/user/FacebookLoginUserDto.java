package org.dbs24.tik.assist.entity.dto.user;

import lombok.Data;

@Data
public class FacebookLoginUserDto {

    private String facebookUserId;
    private String email;
    private String phoneNumber;
}
