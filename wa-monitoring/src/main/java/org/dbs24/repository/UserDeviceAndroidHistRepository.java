/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.repository;

import org.dbs24.entity.UserDeviceAndroidHist;
import org.dbs24.spring.core.data.ApplicationJpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;


public interface UserDeviceAndroidHistRepository  extends ApplicationJpaRepository<UserDeviceAndroidHist, Integer> , JpaSpecificationExecutor<UserDeviceAndroidHist> {
    
}
