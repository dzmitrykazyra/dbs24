/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.card.pmt.rest.payment.wa.api;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dbs24.spring.core.api.EntityInfo;

@Data
@EqualsAndHashCode
public class CreatedWaPayment implements EntityInfo {

    private Integer createdPaymentId;
}
