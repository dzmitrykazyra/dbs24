/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kdg.fs24.entity.kind;

import com.kdg.fs24.application.core.exception.api.InternalAppException;
import com.kdg.fs24.application.core.service.funcs.ServiceFuncs;
import java.util.Map;
import com.kdg.fs24.references.api.ReferenceRec;
import com.kdg.fs24.references.api.AbstractRefRecord;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 *
 * @author kazyra_d
 */
@Entity
@Table(name = "core_EntityKindsRef")
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Data
public class EntityKind extends AbstractRefRecord implements ReferenceRec {

    @Id
    @Column(name = "entity_kind_id")
    private Integer entityKindId;
    @Column(name = "entity_type_id")
    private Integer entityTypeId;
    @Column(name = "entity_kind_name")
    private String entityKindName;

    //==========================================================================
    @Override
    public void record2Map(final Map<String, Integer> map) {
        map.put(this.toString(), this.getEntityKindId());
    }
    //==========================================================================
    public final static EntityKind findEntityKind(final Integer EntityKindId) {
        return AbstractRefRecord.<EntityKind>getRefeenceRecord(EntityKind.class,
                record -> record.getEntityKindId().equals(EntityKindId));
    }
}
