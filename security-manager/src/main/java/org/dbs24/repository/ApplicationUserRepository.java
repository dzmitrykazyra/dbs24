/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.repository;

import org.dbs24.entity.security.ApplicationUser;
import org.dbs24.spring.core.data.ApplicationJpaRepository;
import java.util.Collection;
import java.util.Optional;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional(readOnly = true)
public interface ApplicationUserRepository extends ApplicationJpaRepository<ApplicationUser, Long> {

    public Optional<ApplicationUser> findByName( String name);
    
    public Optional<ApplicationUser> findByLogin( String login);

    @Query(value = "select w.* from w_items w where lower(w.link_header) like %:p_name%", nativeQuery = true)
    public Collection<ApplicationUser> findByNameSql(@Param("p_name") final String keyword);

}
