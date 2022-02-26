/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.tik.dev.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dbs24.application.core.api.ObjectRoot;
import org.dbs24.persistence.api.PersistenceEntity;
import org.dbs24.tik.dev.entity.reference.DeveloperStatus;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

import static javax.persistence.FetchType.LAZY;

@Data
@Entity
@IdClass(DeveloperHistPK.class)
@Table(name = "tda_developers_hist")
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
public class DeveloperHist extends ObjectRoot implements PersistenceEntity {

    @Id
    @Column(name = "developer_id", updatable = false)
    @EqualsAndHashCode.Include
    private Long developerId;

    @Id
    @Column(name = "actual_date")
    private LocalDateTime actualDate;

    @NotNull
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "developer_status_id", referencedColumnName = "developer_status_id")
    private DeveloperStatus developerStatus;

    @Column(name = "email")
    private String email;

    @Column(name = "website")
    private String website;

    @Column(name = "api_key")
    private String apiKey;

    @Column(name = "oauth_client_id")
    private String oauthClientId;

    @Column(name = "country_code")
    private String countryCode;

}
