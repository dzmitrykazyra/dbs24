/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.application.core.thread;

import org.dbs24.application.core.log.LogService;
import org.dbs24.application.core.nullsafe.NullSafe;

/**
 *
 * @author Козыро Дмитрий
 */
@Deprecated
public final class ParallelThreadFlow<T> extends AbstractThreadFlow {

    private T object;

    public ParallelThreadFlow(final T t) {
        this.object = t;
    }

    //==========================================================================
    public final void addParallelThread(final NewThread newThread) {

        final AbstractThread actionThread = newThread.getNewThread();

        //actionThread.setName(String.format("thread %s.%d", this.getClass().getCanonicalName(), threadsList.size() + 1));
        this.getThreadsList().add(actionThread);
        this.setLastThread(actionThread);

        actionThread.startThread();

    }

    //==========================================================================
    @Override
    public void waitForLastThread() {

        String excThreadName = null;

//        if (NullSafe.notNull(getLastThread())) {
//            while (getLastThread().isAlive()) {
//                //LogService.LogInfo(this.getClass(), "dependencyThread.isAlive()");
//                Thread.yield();
//            }
        // ждем завершения всех потоков
        this.getThreadsList()
                .stream()
                .unordered()
                .forEach(thread -> {
                    while (thread.isAlive()) {
                        //LogService.LogInfo(this.getClass(), "dependencyThread.isAlive()");
                        Thread.yield();
                        //thread.setPriority(8);
                    }

                });

        // проверяем, не было ли исключений в потоках
        for (AbstractThread ar : this.getThreadsList()) {

            if ((NullSafe.notNull(ar.getThrowable()))
                    || NullSafe.notNull(ar.getExceptionMsg())) {
                this.setThrowable(ar.getThrowable());
                this.setExceptionMsg(ar.getExceptionMsg());
                excThreadName = ar.getName();
                break;
            }
        }

        //= this.getClass().getSimpleName();
        NullSafe.create(excThreadName)
                .safeExecute((ns_excThreadName) -> {
                    // вывод ошибку наружу, если была
                    this.setIsValid(NullSafe.isNull(this.getThrowable())
                            && NullSafe.isNull(this.getExceptionMsg()));

                    if (!this.getIsValid()) {

                        String errMsg = String.format("%s: (%s) \n",
                                ns_excThreadName,
                                LogService.getCurrentProcName());

                        if (NullSafe.notNull(this.getExceptionMsg())) {
                            errMsg = errMsg.concat(this.getExceptionMsg() + "\n");
                        }
                        if (NullSafe.notNull(this.getThrowable())) {
                            errMsg = errMsg.concat(NullSafe.getErrorMessage(this.getThrowable()) + "\n");
                        }

                        throw new AbstractThreadException(errMsg);

                    }
                }).throwException();
    }

    //==========================================================================
    public T getObject() {
        return object;
    }

    public void setObject(final T object) {
        this.object = object;
    }

}
