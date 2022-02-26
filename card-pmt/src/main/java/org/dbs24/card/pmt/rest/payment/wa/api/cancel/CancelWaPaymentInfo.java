package org.dbs24.card.pmt.rest.payment.wa.api.cancel;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dbs24.spring.core.api.EntityInfo;

@Data
@EqualsAndHashCode
public class CancelWaPaymentInfo implements EntityInfo {

    private Integer paymentId;
    @JsonProperty("google_order_id")
    private String googleOrderId;
    @JsonProperty("apple_transaction_id")
    private String appleTransactionId;
    private String cancelNote;
}
