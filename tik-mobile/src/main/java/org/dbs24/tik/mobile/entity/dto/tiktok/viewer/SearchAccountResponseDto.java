package org.dbs24.tik.mobile.entity.dto.tiktok.viewer;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class SearchAccountResponseDto {

    @JsonProperty("user")
    private TiktokUserDto user;
}