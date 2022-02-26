/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.test;

import java.time.LocalDateTime;
import java.util.Collection;
import lombok.extern.log4j.Log4j2;
import org.dbs24.WaMonitoring;
import org.dbs24.application.core.service.funcs.ServiceFuncs;
import org.dbs24.application.core.service.funcs.TestFuncs;
import org.dbs24.config.WaServerConfig;
import static org.dbs24.consts.WaConsts.RestQueryParams.*;
import static org.dbs24.consts.WaConsts.Uri.*;
import static org.dbs24.consts.WaConsts.Classes.*;
import org.dbs24.rest.api.ActivityInfo;
import org.dbs24.rest.api.ActivityRecord;
import org.dbs24.stmt.StmtProcessor;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import reactor.core.publisher.Mono;
import java.util.Random;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;
import org.dbs24.application.core.locale.NLS;
import org.springframework.cache.annotation.EnableCaching;
import org.dbs24.test.service.LatestActivity;
import org.dbs24.entity.SubscriptionActivity;
import org.dbs24.repository.ActivityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;

@AutoConfigureWebTestClient
@Log4j2
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = {WaMonitoring.class})
@Import({WaServerConfig.class})
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@EnableCaching
public class ActivityTests extends AbstractMonitoringTest {

    final ActivityRepository activityRepository;

    @Autowired
    public ActivityTests(ActivityRepository activityRepository) {
        this.activityRepository = activityRepository;
    }

    @Order(99)
    @Test
    @DisplayName("hi loading")
    //@RepeatedTest(100)
    public void testHiLoadingOld() {

        log.info("testing {}", URI_CREATE_ACTIVITIES);

        final Mono<ActivityInfo> monoActivityInfo = Mono.just(StmtProcessor.create(ACTIVITY_INFO_CLASS, ai -> {

            final Collection<ActivityRecord> records = ServiceFuncs.<ActivityRecord>createCollection();

            final Integer s1 = getLastSubsriptionId();
            final Integer s2 = s1 - 1;
            final Integer s3 = s2 - 1;
            final LocalDateTime actualDate = LocalDateTime.now();

            Stream.generate(new Random()::nextInt)
                    .limit(3)
                    .forEach(record -> records.add(StmtProcessor.create(ActivityRecord.class, ar -> {
                ar.setActualDate(NLS.localDateTime2long(actualDate));
                ar.setIsOnline(TestFuncs.generateBool());
                ar.setSubscriptionId(TestFuncs.generateTestInteger(s1, s2, s3));
            })));

            ai.setRecords(records);

        }));

        this.runTest(() -> {

            final String s1 = "http://193.178.170.145:8040";

            log.info("testing {}{}", s1, URI_CREATE_ACTIVITIES);

            WebClient.builder()
                    .baseUrl(s1)
                    .exchangeStrategies(ExchangeStrategies.builder()
                            .codecs(configurer -> configurer
                            .defaultCodecs()
                            .maxInMemorySize(16 * 1024 * 1024))
                            .build())
                    .build()
                    .post()
                    .uri(uriBuilder
                            -> uriBuilder
                            .path(URI_CREATE_ACTIVITIES)
                            .build())
                    .contentType(APPLICATION_JSON)
                    .accept(APPLICATION_JSON)
                    .body(monoActivityInfo, ACTIVITY_INFO_CLASS)
                    .retrieve()
                    .bodyToMono(CREATED_ACTIVITY_CLASS)
                    .subscribe(pr -> log.info("Created activity: {}", pr));

        });
    }

    @Order(100)
    @Test
    @DisplayName("hi loading")
    //@RepeatedTest(100)
    public void testHiLoading() {

        log.info("testing {}", URI_CREATE_ACTIVITIES);

        final Mono<ActivityInfo> monoActivityInfo = Mono.just(StmtProcessor.create(ACTIVITY_INFO_CLASS, ai -> {

            final Collection<ActivityRecord> records = ServiceFuncs.<ActivityRecord>createCollection();

            final Integer s1 = getLastSubsriptionId();
            final Integer s2 = s1 - 1;
            final Integer s3 = s2 - 1;
            final LocalDateTime actualDate = LocalDateTime.now();

            Stream.generate(new Random()::nextInt)
                    .limit(30)
                    .forEach(record -> records.add(StmtProcessor.create(ActivityRecord.class, ar -> {
                ar.setActualDate(NLS.localDateTime2long(actualDate));
                ar.setIsOnline(TestFuncs.generateBool());
                ar.setSubscriptionId(TestFuncs.generateTestInteger(s1, s2, s3));
            })));

            ai.setRecords(records);

        }));

        this.runTest(() -> {

            log.info("testing {}", URI_CREATE_ACTIVITIES);

            webTestClient
                    .post()
                    .uri(uriBuilder
                            -> uriBuilder
                            .path(URI_CREATE_ACTIVITIES)
                            .build())
                    .contentType(APPLICATION_JSON)
                    .accept(APPLICATION_JSON)
                    .body(monoActivityInfo, ACTIVITY_INFO_CLASS)
                    .exchange()
                    .expectStatus()
                    .isOk();
        });
    }
    //==========================================================================
    final LatestActivity latestActivity = () -> (r, cq, cb) -> {

        final Predicate predicate = cb.conjunction();

        final Subquery<SubscriptionActivity> spSubquery = cq.subquery(SUBSCRIPTION_ACTIVITY_CLASS);
        final Root<SubscriptionActivity> spRoot = spSubquery.from(SUBSCRIPTION_ACTIVITY_CLASS);

        final Expression maxExpr = spRoot.as(SUBSCRIPTION_ACTIVITY_CLASS);

        spSubquery.select(cb.max(maxExpr));

        predicate.getExpressions().add(cb.equal(r.get("activityId"), spSubquery));

        return predicate;
    };

