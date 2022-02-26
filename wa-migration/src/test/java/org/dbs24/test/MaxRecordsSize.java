/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.test;

import java.time.LocalDateTime;
import java.util.Collection;
import javax.persistence.criteria.Predicate;
import lombok.extern.log4j.Log4j2;
import org.dbs24.WaMigration;
import org.dbs24.application.core.nullsafe.StopWatcher;
import org.dbs24.application.core.service.funcs.ServiceFuncs;
import org.dbs24.config.WaServerConfig;
import static org.dbs24.consts.WaConsts.APP_USER_COLLECTION_CLASS;
import static org.dbs24.consts.WaConsts.URI_GET_APP_USERS;
import org.dbs24.entity.VisitNote;
import org.dbs24.jpa.spec.VisitNoteAddTime;
import org.dbs24.repository.VisitNoteRepository;
import org.dbs24.rest.api.AppUserCollection;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Page;
import java.util.stream.IntStream;
import org.dbs24.stmt.StmtProcessor;

@Log4j2
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = {WaMigration.class})
@Import({WaServerConfig.class})
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class MaxRecordsSize extends AbstractMonitoringTest {

    final VisitNoteRepository visitNoteRepository;

    @Autowired
    public MaxRecordsSize(VisitNoteRepository visitNoteRepository) {
        this.visitNoteRepository = visitNoteRepository;

    }
    //==========================================================================
    final VisitNoteAddTime visitNoteAddTime = (from, to) -> (r, cq, cb) -> {

        final Predicate predicate = cb.conjunction();

        predicate.getExpressions().add(cb.between(r.get("addTime"), from, to));
//        predicate.getExpressions().add(cb.greaterThan(r.get("subscriptionPhone"), Integer.valueOf("0")));

        return predicate;

    };

    //==========================================================================
    final Integer pageSize = 1000;
    final LocalDateTime d1 = LocalDateTime.now().minusHours(6);
    final LocalDateTime d2 = LocalDateTime.now().minusHours(5);
    final Collection<VisitNote> visitNotes = ServiceFuncs.<VisitNote>createConcurencyCollection();

    @Order(100)
    @Test
    @DisplayName("test1")
    //@RepeatedTest(10)
    public void bigLoadTest() {

        runTest(() -> {

            final StopWatcher stopWatcher = StopWatcher.create("1");

            final Page<VisitNote> startPage = processPage(0);

            log.info("(d1,d2) -> {}, {} ", d1, d2);

            final Integer pgAmt = startPage.getTotalPages();

            log.info("There {} pages ({} recs per page)", pgAmt, pageSize);

            IntStream.range(1, pgAmt)
                    .parallel()
                    //.forEach(i -> log.info("i=0", i));
                    .forEach(this::processPage);

            log.info("receive {}/{} records, {} ", visitNotes.size(), startPage.getTotalElements(), stopWatcher);

        });

        //StmtProcessor.sleep(50000);
    }

    //==========================================================================
    public Page<VisitNote> processPage(int i) {

        final Pageable pageable = PageRequest.of(i, pageSize);
        final Page<VisitNote> page = visitNoteRepository
                .findAll(visitNoteAddTime.setFilter(d1, d2), pageable);
//        log.info("loadRecords = {}, {}", i, page.getNumberOfElements());
        addRecords(page);

        return page;
    }

    //==========================================================================
    public void addRecords(Page<VisitNote> visitNotes) {

        //if (visitNotes.hasContent()) {
        //log.info("fill visitNotes {}", visitNotes.getContent().size());
        visitNotes.get().forEach(vn -> {
            this.visitNotes.add(vn);
        });
        // }
    }
}
