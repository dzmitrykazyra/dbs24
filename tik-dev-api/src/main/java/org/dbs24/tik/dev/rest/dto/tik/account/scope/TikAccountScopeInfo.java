/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.tik.dev.rest.dto.tik.account.scope;

import lombok.Data;
import org.dbs24.spring.core.api.EntityInfo;

@Data
public class TikAccountScopeInfo implements EntityInfo {

    private Long grantId;
    private Long grantDate;
    private Long tikAccountId;
    private Integer endpointScopeId;
    private Boolean isGranted;

}
