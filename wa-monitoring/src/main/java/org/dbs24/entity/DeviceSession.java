/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
 /*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.entity;

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
@Table(name = "wa_device_sessions")
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
public class DeviceSession extends ObjectRoot implements PersistenceEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_wa_devicesessions")
    @SequenceGenerator(name = "seq_wa_devicesessions", sequenceName = "seq_wa_devicesessions", allocationSize = 1)
    @NotNull
    @Column(name = "session_id", updatable = false)
    @EqualsAndHashCode.Include
    private Long sessionId;

    @NotNull
    @Column(name = "actual_date")
    private LocalDateTime actualDate;

    @ManyToOne(fetch = LAZY)
    @NotNull
    @JoinColumn(name = "device_id", referencedColumnName = "device_id")
    private UserDevice userDevice;

    //@NotNull
    @Column(name = "ip_address")
    private String ipAddress;

    //@NotNull
    @Column(name = "note")
    private String note;

}
