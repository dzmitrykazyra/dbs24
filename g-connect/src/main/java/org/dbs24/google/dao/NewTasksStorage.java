package org.dbs24.google.dao;

import org.dbs24.google.api.ExecOrderAction;

import java.util.List;

public interface NewTasksStorage {

    void addAll(List<ExecOrderAction> tasksToAdd);

    void removeAll(List<ExecOrderAction> tasksToRemove);

    void remove(ExecOrderAction taskToRemove);

    List<ExecOrderAction> getAll();
}
