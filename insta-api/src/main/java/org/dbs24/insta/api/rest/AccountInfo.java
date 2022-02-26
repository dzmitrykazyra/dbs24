/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.insta.api.rest;

import lombok.Data;
import org.dbs24.insta.api.IfsAccountIsCreated;

@Data
public class AccountInfo {

    private Long accountId;
    private Long actualDate;
    private Integer accountStatusId;
    private Long instaId;
    private String userName;
    private String fullName;
    private Integer mediaCount;
    private Integer followers;
    private Integer followees;
    private String biography;
    private Boolean isPrivate;
    private Boolean isVerified;
    private String profilePicUrl;
    private String profilePicHdUrl;

    public void assign(IfsAccountIsCreated ifsAccountIsCreated) {
        this.setAccountId(null);
        this.setAccountStatusId(1);
        this.setActualDate(null);
        this.setBiography(ifsAccountIsCreated.getInstaBiography());
        this.setFollowees(ifsAccountIsCreated.getFollowees());
        this.setFollowers(ifsAccountIsCreated.getFollowers());
        this.setFullName(ifsAccountIsCreated.getInstaFullName());
        this.setInstaId(ifsAccountIsCreated.getInstaId());
        this.setIsPrivate(ifsAccountIsCreated.getIsPrivate());
        this.setIsVerified(ifsAccountIsCreated.getIsVerified());
        this.setMediaCount(ifsAccountIsCreated.getMediacount());
        this.setProfilePicHdUrl(ifsAccountIsCreated.getProfilePicHdUrl());
        this.setProfilePicUrl(ifsAccountIsCreated.getProfilePicUrl());
        this.setUserName(ifsAccountIsCreated.getInstaUserName());
    }

}
