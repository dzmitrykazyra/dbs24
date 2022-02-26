package org.dbs24.google.service;

import org.dbs24.google.api.ExecOrderAction;
import org.dbs24.google.api.OrderActionResult;
import org.dbs24.google.entity.dto.proxy.Proxy;

public interface TaskService {

    OrderActionResult execute(ExecOrderAction taskToExecute);
}
