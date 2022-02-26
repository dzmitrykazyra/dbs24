/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.proxy.core.entity.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dbs24.application.core.api.ObjectRoot;
import org.dbs24.persistence.api.PersistenceEntity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

import static javax.persistence.FetchType.EAGER;
import static javax.persistence.FetchType.LAZY;

@Data
@Entity
@Table(name = "prx_proxy_requests")
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
public class ProxyRequest extends ObjectRoot implements PersistenceEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_prx_request")
    @SequenceGenerator(name = "seq_prx_request", sequenceName = "seq_prx_request", allocationSize = 1)
    @NotNull
    @EqualsAndHashCode.Include
    @Column(name = "request_id", updatable = false)
    private Integer requestId;

    @NotNull
    @Column(name = "request_date")
    private LocalDateTime requestDate;

    @NotNull
    @Column(name = "amount")
    private Integer proxiesAmount;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "proxy_type_id", referencedColumnName = "proxy_type_id")
    private ProxyType proxyType;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "provider_id", referencedColumnName = "provider_id")
    private ProxyProvider proxyProvider;

    @ManyToOne(fetch = EAGER)
    @JoinColumn(name = "country_id", referencedColumnName = "country_id")
    private Country country;

    @ManyToOne(fetch = LAZY)
    @NotNull
    @JoinColumn(name = "application_id", referencedColumnName = "application_id")
    private Application application;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "alg_id", referencedColumnName = "alg_id")
    private AlgSelection algSelection;

    @Column(name = "session_start")
    private LocalDateTime sessionStart;

    @Column(name = "session_finish")
    private LocalDateTime sessionFinish;
}
