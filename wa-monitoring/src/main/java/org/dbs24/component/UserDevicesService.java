/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.component;

import lombok.extern.log4j.Log4j2;
import org.dbs24.application.core.service.funcs.ServiceFuncs;
import org.dbs24.auth.server.api.WaUserSessionInfo;
import org.dbs24.entity.*;
import org.dbs24.entity.dto.RemoveDeviceResult;
import org.dbs24.exception.AppUserIsNotFound;
import org.dbs24.kafka.KafkaService;
import org.dbs24.kafka.KafkaUserDeviceAndroid;
import org.dbs24.kafka.KafkaUserDeviceIos;
import org.dbs24.repository.*;
import org.dbs24.rest.api.ExistsDeviceInfo;
import org.dbs24.rest.api.UpdateDeviceResult;
import org.dbs24.spring.core.api.AbstractApplicationService;
import org.dbs24.stmt.StmtProcessor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Comparator;
import java.util.Optional;
import java.util.function.Supplier;

import static java.lang.String.format;
import static java.time.LocalDateTime.now;
import static java.util.Optional.ofNullable;
import static org.dbs24.consts.SysConst.BOOLEAN_FALSE;
import static org.dbs24.consts.WaConsts.Classes.USER_DEVICE_CLASS;
import static org.dbs24.consts.WaConsts.OperCode.OC_UNKNOWN_DEVCE_STR;
import static org.dbs24.consts.WaConsts.OperCode.OC_UNKNOWN_DEVICE;
import static org.dbs24.consts.WaConsts.References.DT_ANDROID;
import static org.dbs24.consts.WaConsts.Uri.URI_REMOVE_DEVICE_BY_ID;
import static org.dbs24.stmt.StmtProcessor.*;

@Log4j2
@Component
@ConditionalOnProperty(name = "config.restfull.profile.name", havingValue = "wa-monitoring")
public class UserDevicesService extends AbstractApplicationService {

    final UserDeviceRepository userDeviceRepository;
    final UserDeviceHistRepository userDeviceHistRepository;
    final DeviceTypeRepository deviceTypeRepository;
    final UserDeviceAndroidRepository userDeviceAndroidRepository;
    final UserDeviceAndroidHistRepository userDeviceAndroidHistRepository;
    final UserDeviceIosRepository userDeviceIosRepository;
    final UserDeviceIosHistRepository userDeviceIosHistRepository;
    final KafkaService kafkaService;

    public UserDevicesService(UserDeviceHistRepository userDeviceHistRepository, UserDeviceRepository userDeviceRepository, DeviceTypeRepository deviceTypeRepository, UserDeviceAndroidRepository userDeviceAndroidRepository, UserDeviceAndroidHistRepository userDeviceAndroidHistRepository, UserDeviceIosRepository userDeviceIosRepository, UserDeviceIosHistRepository userDeviceIosHistRepository, KafkaService kafkaService) {
        this.userDeviceRepository = userDeviceRepository;
        this.userDeviceHistRepository = userDeviceHistRepository;
        this.deviceTypeRepository = deviceTypeRepository;
        this.userDeviceAndroidRepository = userDeviceAndroidRepository;
        this.userDeviceAndroidHistRepository = userDeviceAndroidHistRepository;
        this.userDeviceIosRepository = userDeviceIosRepository;
        this.userDeviceIosHistRepository = userDeviceIosHistRepository;
        this.kafkaService = kafkaService;
    }

    static final Comparator<UserDevice> latestDeviceSort = (a, b) -> b.getActualDate().compareTo(a.getActualDate());

    public void saveUserDeviceHist(UserDevice userDevice) {
        ifNotNull(userDevice.getDeviceId(), deviceId -> userDeviceHistRepository.save(create(UserDeviceHist.class, udh -> udh.assign(userDevice))));
    }

