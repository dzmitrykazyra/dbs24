package org.dbs24.google.dao.impl;

import lombok.extern.log4j.Log4j2;
import org.dbs24.application.core.service.funcs.ServiceFuncs;
import org.dbs24.google.api.ExecOrderAction;
import org.dbs24.google.dao.InProgressTasksStorage;
import org.springframework.stereotype.Component;
import java.util.Collection;
import java.util.List;

@Log4j2
@Component
public class InProgressTasksStorageImpl implements InProgressTasksStorage {

    private final Collection<ExecOrderAction> tasksInProgress = ServiceFuncs.createConcurencyCollection();

    @Override
    public void add(ExecOrderAction taskToAdd) {

        tasksInProgress.add(taskToAdd);
    }

    @Override
    public void addAll(List<ExecOrderAction> tasksToAdd) {

        tasksInProgress.addAll(tasksToAdd);
    }

    @Override
    public void removeAll(List<ExecOrderAction> tasksToRemove) {

        tasksInProgress.removeAll(tasksToRemove);
    }
}