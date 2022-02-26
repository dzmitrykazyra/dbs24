/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.app.promo.dao;

import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import org.dbs24.app.promo.entity.Comment;
import org.dbs24.app.promo.entity.OrderAction;
import org.dbs24.app.promo.entity.UsedComment;
import org.dbs24.app.promo.repo.CommentRepo;
import org.dbs24.app.promo.repo.UsedCommentRepo;
import org.dbs24.spring.core.api.DaoAbstractApplicationService;
import org.dbs24.stmt.StmtProcessor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Getter
@Log4j2
@Component
public class CommentDao extends DaoAbstractApplicationService {

    final CommentRepo commentRepo;
    final UsedCommentRepo usedCommentRepo;

    public CommentDao(CommentRepo commentRepo, UsedCommentRepo usedCommentRepo) {
        this.commentRepo = commentRepo;
        this.usedCommentRepo = usedCommentRepo;
    }

    //==========================================================================
    public Optional<Comment> findOptionalComment(Integer commentId) {
        return commentRepo.findById(commentId);
    }

    public Comment findComment(Integer commentId) {
        return findOptionalComment(commentId).orElseThrow();
    }

    public void saveComment(Comment comment) {
        commentRepo.save(comment);
    }

    public void saveCommentUsage(OrderAction orderAction, Comment comment) {
        usedCommentRepo.save(StmtProcessor.create(UsedComment.class, uc -> {
            uc.setOrderAction(orderAction);
            uc.setComment(comment);
        }));
    }

}
