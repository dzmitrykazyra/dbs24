package org.dbs24.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode
public class TariffPlanHistPK implements Serializable {

    private Integer tariffPlanId;
    private LocalDateTime actualDate;
}
