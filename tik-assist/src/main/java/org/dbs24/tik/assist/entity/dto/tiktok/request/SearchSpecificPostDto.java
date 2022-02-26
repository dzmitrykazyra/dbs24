package org.dbs24.tik.assist.entity.dto.tiktok.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class SearchSpecificPostDto {

    @JsonProperty("web_link")
    private String webLink;

    @JsonIgnore
    @JsonProperty("share_link")
    private String shareLink;

    @JsonIgnore
    @JsonProperty("short_link")
    private String shortLink;
}
