/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.insta.fs.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dbs24.application.core.api.ObjectRoot;
import org.dbs24.persistence.api.PersistenceEntity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

import static javax.persistence.FetchType.LAZY;

@Data
@Entity
@Table(name = "ifs_sources")
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
public class Source extends ObjectRoot implements PersistenceEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_ifs_sources")
    @SequenceGenerator(name = "seq_ifs_sources", sequenceName = "seq_ifs_sources", allocationSize = 1)
    @NotNull
    @EqualsAndHashCode.Include
    @Column(name = "source_id", updatable = false)
    private Long sourceId;

    @NotNull
    @Column(name = "actual_date")
    private LocalDateTime actualDate;

    @ManyToOne(fetch = LAZY)
    @NotNull
    @JoinColumn(name = "post_id", referencedColumnName = "post_id")
    private Post post;    
    
//    @ManyToOne(fetch = LAZY)
//    @NotNull
//    @JoinColumn(name = "source_type_id", referencedColumnName = "source_type_id")
//    private SourceType sourceType;

    @ManyToOne(fetch = LAZY)
    @NotNull
    @JoinColumn(name = "source_status_id", referencedColumnName = "source_status_id")
    private SourceStatus sourceStatus;

    @Basic(fetch = LAZY)    
    @Column(name = "main_face_box")
    private byte[] mainFaceBox;    
    
    @Column(name = "source_hash")
    private String sourceHash;

    @NotNull
    @Column(name = "source_url")
    private String sourceUrl;

}
