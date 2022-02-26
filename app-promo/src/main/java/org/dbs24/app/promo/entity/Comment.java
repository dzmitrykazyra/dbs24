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
import java.time.LocalDateTime;

import static javax.persistence.GenerationType.SEQUENCE;
import static org.dbs24.app.promo.consts.AppPromoutionConsts.DbSequences.SEQ_GENERAL;

@Data
@Entity
@Table(name = "pr_comments")
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
public class Comment extends ObjectRoot implements PersistenceEntity {

    @Id
    @GeneratedValue(strategy = SEQUENCE, generator = SEQ_GENERAL)
    @SequenceGenerator(name = SEQ_GENERAL, sequenceName = SEQ_GENERAL, allocationSize = 1)
    @NotNull
    @EqualsAndHashCode.Include
    @Column(name = "comment_id", updatable = false)
    private Integer commentId;

    @NotNull
    @Column(name = "create_date")
    private LocalDateTime createDate;

    @NotNull
    @Column(name = "comment_source")
    private String commentSource;

}
