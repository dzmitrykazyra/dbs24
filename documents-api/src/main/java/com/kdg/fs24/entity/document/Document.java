/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kdg.fs24.entity.document;

import com.kdg.fs24.persistence.api.PersistenceEntity;
import com.kdg.fs24.entity.core.api.ActionEntity;
import com.kdg.fs24.entity.core.AbstractPersistenceEntity;
import javax.persistence.*;
import lombok.Data;
import com.kdg.fs24.references.documents.docstatus.DocStatus;
import com.kdg.fs24.references.documents.doctemplate.DocTemplate;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;

/**
 *
 * @author Козыро Дмитрий
 */
@Data
@Entity
@Table(name = "documents")
public class Document implements PersistenceEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_doc_id")
    @SequenceGenerator(name = "seq_doc_id", sequenceName = "seq_doc_id", allocationSize = 1)
    @Column(name = "doc_id")
    private Long docId;

//    @ManyToOne
//    @JoinColumn(name = "doc_id", referencedColumnName = "doc_id")
//    private Document parentDocId;
    @ManyToOne
    @JoinColumn(name = "doc_template_id", referencedColumnName = "doc_template_id", updatable = false)
    private DocTemplate docTemplate;

    @ManyToOne
    @JoinColumn(name = "doc_status_id", referencedColumnName = "doc_status_id")
    private DocStatus docStatus;

    @ManyToOne(targetEntity = AbstractPersistenceEntity.class)
    @JoinColumn(name = "entity_id", referencedColumnName = "entity_id", updatable = false)
    private ActionEntity entity;

    @Column(name = "doc_date")
    private LocalDate docDate;

    @Column(name = "doc_server_date")
    private LocalDateTime docServerDate;

    @Column(name = "doc_close_date")
    private LocalDate docCloseDate;

    @Column(name = "user_id")
    private Long userId;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "document")
    private Collection<DocAttrValue> docAttrs;

}
