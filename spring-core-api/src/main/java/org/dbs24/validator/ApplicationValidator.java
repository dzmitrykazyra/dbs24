package org.dbs24.validator;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import org.dbs24.application.core.locale.NLS;
import org.dbs24.application.core.service.funcs.TestFuncs;
import org.dbs24.spring.core.api.AbstractApplicationService;
import org.dbs24.stmt.StmtProcessor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.lang.Integer.parseInt;
import static org.dbs24.application.core.service.funcs.StringFuncs.long2StringFormatter;
import static org.dbs24.application.core.service.funcs.SysEnvFuncs.runTimeExec;
import static org.dbs24.application.core.service.funcs.SysEnvFuncs.runTimeExecSh;
import static org.dbs24.consts.SysConst.*;
import static org.dbs24.stmt.StmtProcessor.ifNull;
import static org.dbs24.stmt.StmtProcessor.ifTrue;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Log4j2
@Component
@ConditionalOnProperty(name = "config.application.auto-reboot.enable", havingValue = "true")
@EqualsAndHashCode(callSuper = true)
public class ApplicationValidator extends AbstractApplicationService {

    @Value("${config.application.auto-reboot.daily:0}")
    private Integer dailyReboot;
    private LocalTime rebootTime;
    private LocalTime t1;
    private LocalTime t2;
    @Value("${config.application.auto-reboot.frequency:0}")
    private Integer dailyFrequency;

    @Value("${config.application.auto-reboot.max-opened-files.perc-reserve:10}")
    private Integer mofPcFree;

    @Value("${config.application.auto-reboot.deadline:300}")
    private Long dailyRebootDeadLine;

    @Getter
    private Long nextRebootDeadLine;
    @Getter
    private Long nextRebootTime;

    @Value("${config.application.auto-reboot.interval:5000}")
    private Integer interval;
    @Value("${config.application.auto-reboot.interval-rate:2}")
    private Integer intervalRate;

    @Value("${config.application.auto-reboot.freeMemoryLimit:1000000}")
    private Integer freeMemoryLimit;

    @Value("${config.application.auto-reboot.attempts:5}")
    private Integer attempts;
    private Integer attemptsRemain = INTEGER_NULL;

    @Value("${config.application.auto-reboot.liveness.uri:empty}")
    private String livenessUri;
    private LocalDateTime livenessLastSuccess = LOCALDATETIME_NULL;
    @Value("${config.application.auto-reboot.liveness.timeout:60}")
    private Integer livenessTimeout;

    @Value("${config.application.auto-reboot.liveness.path:/api/liveness}")
    private String livenessPath;

    @Value("${config.application.auto-reboot.cmd:ls -aF}")
    private String rebootServiceCmd;

    private Long lastTotalMemory;

    final long currentPid = ProcessHandle.current().pid();
    static final String MAX_OPEN_FILES_KEY_WORD = "Max open files";
    final String openedFilesCmd = String.format("ls -l -d /proc/%s/fd/* | wc -l", currentPid);
    final String cmdGetMaxOpened = String.format("cat /proc/%s/limits | grep \"%s\"", currentPid, MAX_OPEN_FILES_KEY_WORD);
    private Integer lastOpenedFilesAmt = INTEGER_ZERO;

    @Override
    public void initialize() {

        super.initialize();

        StmtProcessor.assertNotNull(String.class, livenessUri, "parameter ${config.application.auto-reboot.liveness.uri}'");

        ifTrue(!livenessUri.isEmpty(), () -> {

            setWebClient(setupSSL(WebClient.builder(), livenessUri)
                    .baseUrl(livenessUri)
                    .exchangeStrategies(ExchangeStrategies.builder()
                            .build())
                    .build());
            log.debug("webClient: successfull created ({})", livenessUri);
        });

        if (dailyFrequency.compareTo(INTEGER_ZERO) > INTEGER_ZERO) {

            final Long currentTime = System.currentTimeMillis();

            ifNull(nextRebootTime, () -> {
                final Long periods = (currentTime / 1000) / dailyFrequency + 1;
                final Integer periodNum = TestFuncs.generateTestRangeInteger(-2, 5);
                nextRebootTime = (periods * dailyFrequency + (periodNum * dailyRebootDeadLine - 60)) * 1000;
                nextRebootDeadLine = nextRebootTime - (dailyRebootDeadLine - 120) * 1000;
                log.info("next reboot at {} (current is {}, dailyFrequency is {} seconds) ",
                        NLS.long2LocalDateTime(nextRebootTime),
                        NLS.long2LocalDateTime(currentTime),
                        dailyFrequency);
            });
        }
    }

