/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.insta.reg.repo;

import org.dbs24.insta.reg.entity.EmailStatus;
import org.dbs24.insta.reg.entity.Email;
import org.dbs24.spring.core.data.ApplicationJpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import java.util.Collection;
import java.util.Optional;

public interface EmailRepo extends ApplicationJpaRepository<Email, String>, JpaSpecificationExecutor<Email>, PagingAndSortingRepository<Email, String> {
    
    
    public Collection<Email> findByEmailStatus(EmailStatus emailStatus);
    public Optional<Email> findByEmailAddress(String emailAddress);
    
}
