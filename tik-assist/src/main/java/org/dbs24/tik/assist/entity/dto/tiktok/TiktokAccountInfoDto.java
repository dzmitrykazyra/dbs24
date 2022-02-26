package org.dbs24.tik.assist.entity.dto.tiktok;

import lombok.Data;
import org.dbs24.stmt.StmtProcessor;

@Data
public class TiktokAccountInfoDto {

    private String tiktokAccountUsername;
    private String iconUrl;

    public static TiktokAccountInfoDto of(TiktokUserDto tiktokUserDto) {

        return StmtProcessor.create(
                TiktokAccountInfoDto.class,
                infoDto -> {
                    infoDto.setTiktokAccountUsername(tiktokUserDto.getName());
                    infoDto.setIconUrl(tiktokUserDto.getAvatar());
                }
        );
    }
}
