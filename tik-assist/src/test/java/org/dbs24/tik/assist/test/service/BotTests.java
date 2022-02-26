/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.tik.assist.test.service;

import lombok.Data;
import lombok.extern.log4j.Log4j2;

import static org.dbs24.consts.SysConst.ALL_PACKAGES;
import org.dbs24.stmt.StmtProcessor;
import org.dbs24.tik.assist.constant.ApiPath;
import org.dbs24.tik.assist.constant.RequestQueryParam;
import org.dbs24.tik.assist.test.AbstractTikAssistTest;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.dbs24.tik.assist.TikAssist;
import org.dbs24.tik.assist.config.TikAssistConfig;
import org.dbs24.tik.assist.entity.dto.bot.CreatedBotDto;
import org.dbs24.tik.assist.entity.dto.bot.BotDto;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import reactor.core.publisher.Mono;

@Data
@Log4j2
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = {TikAssist.class})
@Import({TikAssistConfig.class})
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@EntityScan(basePackages = {ALL_PACKAGES})
public class BotTests extends AbstractTikAssistTest {

    private Integer createdAgentId;

    @Order(100)
    //@Test
    @RepeatedTest(1)
    public void createAgent() {

        runTest(() -> {

            final Mono<BotDto> monoBot = Mono.just(StmtProcessor.create(BotDto.class, botDto -> {

                botDto.setBotStatusId(2);
                botDto.setBotRegistrationTypeId(1);
                botDto.setSecUserId("dafeaefdaf");
                botDto.setPhoneNumber("13431534551");
                botDto.setEmail("bot@email.com");
                botDto.setBotNote("some note");
                botDto.setPassword("314314dpass");
            }));

            final CreatedBotDto createdBotDto
                    = webTestClient
                            .post()
                            .uri(uriBuilder
                                    -> uriBuilder
                                    .path(ApiPath.URI_CREATE_OR_UPDATE_BOT)
                                    .build())
                            .accept(APPLICATION_JSON)
                            .body(monoBot, BotDto.class)
                            .exchange()
                            .expectStatus()
                            .isOk()
                            .expectBody(CreatedBotDto.class)
                            .returnResult()
                            .getResponseBody();

            log.info("Created bot DTO : {} ", createdBotDto);

        });
    }

    @Order(300)
    @Test
    public void getBot() {

        runTest(() -> {

            final BotDto botDto
                    = webTestClient
                    .get()
                    .uri(uriBuilder
                            -> uriBuilder
                            .path(ApiPath.URI_GET_BOT_BY_ID)
                            .queryParam(RequestQueryParam.QP_BOT_ID, 14)
                            .build())
                    .accept(APPLICATION_JSON)
                    .exchange()
                    .expectStatus()
                    .isOk()
                    .expectBody(BotDto.class)
                    .returnResult()
                    .getResponseBody();
            log.info("GOT BOT : {}", botDto);
        });
    }
}
