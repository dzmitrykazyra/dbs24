/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.repository;

import org.dbs24.entity.SubscriptionPhone;
import org.dbs24.entity.AppUser;
import org.dbs24.spring.core.data.ApplicationJpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.Collection;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface SubscriptionPhoneRepository extends ApplicationJpaRepository<SubscriptionPhone, Integer> , JpaSpecificationExecutor<SubscriptionPhone> {

    @Query(value = "select a.* from SubscriptionPhone a where id = (select max(id) from SubscriptionPhone)", nativeQuery = true)
    public SubscriptionPhone findLastSubscriptionPhone();
       
    Collection<SubscriptionPhone> findByAppUser(AppUser appUser);
}
