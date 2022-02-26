/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.tik.dev.entity.reference;

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
@Table(name = "tda_endpoints_ref")
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
public class Endpoint extends ObjectRoot implements PersistenceEntity {

    @Id
    @NotNull
    @Column(name = "endpoint_ref_id", updatable = false)
    @EqualsAndHashCode.Include
    private Integer endpointRefId;

    @NotNull
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "endpoint_scope_id", referencedColumnName = "endpoint_scope_id")
    private EndpointScope endpointScope;

    @NotNull
    @Column(name = "create_date", updatable = false)
    private LocalDateTime createDate;

    @NotNull
    @Column(name = "uri")
    private String uri;

    @NotNull
    @Column(name = "is_actual")
    private Boolean isActual;

    @NotNull
    @Column(name = "description")
    private String description;
}
