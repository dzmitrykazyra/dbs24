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
import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;
import org.dbs24.spring.core.api.ApplicationConfiguration;
import org.dbs24.application.core.nullsafe.StopWatcher;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

@Slf4j
public abstract class AbstractSpringBootApplication {

    public static ApplicationContext applicationContext;

    public static final SpringBootInititializer NO_INITIALIZATION = () -> {
    };

    public static void runSpringBootApplication(
            String[] args,
            Class springBootClass,
            SpringBootInititializer sbi) {

        final StopWatcher stopWatcher = StopWatcher.create(String.format("%s", springBootClass.getCanonicalName()));

        SpringApplication.run(springBootClass, args);

        sbi.initialize();

        log.info(stopWatcher.getStringExecutionTime());

    }

    public static void initializeContext(final Class<? extends ApplicationConfiguration> clazz) {
        // AbstractSpringBootApplication.ctx = NullSafe.createObject(AnnotationConfigApplicationContext.class, clazz);
        AbstractSpringBootApplication.applicationContext = new AnnotationConfigApplicationContext(clazz);
    }

    public static ApplicationContext getApplicationContext() {
        return AbstractSpringBootApplication.applicationContext;
    }
}
