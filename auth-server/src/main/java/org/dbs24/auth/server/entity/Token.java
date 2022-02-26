/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.auth.server.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dbs24.application.core.api.ObjectRoot;
import org.dbs24.persistence.api.PersistenceEntity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.SEQUENCE;
import static org.dbs24.auth.server.consts.AuthConsts.Sequences.SEQ_CARDS;

@Data
@Entity
@Table(name = "tkn_issue_cards")
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
public class Token extends ObjectRoot implements PersistenceEntity {

    @Id
    @GeneratedValue(strategy = SEQUENCE, generator = SEQ_CARDS)
    @SequenceGenerator(name = SEQ_CARDS, sequenceName = SEQ_CARDS, allocationSize = 1)
    @NotNull
    @Column(name = "card_id", updatable = false)
    @EqualsAndHashCode.Include
    private Long cardId;

    @NotNull
    @Column(name = "issue_date", updatable = false)
    private LocalDateTime issueDate;

    @NotNull
    @Column(name = "valid_until", updatable = false)
    private LocalDateTime validUntil;

    @NotNull
    @Column(name = "token_card", updatable = false)
    private String tokenCard;

    @NotNull
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "application_id", referencedColumnName = "application_id")
    private Application application;

    @NotNull
    @Column(name = "request_id", updatable = false)
    private String requestId;

    @NotNull
    @Column(name = "tag")
    private String tag;

}
