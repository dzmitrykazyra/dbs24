/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.references.liases.actiontype;

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
@Data
@Entity
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_ONLY)
@Table(name = "liasActionTypesRef")
public class LiasActionType extends AbstractRefRecord implements ReferenceRec {

    @Id
    @Column(name = "action_type_id")
    private Integer actionTypeId;
    @Column(name = "change_rest_tag")
    private Boolean changeRestTtag;
    @Column(name = "action_type_name")
    private String actionTypeName;

    public static final LiasActionType findLiasActionType( Integer liasActionTypeId) {
        return AbstractRefRecord.<LiasActionType>getRefeenceRecord(LiasActionType.class,
                record -> record.getActionTypeId().equals(liasActionTypeId));
    }
}
