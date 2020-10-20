/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.references.documents.doctemplate;

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
@Table(name = "docTemplatesRef")
public class DocTemplate extends AbstractRefRecord implements ReferenceRec {

    /**
     * @return the docTemplateGroup
     */
    @Id
    @Column(name = "doc_template_id")
    private Integer docTemplateId;
    //private DocTemplateGroup docTemplateGroup;

    @Column(name = "doc_template_group_id")
    private Integer docTemplateGroupId;
    @Column(name = "doc_template_code")
    private String docTemplateCode;
    @Column(name = "doc_template_name")
    private String docTemplateName;
    @Column(name = "pmt_sys_id")
    private Integer pmtSysId;
    @Column(name = "doc_type_id")
    private Integer docTypeId;

    //==========================================================================
    public static final DocTemplate findDocTemplate(final Integer docTemplate) {
        return AbstractRefRecord.<DocTemplate>getRefeenceRecord(DocTemplate.class,
                record -> record.getDocTemplateId().equals(docTemplate));
    }
    //==========================================================================
}
