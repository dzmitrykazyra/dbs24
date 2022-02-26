package org.dbs24.tik.assist.entity.dto.tiktok.response;

import lombok.Data;

import java.util.List;

@Data
public class AccountDeleteResponseDto {

    private List<String> tiktokUsernames;
    private String lastSelectedAccount;

}
