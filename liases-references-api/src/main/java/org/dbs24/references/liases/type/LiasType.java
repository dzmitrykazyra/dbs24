/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.references.liases.type;

import java.util.Map;
import org.dbs24.references.api.ReferenceRec;
import org.dbs24.references.api.AbstractRefRecord;
import org.dbs24.references.liases.kind.LiasKind;
import javax.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Cache;

/**
 *
 * @author kazyra_d
 */
@Data
@Entity
@Table(name = "liasTypesRef")
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.READ_ONLY)
public class LiasType extends AbstractRefRecord implements ReferenceRec {

    @Id
    @Column(name = "lias_type_id")
    private Integer liasTypeId;
    @Column(name = "lias_type_name")
    private String liasTypeName;

    public static final LiasType findLiasType(final Integer liasLiasTypeId) {
        return AbstractRefRecord.<LiasType>getRefeenceRecord(LiasType.class,
                record -> record.getLiasTypeId().equals(liasLiasTypeId));
    }
}
