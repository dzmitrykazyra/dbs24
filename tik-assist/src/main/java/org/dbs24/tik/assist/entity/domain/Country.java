/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.tik.assist.entity.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import lombok.Data;
import org.dbs24.application.core.api.ObjectRoot;
import org.dbs24.persistence.api.PersistenceEntity;

@Data
@Entity
@Table(name = "tik_countries_ref")
public class Country extends ObjectRoot implements PersistenceEntity {

    @Id
    @NotNull
    @Column(name = "country_id", updatable = false)
    private Integer countryId;

    @NotNull
    @Column(name = "country_iso")
    private String countryIso;

    @NotNull
    @Column(name = "country_name")
    private String countryName;
}
