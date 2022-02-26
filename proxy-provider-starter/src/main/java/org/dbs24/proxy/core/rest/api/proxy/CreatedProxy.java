/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.proxy.core.rest.api.proxy;

import lombok.Data;
import org.dbs24.spring.core.api.EntityInfo;

// Post query - Entity Response

@Data
public class CreatedProxy implements EntityInfo {

    private Integer createdProxyId;
}
