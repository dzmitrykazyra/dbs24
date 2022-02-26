/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.insta.tmp.entity;

import java.time.LocalDateTime;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import static javax.persistence.FetchType.LAZY;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import lombok.Data;
import org.dbs24.application.core.api.ObjectRoot;
import org.dbs24.persistence.api.PersistenceEntity;
import org.hibernate.annotations.Type;

@Data
@Entity
@Table(name = "SOURCE_FACES")
public class Face extends ObjectRoot implements PersistenceEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "source_face_id", updatable = false)
    private Long sourceFaceId;

    @NotNull
    @Column(name = "actual_date")
    private LocalDateTime actualDate;

    @ManyToOne(fetch = LAZY)
    @NotNull
    @JoinColumn(name = "source_id", referencedColumnName = "source_id")
    private Source source;

    @Type(type = "org.hibernate.type.BinaryType")
    @Basic(fetch = LAZY)
    @Column(name = "face_box")
    private byte[] faceBox;

    @Column(name = "FACE_BOX_STR")
    private String faceBoxStr;

    @Type(type = "org.hibernate.type.BinaryType")
    @Basic(fetch = LAZY)
    @Column(name = "face_vector")
    private byte[] faceVector;

}
