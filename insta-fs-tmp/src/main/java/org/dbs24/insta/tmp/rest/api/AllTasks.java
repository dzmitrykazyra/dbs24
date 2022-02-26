package org.dbs24.insta.tmp.rest.api;

import lombok.Data;
import org.dbs24.application.core.service.funcs.ServiceFuncs;

import java.util.Collection;

@Data
public class AllTasks {
    private Collection<TaskInfo> tasks = ServiceFuncs.<TaskInfo>createCollection();
}
