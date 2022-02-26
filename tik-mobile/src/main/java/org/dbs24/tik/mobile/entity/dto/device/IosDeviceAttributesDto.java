package org.dbs24.tik.mobile.entity.dto.device;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(setterPrefix = "with")
public class IosDeviceAttributesDto{
    private String systemVersion;
    private String model;
    private String appleId;
    private String identifierForVendor;
    private String ustnameRelease;
    private String ustnameVersion;
    private String ustnameMachine;
    private String icmToken;
}
