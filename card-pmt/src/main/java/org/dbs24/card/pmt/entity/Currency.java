/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.card.pmt.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dbs24.application.core.api.ObjectRoot;
import org.dbs24.persistence.api.PersistenceEntity;
import org.dbs24.persistence.api.ReferenceEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Data
@Entity
@Table(name = "pmt_currencies_ref")
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
public class Currency extends ObjectRoot implements PersistenceEntity, ReferenceEntity {

    @Id
    @NotNull
    @Column(name = "currency_iso", updatable = false)
    @EqualsAndHashCode.Include
    private String currencyIso;

    @NotNull
    @Column(name = "currency_id", updatable = false)
    private Integer currencyId;

    @NotNull
    @Column(name = "currency_name", updatable = false)
    private String currencyName;

}
