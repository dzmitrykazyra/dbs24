/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.references.documents.doctemplategroup;

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
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Table(name = "docTemplateGroupsRef")
public class DocTemplateGroup extends AbstractRefRecord implements ReferenceRec {

    @Id
    @Column(name = "doc_template_group_id")
    private Integer docTemplateGroupId;
    @Column(name = "doc_template_group_name")
    private String docTemplateGroupName;
}
