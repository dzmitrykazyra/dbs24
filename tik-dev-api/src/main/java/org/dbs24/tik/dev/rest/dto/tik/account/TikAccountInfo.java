/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.tik.dev.rest.dto.tik.account;

import lombok.Data;
import org.dbs24.spring.core.api.EntityInfo;

@Data
public class TikAccountInfo implements EntityInfo {

    private Long tikAccountId;
    private Long actualDate;
    private Integer tikAccountStatusId;
    private Long developerId;
    private String tikLogin;
    private String tikPwd;
    private String tikEmail;
    private String tikAuthKey;

}
