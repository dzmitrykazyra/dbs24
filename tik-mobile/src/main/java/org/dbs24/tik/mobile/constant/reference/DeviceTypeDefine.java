package org.dbs24.tik.mobile.constant.reference;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.dbs24.stmt.StmtProcessor;
import org.dbs24.tik.mobile.entity.domain.DeviceType;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
public enum DeviceTypeDefine {
    DT_ANDROID(1, "DT.ANDROID"),
    DT_IOS(2, "DT.IOS");

    private final Integer deviceTypeId;
    private final String deviceTypeName;

    public static List<DeviceType> getAll() {
        return Arrays.stream(DeviceTypeDefine.values()).map(
                deviceTypeEnum -> StmtProcessor.create(
                        DeviceType.class,
                        deviceType -> {
                            deviceType.setDeviceTypeId(deviceTypeEnum.getDeviceTypeId());
                            deviceType.setDeviceTypeName(deviceTypeEnum.getDeviceTypeName());
                        }
                )
        ).collect(Collectors.toList());
    }
}
