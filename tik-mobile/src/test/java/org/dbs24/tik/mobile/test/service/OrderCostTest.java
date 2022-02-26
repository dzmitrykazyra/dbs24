package org.dbs24.tik.mobile.test.service;

import lombok.extern.log4j.Log4j2;
import org.dbs24.test.core.AbstractWebTest;
import org.dbs24.tik.mobile.TikMobile;
import org.dbs24.tik.mobile.config.TikMobileConfig;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import static org.dbs24.consts.SysConst.ALL_PACKAGES;

@Log4j2
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = {TikMobile.class})
@Import({TikMobileConfig.class})
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@EntityScan(basePackages = {ALL_PACKAGES})
public class OrderCostTest extends AbstractWebTest {

    //based on action type
    //based on actions quantity
    //based on dollar to heart proportion

    @Order(100)
    @RepeatedTest(1)
    @Transactional
    public void costByActionType() {

    }

    @Order(200)
    @RepeatedTest(1)
    @Transactional
    public void costByActionsQuantity() {

    }

    @Order(300)
    @RepeatedTest(1)
    @Transactional
    public void heartCost() {

    }
}
