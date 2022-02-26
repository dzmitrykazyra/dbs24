/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.insta.reg.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dbs24.application.core.api.ObjectRoot;
import org.dbs24.persistence.api.PersistenceEntity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

import static javax.persistence.FetchType.LAZY;
import static org.dbs24.consts.SysConst.DATETIME_MS_FORMAT;

@Data
@Entity
@Table(name = "inst_accounts_actions")
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
public class AccountAction extends ObjectRoot implements PersistenceEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_inst_accountactions")
    @SequenceGenerator(name = "seq_inst_accountactions", sequenceName = "seq_inst_accountactions", allocationSize = 1)
    @NotNull
    @EqualsAndHashCode.Include
    @Column(name = "action_code", updatable = false)
    private Long actionCode;

    @ManyToOne(fetch = LAZY)
    @NotNull
    @JoinColumn(name = "account_id", referencedColumnName = "account_id")
    private Account account;
   
    @NotNull
    @Column(name = "action_start_date", updatable = false)
    private LocalDateTime actionStartDate;

    //@NotNull
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DATETIME_MS_FORMAT)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)    
    @Column(name = "action_finish_date", updatable = false)
    private LocalDateTime actionFinishDate;

    @ManyToOne(fetch = LAZY)
    @NotNull
    @JoinColumn(name = "proxy_id", referencedColumnName = "proxy_id")
    private Proxy proxy;

    @ManyToOne(fetch = LAZY)
    @NotNull
    @JoinColumn(name = "action_ref_id", referencedColumnName = "action_ref_id")
    private Action action;

    @Column(name = "actions_notes")
    private String actionsNotes;

    @Column(name = "err_msg")
    private String errMsg;
}
