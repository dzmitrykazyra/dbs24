/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.insta.tmp.entity.dto;

import org.dbs24.insta.tmp.entity.*;
import java.time.LocalDateTime;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import static javax.persistence.FetchType.LAZY;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import org.hibernate.annotations.Type;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import lombok.Data;
import org.dbs24.application.core.api.ObjectRoot;
import org.dbs24.persistence.api.PersistenceEntity;

@Data
@Entity
@Table(name = "SOURCES")
public class SourceDto extends ObjectRoot implements PersistenceEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_sources")
    @SequenceGenerator(name = "seq_sources", sequenceName = "seq_sources", allocationSize = 1)
    @Column(name = "source_id", updatable = false)
    private Long sourceId;

    @NotNull
    @Column(name = "actual_date")
    private LocalDateTime actualDate;

    @Column(name = "post_id")
    private Long postId;

    @ManyToOne(fetch = LAZY)
    @NotNull
    @JoinColumn(name = "source_status_id", referencedColumnName = "source_status_id")
    private SourceStatus sourceStatus;

    @Basic(fetch = LAZY)    
    @Type(type = "org.hibernate.type.BinaryType")    
    @Column(name = "main_face_box")
    private byte[] mainFaceBox;

    @Column(name = "source_hash")
    private String sourceHash;

    @NotNull
    @Column(name = "source_url")
    private String sourceUrl;

}
