/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kdg.fs24.entity.core;

import com.kdg.fs24.application.core.nullsafe.NullSafe;
import com.kdg.fs24.persistence.api.PersistenceSetup;
import com.kdg.fs24.entity.core.api.ActionEntity;
import com.kdg.fs24.entity.type.EntityType;
import com.kdg.fs24.entity.status.EntityStatus;
import java.time.LocalDate;
import java.time.LocalDateTime;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import lombok.Data;

/**
 *
 * @author Козыро Дмитрий
 */
@Entity
@Table(name = "core_Entities")
@Data
@Inheritance(strategy = InheritanceType.JOINED)
//@DiscriminatorColumn( name = "entity_type_id" )
public class AbstractPersistenceEntity implements ActionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_action_id")
    @SequenceGenerator(name = "seq_action_id", sequenceName = "seq_action_id", allocationSize = 1)
    @Column(name = "entity_id", updatable = false)
    private Long entity_id;
//    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
//    @JoinColumn(name = "entity_type_id")
//    private EntityType entityType;
    @ManyToOne(fetch=FetchType.LAZY)
    //@JoinColumn(name = "entity_type_id", updatable = false, insertable = true)
    @JoinColumns({
        @JoinColumn(name = "entity_status_id", referencedColumnName = "entity_status_id")
        ,
    @JoinColumn(name = "entity_type_id", referencedColumnName = "entity_type_id")})
    @NotNull
    private EntityStatus entityStatus;
    @NotNull
    @Column(name = "creation_date", updatable = false)
    private LocalDateTime creation_date;
    @Column(name = "close_date")
    private LocalDate close_date;
    @Column(name = "last_modify")
    private LocalDateTime lastModify;

    @Override
    public Boolean justCreated() {
        return NullSafe.isNull(this.lastModify);
    }

    @Override
    public Long entityId() {
        return (this.entity_id);
    }
}
