package org.dbs24.tik.assist.test.service;

import lombok.Data;
import lombok.extern.log4j.Log4j2;
import org.dbs24.tik.assist.TikAssist;
import org.dbs24.tik.assist.config.TikAssistConfig;
import org.dbs24.tik.assist.test.AbstractTikAssistTest;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.dbs24.consts.SysConst.ALL_PACKAGES;

@Data
@Log4j2
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = {TikAssist.class})
@Import({TikAssistConfig.class})
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@EntityScan(basePackages = {ALL_PACKAGES})
public class SearcherTests extends AbstractTikAssistTest {

/*    @Order(100)
    @Test
    public void test() {

        runTest(() -> {
            SearchLatestPostsRequestDto request = SearchLatestPostsRequestDto
                    .builder()
                    .username("buzova86")
                    .postNumbers(10)
                    .build();

            SearchLatestPostsResponseDto response = webTestClient
                    .post()
                    .uri(uriBuilder
                            -> uriBuilder
                            .path("https://linuxtech.io:5001/api/search_full")
                            .build())
                    .accept(APPLICATION_JSON)
                    .body(request, SearchLatestPostsRequestDto.class)
                    .exchange()
                    .expectStatus()
                    .isOk()
                    .expectBody(SearchLatestPostsResponseDto.class)
                    .returnResult()
                    .getResponseBody();

            log.info("->{}", response);
        });
    }*/
}
