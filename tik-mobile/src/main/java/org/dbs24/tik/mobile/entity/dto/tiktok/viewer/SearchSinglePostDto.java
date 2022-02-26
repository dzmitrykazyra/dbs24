package org.dbs24.tik.mobile.entity.dto.tiktok.viewer;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class SearchSinglePostDto {

    @JsonProperty("posts")
    private TiktokUserPostDto tiktokUserPostDto;
    private String authName;
}
