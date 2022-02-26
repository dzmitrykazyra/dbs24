package org.dbs24.tik.mobile.entity.dto.tiktok;

import lombok.Data;
import org.dbs24.stmt.StmtProcessor;
import org.dbs24.tik.mobile.entity.dto.tiktok.viewer.SearchLatestPostsResponseDto;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Data
public class TiktokPostIdentifierListDto {

    private List<TiktokPostIdentifierDto> tiktokPostIdentifierDtoList;

    public static TiktokPostIdentifierListDto of(SearchLatestPostsResponseDto latestAccountPosts) {

        return StmtProcessor.create(
                TiktokPostIdentifierListDto.class,
                tiktokPostIdentifierListDto -> tiktokPostIdentifierListDto.setTiktokPostIdentifierDtoList(
                        Arrays.stream(latestAccountPosts.getPosts())
                                .map(
                                        latestAccountPost -> StmtProcessor.create(
                                                TiktokPostIdentifierDto.class,
                                                tiktokPostIdentifierDto -> {
                                                    tiktokPostIdentifierDto.setImageUrl(latestAccountPost.getCover());
                                                    tiktokPostIdentifierDto.setVideoUrl(latestAccountPost.getWebLink());
                                                })
                                )
                                .collect(Collectors.toList())));
    }
}
