/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.entity.status;

import java.util.Map;
import org.dbs24.references.api.ReferenceRec;
import org.dbs24.references.api.AbstractRefRecord;
import javax.persistence.*;
import javax.persistence.Entity;
import lombok.Data;
import org.hibernate.annotations.CacheConcurrencyStrategy;

@Entity
@Table(name = "core_EntityStatusesRef")
@Cacheable
//@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Data
@IdClass(EntityStatusPK.class)
public class EntityStatus extends AbstractRefRecord implements ReferenceRec {

    @Id
    @Column(name = "entity_status_id")
    private Integer entityStatusId;
    @Id
    @Column(name = "entity_type_id")
    private Integer entityTypeId;
    @Column(name = "entity_status_name")
    private String entityStatusName;
}
