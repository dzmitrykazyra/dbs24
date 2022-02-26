package org.dbs24.tik.mobile.entity.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "tm_user_profiles_details")
public class UserProfileDetail {

    @Id
    @NotNull
    @Column(name = "user_id", updatable = false)
    private Integer userId;

    @NotNull
    @Column(name = "tiktok_full_name")
    private String tiktokFullName;

    @NotNull
    @Column(name = "tiktok_username")
    private String tiktokUsername;

    @NotNull
    @Column(name = "followers_quantity")
    private Integer followersQuantity;

    @NotNull
    @Column(name = "following_quantity")
    private Integer followingQuantity;

    @NotNull
    @Column(name = "likes_quantity")
    private Integer likesQuantity;

    @NotNull
    @Column(name = "avatar_url")
    private String avatarUrl;
}