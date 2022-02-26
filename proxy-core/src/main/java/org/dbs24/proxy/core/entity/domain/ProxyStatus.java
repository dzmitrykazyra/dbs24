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

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Data
@Entity
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
@Table(name = "prx_proxy_statuses_ref")
public class ProxyStatus extends ObjectRoot implements PersistenceEntity {

    @Id
    @NotNull
    @EqualsAndHashCode.Include
    @Column(name = "proxy_status_id", updatable = false)
    private Integer proxyStatusId;

    @NotNull
    @Column(name = "proxy_status_name")
    private String proxyStatusName;
}
