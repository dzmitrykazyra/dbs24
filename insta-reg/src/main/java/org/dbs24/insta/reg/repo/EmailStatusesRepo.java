/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.insta.reg.repo;

import org.dbs24.insta.reg.entity.EmailStatus;
import org.dbs24.spring.core.data.ApplicationJpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;


public interface EmailStatusesRepo extends ApplicationJpaRepository<EmailStatus, Byte>, JpaSpecificationExecutor<EmailStatus>, PagingAndSortingRepository<EmailStatus, Byte> {
    
}
