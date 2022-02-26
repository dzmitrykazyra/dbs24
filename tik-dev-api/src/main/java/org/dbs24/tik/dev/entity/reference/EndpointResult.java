package org.dbs24.tik.dev.entity.reference;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

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
@Table(name = "tda_endpoints_results_ref")
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
public class EndpointResult extends ObjectRoot implements PersistenceEntity {

    @Id
    @NotNull
    @Column(name = "endpoint_result_id", updatable = false)
    @EqualsAndHashCode.Include
    private Integer endpointResultId;

    @NotNull
    @Column(name = "endpoint_result_name", updatable = false)
    private String endpointResultName;
}
