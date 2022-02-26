package org.dbs24.tik.assist.entity.dto.tiktok;

import lombok.Data;
import org.dbs24.stmt.StmtProcessor;

@Data
public class LastSelectedAccountDto {

    private String lastSelectedAccountUsername;

    public static LastSelectedAccountDto of(String lastSelectedAccountUsername){
        return StmtProcessor.create(
                LastSelectedAccountDto.class,
                lastSelectedAccountDto -> lastSelectedAccountDto.setLastSelectedAccountUsername(lastSelectedAccountUsername)
        );
    }
}
