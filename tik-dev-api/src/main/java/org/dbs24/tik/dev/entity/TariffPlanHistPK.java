package org.dbs24.tik.dev.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode
public class TariffPlanHistPK implements Serializable {

    private Long tariffPlanId;
    private LocalDateTime actualDate;

}
