/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.app.promo.component;

import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import org.dbs24.app.promo.dao.CommentDao;
import org.dbs24.app.promo.entity.Comment;
import org.dbs24.app.promo.entity.OrderAction;
import org.dbs24.app.promo.rest.dto.comment.CommentInfo;
import org.dbs24.app.promo.rest.dto.comment.CreateCommentRequest;
import org.dbs24.app.promo.rest.dto.comment.CreatedComment;
import org.dbs24.app.promo.rest.dto.comment.CreatedCommentResponse;
import org.dbs24.app.promo.rest.dto.comment.validator.CommentInfoValidator;
import org.dbs24.application.core.locale.NLS;
import org.dbs24.rest.api.action.SimpleActionInfo;
import org.dbs24.rest.api.service.AbstractRestApplicationService;
import org.dbs24.stmt.StmtProcessor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Supplier;

import static org.dbs24.rest.api.consts.RestApiConst.RestOperCode.OC_INVALID_ENTITY_ATTRS;
import static org.dbs24.rest.api.consts.RestApiConst.RestOperCode.OC_OK;

@Getter
@Log4j2
@Component
@ConditionalOnProperty(name = "config.restfull.profile.name", havingValue = "app-promo")
public class CommentsService extends AbstractRestApplicationService {

    final CommentDao commentDao;
    final RefsService refsService;
    final CommentInfoValidator botInfoValidator;
    final OrdersActionsService ordersActionsService;

    public CommentsService(RefsService refsService, CommentDao commentDao, CommentInfoValidator commentInfoValidator, OrdersActionsService ordersActionsService) {
        this.refsService = refsService;
        this.commentDao = commentDao;
        this.botInfoValidator = commentInfoValidator;
        this.ordersActionsService = ordersActionsService;
    }

    @FunctionalInterface
    interface CommentsHistBuilder {
        void buildCommentsHist(Comment bot);
    }

    final Supplier<Comment> createNewComment = () -> StmtProcessor.create(Comment.class);

    final BiFunction<CommentInfo, Comment, Comment> assignDto = (commentInfo, comment) -> {

        comment.setCommentSource(commentInfo.getCommentSource());
        comment.setCreateDate(NLS.long2LocalDateTime(commentInfo.getCreateDate()));

        return comment;
    };

    final BiFunction<CommentInfo, CommentsService.CommentsHistBuilder, Comment> assignCommentsInfo = (botInfo, botsHistBuilder) -> {

        final Comment bot = Optional.ofNullable(botInfo.getCommentId())
                .map(getCommentDao()::findComment)
                .orElseGet(createNewComment);

        // store history
        Optional.ofNullable(bot.getCommentId()).ifPresent(borId -> botsHistBuilder.buildCommentsHist(bot));

        assignDto.apply(botInfo, bot);

        return bot;
    };

    //==========================================================================
    @Transactional
    public CreatedCommentResponse createOrUpdateComment(Mono<CreateCommentRequest> monoRequest) {

        return this.<CreatedComment, CreatedCommentResponse>createAnswer(CreatedCommentResponse.class,
                (responseBody, createdComment) -> processRequest(monoRequest, responseBody, request
                        -> responseBody.setErrors(botInfoValidator.validateConditional(request.getEntityInfo(), botInfo
                        -> {

                    final SimpleActionInfo simpleActionInfo = request.getEntityAction();

                    //log.info("simpleActionInfo =  {}", simpleActionInfo);

                    log.debug("create/update bot: {}", botInfo);

                    final Comment comment = findOrCreateComments(botInfo, botHist -> {
                    });

                    final Boolean isNewSetting = StmtProcessor.isNull(comment.getCommentId());

                    getCommentDao().saveComment(comment);

                    final String finalMessage = String.format("Comment is %s (commentId=%d)",
                            isNewSetting ? "created" : "updated",
                            comment.getCommentId());

                    createdComment.setCreatedCommentId(comment.getCommentId());

                    log.debug(finalMessage);

                    responseBody.setCode(OC_OK);
                    responseBody.setMessage(finalMessage);
                    responseBody.complete();
                }, errorInfos -> {
                    responseBody.setCode(OC_INVALID_ENTITY_ATTRS);
                    responseBody.setMessage(errorInfos.stream().findFirst().orElseThrow().getErrorMsg().toUpperCase());
                }))));
    }

    public Comment findOrCreateComments(CommentInfo botInfo, CommentsService.CommentsHistBuilder botsHistBuilder) {
        return assignCommentsInfo.apply(botInfo, botsHistBuilder);
    }

    public void saveCommentUsage(OrderAction orderAction, Comment comment) {
        commentDao.saveCommentUsage(orderAction, comment);
    }

    public Comment findComment(Integer commentId) {
        return commentDao.findComment(commentId);
    }

    public void saveCommentUsage(Integer orderActionId, Integer commentId) {
        log.debug("saveCommentUsage: [orderActionId, commentId] ({}, {})", orderActionId, commentId);
        commentDao.saveCommentUsage(getOrdersActionsService().findOrderAction(orderActionId),
                findComment(commentId));
    }
}
