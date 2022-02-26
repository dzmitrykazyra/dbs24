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
@EqualsAndHashCode
public class UserTokenInfo {
    private Long tokenId;
    private LocalDateTime validDate;
    //private String token;
    private Integer userId;
    private Boolean isValid;
}
