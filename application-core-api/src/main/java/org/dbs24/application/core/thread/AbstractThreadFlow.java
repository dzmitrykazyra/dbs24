/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.application.core.thread;

import org.dbs24.application.core.log.LogService;
import org.dbs24.application.core.nullsafe.NullSafe;
import org.dbs24.application.core.service.funcs.ServiceFuncs;

import java.util.Collection;

/**
 *
 * @author kazyra_d
 */
public abstract class AbstractThreadFlow {

    private Collection<AbstractThread> threadsList;
    private AbstractThread lastThread;
    private Throwable throwable;
    private Boolean isValid;
    private String exceptionMsg;

    public AbstractThreadFlow() {
        super();
    }

    //==========================================================================
    public Collection<AbstractThread> getThreadsList() {

        NullSafe.create(threadsList)
                .whenIsNull(() -> {
                    this.setThreadsList(ServiceFuncs.<AbstractThread>getOrCreateCollection(ServiceFuncs.COLLECTION_NULL));
                });

        return threadsList;
    }

    //==========================================================================
    public void setThreadsList(final Collection<AbstractThread> threadsList) {
        this.threadsList = threadsList;
    }

    //==========================================================================
    public AbstractThread getLastThread() {
        return lastThread;
    }

    //==========================================================================
    public void setLastThread(AbstractThread lastThread) {
        this.lastThread = lastThread;
    }

    //==========================================================================
    protected final void addThread(NewThread newThread) {

        final AbstractThread actionThread = newThread.getNewThread();
        actionThread.setDependencyThread(this.getLastThread());
        //actionThread.setName(String.format("thread %s.%d", this.getClass().getCanonicalName(), threadsList.size() + 1));
        this.getThreadsList().add(actionThread);
        this.setLastThread(actionThread);

        actionThread.startThread();

    }

    //==========================================================================
    public void waitForLastThread() {

        String excThreadName = null;

        if (NullSafe.notNull(getLastThread())) {
            while (getLastThread().isAlive()) {
                //LogService.LogInfo(this.getClass(), "dependencyThread.isAlive()");
                Thread.yield();
            }

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

        } else {
            //LogService.LogInfo(this.getClass(), "dependencyThread is null");
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
        //==========================================================================
    }

    public Throwable getThrowable() {
        return throwable;
    }

    public void setThrowable(final Throwable throwable) {
        this.throwable = throwable;
    }

    public Boolean getIsValid() {
        return isValid;
    }

    public void setIsValid(final Boolean isValid) {
        this.isValid = isValid;
    }

    public String getExceptionMsg() {
        return exceptionMsg;
    }

    public void setExceptionMsg(final String exceptionMsg) {
        this.exceptionMsg = exceptionMsg;
    }

}
