/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.references.liases.status;

import org.dbs24.references.api.AbstractRefRecord;
import org.dbs24.references.api.ReferenceRec;
import java.util.Map;
import javax.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Cache;
/**
 *
 * @author Козыро Дмитрий
 */
@Data
@Entity
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.READ_ONLY)
@Table(name = "liasFinOperStatusesRef")

public class LiasOperStatus extends AbstractRefRecord implements ReferenceRec {

    @Id
    @Column(name = "fin_oper_status_id", updatable = false)
    private Integer finOperStatusId;
    @Column(name = "fin_oper_status_name")
    private String finOperStatusName;

    public static final LiasOperStatus findLiasOperStatus( Integer liasOperStatusId) {
        return AbstractRefRecord.<LiasOperStatus>getRefeenceRecord(LiasOperStatus.class,
                record -> record.getFinOperStatusId().equals(liasOperStatusId));
    }
}
