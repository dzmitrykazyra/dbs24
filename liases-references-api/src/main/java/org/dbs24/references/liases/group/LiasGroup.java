/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.references.liases.group;

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
@Table(name = "liasDebtStatesRef")
public class LiasGroup extends AbstractRefRecord implements ReferenceRec {

    @Id
    @Column(name = "lias_group_id")
    private Integer liasGroupId;

    @Column(name = "lias_group_name")
    private String liasGroupName;
}
