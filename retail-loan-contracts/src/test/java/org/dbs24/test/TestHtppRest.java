/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.test;

import org.dbs24.config.TestUtil4LoanContract;
import org.dbs24.application.core.locale.NLS;
import org.dbs24.config.RetailLoanContractWebSecurityConfig;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.http.MediaType;
import static org.dbs24.consts.EntityReferenceConst.*;
import org.dbs24.entity.RetailLoanContract;
import static org.dbs24.consts.RetailLoanContractConst.*;
import org.dbs24.lias.opers.attrs.LIAS_DATE;
import org.dbs24.lias.opers.attrs.LIAS_SUMM;
import java.math.BigDecimal;
import java.time.LocalDate;
import org.junit.jupiter.api.BeforeEach;
import java.time.Duration;
import org.springframework.beans.factory.annotation.Value;
import javax.ws.rs.core.HttpHeaders;
import lombok.extern.log4j.Log4j2;
import org.dbs24.config.TestRetailLoanContractBootApp;
import org.dbs24.test.core.AbstractWebTest;
import org.dbs24.test.service.TestRetailLoanContractsService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import static org.springframework.web.reactive.function.client.ExchangeFilterFunctions.basicAuthentication;
import reactor.core.publisher.Mono;

@Log4j2
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = {TestRetailLoanContractBootApp.class})
//@EnableAutoConfiguration(exclude = ReactiveSecurityAutoConfiguration.class)
@Import({RetailLoanContractWebSecurityConfig.class})
@AutoConfigureWebTestClient
public class TestHtppRest extends AbstractWebTest {

    @Value("${webflux.security.uid}")
    private String uid = "no_uid";

    @Value("${webflux.security.pwd}")
    private String pwd = "no_pwd";

    @Autowired
    TestRetailLoanContractsService testRetailLoanContractsService;
    
//    @Autowired
//    private PasswordEncoder passwordEncoder;
    @BeforeEach
    public void setUp() {
        log.info("setup test");
    }

