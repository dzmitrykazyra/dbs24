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
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
@Table(name = "ifs_source_faces")
public class Face extends ObjectRoot implements PersistenceEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_ifs_sourcefaces")
    @SequenceGenerator(name = "seq_ifs_sourcefaces", sequenceName = "seq_ifs_sourcefaces", allocationSize = 1)
    @NotNull
    @EqualsAndHashCode.Include
    @Column(name = "source_face_id", updatable = false)
    private Long sourceFaceId;

    @NotNull
    @Column(name = "actual_date")
    private LocalDateTime actualDate;

    @ManyToOne(fetch = LAZY)
    @NotNull
    @JoinColumn(name = "source_id", referencedColumnName = "source_id")
    private Source source;    
    
    @Basic(fetch = LAZY)    
    @Column(name = "face_box")
    private byte[] faceBox;    
    
    @Basic(fetch = LAZY)    
    @Column(name = "face_vector")
    private byte[] faceVector;    

}
