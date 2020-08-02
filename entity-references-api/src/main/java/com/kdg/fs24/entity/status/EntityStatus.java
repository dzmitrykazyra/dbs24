/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kdg.fs24.entity.status;

import java.util.Map;
import com.kdg.fs24.references.api.ReferenceRec;
import com.kdg.fs24.references.api.AbstractRefRecord;
import javax.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 *
 * @author kazyra_d
 */
@Entity
@Table(name = "core_EntityStatusesRef")
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
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

    @Override
    public void record2Map(final Map<String, Integer> map) {
        map.put(this.toString(), this.getEntityStatusId());
    }

    //==========================================================================
    public final static EntityStatus findEntityStatus(final Integer EntityTypeId, final Integer EntityStatusId) {
        return AbstractRefRecord.<EntityStatus>getRefeenceRecord(EntityStatus.class,
                record -> record.getEntityStatusId().equals(EntityStatusId)
                && record.getEntityTypeId().equals(EntityTypeId));
    }
}
