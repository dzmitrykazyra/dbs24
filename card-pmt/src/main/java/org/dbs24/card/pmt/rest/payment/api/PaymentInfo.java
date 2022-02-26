/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.card.pmt.rest.payment.api;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dbs24.spring.core.api.EntityInfo;

import java.math.BigDecimal;

@Data
@EqualsAndHashCode
public class PaymentInfo implements EntityInfo {
    
    private Integer paymentId;
    private Long paymentDate;
    private Integer applicationId;
    private Integer paymentStatusId;
    private Integer paymentServiceId;
    private String currencyIso;
    private String countryCode;
    private BigDecimal paymentSumm;
    private BigDecimal paymentSummEqu;
    private String pmtNote;
    private String tag;    

    //public assign
    
}
