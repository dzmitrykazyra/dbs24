/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.insta.reg.test;

import java.time.LocalDateTime;
import lombok.Data;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import lombok.extern.log4j.Log4j2;
import org.dbs24.insta.reg.InstaRegistry;
import static org.dbs24.consts.SysConst.ALL_PACKAGES;
import org.dbs24.insta.reg.config.InstaRegConfig;
import static org.dbs24.insta.reg.consts.InstaConsts.UriConsts.*;
//import static org.dbs24.consts.WaConsts.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import org.springframework.transaction.annotation.Transactional;
import org.dbs24.insta.reg.rest.api.*;

@Data
@Log4j2
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = {InstaRegistry.class})
@Import({InstaRegConfig.class})
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@EntityScan(basePackages = {ALL_PACKAGES})
public class EmailGoogleReaderTest extends AbstractInstaRegTest {

    @Order(200)
    //@Test
    //@RepeatedTest(1)
    @DisplayName("test2")
    @Transactional(readOnly = true)
    public void testMailReading() {

        runTest(() -> {

            final String emailDetils
                    = this.getEmailService()
                            .getEmailsList()
                            .entrySet()
                            .stream()
                            .skip(getRandom().nextInt(getEmailService().getEmailsList().size() - 1))
                            .findAny()
                            .orElseThrow()
                            .getValue();

            final String userName = emailDetils.split(":")[0];
            final String pass = emailDetils.split(":")[1];

//            log.info("testMailReading: testing {}/{} ", userName, pass);

            this.getEmailService().validateGmailAccount(userName, pass);

        });
    }

    //==========================================================================
    @Order(300)
    //@Test
    @DisplayName("test2")
    public void testGetFakedMail() {

        log.info("testing {}", URI_GET_FAKED_MAIL);

        this.runTest(() -> {

            log.info("testing {}", URI_GET_FAKED_MAIL);


            final FakedMail fakedMail
                    = webTestClient
                            .get()
                            .uri(uriBuilder
                                    -> uriBuilder
                                    .path(URI_GET_FAKED_MAIL)
                                    .build())
                            .accept(APPLICATION_JSON)
                            .exchange()
                            .expectStatus()
                            .isOk()
                            .expectBody(FakedMail.class)
                            .returnResult()
                            .getResponseBody();

            log.info("{}: test result is '{}' ", URI_GET_FAKED_MAIL, fakedMail);
            
            //StmtProcessor.sleep(50000);

        });
    }
    //==========================================================================
    @Order(400)
    @Test
    @DisplayName("test4")
    public void testLatestException() {

        log.info("testing {}", URI_GET_LATEST_EXCEPTIONS);

        this.runTest(() -> {

            log.info("testing {}", URI_GET_LATEST_EXCEPTIONS);

            final AccountActionsCollection accountActionsCollection
                    = webTestClient
                            .get()
                            .uri(uriBuilder
                                    -> uriBuilder
                                    .path(URI_GET_LATEST_EXCEPTIONS)
                                    .build())
                            .accept(APPLICATION_JSON)
                            .exchange()
                            .expectStatus()
                            .isOk()
                            .expectBody(AccountActionsCollection.class)
                            .returnResult()
                            .getResponseBody();

            log.info("{}: test result is '{}' records ", URI_GET_LATEST_EXCEPTIONS, accountActionsCollection.getActions().size());
            
            //StmtProcessor.sleep(50000);

        });
    }

}
