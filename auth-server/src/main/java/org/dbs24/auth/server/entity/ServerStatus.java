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

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Data
@Entity
@Table(name = "tkn_servers_statuses_ref")
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
public class ServerStatus extends ObjectRoot implements PersistenceEntity {

    @Id
    @NotNull
    @Column(name = "server_status_id", updatable = false)
    @EqualsAndHashCode.Include
    private Integer serverStatusId;

    @NotNull
    @Column(name = "server_status_name")
    private String serverStatusName;
}
