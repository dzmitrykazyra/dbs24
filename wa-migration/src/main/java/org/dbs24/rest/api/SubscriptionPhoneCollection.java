/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.rest.api;

import java.util.Collection;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.dbs24.application.core.service.funcs.ServiceFuncs;


@Data
@NoArgsConstructor
public class SubscriptionPhoneCollection {
    private Collection<SubscriptionPhoneInfo> collection = ServiceFuncs.<SubscriptionPhoneInfo>createCollection();
}
