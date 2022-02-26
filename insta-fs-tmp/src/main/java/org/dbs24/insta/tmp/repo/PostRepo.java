/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.insta.tmp.repo;

import java.util.Optional;
import org.dbs24.insta.tmp.entity.Post;
import org.dbs24.spring.core.data.ApplicationJpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;

public interface PostRepo extends ApplicationJpaRepository<Post, Long>, JpaSpecificationExecutor<Post>, PagingAndSortingRepository<Post, Long> {

    Optional<Post> findByInstaPostId(Long instaId);

    @Modifying
    @Transactional
    @Query(value = "INSERT INTO POSTS (POST_ID, ACCOUNT_ID, POST_TYPE_ID, POST_STATUS_ID, ACTUAL_DATE, MEDIA_ID, SHORT_CODE, INSTA_POST_ID) VALUES (nextval('seq_posts'), ?1, ?2, ?3, ?4, ?5, ?6, ?7)", nativeQuery = true)
    void bulkInsert(Long ACCOUNT_ID, Integer POST_TYPE_ID, Integer POST_STATUS_ID, LocalDateTime ACTUAL_DATE, Long MEDIA_ID, String SHORT_CODE, Long INSTA_POST_ID);

}
