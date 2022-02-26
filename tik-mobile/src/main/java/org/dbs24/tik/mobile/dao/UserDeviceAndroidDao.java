package org.dbs24.tik.mobile.dao;

import org.dbs24.tik.mobile.entity.domain.UserDeviceAndroid;

import java.util.Optional;

public interface UserDeviceAndroidDao {

    Optional<UserDeviceAndroid> findDeviceByGsfId(String uid);

    void saveHist(UserDeviceAndroid userDeviceAndroid);

    UserDeviceAndroid save(UserDeviceAndroid userDeviceAndroid);

    UserDeviceAndroid findByDeviceId(Integer deviceId);

}