    @Order(200)
    //@Test
    @DisplayName("get activities list")
    //@RepeatedTest(100)
    @Transactional(readOnly = true)
    public void testGetActivitiesList() {

        log.info("testing {}", URI_GET_ACTIVITIES);

        this.runTest(() -> {

            log.info("testing {}", URI_GET_ACTIVITIES);

            final SubscriptionActivity subscriptionActivity = activityRepository.findOne(latestActivity.getLast()).get();

            log.info("getting user activity: {}", subscriptionActivity.getActivityId());

            final ActivityInfo activityInfo
                    = webTestClient
                            .get()
                            .uri(uriBuilder
                                    -> uriBuilder
                                    .path(URI_GET_ACTIVITIES)
                                    .queryParam(QP_D1, NLS.localDateTime2long(subscriptionActivity.getActualDate().minusDays(1)))
                                    .queryParam(QP_D2, NLS.localDateTime2long(LocalDateTime.now()))
                                    .queryParam(QP_LOGIN_TOKEN, subscriptionActivity.getUserSubscription().getUser().getLoginToken())
                                    .queryParam(QP_PHONE, subscriptionActivity.getUserSubscription().getPhoneNum().replaceAll("[^\\d.]", ""))
                                    .build())
                            .accept(APPLICATION_JSON)
                            .exchange()
                            .expectStatus()
                            .isOk()
                            .expectBody(ACTIVITY_INFO_CLASS)
                            .returnResult()
                            .getResponseBody();

            log.info("testGetActivitiesList: receive {} records", activityInfo.getRecords().size());

        });
    }

    @Order(200)
    //@Test
    @DisplayName("filtering trash")
    public void testClearDuplicatedActivity() {

        final Collection<ActivityRecord> records = ServiceFuncs.<ActivityRecord>createCollection();

//      public class ActivityRecord {
//
//      private Integer subscriptionId;
//      private Long actualDate;
//      private Boolean isOnline;
//      }        
        final Integer s1 = 100;
        final Integer s2 = s1 - 1;
        final Integer s3 = s2 - 1;
        final Integer s4 = s3 - 1;
        final Integer s5 = s4 - 1;
        final Integer s6 = s5 - 1;

        final Integer testLimit = 50;

        Stream.generate(new Random()::nextInt)
                .limit(testLimit)
                .forEach(record -> records.add(StmtProcessor.create(ActivityRecord.class, ar -> {
            ar.setActualDate(NLS.localDateTime2long(TestFuncs.generateTestLocalDateTime()));
            ar.setIsOnline(TestFuncs.generateBool());
            ar.setSubscriptionId(TestFuncs.generateTestInteger(s1, s2, s3, s3, s4, s5, s5));

        })));

        log.info("dirty collection");

        records
                .stream()
                .sorted(this::defSortAlg)
                .forEach(rec -> log.info("rec =  {}", rec));

        log.info("do filtering");

        final Collection<ActivityRecord> filteredCollection
                = records
                        .stream()
                        .filter(ar -> isActualRecord(ar, records))
                        .sorted(this::defSortAlg)
                        .collect(Collectors.toList());

        log.info("filtered collection");
        filteredCollection
                .stream()
                .forEach(rec -> log.info("newrec =  {}", rec));

        log.info("remove {} items ({}/{})", testLimit - filteredCollection.size(), filteredCollection.size(), testLimit);

    }

    private Boolean isActualRecord(ActivityRecord ar, Collection<ActivityRecord> main) {

        final Optional<ActivityRecord> nextActivityRecord = main
                .stream()
                .filter(far -> far.getSubscriptionId().equals(ar.getSubscriptionId()) && (far.getActualDate().compareTo(ar.getActualDate()) < 0))
                .sorted((a, b) -> b.getActualDate().compareTo(a.getActualDate()))
                .limit(1)
                .findFirst();

        return nextActivityRecord.isEmpty() ? true : !(nextActivityRecord.get().getIsOnline().equals(ar.getIsOnline()));
    }

    private int defSortAlg(ActivityRecord a, ActivityRecord b) {
        final int subscription = a.getSubscriptionId().compareTo(b.getSubscriptionId());
        return subscription == 0 ? a.getActualDate().compareTo(b.getActualDate()) : subscription;
    }
}
