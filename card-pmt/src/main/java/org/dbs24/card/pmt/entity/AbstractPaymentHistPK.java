package org.dbs24.card.pmt.entity;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class AbstractPaymentHistPK implements Serializable {

    private Integer paymentId;
    private LocalDateTime actualDate;

}
