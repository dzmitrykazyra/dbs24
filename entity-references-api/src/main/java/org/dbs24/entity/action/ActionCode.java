/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.entity.action;

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
@Data
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Table(name = "core_ActionCodesRef")
public class ActionCode extends AbstractRefRecord implements ReferenceRec {

    @Id
    @Column(name = "action_code")
    private Integer actionCode;
    @Column(name = "action_name")
    private String actionName;
    @Column(name = "app_name")
    private String appName;
    @Column(name = "is_closed")
    private Boolean isClosed;
    //==========================================================================
    public static final ActionCode findActionCode(final Integer ActionCodeId) {
        return AbstractRefRecord.<ActionCode>getRefeenceRecord(ActionCode.class,
                record -> record.getActionCode().equals(ActionCodeId));
    }
}
