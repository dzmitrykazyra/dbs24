/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.spring.boot.api;

import lombok.extern.log4j.Log4j2;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.LoggerContext;
import org.dbs24.application.core.nullsafe.StopWatcher;
import org.dbs24.application.core.service.funcs.GetNetworkAddress;
import org.dbs24.application.core.service.funcs.SysEnvFuncs;
import org.dbs24.spring.core.api.ApplicationConfiguration;
import org.dbs24.stmt.StmtProcessor;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.ExitCodeGenerator;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.GenericApplicationContext;
import reactor.core.publisher.Mono;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.File;
import java.util.Arrays;
import java.util.stream.Stream;

import static org.dbs24.consts.SysConst.*;
import static org.dbs24.stmt.StmtProcessor.notNull;

@Log4j2
public abstract class AbstractSpringBootApplication { //extends SpringBootServletInitializer  {

    private static class SpringBootSubscriber implements Subscriber<Class<?>> {

        final StopWatcher stopWatcher;
        final String applicationName;
        final String[] args;
        final SpringBootInititializer sbi;

        public SpringBootSubscriber(String applicationName, String[] args, SpringBootInititializer sbi) {
            this.applicationName = applicationName;
            this.args = args;
            this.sbi = sbi;
            this.stopWatcher = StopWatcher.create();
        }

        @Override
        public void onSubscribe(Subscription s) {
            s.request(100);
            log.info("Initializing {} ...", applicationName);
        }

        @Override
        public void onNext(Class<?> clazz) {

            log.debug("Initializing Spring Boot application {} ", clazz.getClass().getSimpleName());

            SpringApplication.run(clazz, args);
            sbi.initialize();
            log.info("{}, port {}", GetNetworkAddress.getAllAddresses(), serverPort);

            // log4j
            final String log4j2xml = System.getProperty("log4j.configurationFile");

            if (notNull(log4j2xml)) {
                log.info("Register log4j.configurationFile '{}'", log4j2xml);
                LoggerContext context = (org.apache.logging.log4j.core.LoggerContext) LogManager.getContext(false);
                File file = new File(log4j2xml);
                context.setConfigLocation(file.toURI());
                context.reconfigure();
            }

            StmtProcessor.assertNotNull(clazz, AbstractSpringBootApplication.applicationContext, "AbstractSpringBootApplication.applicationContext");
            StmtProcessor.assertNotNull(clazz, AbstractSpringBootApplication.genericApplicationContext, "AbstractSpringBootApplication.genericApplicationContext");

            final String msgInfo = String.format("SUCCESSFULLY STARTED - %s, [%s], port = %d", applicationName, GetNetworkAddress.getAllAddresses(), serverPort);

            log.info(msgInfo);

            genericApplicationContext.setDisplayName(clazz.getSimpleName());

            isRunning = BOOLEAN_TRUE;

        }

        @Override
        public void onError(Throwable t) {

            log.error("ERROR STARTING '{}' ('{}'), reason: '{}' ", applicationName, AbstractSpringBootApplication.userApplicationName, t.getLocalizedMessage());
            log.error(stopWatcher.getStringExecutionTime());
            log.error("{} exception \n {}: '{}'", applicationName, t.getClass().getCanonicalName(), t.getLocalizedMessage());
        }

        @Override
        public void onComplete() {
            log.info("{}", SysEnvFuncs.getMemoryStatistics());
            log.debug("finally initialization {}", applicationName);
            log.info(stopWatcher.getStringExecutionTime());

        }
    }

    static String userApplicationName;
    static Boolean isRunning = BOOLEAN_FALSE;
    final String className = this.getClass().getSimpleName();
    final StopWatcher stopWatcher = StopWatcher.create("Application uptime");

    public static Boolean debug;
    public static int serverPort;
    public static ApplicationContext applicationContext;
    public static GenericApplicationContext genericApplicationContext;

    //==========================================================================    
    public static final SpringBootInititializer EMPTY_INITIALIZATION = () -> {
    };

    @Autowired
    public void initializeGenericContext(ApplicationContext applicationContext, GenericApplicationContext genericApplicationContext) {
        AbstractSpringBootApplication.applicationContext = applicationContext;
        AbstractSpringBootApplication.genericApplicationContext = genericApplicationContext;
        this.initializeSpringBootApplication();

        log.info("Application '{}' is activated", className.indexOf("$$") > 0 ? className.substring(0, className.indexOf("$$")) : className);
    }

