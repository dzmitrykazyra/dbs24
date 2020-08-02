/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.entity.document;

import org.dbs24.persistence.api.PersistenceEntity;
import org.dbs24.references.documents.docattr.DocAttr;
import javax.persistence.*;
import lombok.Data;

/**
 *
 * @author Козыро Дмитрий
 */
@Data
@Entity
@Table(name = "docAttrs")
@IdClass(DocAttrValuePK.class)
public class DocAttrValue implements PersistenceEntity {

    @Id
    //@ManyToOne(targetEntity = AbstractPersistenceEntity.class)
    @ManyToOne
    @JoinColumn(name = "doc_id", referencedColumnName = "doc_id")
    private Document document;
    @Id
    @ManyToOne
    @JoinColumn(name = "doc_attr_id", referencedColumnName = "doc_attr_id")
    private DocAttr docAttr;

    @Column(name = "doc_attr_value")
    private String docAttrValue;
}
