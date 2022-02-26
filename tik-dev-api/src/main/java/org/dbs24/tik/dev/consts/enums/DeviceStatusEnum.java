package org.dbs24.tik.dev.consts.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.dbs24.application.core.service.funcs.ServiceFuncs;
import org.dbs24.tik.dev.entity.reference.DeviceStatus;

import java.util.Arrays;
import java.util.Collection;

import static org.dbs24.stmt.StmtProcessor.create;


@Getter
@AllArgsConstructor
public enum DeviceStatusEnum {

    DC_ACTIVE(10, "DS.ACTIVE"),
    DC_SHARED(100, "DS.SHARED"),
    DC_BLOCKED(-1, "DS.BLOCKED");

    public static final Collection<DeviceStatus> DEVICE_STATUSES_LIST = ServiceFuncs.<DeviceStatus>createCollection(cp -> Arrays.stream(DeviceStatusEnum.values())
            .map(stringRow -> create(DeviceStatus.class, ref -> {
                ref.setDeviceStatusId(stringRow.getDeviceStatusId());
                ref.setDeviceStatusName(stringRow.getDeviceStatusName());
            })).forEach(cp::add)
    );
    public static final Collection<Integer> DEVICES_STATUSES_LIST_IDS = ServiceFuncs.<Integer>createCollection(cp -> DEVICE_STATUSES_LIST.forEach(ref -> cp.add(ref.getDeviceStatusId())));
    private final Integer deviceStatusId;
    private final String deviceStatusName;

}