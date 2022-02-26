/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.tik.assist.entity.domain;

import java.io.Serializable;
import java.time.LocalDateTime;
import javax.persistence.*;

import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.dbs24.application.core.api.ObjectRoot;
import org.dbs24.persistence.api.PersistenceEntity;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@IdClass(UserHistPK.class)
@Table(name = "tik_users_hist")
public class UserHist extends ObjectRoot implements PersistenceEntity {

    @Id
    @NotNull
    @Column(name = "user_id", updatable = false)
    private Integer userId;

    @Column(name = "email")
    private String email;

    @Id
    @NotNull
    @Column(name = "actual_date")
    private LocalDateTime actualDate;

    @Column(name = "country_id")
    private Integer countryId;

    @Column(name = "user_status_id")
    private Integer userStatusId;

    @Column(name = "hash_pass")
    private String hashPass;
    
    @Column(name = "user_phone_num")
    private String userPhoneNum;

    @Column(name = "google_user_id")
    private String googleUserId;

    @Column(name = "facebook_user_id")
    private String facebookUserId;

    public static UserHist toUserHist(User user) {

        return UserHist.builder()
                .userId(user.getUserId())
                //.countryId(user.getCountry().getCountryId())
                .userStatusId(user.getUserStatus().getUserStatusId())
                .actualDate(user.getActualDate())
                .email(user.getEmail())
                .hashPass(user.getHashPass())
                .userPhoneNum(user.getUserPhoneNum())
                .googleUserId(user.getGoogleUserId())
                .facebookUserId(user.getFacebookUserId())
                .build();
    }
}

@Data
class UserHistPK implements Serializable {

    private Integer userId;
    private LocalDateTime actualDate;
}