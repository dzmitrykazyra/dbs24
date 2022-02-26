package org.dbs24.tik.mobile.entity.dto.tiktok.viewer;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class SearchLatestPostsRequestDto {

    @JsonProperty("username")
    private String username;

    @JsonProperty("post_numbers")
    private Integer postNumbers;
}
