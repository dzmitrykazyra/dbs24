/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.rest.api;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.Collection;
import static org.dbs24.consts.SysConst.DATETIME_MS_FORMAT;
import org.dbs24.entity.AppUser;

@Data
@NoArgsConstructor
public class AppUserInfo {

    private Integer Id;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DATETIME_MS_FORMAT)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime regTime;
    private String AuthToken;
    private String GcmToken;
    private String AppName;
    private String AppVersion;
    private String AndroidSecureId;
    private String DeviceFingerPrint;
    private String GsfId;
    private String CountryCode;
    private String IpAddress;

    private Collection<PaymentInfo> payments;
    private Collection<SubscriptionPhoneInfo> subscriptions;
    private Collection<VisitNoteInfo> visitNotes;

    public void assign(AppUser appUser) {
        this.AndroidSecureId = appUser.getAndriodSecureId();
        this.AppName = appUser.getAppName();
        this.AppVersion = appUser.getAppVersion();
        this.AuthToken = appUser.getAuthToken();
        this.CountryCode = appUser.getCountryCode();
        this.DeviceFingerPrint = appUser.getDeviceFingerPring();
        this.GcmToken = appUser.getGcmTokeN();
        this.GsfId = appUser.getGsfId();
        this.Id = appUser.getId();
        this.IpAddress = appUser.getIpAddress();
        this.regTime = appUser.getRegTime();
    }
}
