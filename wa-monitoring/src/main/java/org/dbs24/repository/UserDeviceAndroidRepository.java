/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.repository;

import java.util.Optional;
import org.dbs24.entity.UserDeviceAndroid;
import org.dbs24.spring.core.data.ApplicationJpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface UserDeviceAndroidRepository extends ApplicationJpaRepository<UserDeviceAndroid, Integer>, JpaSpecificationExecutor<UserDeviceAndroid> {

    public Optional<UserDeviceAndroid> findByDeviceId(Integer deviceId);
    
    public Optional<UserDeviceAndroid> findByGsfId(String gsfId);    

}
