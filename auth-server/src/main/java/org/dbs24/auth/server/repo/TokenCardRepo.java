/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.auth.server.repo;

import org.dbs24.auth.server.entity.Token;
import org.dbs24.spring.core.data.ApplicationJpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

public interface TokenCardRepo extends ApplicationJpaRepository<Token, Long>, JpaSpecificationExecutor<Token>, PagingAndSortingRepository<Token, Long> {

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query(value = "call sp_delete_deprecated_issue_cards(:deprecateDate)", nativeQuery = true)
    void deleteDeprecatedIssueCards(@Param("deprecateDate") LocalDateTime deprecatedDate);
    
}
