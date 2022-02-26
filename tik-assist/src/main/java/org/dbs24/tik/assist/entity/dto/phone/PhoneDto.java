/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.tik.assist.entity.dto.phone;

import lombok.Data;
import org.dbs24.application.core.locale.NLS;
import org.dbs24.stmt.StmtProcessor;
import org.dbs24.tik.assist.entity.domain.Phone;

@Data
public class PhoneDto {

    private Integer phoneId;
    private Long actualDate;
    private Integer phoneStatusId;
    private String deviceId;
    private String installId;
    private byte[] apkAttrs;
    private String apkHashId;

    public static PhoneDto toPhoneDto(Phone phone) {

        return StmtProcessor.create(
                PhoneDto.class,
                phoneDto -> {
                    phoneDto.setPhoneId(phone.getPhoneId());
                    phoneDto.setActualDate(NLS.localDateTime2long(phone.getActualDate()));
                    phoneDto.setPhoneStatusId(phone.getPhoneStatus().getPhoneStatusId());
                    phoneDto.setDeviceId(phone.getDeviceId());
                    phoneDto.setInstallId(phone.getInstallId());
                    phoneDto.setApkAttrs(phone.getApkAttrs());
                    phoneDto.setApkHashId(phone.getApkHashId());
                }
        );
    }
}
