/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.tik.assist.entity.domain;

import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;

import static javax.persistence.FetchType.EAGER;
import static javax.persistence.FetchType.LAZY;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
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
@Table(name = "tik_users")
public class User extends ObjectRoot implements PersistenceEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_tik_users")
    @SequenceGenerator(name = "seq_tik_users", sequenceName = "seq_tik_users", allocationSize = 1)
    @NotNull
    @Column(name = "user_id", updatable = false)
    private Integer userId;

    @Column(name = "email")
    private String email;

    @Column(name = "hash_pass")
    private String hashPass;

    @NotNull
    @Column(name = "actual_date")
    private LocalDateTime actualDate;

    @ManyToOne(fetch = EAGER)
    @JoinColumn(name = "country_id", referencedColumnName = "country_id")
    private Country country;

    @Column(name = "user_phone_num")
    private String userPhoneNum;

    @ManyToOne(fetch = EAGER)
    @NotNull
    @JoinColumn(name = "user_status_id", referencedColumnName = "user_status_id")
    private UserStatus userStatus;

    @Column(name = "google_user_id", updatable = false)
    private String googleUserId;

    @Column(name = "facebook_user_id", updatable = false)
    private String facebookUserId;
}


