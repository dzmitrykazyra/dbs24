/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.tik.assist.repo;

import org.dbs24.spring.core.data.ApplicationJpaRepository;
import org.dbs24.tik.assist.entity.domain.User;
import org.dbs24.tik.assist.entity.domain.UserStatus;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;


public interface UserRepo extends ApplicationJpaRepository<User, Integer>, JpaSpecificationExecutor<User>, PagingAndSortingRepository<User, Integer> {

    Optional<User> findByEmail(String email);

    @Query(value = "SELECT * FROM tik_users WHERE email =:email", nativeQuery = true)
    List<User> findUsersListByEmail(@Param("email") String email);

    Optional<User> findByEmailAndGoogleUserId(String email, String googleUserId);

    Optional<User> findByFacebookUserId(String facebookUserId);

    Optional<User> findByEmailAndUserStatus( String email,  UserStatus userStatus);
}
