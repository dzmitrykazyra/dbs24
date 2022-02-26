package org.dbs24.tik.mobile.dao;

import org.dbs24.tik.mobile.entity.domain.UserDeviceIos;

import java.util.Optional;

public interface UserDeviceIosDao {

    Optional<UserDeviceIos> findDeviceByIdentifierForVendor(String identifier);

    void saveHist(UserDeviceIos userDeviceIos);

    UserDeviceIos save(UserDeviceIos userDeviceIos);

    UserDeviceIos findByDeviceId(Integer deviceId);

}
