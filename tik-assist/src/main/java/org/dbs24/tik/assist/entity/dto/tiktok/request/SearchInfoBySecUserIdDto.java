package org.dbs24.tik.assist.entity.dto.tiktok.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class SearchInfoBySecUserIdDto {

    @JsonProperty("sid")
    private String secUserId;

    @JsonProperty("post_numbers")
    private int postsQuantity;
}
