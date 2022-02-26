/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.repository;

import org.dbs24.entity.UserToken;
import org.dbs24.spring.core.data.ApplicationJpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UserTokenRepository extends ApplicationJpaRepository<UserToken, Long> {

    @Query(value = "select a.* from wa_users_tokens a where token_id = (select max(token_id) from wa_users_tokens)", nativeQuery = true)
    public UserToken findLastToken();

}
