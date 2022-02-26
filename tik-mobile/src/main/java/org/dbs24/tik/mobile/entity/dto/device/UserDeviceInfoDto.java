package org.dbs24.tik.mobile.entity.dto.device;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.Data;
import org.dbs24.stmt.StmtProcessor;
import org.dbs24.tik.mobile.entity.domain.DeviceType;
import org.dbs24.tik.mobile.entity.domain.UserDevice;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class UserDeviceInfoDto {

    private Integer deviceId;
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime actualDate;
    private Integer userId;
    private String deviceTypeName;
    private String appName;
    private String appVersion;

    public static UserDeviceInfoDto of(UserDevice userDevice) {

        return StmtProcessor.create(UserDeviceInfoDto.class, info -> {
            info.setDeviceId(userDevice.getDeviceId());
            info.setActualDate(userDevice.getActualDate());
            info.setUserId(userDevice.getUser().getId());
            info.setDeviceTypeName(userDevice.getDeviceType().getDeviceTypeName());
            info.setAppName(userDevice.getAppName());
            info.setAppVersion(userDevice.getAppVersion());
        });
    }
}
