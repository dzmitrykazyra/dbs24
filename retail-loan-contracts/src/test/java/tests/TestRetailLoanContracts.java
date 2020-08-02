/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tests;

import org.dbs24.application.core.log.LogService;
import org.dbs24.application.core.nullsafe.NullSafe;
import org.dbs24.application.core.nullsafe.StopWatcher;
import org.dbs24.entity.core.api.EntityContractConst;
import lombok.Data;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;
import config.TestRLCConfig;
import org.junit.Test;
import org.dbs24.entity.retail.loan.contracts.RetailLoanContract;
import org.dbs24.consts.RetailLoanContractConst;
import org.dbs24.entity.retail.loan.actions.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.HttpMethod;
import org.springframework.web.reactive.function.BodyInserters;

import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author Козыро Дмитрий
 */
@RunWith(SpringJUnit4ClassRunner.class)
//@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
//@AutoConfigureMockMvc
@Import({TestRLCConfig.class})
//@WebAppConfiguration("classpath:META-INF/web-resources")
//@DataJpaTest
//@Data
public class TestRetailLoanContracts extends TestUtil4LoanContract {

//    @Autowired
//    private MockMvc mockMvc;
    @Test
    public void testRest() throws Exception {
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

        final T retailLoanContract_old = (T) this.<T>loadLastEntity(classRetailLoanContract);

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
                EntityContractConst.ACT_AUTHORIZE_CONTRACT, null);

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
//                EntityContractConst.ACT_CALCULATE_TARIFFS);
//        this.getRetailLoanContractActionsService().executeAction(retailLoanContract, EntityContractConst.ACT_FINISH_CONTRACT);
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
