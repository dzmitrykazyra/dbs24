/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.component;

import lombok.Data;
import lombok.extern.log4j.Log4j2;
import org.dbs24.application.core.locale.NLS;
import org.dbs24.application.core.nullsafe.StopWatcher;
import org.dbs24.application.core.service.funcs.ServiceFuncs;
import org.dbs24.entity.VisitNote;
import org.dbs24.jpa.spec.VisitNoteAfterId;
import org.dbs24.jpa.spec.VisitNoteLatest;
import org.dbs24.repository.AppUserRepository;
import org.dbs24.repository.SubscriptionPhoneRepository;
import org.dbs24.repository.VisitNoteRepository;
import org.dbs24.rest.api.ActivityInfo;
import org.dbs24.rest.api.ActivityRecord;
import org.dbs24.rest.api.CreatedActivity;
import org.dbs24.spring.core.api.AbstractApplicationService;
import org.dbs24.stmt.StmtProcessor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.Random;

import static org.dbs24.consts.SysConst.LONG_ZERO;
import static org.dbs24.consts.WaConsts.*;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Log4j2
@Component
@ConditionalOnProperty(name = "config.wa.online-test.enabled", havingValue = "true")
@EnableScheduling
@EnableAsync
@EnableConfigurationProperties
@ConfigurationProperties(prefix = "test")
public class VisitNoteExporter extends AbstractApplicationService {

    final VisitNoteRepository visitNoteRepository;
    final SubscriptionPhoneRepository subscriptionPhoneRepository;
    final AppUserRepository appUserRepository;

    private Long lastId = Long.valueOf("0");

    final Random random = new Random();

    @Value("${config.wa.online-test.qry.limit:2000}")
    private Integer qryExecLimit = 2000;

    @Value("${config.wa.online-test.url}")
    private String url;

    @Value("${config.wa.online-test.create-activity-path}")
    private String createActivityPath;

    @Value("${config.wa.online-test.get-activity-path}")
    private String getActivityPath;

    private Map<String, String> phonesList;

    public VisitNoteExporter(VisitNoteRepository visitNoteRepository, SubscriptionPhoneRepository subscriptionPhoneRepository, AppUserRepository appUserRepository) {
        this.visitNoteRepository = visitNoteRepository;
        this.subscriptionPhoneRepository = subscriptionPhoneRepository;
        this.appUserRepository = appUserRepository;

    }
    //==========================================================================
    @Override
    public WebClient getWebClient() {

        return Optional.ofNullable(super.getWebClient())
                .orElseGet(() -> {
                    StmtProcessor.assertNotNull(String.class, url, "parameter ${config.wa.path}'");
                    setWebClient(WebClient.builder()
                            .baseUrl(url)
                            .exchangeStrategies(ExchangeStrategies.builder()
                                    .codecs(configurer -> configurer
                                    .defaultCodecs()
                                    .maxInMemorySize(16 * 1024 * 1024))
                                    .build())
                            .build());
                    log.info("webClient: succefull connect to {}", url);
                    return super.getWebClient();
                });
    }
    //==========================================================================

    final VisitNoteLatest vnLatest = () -> (r, cq, cb) -> {

        final Predicate predicate = cb.conjunction();

        final Subquery<VisitNote> spSubquery = cq.subquery(VISIT_NOTE_CLASS);
        final Root<VisitNote> spRoot = spSubquery.from(VISIT_NOTE_CLASS);

        final Expression exp = spRoot.as(VISIT_NOTE_CLASS);

        spSubquery.select(cb.max(exp));

        predicate.getExpressions().add(cb.equal(r.get("id"), spSubquery));

        return predicate;
    };

    //==========================================================================
    final VisitNoteAfterId vnAfter = id -> (r, cq, cb) -> {

        final Predicate predicate = cb.conjunction();
        predicate.getExpressions().add(cb.greaterThan(r.get("id"), id));

        return predicate;
    };

