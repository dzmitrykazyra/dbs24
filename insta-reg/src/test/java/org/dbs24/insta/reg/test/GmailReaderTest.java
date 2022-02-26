/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.insta.reg.test;

import lombok.Data;
import lombok.extern.log4j.Log4j2;
import static org.dbs24.consts.SysConst.ALL_PACKAGES;
import org.dbs24.insta.reg.InstaRegistry;
import org.dbs24.insta.reg.config.InstaRegConfig;
import org.dbs24.insta.reg.email.EmailEntry;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import static org.springframework.http.HttpHeaders.ACCEPT_ENCODING;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import static org.springframework.http.MediaType.APPLICATION_XML_VALUE;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;
import org.dbs24.insta.reg.component.EmailService;

@Data
@Log4j2
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = {InstaRegistry.class})
@Import({InstaRegConfig.class})
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@EntityScan(basePackages = {ALL_PACKAGES})
public class GmailReaderTest extends AbstractInstaRegTest {

    final EmailService emailService;
    
    public GmailReaderTest(EmailService emailService) {
        this.emailService = emailService;
    }
    
    @Order(100)
    @Test
    //@RepeatedTest(1)
    @DisplayName("test2")
    @Transactional(readOnly = true)
    public void testMailReading() {
//        final Mono<Object> monoEmailEntry
//                = gmailReader()
//                        .get()
//                        .uri(uriBuilder
//                                -> uriBuilder
//                                //                                        .path("/readMessages")
//                                .queryParam("email", email)
//                                .queryParam("pass", pass)
//                                .queryParam("proxy_host", proxyHost)
//                                .queryParam("proxy_port", proxyPort)
//                                .queryParam("proxy_user", proxyUser)
//                                .queryParam("proxy_pass", proxyPass)
//                                .build())
//                        //.header(ACCEPT, acceptHeader)
//                        //.header(USER_AGENT, getUserAgent())
//                        //.header(ACCEPT_LANGUAGE, acceptLanguage)
//
//                        .accept(MediaType.APPLICATION_JSON, MediaType.APPLICATION_CBOR, MediaType.APPLICATION_XML)
//                        .header(ACCEPT_ENCODING, "gzip")
//                        .header(CONTENT_TYPE, APPLICATION_XML_VALUE)
//                        .exchangeToMono(response -> {
//
//                            log.info("headers is {}", response.headers());
//
//                            if (response.statusCode().equals(HttpStatus.OK)) {
//                                return response.bodyToMono(EmailEntry.class);
//                            } else if (response.statusCode().is4xxClientError()) {
//                                // Suppress error status code
//                                return response.bodyToMono(EmailEntry.class);
//                            } else {
//                                // Turn to error
//                                return response.createException().flatMap(Mono::error);
//                            }
//                        });
    }

}
