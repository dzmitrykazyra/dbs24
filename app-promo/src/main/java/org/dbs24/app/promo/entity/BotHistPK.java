package org.dbs24.app.promo.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode
public class BotHistPK implements Serializable {

    private Integer botId;
    private LocalDateTime actualDate;

}
