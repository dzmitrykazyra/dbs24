package org.dbs24.tik.mobile.dao.impl;

import org.dbs24.tik.mobile.dao.UserDeviceDao;
import org.dbs24.tik.mobile.entity.domain.UserDevice;
import org.dbs24.tik.mobile.entity.domain.UserDeviceHist;
import org.dbs24.tik.mobile.repo.UserDeviceHistRepo;
import org.dbs24.tik.mobile.repo.UserDeviceRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UserDeviceDaoImpl implements UserDeviceDao {

    private final UserDeviceRepo userDeviceRepo;
    private final UserDeviceHistRepo userDeviceHistRepo;

    @Autowired
    public UserDeviceDaoImpl(UserDeviceRepo userDeviceRepo, UserDeviceHistRepo userDeviceHistRepo) {
        this.userDeviceRepo = userDeviceRepo;
        this.userDeviceHistRepo = userDeviceHistRepo;
    }

    @Override
    public List<UserDevice> findDevicesByUserId(Integer userId) {

        return userDeviceRepo.findAllByUserId(userId);
    }

    @Override
    public UserDevice findUserDeviceById(Integer deviceId) {

        return userDeviceRepo.findById(deviceId)
                .orElseThrow(() -> new RuntimeException("Cannot find user device with id = " + deviceId));
    }

    @Override
    public UserDevice save(UserDevice userDevice) {

        return userDeviceRepo.save(userDevice);
    }

    @Override
    public void saveHist(UserDevice userDevice) {

        userDeviceHistRepo.save(UserDeviceHist.toHist(userDevice));
    }
}
