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

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Data
@Entity
@Deprecated
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
@Table(name = "inst_proxytyperef")
public class ProxyType extends ObjectRoot implements PersistenceEntity {

    @Id
    @NotNull
    @EqualsAndHashCode.Include
    @Column(name = "proxy_type_id", updatable = false)
    private Byte proxyTypeId;

    @Column(name = "proxy_type_name")
    private String proxyTypeName;
}
