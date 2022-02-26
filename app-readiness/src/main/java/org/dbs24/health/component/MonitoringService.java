package org.dbs24.health.component;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import org.dbs24.application.core.nullsafe.StopWatcher;
import org.dbs24.consts.SysConst;
import org.dbs24.spring.core.api.AbstractApplicationService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Predicate;

import static java.lang.Integer.valueOf;
import static org.dbs24.application.core.service.funcs.ServiceFuncs.createCollection;
import static org.dbs24.application.core.service.funcs.SysEnvFuncs.runTimeExec;
import static org.dbs24.consts.SysConst.BOOLEAN_FALSE;
import static org.dbs24.consts.SysConst.BOOLEAN_TRUE;
import static org.dbs24.stmt.StmtProcessor.create;
import static org.dbs24.stmt.StmtProcessor.ifTrue;
import static org.springframework.http.MediaType.APPLICATION_JSON;


@Log4j2
@Component
@EnableAsync
@EnableScheduling
@EnableConfigurationProperties
@ConfigurationProperties(prefix = "refs")
public class MonitoringService extends AbstractApplicationService {

    @Setter
    private Map<Integer, String> applications;
    @Getter
    final Collection<Application> operJournal;

    @Value("${config.reboot.allowed:false}")
    private Boolean rebootMode;

    public MonitoringService() {
        operJournal = createCollection();
    }

    final Predicate<Application> nextTestRun = app -> app.getNextAttemptTest().compareTo(LocalDateTime.now()) <= 0;
    final Consumer<Map.Entry<Integer, String>> initJournal = entry ->

            getOperJournal().add(create(Application.class, application -> {
                final String[] values = entry.getValue().split(";");

                // 0 - appName
                application.setName(values[0]);
                application.setAddress(values[1].trim());
                application.setUri(values[2]);
                application.setAllowedPing(valueOf(values[3].trim()));
                application.setLegalAttepmts(valueOf(values[4].trim()));
                application.setNextAttemptTest(LocalDateTime.now());
                application.setRebootCmd(values[5].trim());

                application.setWebClient(createWebClient(application.getAddress()));
                application.setRemainAttepmts(application.getLegalAttepmts());
                application.setIsValid(BOOLEAN_TRUE);
                application.setCanReboot(BOOLEAN_FALSE);

                log.debug("app = {}", application);

            }));

    final Consumer<Application> applicationConsumer = application ->
            ifTrue(application.getIsValid(), () -> {
                log.debug("processing app: {}, {}", application.getName(), application.getAddress() + application.getUri());

                final StopWatcher stopWatcher = StopWatcher.create("Server uptime");

                application.getWebClient()
                        .get()
                        .uri(uriBuilder
                                -> uriBuilder
                                .path(application.getUri())
                                .build())
                        .accept(APPLICATION_JSON)
                        .retrieve()
                        .bodyToMono(Object.class)
                        .doOnError(throwable -> {

                            log.error("can't execute query = {}", throwable.getMessage());

                            application.setRemainAttepmts(application.getRemainAttepmts() - 1);
                            log.info("RemainAttepmts = {}", application.getRemainAttepmts());

                        }).subscribe(o -> {

                            final Long execTime = stopWatcher.getExecutionTime();

                            log.info("{}: exec ping time: {}", application.getName(), stopWatcher.getStringExecutionTime());

                            ifTrue(application.getAllowedPing() < execTime, () -> {

                                application.setRemainAttepmts(application.getRemainAttepmts() - 1);

                                log.warn("{}: bad execution time: {} (>{} ms), remainAttempts = {}",
                                        application.getName(),
                                        stopWatcher.getStringExecutionTime(),
                                        application.getAllowedPing(),
                                        application.getRemainAttepmts());

                            }, () -> {

                                ifTrue(!application.getLegalAttepmts().equals(application.getRemainAttepmts()), () -> {

                                    application.setRemainAttepmts(application.getLegalAttepmts());
                                    application.setCanReboot(BOOLEAN_TRUE);

                                    log.info("{}: restore remainAttempts = {}",
                                            application.getName(),
                                            application.getRemainAttepmts());

                                });

                                application.setNextAttemptTest(LocalDateTime.now().plusSeconds(30));
                            });
                        });

                // reboot application
                ifTrue(application.getRemainAttepmts().equals(SysConst.INTEGER_ZERO)
                        && application.getCanReboot(), () -> {

                    log.warn("{}: do reboot (rebootMode = {})", application.getName(), rebootMode);
                    application.setNextAttemptTest(LocalDateTime.now().plusMinutes(2));
                    application.setRemainAttepmts(application.getLegalAttepmts());

                    ifTrue(application.getCanReboot() && rebootMode, () -> {

                        log.warn("start reboot, execute: '{}'".toUpperCase(), application.getRebootCmd());

                        log.info(runTimeExec(application.getRebootCmd()));
                    });
                });
            });

    @Scheduled(fixedRateString = "${config.reboot.ping:10000}", cron = "${config.reboot.ping.processing-cron:}")
    public void ping() {
        operJournal.stream().filter(nextTestRun).forEach(applicationConsumer);
    }

    private WebClient createWebClient(String address) {

        final WebClient webClient = (setupSSL(WebClient.builder(), address)
                .baseUrl(address)
                .exchangeStrategies(ExchangeStrategies.builder()
                        .codecs(configurer -> configurer
                                .defaultCodecs()
                                .maxInMemorySize(16 * 1024 * 1024))
                        .build())
                .build());
        log.debug("webClient: successfull created ({})", address);

        return webClient;

    }

    @Override
    public void initialize() {
        super.initialize();

        log.warn("reboot mode: {}", rebootMode);

        applications
                .entrySet()
                .stream()
                .forEach(initJournal);
    }
}