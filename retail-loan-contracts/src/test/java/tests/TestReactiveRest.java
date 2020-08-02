/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tests;

import org.dbs24.application.core.locale.NLS;
import org.dbs24.application.core.nullsafe.NullSafe;
import org.dbs24.application.core.log.LogService;
import org.dbs24.config.RetailLoanContractWebSecurityConfig;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.http.MediaType;
import org.dbs24.entity.core.api.EntityContractConst;
import org.dbs24.entity.retail.loan.contracts.RetailLoanContract;
import org.dbs24.consts.RetailLoanContractConst;
import org.dbs24.lias.opers.attrs.LIAS_DATE;
import org.dbs24.lias.opers.attrs.LIAS_SUMM;
import java.math.BigDecimal;
import java.time.LocalDate;
import org.junit.jupiter.api.BeforeEach;
import java.time.Duration;
import org.springframework.beans.factory.annotation.Value;
import javax.ws.rs.core.HttpHeaders;
import org.springframework.security.crypto.password.PasswordEncoder;
import static org.springframework.web.reactive.function.client.ExchangeFilterFunctions.basicAuthentication;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;

import reactor.core.publisher.Mono;

@ExtendWith(SpringExtension.class)
//@WebFluxTest
//  We create a `@SpringBootTest`, starting an actual server on a `RANDOM_PORT`
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
//@EnableAutoConfiguration(exclude = ReactiveSecurityAutoConfiguration.class)
@Import({RetailLoanContractWebSecurityConfig.class})
public class TestReactiveRest extends TestUtil4LoanContract {

    @Value("${reactive.rest.timeout:5000}")
    private Integer timeoutDefault = 50000;

    @Value("${webflux.security.uid}")
    private String uid = "no_uid";

    @Value("${webflux.security.pwd}")
    private String pwd = "no_pwd";

    // Spring Boot will create a `WebTestClient` for you,
    // already configure and ready to issue requests against "localhost:RANDOM_PORT"
    @Autowired
    private WebTestClient webTestClient;

//    @Autowired
//    private PasswordEncoder passwordEncoder;
    @BeforeEach
    public void setUp() {

        webTestClient = webTestClient
                .mutate()
                .responseTimeout(Duration.ofMillis(timeoutDefault))
                //                .defaultHeaders(header
                //                        -> header.setBasicAuth(this.getEnvironment().getProperty("webflux.security.uid"),
                //                        this.getEnvironment().getProperty("webflux.security.pwd")))
                //.defaultHeaders(header -> header.setBasicAuth(this.uid, this.pwd, StandardCharsets.UTF_8))
                //.defaultHeaders(header -> header.setBasicAuth(this.uid, this.pwd))
                .filter(basicAuthentication(this.uid, this.pwd))
                .defaultHeader(HttpHeaders.USER_AGENT, "WebTestClient")
                //            .defaultHeader(HttpHeaders.AUTHORIZATION, passwordEncoder.encode(this.uid + ":" + this.pwd))
                .build();
    }

    @Test
    public void testCreateRetailLoanContract() {

//        final String pwd4msg = NullSafe.create("")
//                .execute2result(()
//                        -> Base64Utils
//                        .encodeToString((this.uid + ":" + this.pwd).getBytes(StandardCharsets.UTF_8)))
//                .<String>getObject();
        //final String pwd4msg = passwordEncoder.encode(this.uid + ":" + this.pwd);
        //final String pwd4msg = this.uid + ":" + this.pwd;
        //retailLoanContract_old.getCounterparty().entityId();
        final RetailLoanContract retailLoanContract = this.createTestContract_1Y_840();
        final Class<RetailLoanContract> classRetailLoanContract = (Class<RetailLoanContract>) retailLoanContract.getClass();
        final Mono<RetailLoanContract> monoRetailLoanContract = Mono.just(retailLoanContract);

        webTestClient
                //                .mutate().filter(basicAuthentication(this.uid, this.pwd)).build()
                .post()
                .uri(EntityContractConst.URI_CREATE_LOAN_CONTRACT)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                //.header(HttpHeaders.AUTHORIZATION, String.format("Basic %s", pwd4msg))
                .body(monoRetailLoanContract, classRetailLoanContract)
                .exchange()
                //.onStatus(HttpStatus::is5xxServerError, clientResponse -> Mono.error(new Pizza5xxException()))
                // and use the dedicated DSL to test assertions against the response
                .expectStatus()
                .isOk();

        // перечитываем созданную сущность
        final RetailLoanContract createdRetailLoanContract = this.<RetailLoanContract>loadLastEntity(classRetailLoanContract);
        //======================================================================        
        // действие авторизация договора
        webTestClient
                .post()
                .uri(uriBuilder
                        -> uriBuilder
                        .path(EntityContractConst.URI_EXECUTE_ACTION)
                        .queryParam("actionId", EntityContractConst.ACT_AUTHORIZE_CONTRACT)
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
        //======================================================================
        // выдача кредита

        webTestClient
                .post()
                .uri(uriBuilder
                        -> uriBuilder
                        .path(EntityContractConst.URI_EXECUTE_ACTION)
                        .queryParam("actionId", RetailLoanContractConst.ACT_ISSUE_LOAN)
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
        //======================================================================
        // дополнительная выдача кредита

        webTestClient
                .post()
                .uri(uriBuilder
                        -> uriBuilder
                        .path(EntityContractConst.URI_EXECUTE_ACTION)
                        .queryParam("actionId", RetailLoanContractConst.ACT_ISSUE_LOAN)
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
        //======================================================================
        // пересчет тарифицируемых сумм
        webTestClient
                .post()
                .uri(uriBuilder
                        -> uriBuilder
                        .path(EntityContractConst.URI_EXECUTE_ACTION)
                        .queryParam("actionId", EntityContractConst.ACT_CALCULATE_TARIFFS)
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

        LogService.LogInfo(createdRetailLoanContract.getClass(), () -> String.format("Entity successfully created (entity_id=%d) ",
                createdRetailLoanContract.entityId()).toUpperCase());
    }
}
