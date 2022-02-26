package org.dbs24.tik.mobile.service.impl;

import lombok.extern.log4j.Log4j2;
import org.dbs24.tik.mobile.dao.UserDao;
import org.dbs24.tik.mobile.dao.UserProfileDetailDao;
import org.dbs24.tik.mobile.entity.domain.User;
import org.dbs24.tik.mobile.entity.domain.UserProfileDetail;
import org.dbs24.tik.mobile.entity.dto.tiktok.TiktokPostIdentifierListDto;
import org.dbs24.tik.mobile.entity.dto.tiktok.TiktokUserProfileDto;
import org.dbs24.tik.mobile.entity.dto.tiktok.viewer.SearchSinglePostDto;
import org.dbs24.tik.mobile.entity.dto.tiktok.viewer.TiktokUserDto;
import org.dbs24.tik.mobile.service.TiktokAccountService;
import org.dbs24.tik.mobile.service.UserTiktokAccountService;
import org.dbs24.tik.mobile.service.exception.http.BadRequestException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Log4j2
@Component
public class UserTiktokAccountServiceImpl implements UserTiktokAccountService {

    @Value("${user.tiktok.account.posts-to-return}")
    private int tiktokUserPostsToReturn;

    private final TiktokAccountService tiktokAccountService;

    private final UserDao userDao;
    private final UserProfileDetailDao userProfileDetailDao;

    public UserTiktokAccountServiceImpl(TiktokAccountService tiktokAccountService, UserDao userDao, UserProfileDetailDao userProfileDetailDao) {

        this.tiktokAccountService = tiktokAccountService;
        this.userDao = userDao;
        this.userProfileDetailDao = userProfileDetailDao;
    }

    @Override
    public Mono<TiktokPostIdentifierListDto> getLatestUserVideos(Integer userId) {

        String tiktokUsername = userDao.findById(userId).getUsername();

        return tiktokAccountService.searchLatestUserPostsByQuantity(
                tiktokUsername,
                tiktokUserPostsToReturn
        );
    }

    @Override
    public Mono<TiktokUserProfileDto> getUserTiktokProfileInfo(Integer userId) {

        UserProfileDetail userDetail = getUserProfileDetail(userId);
        return Mono.just(TiktokUserProfileDto.of(userDetail));
    }

    @Override
    public Mono<SearchSinglePostDto> findVideoByLink(String link) {

        return tiktokAccountService.searchSinglePostByLink(link).map(postDto -> {
                    if (postDto.getTiktokUserPostDto() == null || postDto.getTiktokUserPostDto().getAwemeId() == null) {
                        throw new BadRequestException();
                    }
                    return postDto;
                }
        );
    }

    @Override
    public String updateAvatarLink(int userId) {

        UserProfileDetail userProfileDetail = getUserProfileDetail(userId);
        return userProfileDetail.getAvatarUrl();
    }

    public Mono<UserProfileDetail> saveDetailsByUser(User user) {

        return tiktokAccountService.searchTiktokUserByUsername(user.getUsername())
                .map(tiktokUserDto -> {

                    UserProfileDetail userDetail = createDefaultUserProfileDetail(tiktokUserDto);
                    userDetail.setUserId(user.getId());
                    return userProfileDetailDao.save(userDetail);
                });
    }

    public UserProfileDetail saveUserDetailsByUserAndTiktokUserDtoMono(User user, TiktokUserDto tiktokUserDto) {

        UserProfileDetail userDetail = createDefaultUserProfileDetail(tiktokUserDto);
        userDetail.setUserId(user.getId());

        return userProfileDetailDao.save(userDetail);
    }

    private UserProfileDetail getUserProfileDetail(Integer userId) {
        User user = userDao.findById(userId);

        UserProfileDetail userDetail = saveDetailsByUser(user).toProcessor().block();
        String freshUsername = userDetail.getTiktokUsername();

        if (!user.getUsername().equals(freshUsername)) {

            user.setUsername(freshUsername);
            userDao.update(user);
        }
        return userDetail;
    }

    private UserProfileDetail createDefaultUserProfileDetail(TiktokUserDto tiktokUserDto) {

        return UserProfileDetail.builder()
                .tiktokFullName(tiktokUserDto.getName())
                .tiktokUsername(tiktokUserDto.getLoginName())
                .followersQuantity(tiktokUserDto.getFollowers())
                .followingQuantity(tiktokUserDto.getFollowing())
                .likesQuantity(tiktokUserDto.getLikes())
                .avatarUrl(tiktokUserDto.getAvatar())
                .build();
    }
}