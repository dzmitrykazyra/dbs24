/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.insta.fs.component;

import java.time.LocalDateTime;
import java.util.Optional;
import lombok.Data;
import lombok.extern.log4j.Log4j2;
import org.dbs24.application.core.locale.NLS;
import org.dbs24.insta.fs.entity.Post;
import org.dbs24.insta.fs.repo.PostRepo;
import org.dbs24.insta.fs.rest.api.PostInfo;
import org.dbs24.insta.fs.rest.api.CreatedPost;
import org.dbs24.spring.core.api.AbstractApplicationService;
import org.dbs24.stmt.StmtProcessor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Data
@Log4j2
@Component
@ConditionalOnProperty(name = "config.restfull.profile.name", havingValue = "insta-fs")
public class PostsService extends AbstractApplicationService {

    final PostRepo postRepo;
    //final PostHistRepo postHistRepo;
    final RefsService refsService;
    final AccountsService accountsService;

    public PostsService(PostRepo postRepo, RefsService refsService, AccountsService accountsService) {
        this.postRepo = postRepo;
        this.refsService = refsService;
        this.accountsService = accountsService;
    }
    //==========================================================================

    @Transactional
    public CreatedPost createOrUpdatePost(PostInfo postInfo) {

        final Post post = findOrCreatePost(postInfo.getPostId());

        // copy 2 history
        //savePostHistory(post);
        post.setActualDate((LocalDateTime) StmtProcessor.nvl(NLS.long2LocalDateTime(postInfo.getActualDate()), LocalDateTime.now()));
        post.setPostStatus(refsService.findPostStatus(postInfo.getPostStatusId()));
        post.setAccount(accountsService.findAccount(postInfo.getAccountId()));
        post.setMediaId(postInfo.getMediaId());
        post.setPostType(refsService.findPostType(postInfo.getPostStatusId()));
        post.setShortCode(postInfo.getShortCode());
        //post.setPostId(post.getPostId());

        savePost(post);

        return StmtProcessor.create(CreatedPost.class, ca -> {

            ca.setPostId(post.getPostId());

            log.debug("try 2 create/update post: {}", post);

        });
    }

    public Post createPost() {
        return StmtProcessor.create(Post.class, a -> {
            a.setActualDate(LocalDateTime.now());
        });
    }

    public Post findPost(Long postId) {

        return postRepo
                .findById(postId)
                .orElseThrow(() -> new RuntimeException(String.format("postId not found (%d)", postId)));
    }

    public Post findOrCreatePost(Long postId) {
        return (Optional.ofNullable(postId)
                .orElseGet(() -> Long.valueOf("0")) > 0)
                ? findPost(postId)
                : createPost();
    }

//    public void savePostHistory(PostHist postHist) {
//        postHistRepo.save(postHist);
//    }
//    public void savePostHistory(Post post) {
//        Optional.ofNullable(post.getPostId())
//                .ifPresent(id -> savePostHistory((StmtProcessor.create(PostHist.class, postHist -> postHist.assign(post)))));
//    }
    public void savePost(Post post) {
        postRepo.save(post);
    }

}
