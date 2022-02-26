package org.dbs24.tik.mobile.dao.impl;

import org.dbs24.tik.mobile.dao.UserDeviceAndroidDao;
import org.dbs24.tik.mobile.entity.domain.UserDeviceAndroid;
import org.dbs24.tik.mobile.entity.domain.UserDeviceAndroidHist;
import org.dbs24.tik.mobile.repo.UserDeviceAndroidHistRepo;
import org.dbs24.tik.mobile.repo.UserDeviceAndroidRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class UserDeviceAndroidDaoImpl implements UserDeviceAndroidDao {

    private final UserDeviceAndroidRepo userDeviceAndroidRepo;
    private final UserDeviceAndroidHistRepo userDeviceAndroidHistRepo;

    @Autowired
    public UserDeviceAndroidDaoImpl(UserDeviceAndroidRepo userDeviceAndroidRepo,
                                    UserDeviceAndroidHistRepo userDeviceAndroidHistRepo) {

        this.userDeviceAndroidRepo = userDeviceAndroidRepo;
        this.userDeviceAndroidHistRepo = userDeviceAndroidHistRepo;
    }

    @Override
    public Optional<UserDeviceAndroid> findDeviceByGsfId(String uid) {

        return userDeviceAndroidRepo.findByGsfId(uid);
    }

    @Override
    public void saveHist(UserDeviceAndroid userDeviceAndroid) {
        userDeviceAndroidHistRepo.save(UserDeviceAndroidHist.toHist(userDeviceAndroid));
    }

    @Override
    public UserDeviceAndroid save(UserDeviceAndroid userDeviceAndroid) {
        return userDeviceAndroidRepo.save(userDeviceAndroid);
    }

    @Override
    public UserDeviceAndroid findByDeviceId(Integer deviceId) {
        return userDeviceAndroidRepo.findById(deviceId)
                .orElseThrow(() -> new RuntimeException("Cannot find android device with id = " + deviceId));
    }
}
