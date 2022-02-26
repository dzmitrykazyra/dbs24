/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.card.pmt.rest.payment.wa.api;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@EqualsAndHashCode
public class UserContractFromPaymentInfo {

    private String loginToken;
    private Long beginDate;
    private Long endDate;
    private Integer contractTypeId;
    private Integer subscriptionsAmount;

}
