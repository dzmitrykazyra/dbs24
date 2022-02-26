package org.dbs24.tik.mobile.dao.impl;

import org.dbs24.tik.mobile.dao.UserDeviceIosDao;
import org.dbs24.tik.mobile.entity.domain.UserDeviceIos;
import org.dbs24.tik.mobile.entity.domain.UserDeviceIosHist;
import org.dbs24.tik.mobile.repo.UserDeviceIosHistRepo;
import org.dbs24.tik.mobile.repo.UserDeviceIosRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class UserDeviceIosDaoImpl implements UserDeviceIosDao {

    private final UserDeviceIosRepo userDeviceIosRepo;
    private final UserDeviceIosHistRepo userDeviceIosHistRepo;

    @Autowired
    public UserDeviceIosDaoImpl(UserDeviceIosRepo userDeviceIosRepo, UserDeviceIosHistRepo userDeviceIosHistRepo) {
        this.userDeviceIosRepo = userDeviceIosRepo;
        this.userDeviceIosHistRepo = userDeviceIosHistRepo;
    }

    @Override
    public Optional<UserDeviceIos> findDeviceByIdentifierForVendor(String identifier) {
        return userDeviceIosRepo.findByIdentifierForVendor(identifier);
    }

    @Override
    public void saveHist(UserDeviceIos userDeviceIos) {
        userDeviceIosHistRepo.save(UserDeviceIosHist.toHist(userDeviceIos));
    }

    @Override
    public UserDeviceIos save(UserDeviceIos userDeviceIos) {
        return userDeviceIosRepo.save(userDeviceIos);
    }

    @Override
    public UserDeviceIos findByDeviceId(Integer deviceId) {
        return userDeviceIosRepo.findById(deviceId)
                .orElseThrow(() -> new RuntimeException("Cannot find ios device with id = " + deviceId));
    }
}
