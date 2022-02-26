package org.dbs24.tik.mobile.dao.impl;

import org.dbs24.tik.mobile.constant.CacheKey;
import org.dbs24.tik.mobile.dao.DeviceTypeDao;
import org.dbs24.tik.mobile.entity.domain.DeviceType;
import org.dbs24.tik.mobile.repo.DeviceTypeRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

@Component
public class DeviceTypeDaoImpl implements DeviceTypeDao {

    private final DeviceTypeRepo deviceTypeRepo;

    @Autowired
    public DeviceTypeDaoImpl(DeviceTypeRepo deviceTypeRepo) {
        this.deviceTypeRepo = deviceTypeRepo;
    }

    @Override
    @Cacheable(CacheKey.CACHE_DEVICE_TYPE_ID)
    public DeviceType findDeviceTypeById(Integer deviceTypeId) {

        return deviceTypeRepo.getById(deviceTypeId);
    }
}
