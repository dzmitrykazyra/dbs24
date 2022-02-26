/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.test;

import lombok.extern.log4j.Log4j2;
import org.dbs24.app.promo.AppPromoution;
import org.dbs24.app.promo.component.OrdersActionsService;
import org.dbs24.app.promo.component.OrdersService;
import org.dbs24.app.promo.config.AppPromoutionConfig;
import org.dbs24.app.promo.entity.BatchSetup;
import org.dbs24.application.core.service.funcs.TestFuncs;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;
import java.util.stream.IntStream;

import static org.dbs24.consts.SysConst.ALL_PACKAGES;
import static org.dbs24.consts.SysConst.INTEGER_ONE;
import static org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@Log4j2
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = RANDOM_PORT, classes = {AppPromoution.class})
@Import({AppPromoutionConfig.class})
@TestInstance(PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@EntityScan(basePackages = {ALL_PACKAGES})
public class OrderActionsTests extends AbstractAppPromoTest {

    private Integer createdOrderActionId;
    final OrdersActionsService ordersActionsService;
    final OrdersService ordersService;

    @Autowired
    public OrderActionsTests(OrdersActionsService ordersActionsService, OrdersService ordersService) {
        this.ordersActionsService = ordersActionsService;
        this.ordersService = ordersService;
    }

    @Order(100)
    @RepeatedTest(5)
    public void createOrderAction() {
        runTest(() -> createdOrderActionId = createOrUpdateTestOrderAction(null));
    }

    @Order(200)
    @Test
    public void updateOrderAction() {
        runTest(() -> createOrUpdateTestOrderAction(createdOrderActionId));
    }

    @Order(300)
    @Test
    public void createOrderActionFromBatchTemplate() {

        runTest(() -> {
            // new batchTemplate
            final Integer newBatchTemplateId = createOrUpdateTestBatchTemplate(null);

            // new batchSetups
            IntStream.range(INTEGER_ONE, TestFuncs.generateTestRangeInteger(8, 15))
                    .forEach(i -> createOrUpdateTestBatchSetup(newBatchTemplateId, null));

            // new order
            final Integer newOrderId = createOrUpdateTestOrder(null, newBatchTemplateId);

            //protected Integer createOrUpdateTestOrderAction(Integer orderId, Integer orderActionId) {

            // Order object
            final org.dbs24.app.promo.entity.Order order = ordersService.findOrder(newOrderId);

            final Optional<BatchSetup> optionalBatchSetup = ordersActionsService.getNextBatchSetup(order);

            optionalBatchSetup.ifPresent( bs -> log.debug("getNextBatchSetup: use batchSetup {}", bs.getBatchSetupId()));

        });
    }

}
