package org.dbs24.app.promo.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode
public class BatchSetupHistPK implements Serializable {

    private Integer batchSetupId;
    private LocalDateTime actualDate;

}