    @Scheduled(fixedRateString = "${config.wa.online-test.update:1000}")
    private void refresh() {

        log.info("vn: last id {}", lastId);

        StmtProcessor.ifTrue(lastId.equals(LONG_ZERO), () -> {
            log.info("vn: getLastest id");

            lastId = visitNoteRepository.findAll(vnLatest.noParams())
                    .stream()
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException(String.format("VisitTimes not found ")))
                    .getId();

            log.info("vn: starting from {}", lastId);

        }, () -> {

            final StopWatcher stopWatcher = StopWatcher.create("Query uptime");

            final Collection<VisitNote> visitNotes = visitNoteRepository
                    .findAll(vnAfter.setId(lastId));

            log.info("vn: found records {}", visitNotes.size());

            StmtProcessor.ifTrue(visitNotes.size() > 0, () -> {

                final Mono<ActivityInfo> monoActivityInfo = Mono.just(StmtProcessor.create(ACTIVITY_INFO_CLASS, ai -> {

                    final Collection<ActivityRecord> records = ServiceFuncs.<ActivityRecord>createCollection();

                    visitNotes.stream()
                            //.parallel()
                            .forEach(record -> records.add(StmtProcessor.create(ActivityRecord.class, ar -> {

                        ar.setActualDate(NLS.localDateTime2long(record.getAddTime()));
                        ar.setIsOnline(record.getIsOnline().equals(1));
                        ar.setSubscriptionId(record.getSubscriptionPhone().getId());
                    })));

                    log.info("vn: send {} records 2 {}", visitNotes.size(), createActivityPath);

                    ai.setRecords(records);

                }));

                final CreatedActivity createdActivity
                        = getWebClient()
                                .post()
                                .uri(uriBuilder
                                        -> uriBuilder
                                        .path(createActivityPath)
                                        .build())
                                .contentType(APPLICATION_JSON)
                                .accept(APPLICATION_JSON)
                                .body(monoActivityInfo, ACTIVITY_INFO_CLASS)
                                .retrieve()
                                .bodyToMono(CREATED_ACTIVITY_CLASS)
                                .block();

                log.info("vn: code = {}, {}", createdActivity.getCode(), createdActivity.getMessage());

                lastId = visitNotes
                        .stream()
                        .max((a, b) -> a.getId().compareTo(b.getId()))
                        .get()
                        .getId();

                log.info("vn: set new lastId = {}, {}", lastId, stopWatcher.getStringExecutionTime());

                StmtProcessor.ifTrue(stopWatcher.getExecutionTime() > qryExecLimit, () -> {

                    log.warn("vn: low execution time detected: {}", stopWatcher.getStringExecutionTime());

                    lastId = Long.valueOf("0");

                });
            });
        });
    }

    //==========================================================================
    @Scheduled(fixedRateString = "${config.wa.online-test.subscription.update:200}")
    private void test4query() {

        // create random request 4 subscription       
        Map.Entry<String, String> map = phonesList
                .entrySet()
                .stream()
                .skip(random.nextInt(phonesList.size() - 1))
                .findAny()
                .orElseThrow(() -> new RuntimeException("phonesList is empty"));

        log.info("test4query - {}", map.getKey());

        final StopWatcher stopWatcher = StopWatcher.create("request 4 activitites");

        final ActivityInfo activityInfo = getWebClient()
                .get()
                .uri(uriBuilder
                        -> uriBuilder
                        .path(getActivityPath)
                        .queryParam("loginToken", String.valueOf(map.getKey()))
                        .queryParam("phone", String.valueOf(map.getValue()))
                        .queryParam("d1", NLS.localDateTime2long(LocalDateTime.now().minusHours(3)))
                        .queryParam("d2", NLS.localDateTime2long(LocalDateTime.now()))
                        .build())
                .header(CONTENT_TYPE, APPLICATION_JSON_VALUE)
                .retrieve()
                //.onStatus(httpStatus -> HttpStatus.NOT_FOUND.equals(httpStatus), clientResponse -> Mono.empty())
                .bodyToMono(ActivityInfo.class)
                .block();

        log.info("Receive activityInfo - {}, {}", activityInfo.getRecords().size(), stopWatcher.getStringExecutionTime());
    }
}
