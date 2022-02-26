/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.insta.tmp.test;

import java.util.Random;
import java.util.stream.Stream;
import lombok.Data;
import lombok.extern.log4j.Log4j2;
import org.dbs24.application.core.service.funcs.TestFuncs;
import static org.dbs24.consts.SysConst.ALL_PACKAGES;
import org.dbs24.insta.tmp.InstaFacesSearch;
import org.dbs24.insta.tmp.config.InstafFsConfig;
import org.dbs24.stmt.StmtProcessor;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.dbs24.insta.tmp.kafka.KafkaService;
import org.dbs24.insta.tmp.kafka.api.IgSourceTask;
import org.springframework.beans.factory.annotation.Autowired;

@Data
@Log4j2
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = {InstaFacesSearch.class})
@Import({InstafFsConfig.class})
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@EntityScan(basePackages = {ALL_PACKAGES})
public class SourcesTasksTests extends AbstractInstaFsTest {

    final KafkaService kafkaService;

    @Autowired
    public SourcesTasksTests(KafkaService kafkaService) {
        this.kafkaService = kafkaService;
    }

    @Order(100)
    @Test
    public void createSourcetask() {

        runTest(() -> {

            log.info("testing {}", "kafkaService: send sourceTask");

            Stream.generate(new Random()::nextInt)
                    .limit(20)
                    .forEach(record
                            -> kafkaService.sendSourceTask(StmtProcessor.create(IgSourceTask.class, ist -> {
                        ist.setSourceId(TestFuncs.generateLong() + record);
                        ist.setSourceUrl(TestFuncs.generateTestString20());

                        log.debug("send kafka msg: {}", ist);

                        StmtProcessor.execute(() -> Thread.sleep(100));

                    })));
        });
    }
}
