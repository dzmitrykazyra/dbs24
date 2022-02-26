package org.dbs24.tik.mobile.entity.dto.tiktok;

import lombok.Data;
import org.dbs24.stmt.StmtProcessor;
import org.dbs24.tik.mobile.entity.domain.UserProfileDetail;
import org.dbs24.tik.mobile.entity.dto.tiktok.viewer.TiktokUserDto;

@Data
public class TiktokUserProfileDto {

    private String tiktokFullName;
    private String tiktokUsername;
    private Integer followersQuantity;
    private Integer followingQuantity;
    private Integer likesQuantity;
    private String avatarUrl;

    public static TiktokUserProfileDto of(TiktokUserDto tiktokUserDto) {

        return StmtProcessor.create(
                TiktokUserProfileDto.class,
                tiktokUserProfileDto -> {
                    tiktokUserProfileDto.setTiktokFullName(tiktokUserDto.getName());
                    tiktokUserProfileDto.setTiktokUsername(tiktokUserDto.getLoginName());
                    tiktokUserProfileDto.setFollowersQuantity(tiktokUserDto.getFollowers());
                    tiktokUserProfileDto.setFollowingQuantity(tiktokUserDto.getFollowing());
                    tiktokUserProfileDto.setLikesQuantity(tiktokUserDto.getLikes());
                    tiktokUserProfileDto.setAvatarUrl(tiktokUserDto.getAvatar());
                }
        );
    }

    public static TiktokUserProfileDto of(UserProfileDetail userProfileDetail) {

        return StmtProcessor.create(
                TiktokUserProfileDto.class,
                tiktokUserProfileDto -> {
                    tiktokUserProfileDto.setTiktokFullName(userProfileDetail.getTiktokFullName());
                    tiktokUserProfileDto.setTiktokUsername(userProfileDetail.getTiktokUsername());
                    tiktokUserProfileDto.setFollowersQuantity(userProfileDetail.getFollowersQuantity());
                    tiktokUserProfileDto.setFollowingQuantity(userProfileDetail.getFollowingQuantity());
                    tiktokUserProfileDto.setLikesQuantity(userProfileDetail.getLikesQuantity());
                    tiktokUserProfileDto.setAvatarUrl(userProfileDetail.getAvatarUrl());
                }
        );
    }
}
