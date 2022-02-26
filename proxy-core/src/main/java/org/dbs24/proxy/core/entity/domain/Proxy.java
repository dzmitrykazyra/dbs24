/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.proxy.core.entity.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
@Table(name = "prx_proxies")
public class Proxy extends ObjectRoot implements PersistenceEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_prx_proxies")
    @SequenceGenerator(name = "seq_prx_proxies", sequenceName = "seq_prx_proxies", allocationSize = 1)
    @NotNull
    @EqualsAndHashCode.Include
    @Column(name = "proxy_id", updatable = false)
    private Integer proxyId;

    @JsonIgnore
    @NotNull
    @Column(name = "actual_date")
    private LocalDateTime actualDate;

    @ManyToOne(fetch = EAGER)
    @NotNull
    @JoinColumn(name = "provider_id", referencedColumnName = "provider_id")
    private ProxyProvider proxyProvider;

    @NotNull
    @Column(name = "url")
    private String url;

    @Column(name = "socks_port")
    private Integer socksPort;

    @Column(name = "http_port")
    private Integer port;

    @Column(name = "login")
    private String login;

    @Column(name = "pass")
    private String pass;

    @Column(name = "url_ip_change")
    private String urlIpChange;

    @ManyToOne(fetch = EAGER)
    @JoinColumn(name = "country_id")
    private Country country;

    @Column(name = "external_ip_address")
    private String externalIpAddress;

    @ManyToOne(fetch = EAGER)
    @NotNull
    @JoinColumn(name = "proxy_status_id")
    private ProxyStatus proxyStatus;

    @ManyToOne(fetch = EAGER)
    @NotNull
    @JoinColumn(name = "proxy_type_id")
    private ProxyType proxyType;

    @JsonIgnore
    @NotNull
    @Column(name = "date_begin")
    private LocalDateTime dateBegin;

    @JsonIgnore
    @Column(name = "date_end")
    private LocalDateTime dateEnd;

    @ManyToOne(fetch = EAGER)
    @JoinColumn(name = "socks_auth_method_id")
    private SocksAuthMethod socksAuthMethod;

    @Basic(fetch = LAZY)
    @Column(name = "socks_client_data")
    private byte[] socksClientData;

    @Column(name = "traffic")
    private Integer traffic;
}
