package org.dbs24.tik.assist.entity.dto.tiktok.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.dbs24.tik.assist.entity.dto.tiktok.TiktokUserPostDto;

@Data
public class SearchSinglePostDto {

    @JsonProperty("posts")
    private TiktokUserPostDto tiktokUserPostDto;
}
