/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.insta.reg.repo;

import java.util.Collection;
import org.dbs24.insta.reg.entity.Account;
import org.dbs24.insta.reg.entity.AccountStatus;
import org.dbs24.insta.reg.entity.Email;
import org.dbs24.spring.core.data.ApplicationJpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

public interface AccountRepo extends ApplicationJpaRepository<Account, Integer>, JpaSpecificationExecutor<Account>, PagingAndSortingRepository<Account, Integer> {

    public Collection<Account> findByEmail(Email email);

    public Collection<Account> findByAccountStatus(AccountStatus accountStatus);

    @Query(value = "select a.* from inst_accounts a where a.email_address = :E and ( a.account_status_id <0 or a.account_status_id= 5 ) ", nativeQuery = true)
    public Collection<Account> findUsedFakedAccounts(@Param("E") Email email);

    @Query(value = "select count(*) from inst_accounts where account_status_id = 5 ", nativeQuery = true)
    public Integer getTotalReadyAccounts();
    
    @Query(value = "select distinct used_ip from inst_accounts where create_date > current_date - INTERVAL '3 DAY' ", nativeQuery = true)
    public Collection<String> getUsedIPs();    

}
