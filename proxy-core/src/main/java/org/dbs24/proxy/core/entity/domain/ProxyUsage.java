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

import static javax.persistence.FetchType.EAGER;
import static javax.persistence.FetchType.LAZY;

@Data
@Entity
@Table(name = "prx_proxy_usages")
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
public class ProxyUsage extends ObjectRoot implements PersistenceEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_prx_proxyusage")
    @SequenceGenerator(name = "seq_prx_proxyusage", sequenceName = "seq_prx_proxyusage", allocationSize = 1)
    @NotNull
    @EqualsAndHashCode.Include
    @Column(name = "usage_id", updatable = false)
    private Integer usageId;

    @ManyToOne(fetch = EAGER)
    @NotNull
    @JoinColumn(name = "request_id", referencedColumnName = "request_id")
    private ProxyRequest proxyRequest;

    @ManyToOne(fetch = EAGER)
    @NotNull
    @JoinColumn(name = "proxy_id", referencedColumnName = "proxy_id")
    private Proxy proxy;
}
