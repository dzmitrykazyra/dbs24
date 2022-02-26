/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.app.notifier.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dbs24.application.core.api.ObjectRoot;
import org.dbs24.persistence.api.PersistenceEntity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

import static javax.persistence.GenerationType.SEQUENCE;
import static org.dbs24.app.notifier.consts.AppNotifierConsts.Sequences.SEQ_MAIN;

@Data
@Entity
@Table(name = "note_messages")
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
public class Message extends ObjectRoot implements PersistenceEntity {

    @Id
    @GeneratedValue(strategy = SEQUENCE, generator = SEQ_MAIN)
    @SequenceGenerator(name = SEQ_MAIN, sequenceName = SEQ_MAIN, allocationSize = 1)
    @NotNull
    @EqualsAndHashCode.Include
    @Column(name = "message_id", updatable = false)
    private Integer messageId;

    @NotNull
    @Column(name = "create_date")
    private LocalDateTime createDate;

    @NotNull
    @Column(name = "actual_date_from")
    private LocalDateTime actualDateFrom;

    @NotNull
    @Column(name = "actual_date_to")
    private LocalDateTime actualDateTo;

    @NotNull
    @Column(name = "is_actual")
    private Boolean isActual;

    @NotNull
    @Column(name = "is_multiply_message")
    private Boolean isMultiplyMessage;

    @NotNull
    @Embedded
    private MessageDetails messageDetails;

    @Column(name = "msg_address")
    private String msgAddress;

    @Column(name = "packages_list")
    private String packagesList;

    @Column(name = "packages_min_version")
    private String packagesMinVersion;

    @Column(name = "packages_max_version")
    private String packagesMaxVersion;

}
