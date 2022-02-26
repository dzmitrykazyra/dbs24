/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.tik.assist.entity.domain;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import lombok.Data;
import org.dbs24.application.core.api.ObjectRoot;
import org.dbs24.persistence.api.PersistenceEntity;

@Data
@Entity
@Table(name = "tik_bot_registration_types_ref")
public class BotRegistrationType extends ObjectRoot implements PersistenceEntity {

    @Id
    @NotNull
    @Column(name = "bot_registration_type_id", updatable = false)
    private Integer botRegistryTypeId;

    @NotNull
    @Column(name = "bot_registration_type_name", updatable = false)
    private String botRegistryTypeName;
}
