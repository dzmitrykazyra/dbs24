/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.spring.boot.api;

/**
 *
 * @author Козыро Дмитрий
 */
import lombok.extern.slf4j.Slf4j;
import org.dbs24.application.core.nullsafe.NullSafe;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;
import org.dbs24.spring.core.api.ApplicationConfiguration;
import org.dbs24.application.core.nullsafe.StopWatcher;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

@Slf4j
public abstract class AbstractSpringBootApplication {

    static ApplicationContext applicationContext;

    public static final SpringBootInititializer EMPTY_INITIALIZATION = () -> {
    };

    //==========================================================================
    public static void runSpringBootApplication(
            String[] args,
            Class<?> springBootClass,
            SpringBootInititializer sbi) {

        final String applicationName = springBootClass.getCanonicalName();
        final StopWatcher stopWatcher = StopWatcher.create(String.format("%s loading ", applicationName));

        log.info("TRY 2 START {} ", applicationName);
        NullSafe.create()
                .execute(() -> {

                    SpringApplication.run(springBootClass, args);
                    sbi.initialize();
                    log.info("APPLICATION SUCCESSFULLY STARTED {} ", applicationName);
                })
                .catchException(e -> {
                    log.error("ERROR STARTING APPLICATION '{}', reason: '{}' ", applicationName, e.getLocalizedMessage());
                    log.error(stopWatcher.getStringExecutionTime());
                    log.error("{} exception \n {}: '{}'",
                            applicationName,
                            e.getClass().getCanonicalName(),
                            e.getLocalizedMessage());
                    log.error("{} APPLICATION IS STOPPED", applicationName);
                    System.exit(-1);
                })
                .finallyBlock(() -> {
                    log.debug("finally initialization");
                    log.info(stopWatcher.getStringExecutionTime());
                });
    }

    //==========================================================================
    public static void initializeContext(final Class<? extends ApplicationConfiguration> clazz) {
        AbstractSpringBootApplication.applicationContext = new AnnotationConfigApplicationContext(clazz);
    }

    public static ApplicationContext getApplicationContext() {
        return AbstractSpringBootApplication.applicationContext;
    }
}
