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
import java.time.LocalDateTime;

import static javax.persistence.FetchType.LAZY;

@Data
@Entity
@Table(name = "tkn_servers_hist")
@IdClass(ServerHistPK.class)
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
public class ServerHist extends ObjectRoot implements PersistenceEntity {

    @Id
    @Column(name = "server_id", updatable = false)
    @EqualsAndHashCode.Include
    private Integer serverId;

    @Id
    @Column(name = "actual_date", updatable = false)
    private LocalDateTime actualDate;

    @Column(name = "pid", updatable = false)
    private Long pid;

    @Column(name = "server_address", updatable = false)
    private String serverAddress;

    @Column(name = "registry_date", updatable = false)
    private LocalDateTime registryDate;

    @Column(name = "reboot_date", updatable = false)
    private LocalDateTime rebootDate;

    @Column(name = "deadline_date", updatable = false)
    private LocalDateTime deadlineDate;

    @Column(name = "close_date")
    private LocalDateTime closeDate;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "application_id", referencedColumnName = "application_id")
    private Application application;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "server_status_id", referencedColumnName = "server_status_id")
    private ServerStatus serverStatus;

    @Column(name = "user_capacity")
    private Integer userCapacity;

    @Column(name = "country_code")
    private String countryCode;

}
