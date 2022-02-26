/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.insta.tmp.repo;

import org.dbs24.insta.tmp.entity.Account;
import org.dbs24.spring.core.data.ApplicationJpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import java.util.Collection;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;

public interface AccountRepo extends ApplicationJpaRepository<Account, Long>, JpaSpecificationExecutor<Account>, PagingAndSortingRepository<Account, Long> {
    Collection<Account> findByInstaId(Long instaId);
    
    @Modifying
    @Transactional
    @Query(value = "INSERT INTO ACCOUNTS (ACCOUNT_ID, ACCOUNT_STATUS_ID, ACTUAL_DATE, INSTA_ID, USER_NAME, FULL_NAME, MEDIA_COUNT, FOLLOWERS_CNT, FOLLOWING_CNT, BIOGRAPHY, IS_PRIVATE, IS_VERIFIED, PROFILE_PIC_URL, PROFILE_PIC_URL_HD) VALUES (nextval('seq_accounts'), ?1, ?2, ?3, ?4, ?5, ?6, ?7, ?8, ?9, ?10, ?11, ?12, ?13)", nativeQuery = true)
    void bulkInsert(Integer ACCOUNT_STATUS_ID, LocalDateTime ACTUAL_DATE, Long INSTA_ID, String USER_NAME, String FULL_NAME, Integer MEDIA_COUNT, Integer FOLLOWERS_CNT, Integer FOLLOWING_CNT, String BIOGRAPHY, Integer IS_PRIVATE, Integer IS_VERIFIED, String PROFILE_PIC_URL, String PROFILE_PIC_URL_HD);
    
}
