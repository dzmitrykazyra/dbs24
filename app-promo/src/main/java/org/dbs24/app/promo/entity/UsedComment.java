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

@Data
@Entity
@IdClass(UsedCommentPK.class)
@Table(name = "pr_used_comments")
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
public class UsedComment extends ObjectRoot implements PersistenceEntity {

    @Id
    @NotNull
    @EqualsAndHashCode.Include
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "action_id", referencedColumnName = "action_id")
    private OrderAction orderAction;

    @Id
    @NotNull
    @EqualsAndHashCode.Include
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "comment_id", referencedColumnName = "comment_id")
    private Comment comment;

}
