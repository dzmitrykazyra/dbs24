/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.repository;

import org.dbs24.entity.UserDevice;
import org.dbs24.entity.DeviceType;
import org.dbs24.entity.User;
import java.util.Optional;
import java.util.Collection;
import java.time.LocalDateTime;
import org.dbs24.spring.core.data.ApplicationJpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UserDeviceRepository extends ApplicationJpaRepository<UserDevice, Integer> {

    @Query(value = "select a.* from wa_users_devices a where device_id = (select max(device_id) from wa_users_devices)", nativeQuery = true)
    public UserDevice findLastDevice();

    public Optional<UserDevice> findByUserAndDeviceType(User user, DeviceType deviceType);

    public Collection<UserDevice> findByActualDateGreaterThan(LocalDateTime actualDate);
    
    public Collection<UserDevice> findByUser(User user);    

}
