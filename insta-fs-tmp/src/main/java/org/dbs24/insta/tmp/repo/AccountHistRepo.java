/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.insta.tmp.repo;

import org.dbs24.insta.tmp.entity.AccountHist;
import org.dbs24.spring.core.data.ApplicationJpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 *
 * @author kdg
 */
public interface AccountHistRepo extends ApplicationJpaRepository<AccountHist, Long>, JpaSpecificationExecutor<AccountHist>, PagingAndSortingRepository<AccountHist, Long> {
    
}
