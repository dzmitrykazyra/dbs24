/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.references.documents.docattr;

import org.dbs24.references.api.ReferenceRec;
import java.util.Map;
import org.dbs24.references.api.AbstractRefRecord;
import javax.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Cache;

@Data
@Entity
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Table(name = "docAttrsRef")
public class DocAttr extends AbstractRefRecord implements ReferenceRec {

    @Id
    @Column(name = "doc_attr_id")
    private Integer docAttrId;
    @Column(name = "doc_attr_code")
    private String docAttrCcode;
    @Column(name = "doc_attr_name")
    private String docAttrName;

    //==========================================================================
    public static final DocAttr findDocAttr( Integer attrId) {
        return AbstractRefRecord.<DocAttr>getRefeenceRecord(DocAttr.class,
                record -> record.getDocAttrId().equals(attrId));
    }
}
