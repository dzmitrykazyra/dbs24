/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.spring.boot.api;

import java.util.Arrays;
import javax.annotation.PreDestroy;
import lombok.extern.log4j.Log4j2;
import static org.dbs24.consts.SysConst.*;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;
import org.dbs24.spring.core.api.ApplicationConfiguration;
import org.dbs24.application.core.nullsafe.StopWatcher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import reactor.core.publisher.Mono;
import org.dbs24.application.core.service.funcs.SysEnvFuncs;
import org.dbs24.stmt.StmtProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.util.Assert;
import org.springframework.web.reactive.config.WebFluxConfigurationSupport;
import org.dbs24.service.MonitoringRSocketService;
import static org.dbs24.rsocket.api.MessageType.*;

@Log4j2
public abstract class AbstractSpringBootApplication {

    static String userApplicationName;
    static Boolean isRunning = BOOLEAN_FALSE;

    final StopWatcher stopWatcher = StopWatcher.create("Server uptime");

    public static ApplicationContext applicationContext;
    public static GenericApplicationContext genericApplicationContext;
    public static WebFluxConfigurationSupport webFluxConfigurationSupport;
    public static MonitoringRSocketService monitoringRSocketService;

    //==========================================================================    
    public static final SpringBootInititializer EMPTY_INITIALIZATION = () -> {
    };

    @Autowired
    public void initializeGenericContext(
            ApplicationContext applicationContext,
            GenericApplicationContext genericApplicationContext,
            WebFluxConfigurationSupport webFluxConfigurationSupport,
            MonitoringRSocketService monitoringRSocketService) {
        AbstractSpringBootApplication.applicationContext = applicationContext;
        AbstractSpringBootApplication.genericApplicationContext = genericApplicationContext;
        AbstractSpringBootApplication.webFluxConfigurationSupport = webFluxConfigurationSupport;
        AbstractSpringBootApplication.monitoringRSocketService = monitoringRSocketService;
        this.initializeSpringBootApplication();
        log.info("{}: application is initialized) ", this.getClass().getSimpleName());
    }

    @Value("${spring.application.name:application name not defined}")
    public void initializeApplicationName(String userApplicationName) {
        AbstractSpringBootApplication.userApplicationName
                = StmtProcessor.isNull(userApplicationName)
                ? NOT_DEFINED : userApplicationName;
    }

    //==========================================================================
    public static void runSpringBootApplication(
            String[] args,
            Class<?> springBootClass,
            SpringBootInititializer sbi) {

        log.info(SysEnvFuncs.getSysEnvFuns());
        log.info(SysEnvFuncs.getSysProperties());

        final String applicationName = springBootClass.getCanonicalName();

        Mono.just(springBootClass)
                .log()
                .subscribe(new Subscriber<Class<?>>() {

                    private StopWatcher stopWatcher;

                    @Override
                    public void onSubscribe(Subscription s) {
                        s.request(1);
                        this.stopWatcher = StopWatcher.create();
                        log.info("Initializing {} ...", applicationName);
                    }

                    @Override
                    public void onNext(Class<?> clazz) {
                        SpringApplication.run(clazz, args);
                        sbi.initialize();

                        this.validateNullProperty(clazz, AbstractSpringBootApplication.applicationContext, "AbstractSpringBootApplication.applicationContext");
                        this.validateNullProperty(clazz, AbstractSpringBootApplication.genericApplicationContext, "AbstractSpringBootApplication.genericApplicationContext");
                        this.validateNullProperty(clazz, AbstractSpringBootApplication.webFluxConfigurationSupport, "AbstractSpringBootApplication.webFluxConfigurationSupport");

                        log.info("SUCCESSFULLY STARTED {} ", applicationName);

                        genericApplicationContext.setDisplayName(springBootClass.getSimpleName());

                        monitoringRSocketService.send(MONITORING_START_APPLICATION, "SUCCESSFULLY STARTED " + applicationName);

                        isRunning = BOOLEAN_TRUE;
                    }

                    private void validateNullProperty(Class<?> clazz, Object object, String errMsg) {
                        Assert.notNull(object, String.format("%s: fucking %s is null! (check spring.main.lazy-initialization=false)", clazz.getSimpleName(), errMsg));
                    }

                    @Override
                    public void onError(Throwable t) {

                        log.error("ERROR STARTING '{}' ('{}'), reason: '{}' ",
                                applicationName,
                                AbstractSpringBootApplication.userApplicationName,
                                t.getLocalizedMessage());
                        log.error(stopWatcher.getStringExecutionTime());
                        log.error("{} exception \n {}: '{}'",
                                applicationName,
                                t.getClass().getCanonicalName(),
                                t.getLocalizedMessage());

                        monitoringRSocketService.send(MONITORING_ERROR_STARTING_APPLICATION, t.getLocalizedMessage());

                    }

                    @Override
                    public void onComplete() {
                        log.info("{}", SysEnvFuncs.getMemoryStatistics());
                        log.debug("finally initialization {}", applicationName);
                        log.info(stopWatcher.getStringExecutionTime());
                        //log.debug("Spring beans definitions: {}", getBeansDefs());
                        monitoringRSocketService.send(MONITORING_SUCCESS_STARTING_APPLICATION, SysEnvFuncs.getMemoryStatistics());
                    }
                });

        if (!isRunning) {

            log.info("SHOUTDOWN APPLICATION {}", applicationName);
            monitoringRSocketService.send(MONITORING_ERROR_STARTING_APPLICATION, applicationName + " is stopped");

            new Thread(() -> {
                StmtProcessor.execute(() -> Thread.sleep(2000));
                //SpringApplication.exit(applicationContext);
                System.exit(-1);
            }).start();
        }
    }

    //==========================================================================
    public static void initializeContext(Class<? extends ApplicationConfiguration> clazz) {
        AbstractSpringBootApplication.applicationContext = new AnnotationConfigApplicationContext(clazz);
        log.info("Application is initialized '{}') ", clazz.getCanonicalName());
    }

    //==========================================================================
    public static ApplicationContext getApplicationContext() {
        return AbstractSpringBootApplication.applicationContext;
    }

    //==========================================================================
    @PreDestroy
    protected void destroyApplication() {

        log.info("{}", SysEnvFuncs.getMemoryStatistics());

        final String applicationName = this.getClass().getCanonicalName();
        final String logMsg = String.format("STOPPED (%s, %s) ", applicationName,
                AbstractSpringBootApplication.userApplicationName);
        log.info(logMsg);

        monitoringRSocketService.send(MONITORING_FINISH_APPLICATION, logMsg);
        monitoringRSocketService.send(MONITORING_FINISH_APPLICATION, SysEnvFuncs.getMemoryStatistics());

        log.info("{}: {}", applicationName, stopWatcher.getStringExecutionTime());
    }

    //==========================================================================
    public static String getBeansDefs() {

        final String[] beansDefs = genericApplicationContext
                .getBeanDefinitionNames();

        return Arrays.stream(beansDefs)
                .sorted((s1, s2) -> s1.compareTo(s2))
                .reduce(String.format("Spring beans list (%d): \n",
                        beansDefs.length),
                        (x, y) -> {
                            final BeanDefinition bd = genericApplicationContext.getBeanDefinition(y);
                            final String bcn = String.format("%s: %s [%s, %s]",
                                    y,
                                    bd.getClass(),
                                    bd.getScope(),
                                    bd.getResourceDescription());
                            return x.concat("\n").concat(bcn);
                        });
    }

    //==========================================================================
    public void initializeSpringBootApplication() {

    }
}
