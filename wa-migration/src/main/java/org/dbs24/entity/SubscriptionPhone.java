/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.entity;

import java.time.LocalDateTime;
import javax.persistence.*;
import static javax.persistence.FetchType.LAZY;
import javax.validation.constraints.NotNull;
import lombok.Data;
import org.dbs24.application.core.api.ObjectRoot;
import org.dbs24.persistence.api.PersistenceEntity;

@Data
@Entity
@Table(name = "subscriptionPhone")
public class SubscriptionPhone extends ObjectRoot implements PersistenceEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false)
    private Integer id;

    @NotNull
    @Column(name = "PHONE_NUM")
    private Long phoneNum;

    //@NotNull
    @Column(name = "ADD_TIME")
    private LocalDateTime addTime;

    //@NotNull
    @Column(name = "IS_VALID")
    private Integer isValid;

    @ManyToOne(fetch = FetchType.LAZY)
    //@NotNull
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private AppUser appUser;

    @ManyToOne(fetch = FetchType.LAZY)
    //@NotNull
    @JoinColumn(name = "key_id", referencedColumnName = "id")
    private AuthKey authKey;

    //@NotNull
    @Column(name = "AVATAR_ID")
    private Integer avatarId;

    @Lob
    @Column(name = "avatar")
    @Basic(fetch = LAZY)
    private byte[] avatar;

    @NotNull
    @Column(name = "NOTIFY")
    private Integer notify;

    @NotNull
    @Column(name = "ASSIGNED_NAME")
    private String assignedName;

    @NotNull
    @Column(name = "IS_REMOVED")
    private Integer isRemoved;

}
