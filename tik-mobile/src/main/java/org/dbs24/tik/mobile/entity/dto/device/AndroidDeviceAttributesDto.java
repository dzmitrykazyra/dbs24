package org.dbs24.tik.mobile.entity.dto.device;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(setterPrefix = "with")
public class AndroidDeviceAttributesDto {
    private String gsfId;
    private String secureId;
    private String versionSdkInt;
    private String versionRelease;
    private String board;
    private String fingerprint;
    private String manufacter;
    private String supportedAbis;
    private String fcmToken;
}
