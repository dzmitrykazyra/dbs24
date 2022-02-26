package org.dbs24.tik.mobile.entity.dto.tiktok.viewer;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class SearchSpecificPostDto {

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("web_link")
    private String webLink;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("share_link")
    private String shareLink;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("short_link")
    private String shortLink;
}