/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.rest.api;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
public class ActivitiesRequest {
//    private Integer userId;
//    private String loginToken;
@EqualsAndHashCode.Include
    private Integer subscriptionId;
    private LocalDateTime d1;
    private LocalDateTime d2;   
}
