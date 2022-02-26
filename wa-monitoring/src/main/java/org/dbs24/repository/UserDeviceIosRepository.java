/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.repository;

import org.dbs24.entity.UserDeviceIos;
import org.dbs24.spring.core.data.ApplicationJpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface UserDeviceIosRepository extends ApplicationJpaRepository<UserDeviceIos, Integer>, JpaSpecificationExecutor<UserDeviceIos> {

    Optional<UserDeviceIos> findByDeviceId(Integer deviceId);

    Optional<UserDeviceIos> findByIdentifierForVendor(String identifierForVendor);

    Optional<UserDeviceIos> findByAppleId(String appleId);

}
