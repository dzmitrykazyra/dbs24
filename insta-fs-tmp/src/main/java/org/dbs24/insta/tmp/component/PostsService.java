/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.insta.tmp.component;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import lombok.Data;
import lombok.extern.log4j.Log4j2;
import org.dbs24.application.core.locale.NLS;
import org.dbs24.application.core.service.funcs.ServiceFuncs;
import org.dbs24.application.core.service.funcs.TestFuncs;
import org.dbs24.consts.SysConst;
import static org.dbs24.insta.api.consts.InstaConsts.Consumers.CON_GROUP_ID;
import static org.dbs24.insta.tmp.consts.IfsConst.Kafka.KAFKA_POSTS;
import static org.dbs24.insta.tmp.consts.IfsConst.References.PostStatuses.PS_ACTUAL;
import org.dbs24.insta.tmp.entity.Post;
import org.dbs24.insta.tmp.entity.dto.PostDto;
import org.dbs24.insta.tmp.kafka.api.IgPost;
import org.dbs24.insta.tmp.repo.PostRepo;
import org.dbs24.insta.tmp.repo.PostDtoRepo;
import org.dbs24.insta.tmp.rest.api.PostInfo;
import org.dbs24.insta.tmp.rest.api.CreatedPost;
import org.dbs24.spring.core.api.AbstractApplicationService;
import org.dbs24.stmt.StmtProcessor;
import org.hibernate.annotations.BatchSize;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

@Data
@Log4j2
@Component
@ConditionalOnProperty(name = "config.restfull.profile.name", havingValue = "insta-tmp")
public class PostsService extends AbstractApplicationService {

    final PostRepo postRepo;
    final PostDtoRepo postDtoRepo;
    final RefsService refsService;
    final AccountsService accountsService;
    final Collection<IgPost> hotPosts;

    final AtomicInteger postCounter = new AtomicInteger();

    public PostsService(PostRepo postRepo, PostDtoRepo postDtoRepo, RefsService refsService, AccountsService accountsService) {
        this.postRepo = postRepo;
        this.postDtoRepo = postDtoRepo;
        this.refsService = refsService;
        this.accountsService = accountsService;

        this.hotPosts = ServiceFuncs.createConcurencyCollection();

        postCounter.set(0);

    }

    //==========================================================================
    @KafkaListener(id = KAFKA_POSTS, groupId = CON_GROUP_ID, topics = KAFKA_POSTS)
    public void createPostsFromKafka(Collection<IgPost> posts) {

        postCounter.addAndGet(posts.size());

        log.debug("{}: receive posts: {}/{}", KAFKA_POSTS, posts.size(), postCounter.get());

        hotPosts.addAll(posts);
    }

//    @Scheduled(fixedRateString = "${config.crw.posts.processing-interval:2000}", cron = "${config.crw.posts.processing-interval.processing-cron:}")
//    @Transactional
    protected void saveAllPosts() {

        hotPosts
                .stream()
                .filter(post -> !post.getIsAdded())
                .forEach(post -> {

                    postRepo.bulkInsert(post.getInstaId(),
                            post.getPostTypeId(),
                            PS_ACTUAL,
                            LocalDateTime.now(),
                            post.getMediaId(),
                            post.getShortCode(),
                            post.getInstaPostId());
                    post.setIsAdded(Boolean.TRUE);
                });

        hotPosts.removeIf(post -> post.getIsAdded());

    }

    @Scheduled(fixedRateString = "${config.crw.posts.processing-interval:2000}", cron = "${config.crw.posts.processing-interval.processing-cron:}")
    @Transactional
    protected void savePosts() {

        final Collection<PostDto> newPosts = hotPosts
                .stream()
                .filter(post -> !post.getIsAdded())
                .map(this::createPostEntity)
                .collect(Collectors.toList());

        StmtProcessor.ifTrue(!newPosts.isEmpty(), () -> {
            log.debug("{}: store posts: {}/{}", KAFKA_POSTS, newPosts.size(), hotPosts.size());
            savePosts(newPosts);
            hotPosts.removeIf(post -> post.getIsAdded());
        });
    }

    //==========================================================================
    private PostDto createPostEntity(IgPost igPost) {

        return StmtProcessor.create(PostDto.class, postDto -> {

            Assert.notNull(igPost.getInstaId(), String.format("%s: igPost.getInstaId() is null!",
                    igPost.getClass().getCanonicalName()));

            postDto.setActualDate((LocalDateTime) StmtProcessor.nvl(NLS.long2LocalDateTime(igPost.getActualDate()), LocalDateTime.now()));
            postDto.setPostStatus(refsService.findPostStatus(igPost.getPostStatusId()));
            //postDto.setAccount(accountsService.findAccountByInstaId(igPost.getInstaId()));
            postDto.setAccountId(igPost.getInstaId());
            postDto.setInstaPostId(igPost.getInstaPostId());
            postDto.setMediaId(igPost.getMediaId());
            postDto.setPostType(refsService.findPostType(igPost.getPostStatusId()));
            postDto.setShortCode(igPost.getShortCode());

//            StmtProcessor.ifNull(postDto.getAccount().getAccountId(), () -> log.error("account not commited yet ({}, getAccountId() = null)", igPost.getInstaId()));
//            StmtProcessor.ifNull(postDto.getAccount().getInstaId(), () -> log.error("account not commited yet ({}, getInstaId() = null)", igPost.getInstaId()));
            igPost.setIsAdded(Boolean.TRUE);

        });
    }

    @Transactional
    public CreatedPost createOrUpdatePost(PostInfo postInfo) {

        final Post post = findOrCreatePost(postInfo.getPostId());

        // copy 2 history
        //savePostHistory(post);
        post.setActualDate((LocalDateTime) StmtProcessor.nvl(NLS.long2LocalDateTime(postInfo.getActualDate()), LocalDateTime.now()));
        post.setPostStatus(refsService.findPostStatus(postInfo.getPostStatusId()));
        post.setAccount(accountsService.findAccount(postInfo.getAccountId()));
        post.setInstaPostId(postInfo.getInstaPostId());
        post.setMediaId(postInfo.getMediaId());
        post.setPostType(refsService.findPostType(postInfo.getPostStatusId()));
        post.setShortCode(postInfo.getShortCode());
        //post.setPostId(post.getPostId());

        savePost(post);

        return StmtProcessor.create(CreatedPost.class, ca -> {

            ca.setPostId(post.getPostId());

            log.debug("try 2 create/update post: {}", post.getMediaId());

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

    public Post findPostByInstaPostId(Long instaPostId) {

        return postRepo
                .findByInstaPostId(instaPostId)
                .orElseThrow(() -> new RuntimeException(String.format("postId not found (instaPostId = %d)", instaPostId)));
    }

    public Post findOrCreatePost(Long postId) {
        return (Optional.ofNullable(postId)
                .orElseGet(() -> Long.valueOf("0")) > 0)
                ? findPost(postId)
                : createPost();
    }

    public void savePost(Post post) {
        postRepo.save(post);
    }

    public synchronized void savePosts(Collection<PostDto> posts) {
        postDtoRepo.saveAllAndFlush(posts);
    }
}
