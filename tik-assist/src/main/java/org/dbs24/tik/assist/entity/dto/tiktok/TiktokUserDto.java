package org.dbs24.tik.assist.entity.dto.tiktok;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class TiktokUserDto {

    @JsonProperty("login_name")
    private String loginName;

    @JsonProperty("name")
    private String name;

    @JsonProperty("followers")
    private Integer followers;

    @JsonProperty("following")
    private Integer following;

    @JsonProperty("likes")
    private Integer likes;

    @JsonProperty("avatar")
    private String avatar;

    @JsonProperty("sid")
    private String sid;
}
