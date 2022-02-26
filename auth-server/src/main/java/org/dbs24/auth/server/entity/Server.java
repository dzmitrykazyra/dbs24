/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.auth.server.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dbs24.application.core.api.ObjectRoot;
import org.dbs24.persistence.api.PersistenceEntity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.SEQUENCE;
import static org.dbs24.auth.server.consts.AuthConsts.Sequences.SEQ_SERVER;

@Data
@Entity
@Table(name = "tkn_servers")
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
public class Server extends ObjectRoot implements PersistenceEntity {

    @Id
    @GeneratedValue(strategy = SEQUENCE, generator = SEQ_SERVER)
    @SequenceGenerator(name = SEQ_SERVER, sequenceName = SEQ_SERVER, allocationSize = 1)
    @NotNull
    @Column(name = "server_id", updatable = false)
    @EqualsAndHashCode.Include
    private Integer serverId;

    @NotNull
    @Column(name = "actual_date", updatable = false)
    private LocalDateTime actualDate;

    @NotNull
    @Column(name = "pid", updatable = false)
    private Long pid;

    @NotNull
    @Column(name = "server_address", updatable = false)
    private String serverAddress;

    @NotNull
    @Column(name = "registry_date", updatable = false)
    private LocalDateTime registryDate;

    @NotNull
    @Column(name = "reboot_date", updatable = false)
    private LocalDateTime rebootDate;

    @NotNull
    @Column(name = "deadline_date", updatable = false)
    private LocalDateTime deadlineDate;

    @Column(name = "close_date")
    private LocalDateTime closeDate;

    @NotNull
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "application_id", referencedColumnName = "application_id")
    private Application application;

    @NotNull
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "server_status_id", referencedColumnName = "server_status_id")
    private ServerStatus serverStatus;

    @Column(name = "user_capacity")
    private Integer userCapacity;

    @Column(name = "country_code")
    private String countryCode;
}