    //==================================================================================================================
    @Scheduled(fixedRateString = "${config.application.auto-reboot.interval:5000}")
    public void rebootApplicationScheduler() {

        if (needReboot()) {

            ifNull(attemptsRemain, () -> attemptsRemain = attempts);

            if (--attemptsRemain < INTEGER_ZERO) {
                reboot();
                attemptsRemain = attempts;
            }
        } else {
            attemptsRemain = attempts;
        }

        // daily reboot
        if (dailyReboot.compareTo(INTEGER_ZERO) > INTEGER_ZERO) {
            ifNull(rebootTime, () -> {
                rebootTime = LocalTime.ofSecondOfDay(dailyReboot);
                t1 = rebootTime;
                t2 = t1.plusSeconds(interval * intervalRate / 1000);
            });

            final LocalTime now = LocalTime.now();

            if (now.compareTo(t1) >= INTEGER_ZERO && now.compareTo(t2) <= INTEGER_ZERO) {
                log.warn("daily reboot perform at {} [{} -> {}]".toUpperCase(), rebootTime, t1, t2);
                reboot();
            }
        }

        if (dailyFrequency.compareTo(INTEGER_ZERO) > INTEGER_ZERO) {

            final Long currentTime = System.currentTimeMillis();

            if (currentTime > nextRebootTime) {
                log.warn("frequency reboot perform at {} ".toUpperCase(),
                        NLS.long2LocalDateTime(nextRebootTime));
                reboot();
                nextRebootTime = LONG_ZERO;
            }
        }

        // liveness checking
        if (!livenessUri.equals("empty")) {

            ifNull(livenessLastSuccess, () -> livenessLastSuccess = LocalDateTime.now());

            //log.debug("check '{}{}", livenessUri, livenessPath);

            // create contract
            getWebClient()
                    .get()
                    .uri(uriBuilder
                            -> uriBuilder
                            .path(livenessPath)
                            .build())
                    .header(CONTENT_TYPE, APPLICATION_JSON_VALUE)
                    .retrieve()
                    //.onStatus(httpStatus -> HttpStatus.NOT_FOUND.equals(httpStatus), clientResponse -> Mono.empty())
                    .bodyToMono(Object.class)
                    .subscribe(si -> livenessLastSuccess = LocalDateTime.now(),
                            throwable -> {
                                log.error("could not execute '{}{}, {})", livenessUri, livenessPath, throwable.getMessage());
                                checkLivenessFail();
                            });

            checkLivenessFail();
        }

        if (mofPcFree.compareTo(INTEGER_ZERO) > INTEGER_ZERO) {

            final String openedFilesStr = runTimeExecSh(openedFilesCmd);
            final String openedFilesLimitCmdStr = runTimeExecSh(cmdGetMaxOpened);

            final String regex = "(\\d+)";
            final Pattern pattern = Pattern.compile(regex);
            final Matcher matcher = pattern.matcher(openedFilesLimitCmdStr);
            final String maxLimit = matcher.find() ? matcher.group(1) : NOT_DEFINED;

            if (maxLimit.equals(NOT_DEFINED)) {
                log.warn("{}: unknown or not defined", MAX_OPEN_FILES_KEY_WORD);
            } else {

                final int maxOpenedFiles = parseInt(maxLimit);
                final int openedFiles = parseInt(openedFilesStr);

                if (!lastOpenedFilesAmt.equals(openedFiles)) {
                    lastOpenedFilesAmt = openedFiles;
                    log.info("PID: {}, {}: {}/{}", currentPid, MAX_OPEN_FILES_KEY_WORD, openedFilesStr, maxLimit);
                }

                if ((float) openedFiles / (float) maxOpenedFiles + (float) mofPcFree / 100 >= 1) {
                    log.warn("PID: {}, max-opened-files violation reboot (used {}/{}) ".toUpperCase(), currentPid, openedFiles, maxOpenedFiles);
                    //reboot();
                }
            }
        }
    }

    private void checkLivenessFail() {
        if (livenessLastSuccess.plusSeconds(livenessTimeout).compareTo(LocalDateTime.now()) < 0) {
            log.warn("reboot: could not execute '{}{})".toUpperCase(), livenessUri, livenessPath);
            reboot();
        }
    }

    //==================================================================================================================
    protected Boolean needReboot() {

        final Runtime instance = Runtime.getRuntime();

        final Long totalMemory = instance.totalMemory();

        ifNull(lastTotalMemory, () -> {
            lastTotalMemory = totalMemory;
            log.info("initial total application memory: {} bytes", long2StringFormatter(lastTotalMemory));
        }, () -> {
            ifTrue(totalMemory < lastTotalMemory,
                    () -> log.warn("+++ memory leak detected ({} -> {}, {} bytes)",
                            long2StringFormatter(lastTotalMemory),
                            long2StringFormatter(totalMemory),
                            long2StringFormatter(totalMemory - lastTotalMemory)));
            ifTrue(totalMemory > lastTotalMemory,
                    () -> log.info("+++ memory increase detected ({} -> {}, +{} bytes)",
                            long2StringFormatter(lastTotalMemory),
                            long2StringFormatter(totalMemory),
                            long2StringFormatter(totalMemory - lastTotalMemory)));
            lastTotalMemory = totalMemory;
        });

        final Boolean isNeedReboot = instance.freeMemory() < freeMemoryLimit;

        ifTrue(isNeedReboot, () -> log.warn("needReboot, memory limit: {}/{} bytes, attempts remain: {}",
                long2StringFormatter(instance.freeMemory()),
                long2StringFormatter(freeMemoryLimit), attemptsRemain));

        return isNeedReboot;
    }

    protected void reboot() {
        log.warn("start reboot, execute: '{}'".toUpperCase(), rebootServiceCmd);

        log.info(runTimeExec(rebootServiceCmd));
    }
}
