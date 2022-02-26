/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.app.promo.exec;

import lombok.extern.log4j.Log4j2;
import org.dbs24.stmt.StmtProcessor;

import java.util.Collection;
import java.util.stream.IntStream;

import static org.dbs24.application.core.service.funcs.ServiceFuncs.createCollection;
import static org.dbs24.consts.SysConst.BOOLEAN_FALSE;
import static org.dbs24.consts.SysConst.BOOLEAN_TRUE;

@Log4j2
public class ThreadExecutorService {

    final Integer threadsLimit;
    final Integer threadSleep;
    final String executorName;
    final TaskProcessor taskProcessor;

    private Boolean stop = BOOLEAN_FALSE;

    final Collection<TaskThread> threads = createCollection();

    public static ThreadExecutorService create(String executorName, Integer threadsLimit, Integer threadSleep, TaskProcessor taskProcessor) {
        return new ThreadExecutorService(executorName, threadsLimit, threadSleep, taskProcessor);
    }

    public ThreadExecutorService(String executorName, Integer threadsLimit, Integer threadSleep, TaskProcessor taskProcessor) {
        this.executorName = executorName;
        this.taskProcessor = taskProcessor;
        this.threadsLimit = threadsLimit;
        this.threadSleep = threadSleep;

        final Runnable target = () -> {

            while (!stop) {
                try {
                    taskProcessor.execute();
                } catch (Throwable throwable) {
                    log.error("ThreadExecutorService<{}>: {}",
                            executorName,
                            throwable.getMessage());
                    throwable.printStackTrace();
                }
                sleep(threadSleep);
            }
        };

        IntStream.range(0, threadsLimit)
                .forEach(i -> {
                    final TaskThread taskThread = new TaskThread(target);

                    taskThread.setName(String.format("%s#%d", executorName, i));

                    threads.add(taskThread);
                });

        log.debug("create threads pool: {} threads", threads.size());

        StmtProcessor.runNewThread(() -> threads.stream().forEach(th -> {
            th.startThread();
            sleep(threadSleep / threadsLimit);
            //th.suspend();
        }));

    }

    private void sleep(Integer ms) {
        try {
            Thread.sleep(ms);
        } catch (Throwable throwable) {
            if (!stop) {
                throwable.printStackTrace();
            }
        }
    }


    public void destroy() {

        stop = BOOLEAN_TRUE;

        StmtProcessor.runNewThread(() -> {

            final String className = this.getClass().getSimpleName();

            threads.stream().forEach(th -> {
                //if (th.isAlive()) {
                log.debug("kill thread {}", th.getName());
                try {
                    th.interrupt();
                } catch (Throwable throwable) {
                    log.warn(throwable.getMessage());
                }
            });

            log.info("Service '{}' is destroyed", className.indexOf("$$") > 0 ? className.substring(0, className.indexOf("$$")) : className);

        });
    }
}
