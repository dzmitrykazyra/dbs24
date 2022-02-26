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
import static javax.persistence.GenerationType.SEQUENCE;
import static org.dbs24.tik.dev.consts.TikDevApiConsts.Databases.SEQ_GENERAL;

@Data
@Entity
@Table(name = "tda_developers")
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
public class Developer extends ObjectRoot implements PersistenceEntity {

    @Id
    @GeneratedValue(strategy = SEQUENCE, generator = SEQ_GENERAL)
    @SequenceGenerator(name = SEQ_GENERAL, sequenceName = SEQ_GENERAL, allocationSize = 1)
    @NotNull
    @Column(name = "developer_id", updatable = false)
    @EqualsAndHashCode.Include
    private Long developerId;

    @NotNull
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
