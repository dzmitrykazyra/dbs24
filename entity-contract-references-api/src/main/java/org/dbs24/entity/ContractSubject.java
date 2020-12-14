/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.entity;

import org.dbs24.references.api.ReferenceRec;
import org.dbs24.references.api.AbstractRefRecord;
import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Data;
import org.hibernate.annotations.CacheConcurrencyStrategy;

@Entity
@Table(name = "core_ContractSubjectsRef")
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Data
public class ContractSubject extends AbstractRefRecord implements ReferenceRec {

    @Id
    @Column(name = "contract_subject_id")
    private Integer contractSubjectId;
    @Column(name = "contract_subject_name")
    private String contractSubjectName;
}
