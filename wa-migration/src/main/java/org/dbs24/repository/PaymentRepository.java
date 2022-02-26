/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.repository;

import org.dbs24.entity.Payment;
import org.dbs24.entity.AppUser;
import java.util.Collection;
import org.dbs24.spring.core.data.ApplicationJpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PaymentRepository extends ApplicationJpaRepository<Payment, Integer> , JpaSpecificationExecutor<Payment> {

    @Query(value = "select * from payment where user_id = :U ", nativeQuery = true)
    public Collection<Payment> findLastSomePayments(@Param("U") AppUser appUser);
    
    public Collection<Payment> findByAppUser(AppUser appUser);
    
    public Collection<Payment> findByIdAndAppUser(Integer id, AppUser appUser);

}
