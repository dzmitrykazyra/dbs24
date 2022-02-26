package org.dbs24.tik.assist.entity.dto.tiktok;

import lombok.Builder;
import lombok.Data;
import org.dbs24.tik.assist.entity.domain.PlanTemplate;
import org.dbs24.tik.assist.entity.domain.UserPlan;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Data
@Builder
public class TiktokPlanDto {

    private Integer accountId;
    private String fullName;
    private String username;
    private String avatarUrl;
    private TiktokPlanInfoDto planInfoDto;

    public static TiktokPlanDto of(TiktokUserDto tiktokUserDto, UserPlan userPlan, Integer accountId){
        TiktokPlanDtoBuilder tiktokPlanDtoBuilder = TiktokPlanDto.builder()
                .accountId(accountId)
                .fullName(tiktokUserDto.getName())
                .username(tiktokUserDto.getLoginName())
                .avatarUrl(tiktokUserDto.getAvatar());

        return tiktokPlanDtoBuilder
                .planInfoDto(TiktokPlanInfoDto.of(userPlan))
                .build();
    }

    public static TiktokPlanDto of(TiktokUserDto tiktokUserDto, Integer accountId){
        return TiktokPlanDto.builder()
                .accountId(accountId)
                .fullName(tiktokUserDto.getName())
                .username(tiktokUserDto.getLoginName())
                .avatarUrl(tiktokUserDto.getAvatar())
                .planInfoDto(null)
                .build();

    }

}