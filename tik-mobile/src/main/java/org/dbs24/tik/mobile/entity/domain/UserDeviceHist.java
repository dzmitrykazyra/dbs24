package org.dbs24.tik.mobile.entity.domain;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;

import static javax.persistence.FetchType.LAZY;

@Entity
@Table(name = "tm_user_devices_hist")
@IdClass(UserDeviceHistPK.class)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(setterPrefix = "with")
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
public class UserDeviceHist {

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

    @ManyToOne(fetch = LAZY)
    @NotNull
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = LAZY)
    @NotNull
    @JoinColumn(name = "device_type_id")
    private DeviceType deviceType;

    @Column(name = "app_name")
    private String appName;

    @Column(name = "app_version")
    private String appVersion;

    public static UserDeviceHist toHist(UserDevice userDevice) {

        return UserDeviceHist.builder()
                .withActualDate(userDevice.getActualDate())
                .withDeviceId(userDevice.getDeviceId())
                .withUser(userDevice.getUser())
                .withAppVersion(userDevice.getAppVersion())
                .withAppName(userDevice.getAppName())
                .withDeviceType(userDevice.getDeviceType())
                .build();
    }

}

@Data
class UserDeviceHistPK implements Serializable {
    private Integer deviceId;
    private LocalDateTime actualDate;
}