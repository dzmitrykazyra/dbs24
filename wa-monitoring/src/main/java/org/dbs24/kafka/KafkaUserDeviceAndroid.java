/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.kafka;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dbs24.application.core.locale.NLS;
import org.dbs24.entity.UserDeviceAndroid;
import org.dbs24.kafka.api.KafkaMessage;

@Data
@EqualsAndHashCode(callSuper = true)
public class KafkaUserDeviceAndroid extends KafkaMessage{
    
    private Integer userId;
    private Integer deviceId;
    private Long actualDate;
    private String gsfId;
    private String fcmToken;
    private String secureId;
    private String deviceFingerprint;
    private String versionSdkInt;
    private String versionRelease;
    private String board;
    private String manufacter;
    private String supportedAbis;
    private String appName;
    
    public void assign(UserDeviceAndroid userDeviceAndroid, Integer userId, String appName) {
        setUserId(userId);
        setDeviceId(userDeviceAndroid.getDeviceId());
        setActualDate(NLS.localDateTime2long(userDeviceAndroid.getActualDate()));
        setGsfId(userDeviceAndroid.getGsfId());
        setFcmToken(userDeviceAndroid.getFcmToken());
        setSecureId(userDeviceAndroid.getSecureId());
        setDeviceFingerprint(userDeviceAndroid.getDeviceFingerprint());
        setVersionSdkInt(userDeviceAndroid.getVersionSdkInt());
        setVersionRelease(userDeviceAndroid.getVersionRelease());
        setBoard(userDeviceAndroid.getBoard());
        setManufacter(userDeviceAndroid.getManufacter());
        setSupportedAbis(userDeviceAndroid.getSupportedAbis());
        setAppName(appName);
    }
}
