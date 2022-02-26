/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.insta.tmp.entity.dto;

import java.time.LocalDateTime;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import static javax.persistence.FetchType.LAZY;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import lombok.Data;
import org.dbs24.application.core.api.ObjectRoot;
import org.dbs24.persistence.api.PersistenceEntity;
import org.hibernate.annotations.Type;

@Data
@Entity
@Table(name = "SOURCE_FACES")
public class FaceDto extends ObjectRoot implements PersistenceEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_faces")
    @SequenceGenerator(name = "seq_faces", sequenceName = "seq_faces", allocationSize = 1)
    @Column(name = "source_face_id", updatable = false)
    private Long sourceFaceId;

    @NotNull
    @Column(name = "actual_date")
    private LocalDateTime actualDate;

    @Column(name = "source_id")
    private Long sourceId;

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

    //==================================================================================================================
    @Column(name = "user_name")
    private String userName;

    @Column(name = "insta_id")
    private Long instaId;
}