    //@CacheEvict(value = {CACHE_DEVICE_BY_USER, CACHE_ANDROID_DEVICE, CACHE_IOS_DEVICE}, allEntries = true, beforeInvocation = true)
    public void saveUserDevice(UserDevice userDevice) {
        userDeviceRepository.save(userDevice);
    }

    public UserDevice createUserDevice() {
        return create(USER_DEVICE_CLASS, a -> a.setActualDate(now()));
    }

    public UserDevice findUserDevice(Integer userDeviceId) {

        return userDeviceRepository
                .findById(userDeviceId)
                .orElseThrow(() -> new AppUserIsNotFound(format("userDeviceId not found (%d)", userDeviceId)));
    }

    final Supplier<UserDevice> newDevice = this::createUserDevice;

    public UserDevice findOrCreateUserDevice(Integer userDeviceId) {
        return ofNullable(userDeviceId)
                .map(this::findUserDevice)
                .orElseGet(newDevice);
    }

    public Optional<User> findUserByDeviceId(Integer deviceId) {
        return userDeviceRepository.findById(deviceId).map(UserDevice::getUser);
    }

    //@CacheEvict(value = {CACHE_DEVICE_BY_USER, CACHE_ANDROID_DEVICE, CACHE_IOS_DEVICE}, allEntries = true, beforeInvocation = true)
    public void saveUserDeviceAndroid(UserDeviceAndroid userDeviceAndroid, String appName, Boolean notifyKafka) {
        userDeviceAndroidRepository.save(userDeviceAndroid);

        // notify
        if (notifyKafka) {
            kafkaService.notifyModifiedAndroidDevice(create(KafkaUserDeviceAndroid.class,
                    udsi -> udsi.assign(userDeviceAndroid, findUserByDeviceId(userDeviceAndroid.getDeviceId()).orElseThrow().getUserId(), appName)));
        }
    }

    public void saveUserDeviceAndroidHist(UserDeviceAndroid userDeviceAndroid) {
        userDeviceAndroidHistRepository.save(create(UserDeviceAndroidHist.class, uah -> uah.assign(userDeviceAndroid)));
    }

    //@CacheEvict(value = {CACHE_DEVICE_BY_USER, CACHE_ANDROID_DEVICE, CACHE_IOS_DEVICE}, allEntries = true, beforeInvocation = true)
    public void saveUserDeviceIos(UserDeviceIos userDeviceIos, String appName, Boolean notifyKafa) {
        userDeviceIosRepository.save(userDeviceIos);

        // notify
        if (notifyKafa) {
            kafkaService.notifyModifiedIosDevice(create(KafkaUserDeviceIos.class,
                    idsi -> idsi.assign(userDeviceIos, findUserByDeviceId(userDeviceIos.getDeviceId()).orElseThrow().getUserId(), appName)));
        }
    }

    public void saveUserDeviceIosHist(UserDeviceIos userDeviceIos) {
        userDeviceIosHistRepository.save(create(UserDeviceIosHist.class, uah -> uah.assign(userDeviceIos)));
    }

    public Collection<UserDevice> findAllUserDevices(User user) {

        return StmtProcessor.notNull(user.getUserId())
                ? userDeviceRepository.findByUser(user)
                : ServiceFuncs.createCollection();
    }

    //@Cacheable(CACHE_ANDROID_DEVICE)
    public UserDeviceAndroid findUserDeviceAndroid(Integer deviceId) {

        return userDeviceAndroidRepository
                .findByDeviceId(deviceId)
                .orElseThrow(() -> new AppUserIsNotFound(format("Android deviceId not found (device_id = %d)", deviceId)));
    }

    //@Cacheable(CACHE_IOS_DEVICE)
    public UserDeviceIos findUserDeviceIos(Integer deviceId) {

        return userDeviceIosRepository
                .findByDeviceId(deviceId)
                .orElseThrow(() -> new AppUserIsNotFound(format("Ios deviceId not found (device_id = %d)", deviceId)));
    }

    public Optional<UserDeviceAndroid> findUserDeviceAndroidByGsfId(String gsfId) {

        return userDeviceAndroidRepository
                .findByGsfId(gsfId);
    }

