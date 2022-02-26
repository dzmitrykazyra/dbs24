package org.dbs24.tik.assist.entity.dto.tiktok.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.dbs24.tik.assist.entity.dto.tiktok.TiktokUserDto;

@Data
public class SearchAccountResponseDto {

    @JsonProperty("user")
    private TiktokUserDto user;
}