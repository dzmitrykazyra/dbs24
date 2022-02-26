/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.tik.dev.service;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import org.dbs24.rest.api.action.SimpleActionInfo;
import org.dbs24.rest.api.service.AbstractRestApplicationService;
import org.dbs24.tik.dev.dao.DeviceDao;
import org.dbs24.tik.dev.entity.Device;
import org.dbs24.tik.dev.entity.DeviceHist;
import org.dbs24.tik.dev.rest.dto.device.CreateDeviceRequest;
import org.dbs24.tik.dev.rest.dto.device.CreatedDevice;
import org.dbs24.tik.dev.rest.dto.device.CreatedDeviceResponse;
import org.dbs24.tik.dev.rest.dto.device.DeviceInfo;
import org.dbs24.tik.dev.rest.dto.device.validator.DeviceInfoValidator;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import java.util.function.BiFunction;
import java.util.function.Supplier;

import static java.time.LocalDateTime.now;
import static java.util.Optional.ofNullable;
import static org.dbs24.application.core.locale.NLS.long2LocalDateTime;
import static org.dbs24.rest.api.consts.RestApiConst.RestOperCode.OC_INVALID_ENTITY_ATTRS;
import static org.dbs24.rest.api.consts.RestApiConst.RestOperCode.OC_OK;
import static org.dbs24.stmt.StmtProcessor.*;

@Getter
@Log4j2
@Component
@EqualsAndHashCode
public class DevicesService extends AbstractRestApplicationService {

    final DeviceDao deviceDao;
    final RefsService refsService;
    final DevelopersService developersService;
    final TariffPlanService tariffPlanService;
    final DeviceInfoValidator deviceInfoValidator;

    public DevicesService(RefsService refsService, DeviceDao deviceDao, DeviceInfoValidator deviceInfoValidator, DevelopersService developersService, TariffPlanService tariffPlanService) {

        this.refsService = refsService;
        this.deviceDao = deviceDao;
        this.deviceInfoValidator = deviceInfoValidator;
        this.developersService = developersService;
        this.tariffPlanService = tariffPlanService;
    }

    @FunctionalInterface
    interface DevicesHistBuilder {
        void buildDevicesHist(Device device);
    }

    final Supplier<Device> createNewDevice = () -> create(Device.class);

    final BiFunction<DeviceInfo, Device, Device> assignDto = (deviceInfo, device) -> {

        device.setActualDate(long2LocalDateTime(deviceInfo.getActualDate()));
        ifNull(device.getActualDate(), () -> device.setActualDate(now()));
        device.setDeviceIdStr(deviceInfo.getDeviceIdStr());
        device.setApkAttrs(deviceInfo.getApkAttrs());
        device.setApkHashId(deviceInfo.getApkHashId());
        device.setInstallId(deviceInfo.getInstallId());
        device.setDeviceStatus(getRefsService().findDeviceStatus(deviceInfo.getDeviceStatusId()));

        return device;
    };

    final BiFunction<DeviceInfo, DevicesHistBuilder, Device> assignDevicesInfo = (deviceInfo, devicesHistBuilder) -> {

        final Device device = ofNullable(deviceInfo.getDeviceId())
                .map(this::findDevice)
                .orElseGet(createNewDevice);

        // store history
        ofNullable(device.getDeviceId()).ifPresent(borId -> devicesHistBuilder.buildDevicesHist(device));

        assignDto.apply(deviceInfo, device);

        return device;
    };

    //==========================================================================
    @Transactional
    public CreatedDeviceResponse createOrUpdateDevice(Mono<CreateDeviceRequest> monoRequest) {

        return this.<CreatedDevice, CreatedDeviceResponse>createAnswer(CreatedDeviceResponse.class,
                (responseBody, createdDevice) -> processRequest(monoRequest, responseBody, request
                        -> responseBody.setErrors(deviceInfoValidator.validateConditional(request.getEntityInfo(), deviceInfo
                        -> {

                    final SimpleActionInfo simpleActionInfo = request.getEntityAction();

                    //log.info("simpleActionInfo =  {}", simpleActionInfo);

                    log.debug("create/update device: {}", deviceInfo);

                    //StmtProcessor.assertNotNull(String.class, deviceInfo.getPackageName(), "packageName name is not defined");

                    final Device device = findOrCreateDevices(deviceInfo, deviceHist -> saveDeviceHist(createDeviceHist(deviceHist)));

                    final Boolean isNewSetting = isNull(device.getDeviceId());

                    getDeviceDao().saveDevice(device);

                    final String finalMessage = String.format("Device is %s (DeviceId=%d)",
                            isNewSetting ? "created" : "updated",
                            device.getDeviceId());

                    createdDevice.setCreatedDeviceId(device.getDeviceId());
                    responseBody.complete();

                    log.debug(finalMessage);

                    responseBody.setCode(OC_OK);
                    responseBody.setMessage(finalMessage);
                }, errorInfos -> {
                    responseBody.setCode(OC_INVALID_ENTITY_ATTRS);
                    responseBody.setMessage(errorInfos.stream().findFirst().orElseThrow().getErrorMsg().toUpperCase());
                }))));
    }

    public Device findOrCreateDevices(DeviceInfo deviceInfo, DevicesService.DevicesHistBuilder devicesHistBuilder) {
        return assignDevicesInfo.apply(deviceInfo, devicesHistBuilder);
    }

    private DeviceHist createDeviceHist(Device device) {
        return create(DeviceHist.class, deviceHist -> {
            deviceHist.setDeviceId(device.getDeviceId());
            deviceHist.setActualDate(device.getActualDate());
            deviceHist.setDeviceStatus(device.getDeviceStatus());
            deviceHist.setDeviceIdStr(device.getDeviceIdStr());
            deviceHist.setApkHashId(device.getApkHashId());
            deviceHist.setInstallId(device.getInstallId());
            deviceHist.setApkAttrs(device.getApkAttrs());
        });
    }

    private void saveDeviceHist(DeviceHist deviceHist) {
        getDeviceDao().saveDeviceHist(deviceHist);
    }

    public Device findDevice(Long deviceId) {
        return getDeviceDao().findDevice(deviceId);
    }
}
