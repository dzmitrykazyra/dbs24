package org.dbs24.tik.mobile.entity.dto.user;

import lombok.Data;
import org.dbs24.stmt.StmtProcessor;

@Data
public class TokenDto {

    private String jwt;

    public static TokenDto of(String jwt) {

        return StmtProcessor.create(
                TokenDto.class,
                tokenDto -> tokenDto.setJwt(jwt)
        );
    }
}
