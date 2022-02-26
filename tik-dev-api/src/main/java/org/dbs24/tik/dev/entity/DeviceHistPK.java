package org.dbs24.tik.dev.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode
public class DeviceHistPK implements Serializable {

    private Long deviceId;
    private LocalDateTime actualDate;

}
