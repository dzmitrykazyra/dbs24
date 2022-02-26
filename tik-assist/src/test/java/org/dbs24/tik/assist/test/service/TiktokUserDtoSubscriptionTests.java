package org.dbs24.tik.assist.test.service;

import lombok.Data;
import lombok.extern.log4j.Log4j2;
import org.dbs24.stmt.StmtProcessor;
import org.dbs24.tik.assist.TikAssist;
import org.dbs24.tik.assist.config.TikAssistConfig;
import org.dbs24.tik.assist.constant.ApiPath;
import org.dbs24.tik.assist.entity.dto.subscription.UserSubscriptionIdDto;
import org.dbs24.tik.assist.entity.dto.subscription.ByTemplateUserSubscriptionDto;
import org.dbs24.tik.assist.test.AbstractTikAssistTest;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import reactor.core.publisher.Mono;

import static org.dbs24.consts.SysConst.ALL_PACKAGES;
import static org.springframework.http.MediaType.APPLICATION_JSON;

@Data
@Log4j2
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = {TikAssist.class})
@Import({TikAssistConfig.class})
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@EntityScan(basePackages = {ALL_PACKAGES})
public class TiktokUserDtoSubscriptionTests extends AbstractTikAssistTest {

    //todo change
    @Order(100)
    @Test
    public void subscriptionByTemplate() {

        runTest(() -> {

            final Mono<ByTemplateUserSubscriptionDto> userSubscriptionDtoMono = Mono.just(
                    StmtProcessor.create(
                            ByTemplateUserSubscriptionDto.class,
                            byTemplateUserSubscriptionDto -> {
                                byTemplateUserSubscriptionDto.setSubscriptionTemplateId(1);
                                byTemplateUserSubscriptionDto.setPromocodeValue("promocodeeeeee");
                            }
                    )
            );

            final UserSubscriptionIdDto userSubscriptionIdDto
                    = webTestClient
                    .post()
                    .uri(uriBuilder
                            -> uriBuilder
                            .path(ApiPath.URI_CREATE_USER_SUBSCRIPTION_BY_TEMPLATE)
                            .build())
                    .accept(APPLICATION_JSON)
                    .body(userSubscriptionDtoMono, ByTemplateUserSubscriptionDto.class)
                    .exchange()
                    .expectStatus()
                    .isOk()
                    .expectBody(UserSubscriptionIdDto.class)
                    .returnResult()
                    .getResponseBody();
        });
    }
}
