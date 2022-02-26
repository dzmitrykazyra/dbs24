/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dbs24.application.core.api.ObjectRoot;
import org.dbs24.persistence.api.PersistenceEntity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

import static javax.persistence.FetchType.LAZY;

@Data
@Entity
@IdClass(UserHistPK.class)
@Table(name = "wa_users_hist")
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
public class UserHist extends ObjectRoot implements PersistenceEntity {

    @Id
    @NotNull
    @Column(name = "user_id", updatable = false)
    @EqualsAndHashCode.Include
    private Integer userId;

    @Id
    @NotNull
    @Column(name = "actual_date")
    @EqualsAndHashCode.Include
    private LocalDateTime actualDate;

    @ManyToOne(fetch = LAZY)
    @NotNull
    @JoinColumn(name = "country_id", referencedColumnName = "country_id")
    private Country country;

    @NotNull
    @Column(name = "login_token")
    private String loginToken;

    //@NotNull
    @Column(name = "user_phone_num")
    private String userPhoneNum;

    @NotNull
    @Column(name = "user_name")
    private String userName;

    //@NotNull
    @Column(name = "email")
    private String email;

}
