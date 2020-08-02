/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.entity.type;

import org.dbs24.application.core.nullsafe.NullSafe;
import java.util.Map;
import org.dbs24.references.api.ReferenceRec;
import org.dbs24.references.api.AbstractRefRecord;
import javax.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CacheConcurrencyStrategy;


/**
 *
 * @author kazyra_d
 */
@Entity
@Table(name = "core_EntityTypesRef")
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Data
public class EntityType extends AbstractRefRecord implements ReferenceRec {

    @Id
    @Column(name = "entity_type_id")
    private Integer entityTypeId;
    @Column(name = "entity_type_name")
    private String entityTypeName;
    @Column(name = "entity_app_name")
    private String entityAppName;

    @Override
    public void record2Map(final Map<String, Integer> map) {
        map.put(this.toString(), this.getEntityTypeId());
    }
}
