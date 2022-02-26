/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.repository;

import org.dbs24.spring.core.data.ApplicationJpaRepository;
import org.dbs24.entity.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.Query;

public interface UserRepository extends ApplicationJpaRepository<User, Integer> {

    @Query(value = "select a.* from wa_Users a where user_id = (select max(user_id) from wa_Users)", nativeQuery = true)
    public User findLastUser();
    
    public Optional<User> findByLoginToken(String loginToken);    

}
