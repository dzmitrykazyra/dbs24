/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.references.liases.kind;

import java.util.Map;
import org.dbs24.references.api.ReferenceRec;
import org.dbs24.references.api.AbstractRefRecord;
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
@Table(name = "liasKindsRef")
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.READ_ONLY)
public class LiasKind extends AbstractRefRecord implements ReferenceRec {

    @Id
    @Column(name = "lias_kind_id")
    private Integer liasKindId;
    @Column(name = "is_claim")
    private Boolean isClaim;
    @Column(name = "lias_group_id")
    private Integer liasGroupId;
    @Column(name = "lias_kind_name")
    private String liasKindName;

    public static final LiasKind findLiasKind( Integer liasLiasKindId) {
        return AbstractRefRecord.<LiasKind>getRefeenceRecord(LiasKind.class,
                record -> record.getLiasKindId().equals(liasLiasKindId));
    }
}
