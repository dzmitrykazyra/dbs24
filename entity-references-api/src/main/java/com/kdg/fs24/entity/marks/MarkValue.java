/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kdg.fs24.entity.marks;

import com.kdg.fs24.references.api.AbstractRefRecord;
import java.util.Map;
import com.kdg.fs24.references.api.ReferenceRec;
import javax.persistence.*;
import lombok.Data;

/**
 *
 * @author kazyra_d
 */
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

    @Override
    public void record2Map(final Map<String, Integer> map) {
        map.put(String.format("%d - %s", this.getMarkValueId(), this.toString()), this.getMarkValueId());
    }
}
