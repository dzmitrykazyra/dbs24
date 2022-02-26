/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.insta.reg.repo;

import org.dbs24.insta.reg.entity.AccountStatus;
import org.dbs24.spring.core.data.ApplicationJpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 *
 * @author kdg
 */
public interface AccountStatusRepo extends ApplicationJpaRepository<AccountStatus, Byte>, JpaSpecificationExecutor<AccountStatus>, PagingAndSortingRepository<AccountStatus, Byte> {
    
}
