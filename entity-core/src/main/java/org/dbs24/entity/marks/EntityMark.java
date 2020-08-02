/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.entity.marks;

import com.fasterxml.jackson.annotation.JsonIgnore;
import javax.persistence.*;
import lombok.Data;
import org.dbs24.entity.core.api.ActionEntity;
import org.dbs24.entity.core.api.Action;
import org.dbs24.persistence.api.PersistenceEntity;
import org.dbs24.entity.core.AbstractPersistenceEntity;
import org.dbs24.entity.core.AbstractPersistenceAction;

/**
 *
 * @author Козыро Дмитрий
 */
@Entity
@Data
@Table(name = "core_entityMarks")
//@PrimaryKeyJoinColumns(value = {
//    @PrimaryKeyJoinColumn(name = "entity_id", referencedColumnName = "entity_id")
//    , @PrimaryKeyJoinColumn(name = "action_id", referencedColumnName = "action_id")})
@IdClass(EntityMarkPK.class)
public class EntityMark implements PersistenceEntity {

    @Id
    @ManyToOne(targetEntity = AbstractPersistenceEntity.class)
    @JoinColumn(name = "entity_id", referencedColumnName = "entity_id")
    @JsonIgnore
    private ActionEntity entity;
    @Id
    @ManyToOne(targetEntity = AbstractPersistenceAction.class)
    @JoinColumn(name = "action_id", referencedColumnName = "action_id")
    @JsonIgnore
    private Action action;
    @ManyToOne
    @JoinColumns(value = {
        @JoinColumn(name = "mark_id", referencedColumnName = "mark_id")
        ,
        @JoinColumn(name = "mark_value_id", referencedColumnName = "mark_value_id")})
    private MarkValue markValue;
    @Column(name = "mark_direction")
    private Boolean direction;
}
