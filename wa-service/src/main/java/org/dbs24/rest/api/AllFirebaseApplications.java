/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.rest.api;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dbs24.application.core.service.funcs.ServiceFuncs;

import java.util.Collection;

@Data
@EqualsAndHashCode
public class AllFirebaseApplications {

    private Collection<FireBaseApplicationInfo> apps = ServiceFuncs.<FireBaseApplicationInfo>createCollection();
}
