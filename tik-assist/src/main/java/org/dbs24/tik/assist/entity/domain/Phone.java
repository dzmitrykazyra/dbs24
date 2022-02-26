/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.tik.assist.entity.domain;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import lombok.Data;
import java.time.LocalDateTime;
import static javax.persistence.FetchType.LAZY;
import org.dbs24.application.core.api.ObjectRoot;
import org.dbs24.persistence.api.PersistenceEntity;

@Data
@Entity
@Table(name = "tik_phones")
public class Phone extends ObjectRoot implements PersistenceEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_tik_phones")
    @SequenceGenerator(name = "seq_tik_phones", sequenceName = "seq_tik_phones", allocationSize = 1)
    @NotNull
    @Column(name = "phone_id", updatable = false)
    private Integer phoneId;

    @NotNull
    @Column(name = "actual_date")
    private LocalDateTime actualDate;

    @NotNull
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "phone_status_id", referencedColumnName = "phone_status_id")
    private PhoneStatus phoneStatus;
    
    @NotNull
    @Column(name = "device_id")
    private String deviceId;

    @NotNull
    @Column(name = "install_id")
    private String installId;

    @NotNull
    @Column(name = "apk_attributes")
    private byte[] apkAttrs;

    @NotNull
    @Column(name = "apk_hash_id")
    private String apkHashId;

}
