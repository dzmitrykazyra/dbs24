/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.repository;

import org.dbs24.entity.UserContract;
import org.dbs24.entity.User;
import org.dbs24.entity.ContractStatus;
import org.dbs24.spring.core.data.ApplicationJpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.Collection;
import java.util.Optional;


public interface UserContractRepository extends ApplicationJpaRepository<UserContract, Integer>, JpaSpecificationExecutor<UserContract> {

    @Query(value = "select a.* from wa_users_contracts a where contract_id = (select max(contract_id) from wa_users_contracts)", nativeQuery = true)
    UserContract findLastContract();

    Collection<UserContract> findByUser(User user);

    Collection<UserContract> findByUserAndContractStatus(User user, ContractStatus contractStatus);

}