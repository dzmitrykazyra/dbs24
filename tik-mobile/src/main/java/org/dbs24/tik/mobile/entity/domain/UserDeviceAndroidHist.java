package org.dbs24.tik.mobile.entity.domain;

import lombok.*;
import org.dbs24.application.core.api.ObjectRoot;
import org.dbs24.persistence.api.PersistenceEntity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Entity
@IdClass(UserDeviceAndroidHistPK.class)
@Table(name = "tm_user_devices_android_hist")
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
@AllArgsConstructor
@NoArgsConstructor
@Builder(setterPrefix = "with")
public class UserDeviceAndroidHist {

    @Id
    @NotNull
    @Column(name = "device_id", updatable = false)
    @EqualsAndHashCode.Include
    private Integer deviceId;

    @Id
    @NotNull
    @Column(name = "actual_date")
    @EqualsAndHashCode.Include
    private LocalDateTime actualDate;

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

    public static UserDeviceAndroidHist toHist(UserDeviceAndroid userDeviceAndroid) {

        return UserDeviceAndroidHist.builder()
                .withDeviceId(userDeviceAndroid.getDeviceId())
                .withActualDate(userDeviceAndroid.getActualDate())
                .withGsfId(userDeviceAndroid.getGsfId())
                .withFcmToken(userDeviceAndroid.getFcmToken())
                .withSecureId(userDeviceAndroid.getSecureId())
                .withDeviceFingerprint(userDeviceAndroid.getDeviceFingerprint())
                .withVersionSdkInt(userDeviceAndroid.getVersionSdkInt())
                .withVersionRelease(userDeviceAndroid.getVersionRelease())
                .withBoard(userDeviceAndroid.getBoard())
                .withManufacter(userDeviceAndroid.getManufacter())
                .withSupportedAbis(userDeviceAndroid.getSupportedAbis())
                .build();

    }

}

class UserDeviceAndroidHistPK implements Serializable {
    private Integer deviceId;
    private LocalDateTime actualDate;
}