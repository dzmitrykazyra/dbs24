/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.rest.api;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
@NoArgsConstructor
public class UserContractFromPaymentInfo {

    @EqualsAndHashCode.Include
    private String loginToken;
    private Long beginDate;
    private Long endDate;
    private Integer subscriptionsAmount;
    private Integer contractTypeId;
}
