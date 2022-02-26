/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.test;

import lombok.Data;
import lombok.extern.log4j.Log4j2;
import org.dbs24.app.promo.AppPromoution;
import org.dbs24.app.promo.config.AppPromoutionConfig;
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
@SpringBootTest(webEnvironment = RANDOM_PORT, classes = {AppPromoution.class})
@Import({AppPromoutionConfig.class})
@TestInstance(PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@EntityScan(basePackages = {ALL_PACKAGES})
public class PackagesTests extends AbstractAppPromoTest {

    private Integer createdPackageId;

    @Order(100)
    @RepeatedTest(5)
    public void createPackage() {
        runTest(() -> createdPackageId = createOrUpdateTestPackage(null));
    }

    @Order(200)
    @Test
    public void updatePackage() {
        runTest(() -> createOrUpdateTestPackage(createdPackageId));
    }
}
