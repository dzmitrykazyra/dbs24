package org.dbs24.tik.mobile.entity.dto.device;

import lombok.Data;

@Data
public class UpdateDeviceAttributesRequestDto {
    private Integer deviceId;
    private String appName;
    private String appVersion;
    private AndroidDeviceAttributesDto androidAttributes;
    private IosDeviceAttributesDto iosAttributes;
}
