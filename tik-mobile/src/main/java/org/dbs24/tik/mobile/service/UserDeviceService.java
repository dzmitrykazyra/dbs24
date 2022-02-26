package org.dbs24.tik.mobile.service;


import org.dbs24.tik.mobile.entity.dto.device.DeviceIdResponseDto;
import org.dbs24.tik.mobile.entity.dto.device.UpdateDeviceAttributesRequestDto;
import org.dbs24.tik.mobile.entity.dto.device.UserDeviceListDto;
import reactor.core.publisher.Mono;

public interface UserDeviceService {

    Mono<UserDeviceListDto> getAllUserDevices(Integer userId);

    Mono<DeviceIdResponseDto> removeDeviceById(String uid);

    Mono<DeviceIdResponseDto> createOrUpdateDeviceAttributes(Mono<UpdateDeviceAttributesRequestDto> deviceAttributesDtoMono, Integer userId);

}
