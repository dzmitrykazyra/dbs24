/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.proxy.starter;

import javax.annotation.PostConstruct;
import lombok.extern.log4j.Log4j2;
import org.dbs24.spring.core.api.AbstractApplicationService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingClass;
import org.springframework.stereotype.Service;
import java.util.Collection;
import java.util.stream.IntStream;
import org.dbs24.application.core.service.funcs.ServiceFuncs;
import org.dbs24.stmt.StmtProcessor;

@Log4j2
@Service
@ConditionalOnMissingClass("org.dbs24.proxy.core.component.ProxyService")
public class ThreadExecutorService extends AbstractApplicationService {

    @Value("${config.proxy-provider.threads.enable:false}")
    private Boolean threadsEnable;

    @Value("${config.proxy-provider.threads.limit:10000}")
    private Integer threadsLimit;

    @Value("${config.proxy-provider.threads.sleep:200000}")
    private Integer threadSleep;

    final Collection<TaskThread> threads = ServiceFuncs.createCollection();

    //==========================================================================
    @PostConstruct
    public void initializeService() {

        if (false) {

            IntStream.range(0, threadsLimit)
                    .forEach(i -> {
                        log.info("HIII");

                        final TaskThread taskThread = new TaskThread(() -> {

                            while (true) {
                                try {
                                    doSomeTask();
                                } catch (Throwable throwable) {
                                    log.error(throwable.getMessage());
                                    throwable.printStackTrace();
                                }
                                sleep(threadSleep);
                            }
                        });

                        threads.add(taskThread);
                    });

            log.debug("create threads pool: {} threads", threads.size());

            StmtProcessor.runNewThread(() -> threads.stream().forEach(th -> {
                th.startThread();
                sleep(threadSleep / threadsLimit);
                //th.suspend();
            }));
        } else {
            log.info("threads pool is disabled");
        }
    }

    private void sleep(Integer ms) {
        try {
            Thread.sleep(ms);
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }

    @Override
    public void destroy() {
        final String className = this.getClass().getSimpleName();

        threads.stream().forEach(th -> {
            //if (th.isAlive()) {
            log.debug("kill thread {}", th.getName());
            th.interrupt();
            //
        });

        log.info("Service '{}' is destroyed", className.indexOf("$$") > 0 ? className.substring(0, className.indexOf("$$")) : className);
    }

    //==========================================================================
    private void doSomeTask() {
        //log.info("tn size = {}", threads.size());
    }
}
