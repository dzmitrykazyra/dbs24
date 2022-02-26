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
@Table(name = "wa_users_tokens")
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
public class UserToken extends ObjectRoot implements PersistenceEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_wa_tokens")
    @SequenceGenerator(name = "seq_wa_tokens", sequenceName = "seq_wa_tokens", allocationSize = 1)
    @NotNull
    @Column(name = "token_id", updatable = false)
    @EqualsAndHashCode.Include
    private Long tokenId;

    @NotNull
    @Column(name = "created")
    private LocalDateTime created;

    @NotNull
    @Column(name = "valid_date")
    private LocalDateTime validDate;

    @NotNull
    @Column(name = "token")
    private String token;

    @ManyToOne(fetch = LAZY)
    @NotNull
    @JoinColumn(name = "user_id", referencedColumnName = "user_id")
    private User user;

    @NotNull
    @Column(name = "is_valid")
    private Boolean isValid;

}
