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
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Collection;

import static org.dbs24.consts.SysConst.DATETIME_MS_FORMAT;

@Data
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
public class AppUserInfo {

    @EqualsAndHashCode.Include
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
    private String DeviceFingerprint;
    private String GsfId;
    private String CountryCode;
    private String IpAddRess;

    private Collection<PaymentInfo> payments;
    private Collection<SubscriptionPhoneInfo> subscriptions;
    private Collection<VisitNoteInfo> visitNotes;

}
