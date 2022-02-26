package org.dbs24.tik.mobile.entity.domain;


import lombok.*;
import org.dbs24.tik.mobile.entity.dto.device.AndroidDeviceAttributesDto;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

import static java.time.LocalDateTime.now;

@Data
@Entity
@Table(name = "tm_user_devices_android")
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
@AllArgsConstructor
@NoArgsConstructor
@Builder(setterPrefix = "with")
public class UserDeviceAndroid {
    @Id
    @NotNull
    @Column(name = "device_id")
    @EqualsAndHashCode.Include
    private Integer deviceId;

    @NotNull
    @Column(name = "actual_date")
    private LocalDateTime actualDate;

    @NotNull
    @Column(name = "gsf_id")
    private String gsfId;

    @Column(name = "fcm_token")
    private String fcmToken;

    @Column(name = "secure_id")
    private String secureId;

    @Column(name = "device_fingerprint")
    private String deviceFingerprint;

    @Column(name = "version_sdk_int")
    private String versionSdkInt;

    @Column(name = "version_release")
    private String versionRelease;

    @Column(name = "board")
    private String board;

    @Column(name = "manufacter")
    private String manufacter;

    @Column(name = "supported_abis")
    private String supportedAbis;

    public static UserDeviceAndroid of(AndroidDeviceAttributesDto attributes, Integer deviceId) {
        return UserDeviceAndroid.builder()
                .withDeviceId(deviceId)
                .withActualDate(now())
                .withDeviceFingerprint(attributes.getFingerprint())
                .withGsfId(attributes.getGsfId())
                .withBoard(attributes.getBoard())
                .withFcmToken(attributes.getFcmToken())
                .withManufacter(attributes.getManufacter())
                .withSecureId(attributes.getSecureId())
                .withVersionSdkInt(attributes.getVersionSdkInt())
                .withVersionRelease(attributes.getVersionRelease())
                .withSupportedAbis(attributes.getSupportedAbis())
                .build();
    }
}
