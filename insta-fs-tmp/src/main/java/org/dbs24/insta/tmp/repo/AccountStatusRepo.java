/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.insta.tmp.repo;

import org.dbs24.spring.core.data.ApplicationJpaRepository;
import org.dbs24.insta.tmp.entity.AccountStatus;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface AccountStatusRepo extends ApplicationJpaRepository<AccountStatus, Integer>, JpaSpecificationExecutor<AccountStatus>, PagingAndSortingRepository<AccountStatus, Integer> {
    
}
