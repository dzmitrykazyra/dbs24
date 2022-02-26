package org.dbs24.tik.mobile.service;

import org.dbs24.tik.mobile.entity.domain.User;
import org.dbs24.tik.mobile.entity.domain.UserProfileDetail;
import org.dbs24.tik.mobile.entity.dto.tiktok.TiktokPostIdentifierListDto;
import org.dbs24.tik.mobile.entity.dto.tiktok.TiktokUserProfileDto;
import org.dbs24.tik.mobile.entity.dto.tiktok.viewer.SearchSinglePostDto;
import org.dbs24.tik.mobile.entity.dto.tiktok.viewer.TiktokUserDto;
import reactor.core.publisher.Mono;

public interface UserTiktokAccountService {

    Mono<TiktokPostIdentifierListDto> getLatestUserVideos(Integer userId);

    Mono<TiktokUserProfileDto> getUserTiktokProfileInfo(Integer userId);

    Mono<SearchSinglePostDto> findVideoByLink(String link);

    Mono<UserProfileDetail> saveDetailsByUser(User user);

    UserProfileDetail saveUserDetailsByUserAndTiktokUserDtoMono(User user, TiktokUserDto tiktokUserDto);

    String updateAvatarLink(int userId);
}