    @DisplayName("makes 10 fireAndForget requests")
    @Order(100)
    @Test
    public void testCreateRetailLoanContract() {

        final RetailLoanContract retailLoanContract = testRetailLoanContractsService.createTestContract_1Y_840();
        final Class<RetailLoanContract> classRetailLoanContract = (Class<RetailLoanContract>) retailLoanContract.getClass();
        final Mono<RetailLoanContract> monoRetailLoanContract = Mono.just(retailLoanContract);

        this.runTest(() -> {

            log.info("testing {}", URI_CREATE_LOAN_CONTRACT);

//        final String pwd4msg = NullSafe.create("")
//                .execute2result(()
//                        -> Base64Utils
//                        .encodeToString((this.uid + ":" + this.pwd).getBytes(StandardCharsets.UTF_8)))
//                .<String>getObject();
            //final String pwd4msg = passwordEncoder.encode(this.uid + ":" + this.pwd);
            //final String pwd4msg = this.uid + ":" + this.pwd;
            //retailLoanContract_old.getCounterparty().entityId();
            webTestClient
                    //                .mutate().filter(basicAuthentication(this.uid, this.pwd)).build()
                    .post()
                    .uri(URI_CREATE_LOAN_CONTRACT)
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
                    //.header(HttpHeaders.AUTHORIZATION, String.format("Basic %s", pwd4msg))
                    .body(monoRetailLoanContract, classRetailLoanContract)
                    .exchange()
                    //.onStatus(HttpStatus::is5xxServerError, clientResponse -> Mono.error(new Pizza5xxException()))
                    // and use the dedicated DSL to test assertions against the response
                    .expectStatus()
                    .isOk();

        });

        // перечитываем созданную сущность
        final RetailLoanContract createdRetailLoanContract = this.<RetailLoanContract>loadLastEntity(classRetailLoanContract);

        //======================================================================        
        this.runTest(() -> {

            log.info("testing {}, {}",
                    URI_EXECUTE_ACTION, ACT_AUTHORIZE_CONTRACT);
            // действие авторизация договора
            webTestClient
                    .post()
                    .uri(uriBuilder
                            -> uriBuilder
                            .path(URI_EXECUTE_ACTION)
                            .queryParam("actionId", ACT_AUTHORIZE_CONTRACT)
                            .queryParam("entityId", createdRetailLoanContract.entityId())
                            .build())
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
                    //.body(createdRetailLoanContract, classRetailLoanContract)
                    .exchange()
                    //.onStatus(HttpStatus::is5xxServerError, clientResponse -> Mono.error(new Pizza5xxException()))
                    // and use the dedicated DSL to test assertions against the response
                    .expectStatus()
                    .isOk();
        });
        //======================================================================
        // выдача кредита

        this.runTest(() -> {

            log.info("testing {}, {}", URI_EXECUTE_ACTION, ACT_ISSUE_LOAN);
            webTestClient
                    .post()
                    .uri(uriBuilder
                            -> uriBuilder
                            .path(URI_EXECUTE_ACTION)
                            .queryParam("actionId", ACT_ISSUE_LOAN)
                            .queryParam("entityId", createdRetailLoanContract.entityId())
                            .queryParam(LIAS_SUMM.class.getSimpleName(), BigDecimal.valueOf(2100.13))
                            .queryParam(LIAS_DATE.class.getSimpleName(), NLS.getStringDate(LocalDate.now().plusDays(10)))
                            .build())
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
                    //                .body(createdRetailLoanContract, classRetailLoanContract)
                    .exchange()
                    //.onStatus(HttpStatus::is5xxServerError, clientResponse -> Mono.error(new Pizza5xxException()))
                    // and use the dedicated DSL to test assertions against the response
                    .expectStatus()
                    .isOk();
        });

        //======================================================================
        // дополнительная выдача кредита
        this.runTest(() -> {

            log.info("testing {}, {}", URI_EXECUTE_ACTION, ACT_ISSUE_LOAN);

            webTestClient
                    .post()
                    .uri(uriBuilder
                            -> uriBuilder
                            .path(URI_EXECUTE_ACTION)
                            .queryParam("actionId", ACT_ISSUE_LOAN)
                            .queryParam("entityId", createdRetailLoanContract.entityId())
                            .queryParam(LIAS_SUMM.class.getSimpleName(), BigDecimal.valueOf(4240.18))
                            .queryParam(LIAS_DATE.class.getSimpleName(), NLS.getStringDate(LocalDate.now().plusDays(25)))
                            .build())
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
                    //                .body(createdRetailLoanContract, classRetailLoanContract)
                    .exchange()
                    //.onStatus(HttpStatus::is5xxServerError, clientResponse -> Mono.error(new Pizza5xxException()))
                    // and use the dedicated DSL to test assertions against the response
                    .expectStatus()
                    .isOk();

        });
        //======================================================================
        // пересчет тарифицируемых сумм

        this.runTest(() -> {

            log.info("testing {}, {}", URI_EXECUTE_ACTION, ACT_CALCULATE_TARIFFS);

            webTestClient
                    .post()
                    .uri(uriBuilder
                            -> uriBuilder
                            .path(URI_EXECUTE_ACTION)
                            .queryParam("actionId", ACT_CALCULATE_TARIFFS)
                            .queryParam("entityId", createdRetailLoanContract.entityId())
                            //                        .queryParam(LIAS_SUMM.class.getSimpleName(), BigDecimal.valueOf(4240.18))
                            //                        .queryParam(LIAS_DATE.class.getSimpleName(), NLS.getStringDate(LocalDate.now().plusDays(25)))
                            .build())
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
                    //                .body(createdRetailLoanContract, classRetailLoanContract)
                    .exchange()
                    //.onStatus(HttpStatus::is5xxServerError, clientResponse -> Mono.error(new Pizza5xxException()))
                    // and use the dedicated DSL to test assertions against the response
                    .expectStatus()
                    .isOk();
        });
    }
}
