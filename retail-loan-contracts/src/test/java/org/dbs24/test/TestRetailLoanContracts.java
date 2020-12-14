/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.test;

import java.time.Duration;
import javax.ws.rs.core.HttpHeaders;
import static javax.ws.rs.core.HttpHeaders.USER_AGENT;
import org.dbs24.config.TestUtil4LoanContract;
import static org.dbs24.consts.EntityReferenceConst.*;
import lombok.extern.log4j.Log4j2;
import org.dbs24.config.RetailLoanContractWebSecurityConfig;
import org.dbs24.config.TestRetailLoanContractCommonConfig;
import org.dbs24.entity.RetailLoanContract;
import org.dbs24.consts.RetailLoanContractConst;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.junit.jupiter.api.Test;
import org.dbs24.config.TestRetailLoanContractBootApp;
import static org.dbs24.test.core.AbstractWebTest.WEBTEST_CLIENT_NAME;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;

@ExtendWith(SpringExtension.class)
@Log4j2
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT, classes = {TestRetailLoanContractBootApp.class})
//@Import({TestRetailLoanContractCommonConfig.class})
@Import({RetailLoanContractWebSecurityConfig.class})
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TestRetailLoanContracts extends TestUtil4LoanContract {

    @BeforeEach
    public void setUp() {
        log.info("setup test");
    }

    @Test
    @Order(100)
    public void testRest() {

        log.info("start test");

//        NullSafe.create()
//                .execute(() -> {
//
//                    this.mockMvc.perform(get(""//this.getREST_URL_4LOANCONTRACTS()
//                            .concat("/greeting")))
//                            .andDo(print()).andExpect(status().isOk())
//                            .andExpect(jsonPath("$.content").value("Hello, World!"));
//
//                })
//                .
//        throwException();
//        final String s1 = this.getRestTemplate()
//                .getForObject(this.getTestRestAddress().concat("/"),
//                        String.class);
//        LogService.LogInfo(this.getClass(), () -> s1);
//        final String s1 = this.getRestTemplate()
//                .getForObject(this.getTestRestAddress()
//                        .concat(LogService.PATH_REST)
//                        .concat("/testgreeting"),
//                        String.class);
//
//        LogService.LogInfo(this.getClass(), () -> s1);
//        WebClient.RequestHeadersSpec requestSpec1 = WebClient
//                .create(this.getTestRestAddress())
//                .method(HttpMethod.POST)
//                .uri(LogService.PATH_REST.concat(RetailLoanContractConst.REST_RETAIL_LOAN_REPAYMENT))
//                .body(BodyInserters.fromObject("bla-bla"));
//        
//        final WebClient client = WebClient.create(this.getTestRestAddress());
//
//        client.get()
//                .uri("/hello")
//                .accept(MediaType.TEXT_PLAIN)
//                .exchange()
//                .flatMap(res -> res.bodyToMono(String.class)).block();
//        final String s1 = this.getRestTemplate()
//                .postForObject(this.getTestRestAddress()
//                        .concat(LogService.PATH_REST)
//                        .concat(RetailLoanContractConst.REST_RETAIL_LOAN_REPAYMENT),
//                        (String) "bla-bla",
//                        String.class);
//
//        LogService.LogInfo(this.getClass(), () -> s1);
    }

    //@Test
    public <T extends RetailLoanContract> void testRetailLoanContract() {
        //this.initializeTest();

        final Class<T> classRetailLoanContract = (Class<T>) RetailLoanContract.class;

        final T retailLoanContract_old = this.<T>loadLastEntity(classRetailLoanContract);

        //retailLoanContract_old.getCounterparty().entityId();
        final T retailLoanContract = (T) this.createTestContract_1Y_840();
//        
//        
//            final Counterparty counterparty,
//            final EntityKind entityKind,
//            final Currency currency,
//            final TariffPlan tariffPlan,
//            final String contractNum,
//            final LocalDate contractDate,
//            final LocalDate beginDate,
//            final LocalDate endDate,
//            final BigDecimal contractSumm,
//            final LoanSource loanSource,
//            final PmtScheduleAlg pmtScheduleAlg,
//            final PmtScheduleTerm pmtScheduleTerm
        this.getRetailLoanContractActionsService().executeAction(
                retailLoanContract,
                RetailLoanContractConst.MODIFY_INDIVIDUAL_LOAN_CONTRACT, null);
        this.getRetailLoanContractActionsService().executeAction(
                retailLoanContract,
                ACT_AUTHORIZE_CONTRACT, null);

//        final Long entityId = retailLoanContract.getEntity_id();
//
////        final RetailLoanContract entityNew
////                = this.getRetailLoanContractActionsService()
////                        .reloadCreatedEntity(RetailLoanContract.class, entityId);
//        this.getRetailLoanContractActionsService().<ActIssueLoan>executeAction(
//                retailLoanContract,
//                RetailLoanContractConst.ACT_ISSUE_LOAN,
//                (action) -> {
//                    action.setLiasIssueSum(BigDecimal.valueOf(100.15));
//                    action.setLiasDate(LocalDate.now());
//                });
//        this.getRetailLoanContractActionsService().<ActIssueLoan>executeAction(
//                retailLoanContract,
//                RetailLoanContractConst.ACT_ISSUE_LOAN,
//                (action) -> {
//                    action.setLiasIssueSum(BigDecimal.valueOf(2100.13));
//                    action.setLiasDate(LocalDate.now().plusDays(10));
//                });
//
//        this.getRetailLoanContractActionsService().<ActIssueLoan>executeAction(
//                retailLoanContract,
//                RetailLoanContractConst.ACT_ISSUE_LOAN,
//                (action) -> {
//                    action.setLiasIssueSum(BigDecimal.valueOf(2301.13));
//                    action.setLiasDate(LocalDate.now().plusDays(20));
//                });
//
//        this.getRetailLoanContractActionsService().<ActRepaymentLoan>executeAction(
//                retailLoanContract,
//                RetailLoanContractConst.ACT_REPAYMENT_LOAN,
//                (action) -> {
//                    action.setLiasRepaymentSum(BigDecimal.valueOf(-120.24));
//                    action.setLiasDate(LocalDate.now().plusDays(25));
//                });
//        this.getRetailLoanContractActionsService().executeAction(
//                retailLoanContract,
//                ACT_CALCULATE_TARIFFS);
//        this.getRetailLoanContractActionsService().executeAction(retailLoanContract, ACT_FINISH_CONTRACT);
//        this.getRetailLoanContractActionsService()
//                .<T>reloadCreatedEntity(classRetailLoanContract, retailLoanContract.getEntity_id());
        this.<T>loadLastEntity(classRetailLoanContract);

        // n+1 тест
//        this.<T>loadAllEntities(classRetailLoanContract)
//                .stream()
//                .unordered()
//                .forEach(entity -> {
//                    final StopWatcher stopWatcher = StopWatcher.create(entity.getContractNum());
//                    //entity.getEntityActions().size();
//
//                    LogService.LogInfo(this.getClass(), () -> String.format("%s: %s",
//                            entity.getClass().getSimpleName(),
//                            stopWatcher.getStringExecutionTime()));
//                });
    }
}
