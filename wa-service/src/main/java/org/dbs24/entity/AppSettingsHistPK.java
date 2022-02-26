package org.dbs24.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode
public class AppSettingsHistPK implements Serializable {

    private Integer appSettingId;
    private LocalDateTime actualDate;

}
