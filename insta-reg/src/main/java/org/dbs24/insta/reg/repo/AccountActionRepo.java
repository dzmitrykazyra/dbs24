/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.insta.reg.repo;

import java.util.Collection;
import org.dbs24.insta.reg.entity.AccountAction;
import org.dbs24.spring.core.data.ApplicationJpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface AccountActionRepo extends ApplicationJpaRepository<AccountAction, Long>, JpaSpecificationExecutor<AccountAction>, PagingAndSortingRepository<AccountAction, Long> {

    @Query(value = "select ac.* from inst_accounts_actions ac where ac.err_msg is not null order by ac.action_code desc limit 100 ", nativeQuery = true)
    public Collection<AccountAction> getLatestExceptions();

}
