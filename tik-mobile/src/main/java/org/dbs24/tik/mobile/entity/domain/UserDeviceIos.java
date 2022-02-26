package org.dbs24.tik.mobile.entity.domain;

import lombok.*;
import org.dbs24.tik.mobile.entity.dto.device.IosDeviceAttributesDto;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

import static java.time.LocalDateTime.now;

@Data
@Entity
@Table(name = "tm_user_devices_ios")
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
@AllArgsConstructor
@NoArgsConstructor
@Builder(setterPrefix = "with")
public class UserDeviceIos {

    @Id
    @NotNull
    @Column(name = "device_id")
    @EqualsAndHashCode.Include
    private Integer deviceId;

    @NotNull
    @Column(name = "actual_date")
    private LocalDateTime actualDate;

    @NotNull
    @Column(name = "apple_id")
    private String appleId;

    @NotNull
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

    public static UserDeviceIos of(IosDeviceAttributesDto attributes, Integer deviceId) {

        return UserDeviceIos.builder()
                .withDeviceId(deviceId)
                .withActualDate(now())
                .withIdentifierForVendor(attributes.getIdentifierForVendor())
                .withAppleId(attributes.getAppleId())
                .withModel(attributes.getModel())
                .withIcmToken(attributes.getIcmToken())
                .withSystemVersion(attributes.getSystemVersion())
                .withUstnameMachine(attributes.getUstnameMachine())
                .withUstnameRelease(attributes.getUstnameRelease())
                .withUstnameVersion(attributes.getUstnameVersion())
                .build();
    }
}
