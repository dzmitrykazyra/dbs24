/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.application.core.thread;

import org.dbs24.application.core.nullsafe.NullSafe;
import static org.dbs24.consts.SysConst.*;

/**
 *
 * @author kazyra_d
 */
public abstract class AbstractThread extends Thread {

    private Integer order;
    private AbstractThread dependencyThread;
    private Boolean isValid;
    private String exceptionMsg;
    private Throwable throwable;

    public AbstractThread() {
        super();
    }

    public AbstractThread(Runnable target) {
        super(target);
    }

    public AbstractThread( ThreadGroup tg, String threadName) {

        super(tg, threadName);
        //this.startThread();

    }
//    public AbstractThread(AbstractThread dependencyThread) {
//        this();
//        this.dependencyThread = dependencyThread;
//    }
    //==========================================================================

    public void startThread() {

        // запуск потока
        this.start();
        //this.run();

    }

    public void beforeRunThread() {
        this.waitForParentThread();

        //LogService.LogInfo(this.getClass(), String.format("run thread (%s)", this.getName()));
    }

    protected void finishThread() {

        //LogService.LogInfo(this.getClass(), String.format("finish thread (%s)", this.getName()));
    }

    //==========================================================================
    protected void waitForParentThread() {
        if (NullSafe.notNull(dependencyThread)) {
            while (dependencyThread.isAlive()) {
                //               LogService.LogInfo(this.getClass(), "dependencyThread.isAlive()");
                Thread.yield();
            }
        } else {
            //LogService.LogInfo(this.getClass(), "dependencyThread is null");
        }

    }

    protected Boolean canRun() {
        Boolean canRun = BOOLEAN_TRUE;

        if (NullSafe.notNull(dependencyThread)) {
            canRun = dependencyThread.getIsValid();
        }

        return canRun;

    }

    //==========================================================================
    protected final void internalRun(ThreadExecutor threadExecutor) {

        this.beforeRunThread();

        if (this.canRun()) {

            final NullSafe nullSafe = NullSafe.create()
                    .execute(() -> {
                        threadExecutor.internalRun();
                    });

            setIsValid(!nullSafe.getExceptionFlag());

            if (!getIsValid()) {
                setExceptionMsg(nullSafe.getExceptionMsg());
                this.setThrowable(nullSafe.getThrowable());
            }
        }

        this.finishThread();
    }

    //==========================================================================
    public Integer getOrder() {
        return order;
    }

    public void setOrder( Integer order) {
        this.order = order;
    }

    public AbstractThread getDependencyThread() {
        return dependencyThread;
    }

    public void setDependencyThread(AbstractThread dependencyThread) {
        this.dependencyThread = dependencyThread;
    }

    public Boolean getIsValid() {
        return isValid;
    }

    public void setIsValid( Boolean isValid) {
        this.isValid = isValid;
    }

    public String getExceptionMsg() {
        return exceptionMsg;
    }

    public void setExceptionMsg( String exceptionMsg) {
        this.exceptionMsg = exceptionMsg;
    }

    public Throwable getThrowable() {
        return throwable;
    }

    public void setThrowable( Throwable throwable) {
        this.throwable = throwable;
    }
}
