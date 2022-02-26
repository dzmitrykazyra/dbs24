package org.dbs24.google.dao.impl;

import lombok.extern.log4j.Log4j2;
import org.dbs24.google.api.ExecOrderAction;
import org.dbs24.google.dao.NewTasksStorage;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Log4j2
@Component
public class NewTasksStorageImpl implements NewTasksStorage {

    private final List<ExecOrderAction> newTasks = new ArrayList<>();

    @Override
    public void addAll(List<ExecOrderAction> tasksToAdd) {

        newTasks.addAll(tasksToAdd);
    }

    @Override
    public void removeAll(List<ExecOrderAction> tasksToRemove) {
        newTasks.removeAll(tasksToRemove);
    }

    @Override
    public void remove(ExecOrderAction taskToRemove) {

        newTasks.remove(taskToRemove);
    }

    @Override
    public List<ExecOrderAction> getAll() {

        return newTasks;
    }
}
