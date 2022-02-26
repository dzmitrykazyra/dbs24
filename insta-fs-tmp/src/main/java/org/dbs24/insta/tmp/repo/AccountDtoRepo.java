/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.insta.tmp.repo;

import org.dbs24.insta.tmp.entity.dto.AccountDto;
import org.dbs24.spring.core.data.ApplicationJpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;

/**
 * @author kdg
 */
public interface AccountDtoRepo extends ApplicationJpaRepository<AccountDto, Long>, JpaSpecificationExecutor<AccountDto> {

    @Query(value = "select a.*\n" +
            "  from sources s, posts p, accounts a\n" +
            "  where s.source_id = :PID\n" +
            "    and s.post_id = p.insta_post_id\n" +
            "    and p.account_id = a.insta_id ", nativeQuery = true)
    Collection<AccountDto> findAccountBySourceId(@Param("PID") Long postId);

}
