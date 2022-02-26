/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.insta.reg.entity;

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
@Table(name = "inst_proxies")
@Deprecated
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
public class Proxy extends ObjectRoot implements PersistenceEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_inst_proxies")
    @SequenceGenerator(name = "seq_inst_proxies", sequenceName = "seq_inst_proxies", allocationSize = 1)
    @NotNull
    @EqualsAndHashCode.Include
    @Column(name = "proxy_id", updatable = false)
    private Integer proxyId;

    @ManyToOne(fetch = LAZY)
    @NotNull
    @JoinColumn(name = "proxy_status_id", referencedColumnName = "proxy_status_id")
    private ProxyStatus proxyStatus;

    @ManyToOne(fetch = LAZY)
    @NotNull
    @JoinColumn(name = "proxy_type_id", referencedColumnName = "proxy_type_id")
    private ProxyType proxyType;

    @NotNull
    @Column(name = "create_date", updatable = false)
    private LocalDateTime createDate;

    @NotNull
    @Column(name = "actual_date")
    private LocalDateTime actualDate;

    @NotNull
    @Column(name = "address")
    private String address;

    @Column(name = "credit")
    private String credit;

    @Column(name = "uid")
    private String uid;

    @Column(name = "pwd")
    private String pwd;

    @Column(name = "proxy_notes")
    private String proxyNotes;
}
