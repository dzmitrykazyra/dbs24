/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.rest.api;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Deprecated
public class UserInfo {

    private Integer userId;
    private Long actualDate;
    private String loginToken;
    private String countryId;
    private String secretLogin;
    private String userPhoneNum;
    private String userName;
    private String email;

}
