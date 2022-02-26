package org.dbs24.tik.mobile.entity.dto.device;

import lombok.Data;
import org.dbs24.tik.mobile.entity.domain.UserDevice;

import java.util.List;

@Data
public class UserDeviceListDto {

    private List<UserDeviceInfoDto> userDevices;

}
