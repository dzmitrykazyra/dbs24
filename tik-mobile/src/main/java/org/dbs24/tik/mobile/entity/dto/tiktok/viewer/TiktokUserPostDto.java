package org.dbs24.tik.mobile.entity.dto.tiktok.viewer;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class TiktokUserPostDto {

    @JsonProperty("cover")
    private String cover;

    @JsonProperty("animated_cover")
    private String animatedCover;

    @JsonProperty("aweme_id")
    private String awemeId;

    @JsonProperty("download_links")
    private String[] downloadLinks;

    @JsonProperty("play_links")
    private String[] playLinks;

    @JsonProperty("share_link")
    private String shareLink;

    @JsonProperty("web_link")
    private String webLink;

    @JsonProperty("short_link")
    private String shortLink;
}
