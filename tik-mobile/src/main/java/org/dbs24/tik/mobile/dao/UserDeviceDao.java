package org.dbs24.tik.mobile.dao;

import org.dbs24.tik.mobile.entity.domain.UserDevice;

import java.util.List;

public interface UserDeviceDao {

    List<UserDevice> findDevicesByUserId(Integer userId);

    UserDevice findUserDeviceById(Integer deviceId);

    UserDevice save(UserDevice userDevice);

    void saveHist(UserDevice userDevice);

}
