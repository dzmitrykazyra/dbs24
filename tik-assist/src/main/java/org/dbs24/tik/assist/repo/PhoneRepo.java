/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.tik.assist.repo;

import org.dbs24.spring.core.data.ApplicationJpaRepository;
import org.dbs24.tik.assist.entity.domain.Phone;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import java.util.Collection;
import org.dbs24.tik.assist.entity.domain.PhoneStatus;

public interface PhoneRepo extends ApplicationJpaRepository<Phone, Integer>, JpaSpecificationExecutor<Phone>, PagingAndSortingRepository<Phone, Integer> {
    
    Collection<Phone> findByPhoneStatus(PhoneStatus phoneStatus);

}
