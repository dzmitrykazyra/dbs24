package org.dbs24.tik.dev.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode
public class TariffPlanPriceHistPK implements Serializable {

    private Long tariffPriceId;
    private LocalDateTime actualDate;

}
