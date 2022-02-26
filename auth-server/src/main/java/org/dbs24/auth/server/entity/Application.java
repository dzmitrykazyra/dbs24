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
@Table(name = "tkn_applications")
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
public class Application extends ObjectRoot implements PersistenceEntity {

    @Id
    @NotNull
    @Column(name = "application_id", updatable = false)
    @EqualsAndHashCode.Include
    private Integer applicationId;

    @Column(name = "application_code")
    private String applicationCode;

    @Column(name = "application_name")
    private String applicationName;
}
