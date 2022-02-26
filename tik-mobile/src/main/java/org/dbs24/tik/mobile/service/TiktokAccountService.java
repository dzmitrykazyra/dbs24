package org.dbs24.tik.mobile.service;


import org.dbs24.tik.mobile.entity.dto.tiktok.TiktokPostIdentifierListDto;
import org.dbs24.tik.mobile.entity.dto.tiktok.viewer.SearchSinglePostDto;
import org.dbs24.tik.mobile.entity.dto.tiktok.viewer.TiktokUserDto;
import reactor.core.publisher.Mono;

public interface TiktokAccountService {

    Mono<TiktokUserDto> searchTiktokUserBySecUserId(String secUserId);

    Mono<TiktokUserDto> searchTiktokUserByUsername(String username);

    Mono<TiktokPostIdentifierListDto> searchLatestUserPostsByQuantity(String tiktokUsername, int latestPostsQuantity);

    Mono<SearchSinglePostDto> searchSinglePostByLink(String postWeblink);
}
