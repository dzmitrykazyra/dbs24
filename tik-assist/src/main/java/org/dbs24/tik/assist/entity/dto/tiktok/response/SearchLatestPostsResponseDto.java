package org.dbs24.tik.assist.entity.dto.tiktok.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.dbs24.tik.assist.entity.dto.tiktok.TiktokUserDto;
import org.dbs24.tik.assist.entity.dto.tiktok.TiktokUserPostDto;

@Data
public class SearchLatestPostsResponseDto {

    @JsonProperty("user")
    private TiktokUserDto user;

    @JsonProperty("posts")
    private TiktokUserPostDto[] posts;
}
