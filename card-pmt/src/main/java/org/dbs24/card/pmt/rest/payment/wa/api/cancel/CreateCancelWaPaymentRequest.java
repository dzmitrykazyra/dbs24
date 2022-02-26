/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.card.pmt.rest.payment.wa.api.cancel;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dbs24.rest.api.action.SimpleActionInfo;
import org.dbs24.spring.core.api.PostRequestBody;

@Data
@EqualsAndHashCode
public class CreateCancelWaPaymentRequest implements PostRequestBody {
    @JsonIgnore
    private SimpleActionInfo entityAction;
    private CancelWaPaymentInfo entityInfo;
}
