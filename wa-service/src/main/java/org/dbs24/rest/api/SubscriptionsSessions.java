package org.dbs24.rest.api;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dbs24.application.core.service.funcs.ServiceFuncs;

import java.util.Collection;

@Data
@EqualsAndHashCode
public class SubscriptionsSessions {
    private Collection<SubscriptionSessionInfo> sessions = ServiceFuncs.createCollection();
}
