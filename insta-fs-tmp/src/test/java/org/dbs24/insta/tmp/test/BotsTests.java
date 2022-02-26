/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.insta.tmp.test;

import java.time.LocalDateTime;
import lombok.Data;
import lombok.extern.log4j.Log4j2;
import org.dbs24.application.core.locale.NLS;
import org.dbs24.application.core.service.funcs.TestFuncs;
import static org.dbs24.consts.SysConst.ALL_PACKAGES;
import org.dbs24.insta.tmp.InstaFacesSearch;
import org.dbs24.insta.tmp.config.InstafFsConfig;
import static org.dbs24.insta.tmp.consts.IfsConst.References.BotStatuses.BS_ACTUAL;
import static org.dbs24.insta.tmp.consts.IfsConst.RestQueryParams.QP_BOT_ID;
import static org.dbs24.insta.tmp.consts.IfsConst.UriConsts.URI_CREATE_OR_UPDATE_BOT;
import static org.dbs24.insta.tmp.consts.IfsConst.UriConsts.URI_GET_BOT;
import org.dbs24.insta.tmp.rest.api.BotInfo;
import org.dbs24.insta.tmp.rest.api.CreatedBot;
import org.dbs24.insta.tmp.rest.api.InstaAccountInfo;
import org.dbs24.stmt.StmtProcessor;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import reactor.core.publisher.Mono;

@Data
@Log4j2
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = {InstaFacesSearch.class})
@Import({InstafFsConfig.class})
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@EntityScan(basePackages = {ALL_PACKAGES})
public class BotsTests extends AbstractInstaFsTest {

    private Integer createdBotId;

    @Order(100)
    @Test
    @RepeatedTest(5)
    //@Transactional(readOnly = true)
    public void createBot() {

        runTest(() -> {

            log.info("testing {}", URI_CREATE_OR_UPDATE_BOT);

            final Mono<BotInfo> monoBot = Mono.just(StmtProcessor.create(BotInfo.class, botInfo -> {
                botInfo.setActualDate(NLS.localDateTime2long(LocalDateTime.now()));
                botInfo.setBotStatusId(BS_ACTUAL);
                botInfo.setEmail(TestFuncs.generateTestString15());
                botInfo.setPassword(TestFuncs.generateTestString15());
                botInfo.setSessionId(TestFuncs.generateTestString15());
                botInfo.setUserAgent(TestFuncs.generateTestString15());
                botInfo.setUserName(TestFuncs.generateTestString15());

            }));

            final CreatedBot createdBot
                    = webTestClient
                            .post()
                            .uri(uriBuilder
                                    -> uriBuilder
                                    .path(URI_CREATE_OR_UPDATE_BOT)
                                    .build())
                            .accept(APPLICATION_JSON)
                            .body(monoBot, BotInfo.class)
                            .exchange()
                            .expectStatus()
                            .isOk()
                            .expectBody(CreatedBot.class)
                            .returnResult()
                            .getResponseBody();

            log.info("{}: test result is '{}' ", URI_CREATE_OR_UPDATE_BOT, createdBot);

            createdBotId = createdBot.getBotId();

        });
    }

    @Order(200)
    @Test
    public void updateBot() {

        runTest(() -> {

            log.info("testing {}", URI_CREATE_OR_UPDATE_BOT);

            final Mono<BotInfo> monoBot = Mono.just(StmtProcessor.create(BotInfo.class, botInfo -> {
                botInfo.setActualDate(NLS.localDateTime2long(LocalDateTime.now()));
                botInfo.setBotId(createdBotId);
                botInfo.setBotStatusId(BS_ACTUAL);
                botInfo.setEmail(TestFuncs.generateTestString15());
                botInfo.setPassword(TestFuncs.generateTestString15());
                botInfo.setSessionId(TestFuncs.generateTestString15());
                botInfo.setUserAgent(TestFuncs.generateTestString15());
                botInfo.setUserName(TestFuncs.generateTestString15());

            }));

            final CreatedBot createdBot
                    = webTestClient
                            .post()
                            .uri(uriBuilder
                                    -> uriBuilder
                                    .path(URI_CREATE_OR_UPDATE_BOT)
                                    .build())
                            .accept(APPLICATION_JSON)
                            .body(monoBot, BotInfo.class)
                            .exchange()
                            .expectStatus()
                            .isOk()
                            .expectBody(CreatedBot.class)
                            .returnResult()
                            .getResponseBody();

            log.info("{}: test result is '{}' ", URI_CREATE_OR_UPDATE_BOT, createdBot);

            createdBotId = createdBot.getBotId();

        });
    }

    @Order(300)
    @Test
    public void getBot() {
        runTest(() -> {

            log.info("testing {}", URI_GET_BOT);

            final BotInfo botInfo = webTestClient
                    .get()
                    .uri(uriBuilder
                            -> uriBuilder
                            .path(URI_GET_BOT)
                            .queryParam(QP_BOT_ID, createdBotId)
                            .build())
                    .accept(APPLICATION_JSON)
                    .exchange()
                    .expectStatus()
                    .isOk()
                    .expectBody(BotInfo.class)
                    .returnResult()
                    .getResponseBody();

            log.info("receive botInfo = {} ", botInfo);

        });
    }
}
