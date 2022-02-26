/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.insta.fs.repo;

import org.dbs24.insta.fs.entity.Account;
import org.dbs24.spring.core.data.ApplicationJpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import java.util.Optional;

public interface AccountRepo extends ApplicationJpaRepository<Account, Long>, JpaSpecificationExecutor<Account>, PagingAndSortingRepository<Account, Long> {
    Optional<Account> findByInstaId(Long instaId);
}
