package org.dbs24.insta.tmp.rest.api;

import lombok.Data;
import org.dbs24.application.core.service.funcs.ServiceFuncs;

import java.util.Collection;

@Data
public class AllAccounts {

    private Collection<AccountInfo> accounts = ServiceFuncs.<AccountInfo>createCollection();

}
