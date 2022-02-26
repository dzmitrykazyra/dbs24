package org.dbs24.tik.mobile.entity.domain;




import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;


@Data
@Entity
@IdClass(UserDeviceIosHistPK.class)
@Table(name = "tm_user_devices_ios_hist")
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
@AllArgsConstructor
@NoArgsConstructor
@Builder(setterPrefix = "with")
public class UserDeviceIosHist {

    @Id
    @NotNull
    @Column(name = "device_id")
    @EqualsAndHashCode.Include
    private Integer deviceId;

    @Id
    @NotNull
    @Column(name = "actual_date")
    @EqualsAndHashCode.Include
    private LocalDateTime actualDate;

    @Column(name = "apple_id")
    private String appleId;

    @Column(name = "identifier_for_vendor")
    private String identifierForVendor;

    @Column(name = "system_version")
    private String systemVersion;

    @Column(name = "model")
    private String model;

    @Column(name = "ustname_release")
    private String ustnameRelease;

    @Column(name = "ustname_version")
    private String ustnameVersion;

    @Column(name = "ustname_machine")
    private String ustnameMachine;

    @Column(name = "icm_token")
    private String icmToken;

    public static UserDeviceIosHist toHist(UserDeviceIos userDeviceIos) {

        return UserDeviceIosHist.builder()
                .withDeviceId(userDeviceIos.getDeviceId())
                .withActualDate(userDeviceIos.getActualDate())
                .withAppleId(userDeviceIos.getAppleId())
                .withIdentifierForVendor(userDeviceIos.getIdentifierForVendor())
                .withSystemVersion(userDeviceIos.getSystemVersion())
                .withModel(userDeviceIos.getModel())
                .withUstnameRelease(userDeviceIos.getUstnameRelease())
                .withUstnameVersion(userDeviceIos.getUstnameVersion())
                .withUstnameMachine(userDeviceIos.getUstnameMachine())
                .withIcmToken(userDeviceIos.getIcmToken())
                .build();
    }

}

class UserDeviceIosHistPK implements Serializable {
    private Integer deviceId;
    private LocalDateTime actualDate;
}