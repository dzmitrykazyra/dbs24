package org.dbs24.tik.assist.entity.dto.user;

import lombok.Data;
import org.dbs24.stmt.StmtProcessor;

@Data
public class AuthDto {

    private String token;

    public static AuthDto toDto(String jwt) {

        return StmtProcessor.create(
                AuthDto.class,
                authDto -> authDto.setToken(jwt)
        );
    }
}
