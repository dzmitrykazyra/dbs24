package org.dbs24.tik.mobile.service.impl;

import lombok.extern.log4j.Log4j2;
import org.dbs24.stmt.StmtProcessor;
import org.dbs24.tik.mobile.constant.ApiPath;
import org.dbs24.tik.mobile.dao.*;
import org.dbs24.tik.mobile.entity.domain.User;
import org.dbs24.tik.mobile.entity.domain.UserDevice;
import org.dbs24.tik.mobile.entity.domain.UserDeviceAndroid;
import org.dbs24.tik.mobile.entity.domain.UserDeviceIos;
import org.dbs24.tik.mobile.entity.dto.device.*;
import org.dbs24.tik.mobile.service.UserDeviceService;
import org.dbs24.tik.mobile.service.exception.http.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.stream.Collectors;

import static java.time.LocalDateTime.now;
import static org.dbs24.tik.mobile.constant.reference.DeviceTypeDefine.DT_ANDROID;
import static org.dbs24.tik.mobile.constant.reference.DeviceTypeDefine.DT_IOS;

@Service
@Log4j2
public class UserDeviceServiceImpl implements UserDeviceService {

    private final UserDeviceDao userDeviceDao;
    private final UserDeviceAndroidDao userDeviceAndroidDao;
    private final UserDeviceIosDao userDeviceIosDao;
    private final DeviceTypeDao deviceTypeDao;
    private final UserDao userDao;

    @Autowired
    public UserDeviceServiceImpl(UserDeviceDao userDeviceDao,
                                 UserDeviceAndroidDao userDeviceAndroidDao,
                                 UserDeviceIosDao userDeviceIosDao,
                                 DeviceTypeDao deviceTypeDao,
                                 UserDao userDao) {

        this.userDeviceDao = userDeviceDao;
        this.userDeviceAndroidDao = userDeviceAndroidDao;
        this.userDeviceIosDao = userDeviceIosDao;
        this.deviceTypeDao = deviceTypeDao;
        this.userDao = userDao;
    }

    @Override
    public Mono<UserDeviceListDto> getAllUserDevices(Integer userId) {
        final List<UserDevice> userDevices = userDeviceDao.findDevicesByUserId(userId);

        return Mono.just(
                StmtProcessor.create(UserDeviceListDto.class,
                        devices -> devices.setUserDevices(userDevices.stream()
                                .map(UserDeviceInfoDto::of)
                                .collect(Collectors.toList())
                        )
                )
        );
    }

    @Override
    @Transactional
    public Mono<DeviceIdResponseDto> createOrUpdateDeviceAttributes(Mono<UpdateDeviceAttributesRequestDto> deviceAttributesDtoMono, Integer userId) {

        return deviceAttributesDtoMono.map(attributesDto -> {

            Integer deviceTypeId = attributesDto.getAndroidAttributes() == null ? DT_IOS.getDeviceTypeId() : DT_ANDROID.getDeviceTypeId();
            User user = userDao.findById(userId);

            UserDevice userDevice = createOrUpdateUserDevice(attributesDto, user, deviceTypeId);

            if (attributesDto.getDeviceId() == null) {
                createDeviceAttributes(attributesDto, userDevice.getDeviceId());
            } else {
                updateDeviceAttributes(attributesDto, userDevice.getDeviceId());
            }

            return StmtProcessor.create(DeviceIdResponseDto.class, response -> response.setDeviceId(userDevice.getDeviceId()));
        });

    }

    @Override
    @Transactional
    public Mono<DeviceIdResponseDto> removeDeviceById(String uid) {
        final String msgForSave = "removed device by "
                .concat(ApiPath.DEVICE_REMOVE_BY_ID).concat(", (old value = ")
                .concat(uid).concat("), ")
                .concat(now().toString());

        final DeviceIdResponseDto response = StmtProcessor.create(DeviceIdResponseDto.class);

        userDeviceAndroidDao.findDeviceByGsfId(uid)
                .ifPresent(device -> {
                    userDeviceAndroidDao.saveHist(device);

                    device.setGsfId(msgForSave);
                    device.setActualDate(now());

                    response.setDeviceId(userDeviceAndroidDao.save(device).getDeviceId());
                });

        userDeviceIosDao.findDeviceByIdentifierForVendor(uid)
                .ifPresent(device -> {
                    userDeviceIosDao.saveHist(device);

                    device.setIdentifierForVendor(msgForSave);
                    device.setActualDate(now());

                    response.setDeviceId(userDeviceIosDao.save(device).getDeviceId());
                });

        if (response.getDeviceId() == null) {
            throw new BadRequestException();
        }

        return Mono.just(response);
    }

    private UserDevice createOrUpdateUserDevice(UpdateDeviceAttributesRequestDto attributes, User user, Integer deviceTypeId) {
        final UserDevice userDevice;

        if (attributes.getDeviceId() == null) {
            userDevice = UserDevice.builder()
                    .withUser(user)
                    .withActualDate(now())
                    .withDeviceType(deviceTypeDao.findDeviceTypeById(deviceTypeId))
                    .withAppName(attributes.getAppName())
                    .withAppVersion(attributes.getAppVersion())
                    .build();
        } else {
            userDevice = userDeviceDao.findUserDeviceById(attributes.getDeviceId());
            userDeviceDao.saveHist(userDevice);
            userDevice.setAppName(attributes.getAppName());
            userDevice.setAppVersion(attributes.getAppVersion());
        }

        return userDeviceDao.save(userDevice);
    }

    private void createDeviceAttributes(UpdateDeviceAttributesRequestDto attributes, Integer deviceId) {

        StmtProcessor.ifTrue(attributes.getAndroidAttributes() == null,
                () -> userDeviceIosDao.save(
                        UserDeviceIos.of(attributes.getIosAttributes(), deviceId)
                ),
                () -> userDeviceAndroidDao.save(
                        UserDeviceAndroid.of(attributes.getAndroidAttributes(), deviceId)
                )
        );
    }

    private void updateDeviceAttributes(UpdateDeviceAttributesRequestDto attributes, Integer deviceId) {

        StmtProcessor.ifTrue(attributes.getAndroidAttributes() != null,
                () -> {
                    final UserDeviceAndroid androidDevice = userDeviceAndroidDao.findByDeviceId(attributes.getDeviceId());
                    userDeviceAndroidDao.saveHist(androidDevice);
                    userDeviceAndroidDao.save(UserDeviceAndroid.of(attributes.getAndroidAttributes(), deviceId));
                },
                () -> {
                    final UserDeviceIos iosDevice = userDeviceIosDao.findByDeviceId(attributes.getDeviceId());
                    userDeviceIosDao.saveHist(iosDevice);
                    userDeviceIosDao.save(UserDeviceIos.of(attributes.getIosAttributes(), deviceId));
                });
    }
}