    public Optional<UserDeviceIos> findUserDeviceIosByAppleId(String appleId) {

        return userDeviceIosRepository
                .findByAppleId(appleId);
    }

    public Optional<UserDeviceIos> findUserDeviceIosByIdentifierForVendor(String identifierForVendor) {

        return userDeviceIosRepository
                .findByIdentifierForVendor(identifierForVendor);
    }

    //==========================================================================
    public ExistsDeviceInfo existsDevice(DeviceType deviceType, String deviceUid) {
        return create(ExistsDeviceInfo.class, edi
                -> edi.setIsExists(deviceType.getDeviceTypeId().equals(DT_ANDROID)
                ? findUserDeviceAndroidByGsfId(deviceUid).isPresent()
                : findUserDeviceIosByIdentifierForVendor(deviceUid).isPresent())
        );
    }

    //==========================================================================
    @Transactional
    public UpdateDeviceResult updateDeviceAttrs(WaUserSessionInfo waUserSessionInfo) {
        return create(UpdateDeviceResult.class, udr -> {

            udr.setDeviceId(waUserSessionInfo.getDeviceId());
            udr.setAnswerCode(OC_UNKNOWN_DEVICE);
            udr.setNote(OC_UNKNOWN_DEVCE_STR + format(" (%d)", waUserSessionInfo.getDeviceId()));

        });
    }

    //==========================================================================
    @Transactional
    public RemoveDeviceResult removeDevicebyId(String deviceId) {
        return create(RemoveDeviceResult.class, rdr -> {

            final String msgTemplate = "removed device by %s, J63, (old value = %s), ".concat(now().toString());

            // search gsfId
            userDeviceAndroidRepository
                    .findByGsfId(deviceId)
                    .ifPresent(device -> {

                        rdr.getDeletedDevices().add(format("remove android device (%s, device_id=%d, fcmToken=%s)", deviceId, device.getDeviceId(), device.getFcmToken()));

                        // save history
                        saveUserDeviceAndroidHist(device);

                        final String newGsfId = format(msgTemplate, URI_REMOVE_DEVICE_BY_ID, device.getGsfId());

                        device.setGsfId(newGsfId);
                        device.setActualDate(now());

                        saveUserDeviceAndroid(device, newGsfId, BOOLEAN_FALSE);

                    });

            // search IdentifierForVendor
            userDeviceIosRepository
                    .findByIdentifierForVendor(deviceId)
                    .ifPresent(device -> {

                        rdr.getDeletedDevices().add(format("remove ios device (%s, device_id=%d, icmToken=%s)", deviceId, device.getDeviceId(), device.getIcmToken()));

                        // save history
                        saveUserDeviceIosHist(device);

                        final String IdentifierForVendor = format(msgTemplate, URI_REMOVE_DEVICE_BY_ID, device.getIdentifierForVendor());

                        device.setIdentifierForVendor(IdentifierForVendor);
                        device.setActualDate(now());

                        saveUserDeviceIos(device, IdentifierForVendor, BOOLEAN_FALSE);

                    });

            // search appleId
            userDeviceIosRepository
                    .findByAppleId(deviceId)
                    .ifPresent(device -> {

                        rdr.getDeletedDevices().add(format("remove ios device (%s, device_id=%d, appleId=%s)", deviceId, device.getDeviceId(), device.getAppleId()));

                        // save history
                        saveUserDeviceIosHist(device);

                        final String appleId = format(msgTemplate, URI_REMOVE_DEVICE_BY_ID, device.getAppleId());

                        device.setAppleId(appleId);
                        device.setActualDate(now());

                        saveUserDeviceIos(device, appleId, BOOLEAN_FALSE);

                    });

            ifTrue(rdr.getDeletedDevices().isEmpty(),
                    () -> rdr.getDeletedDevices().add(format("no devices found (%s) ", deviceId)));

        });
    }
}
