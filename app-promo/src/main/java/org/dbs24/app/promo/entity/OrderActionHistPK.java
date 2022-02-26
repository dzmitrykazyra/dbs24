package org.dbs24.app.promo.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode
public class OrderActionHistPK implements Serializable {

    private Integer actionId;
    private LocalDateTime actualDate;

}
