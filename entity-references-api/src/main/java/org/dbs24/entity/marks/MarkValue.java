/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.entity.marks;

import org.dbs24.references.api.AbstractRefRecord;
import java.util.Map;
import org.dbs24.references.api.ReferenceRec;
import javax.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "core_marksValuesRef")
//@PrimaryKeyJoinColumn(name = "mark_id", referencedColumnName = "mark_id")
@IdClass(MarkValuePK.class)
public class MarkValue extends AbstractRefRecord implements ReferenceRec {

    @Id
    @Column(name = "mark_id")
    private Integer markId;
    @Id
    @Column(name = "mark_value_id")
    private Integer markValueId;
    @Column(name = "mark_value_name")
    private String markValueName;
}
