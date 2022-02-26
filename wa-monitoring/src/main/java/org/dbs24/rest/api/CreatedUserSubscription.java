/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.rest.api;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dbs24.rest.api.dto.OperationResult;

@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
public class CreatedUserSubscription extends OperationResult {

    @EqualsAndHashCode.Include
    private Integer subscriptionId;
    private Long allowedActionDate;
}