    @Value("${spring.application.name:application name not defined}")
    public void initializeApplicationName(String userApplicationName) {
        AbstractSpringBootApplication.userApplicationName = StmtProcessor.isNull(userApplicationName) ? NOT_DEFINED : userApplicationName;
    }

    @Value("${debug:false}")
    public void initializeDebug(Boolean appDebug) {
        AbstractSpringBootApplication.debug = StmtProcessor.isNull(appDebug) ? BOOLEAN_FALSE : appDebug;
    }

    @Value("${server.port}")
    public void initializeServerPort(int serverPort) {
        AbstractSpringBootApplication.serverPort = StmtProcessor.isNull(serverPort) ? 8080 : serverPort;
    }

    //==========================================================================
    public static void runSpringBootApplication(String[] args, Class<?> springBootClass, SpringBootInititializer sbi) {

        final String applicationName = springBootClass.getCanonicalName();

        Mono.just(springBootClass).subscribe(new SpringBootSubscriber(applicationName, args, sbi));

        // check mandatory beans
//        Optional.ofNullable(AnnotationFuncs.<MandatoryBeans>getAnnotation(springBootClass, MandatoryBeans.class))
//                .ifPresent(beans -> assertBeans(springBootClass, beans.value()));

        if (!isRunning) {

            log.info("ShutDOWN APPLICATION {}", applicationName);

            StmtProcessor.runNewThread(() -> {
                StmtProcessor.execute(() -> Thread.sleep(2000));
                //SpringApplication.exit(applicationContext);
                System.exit(-1);
            });
        }
    }

    @Bean
    public ExitCodeGenerator exitCodeGenerator() {
        return () -> 42;
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

    private void printBeans(Boolean printList) {

        final String[] beansDefs = genericApplicationContext.getBeanDefinitionNames();

        final String header = String.format("Spring beans collection (%d pts)", beansDefs.length);

        if (!printList) {
            log.info(header);
        } else {
            log.info(Arrays.stream(beansDefs).sorted((s1, s2) -> s1.compareTo(s2)).reduce(header.concat("\n"), (x, y) -> {
                final BeanDefinition bd = genericApplicationContext.getBeanDefinition(y);
                final String bcn = String.format("%s: %s [%s, %s]", y, bd.getClass(), bd.getScope(), bd.getResourceDescription());
                return x.concat("\n").concat(bcn);
            }));
        }
    }

    //==========================================================================
    @PreDestroy
    protected void destroyApplication() {

        log.info("{}", SysEnvFuncs.getMemoryStatistics());

        final String applicationName = this.getClass().getCanonicalName();
        final String logMsg = String.format("STOPPED (%s, %s) ", applicationName, AbstractSpringBootApplication.userApplicationName);
        log.info(logMsg);

        log.info("{}: {}", applicationName, stopWatcher.getStringExecutionTime());
    }

    //==========================================================================
    public void initializeSpringBootApplication() {
    }

    //==========================================================================
    @Bean
    CommandLineRunner validateApplication(GenericApplicationContext genericApplicationContext) {

        return args -> {
            if (args.length > 0) {

                log.info(Stream.of(args)
                        .map(x -> (String) (x.contains("password") ? x.substring(0, x.indexOf("=")).concat("=[SECURED]") :
                                x.contains("username") ? x.substring(0, x.indexOf("=")).concat("=[SECURED]") : x))
                        .reduce("Modified application arguments" + "\n", (x, y) -> x.concat(String.format(" * '%s'\n", y))));
            }

            final String applicationName = this.getClass().getCanonicalName();

            log.info("Application is validated '{}'", applicationName.indexOf("$$") > 0 ? applicationName.substring(0, applicationName.indexOf("$$")) : applicationName);
        };
    }

    //==========================================================================
    @Value("${config.env.print:false}")
    private Boolean envPrint;

    @PostConstruct
    public void postInitialize() {
        if (envPrint) {
            log.info(SysEnvFuncs.getSysEnvFuns());
            log.info(SysEnvFuncs.getSysProperties());
        }
        printBeans(AbstractSpringBootApplication.debug);
    }
}
