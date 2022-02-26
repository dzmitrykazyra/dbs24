/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.app.promo.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dbs24.application.core.api.ObjectRoot;
import org.dbs24.persistence.api.PersistenceEntity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.SEQUENCE;
import static org.dbs24.app.promo.consts.AppPromoutionConsts.DbSequences.SEQ_GENERAL;

@Data
@Entity
@Table(name = "pr_bots")
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
public class Bot extends ObjectRoot implements PersistenceEntity {

    @Id
    @GeneratedValue(strategy = SEQUENCE, generator = SEQ_GENERAL)
    @SequenceGenerator(name = SEQ_GENERAL, sequenceName = SEQ_GENERAL, allocationSize = 1)
    @NotNull
    @Column(name = "bot_id", updatable = false)
    @EqualsAndHashCode.Include
    private Integer botId;

    @NotNull
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "bot_status_id", referencedColumnName = "bot_status_id")
    private BotStatus botStatus;

    @NotNull
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "bot_provider_id", referencedColumnName = "provider_id")
    private Provider provider;

    @Column(name = "pass")
    private String pass;

    @Column(name = "login")
    private String login;

    @Column(name = "proxy_id")
    private Integer proxyId;

    @Column(name = "session_id")
    private String sessionId;

    @Column(name = "bot_note")
    private String botNote;
}
