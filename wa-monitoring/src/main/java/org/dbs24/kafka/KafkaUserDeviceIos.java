/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.kafka;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dbs24.application.core.locale.NLS;
import org.dbs24.entity.UserDeviceIos;
import org.dbs24.kafka.api.KafkaMessage;

@Data
@EqualsAndHashCode(callSuper = true)
public class KafkaUserDeviceIos extends KafkaMessage {

    private Integer userId;
    private Integer deviceId;
    private Long actualDate;
    private String identifierForVendor;
    private String systemVersion;
    private String model;
    private String ustnameRelease;
    private String ustnameVersion;
    private String ustnameMachine;
    private String icmToken;
    private String appName;    

    public void assign(UserDeviceIos userDeviceIos, Integer userId, String appName) {
        setUserId(userId);
        setDeviceId(userDeviceIos.getDeviceId());
        setActualDate(NLS.localDateTime2long(userDeviceIos.getActualDate()));
        setIdentifierForVendor(userDeviceIos.getIdentifierForVendor());
        setSystemVersion(userDeviceIos.getSystemVersion());
        setModel(userDeviceIos.getModel());
        setUstnameRelease(userDeviceIos.getUstnameRelease());
        setUstnameVersion(userDeviceIos.getUstnameVersion());
        setUstnameMachine(userDeviceIos.getUstnameMachine());
        setIcmToken(userDeviceIos.getIcmToken());
        setAppName(appName);
    }
}
