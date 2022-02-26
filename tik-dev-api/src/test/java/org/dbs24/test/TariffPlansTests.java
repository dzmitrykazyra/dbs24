/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.test;

import lombok.extern.log4j.Log4j2;
import org.dbs24.tik.dev.TikDevApi;
import org.dbs24.tik.dev.config.TikDevConfig;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.dbs24.consts.SysConst.ALL_PACKAGES;
import static org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@Log4j2
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = RANDOM_PORT, classes = {TikDevApi.class})
@Import({TikDevConfig.class})
@TestInstance(PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@EntityScan(basePackages = {ALL_PACKAGES})
public class TariffPlansTests extends AbstractTikDevApiTest {

    private Long createdTariffPlanId;

    @Order(100)
    @RepeatedTest(5)
    void createTariffPlan() {
        runTest(() -> createdTariffPlanId = createTestTariffPlan());
    }

    @Order(200)
    @Test
    void updateTariffPlan() { runTest(() -> createOrUpdateTestTariffPlan(createdTariffPlanId));
    }
}
