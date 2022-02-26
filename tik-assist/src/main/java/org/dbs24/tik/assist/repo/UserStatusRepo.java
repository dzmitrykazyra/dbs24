/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.tik.assist.repo;

import org.dbs24.tik.assist.entity.domain.UserStatus;
import org.dbs24.spring.core.data.ApplicationJpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Optional;

public interface UserStatusRepo extends ApplicationJpaRepository<UserStatus, Integer>, JpaSpecificationExecutor<UserStatus>, PagingAndSortingRepository<UserStatus, Integer> {

    Optional<UserStatus> findByUserStatusName(String userStatusName);
}
