/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.card.pmt.rest.payment.api;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dbs24.rest.api.action.SimpleActionInfo;
import org.dbs24.spring.core.api.PostRequestBody;

@Data
@EqualsAndHashCode
public class CreatePaymentRequest implements PostRequestBody {

    private SimpleActionInfo entityAction;
    private PaymentInfo entityInfo;

}
