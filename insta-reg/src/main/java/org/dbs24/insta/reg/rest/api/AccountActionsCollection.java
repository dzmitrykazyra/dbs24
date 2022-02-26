/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.insta.reg.rest.api;

import java.util.Collection;
import lombok.Data;
import org.dbs24.application.core.service.funcs.ServiceFuncs;

@Data
public class AccountActionsCollection {

    private Collection<ExceptionInfo> actions = ServiceFuncs.<ExceptionInfo>createCollection();
}
