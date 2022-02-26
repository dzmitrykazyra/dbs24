package org.dbs24.test;

import io.jsonwebtoken.lang.Collections;
import lombok.extern.log4j.Log4j2;
import org.dbs24.proxy.core.ProxyCoreApplication;
import org.dbs24.proxy.core.config.ProxyCoreConfig;
import org.dbs24.proxy.core.config.ProxyCoreRestConfig;
import org.dbs24.proxy.core.entity.domain.Proxy;
import org.dbs24.proxy.core.entity.dto.BookProxiesDto;
import org.dbs24.proxy.core.entity.dto.BookProxyByIdDto;
import org.dbs24.proxy.core.entity.dto.request.BookProxiesRequest;
import org.dbs24.proxy.core.entity.dto.request.BookProxyRequest;
import org.dbs24.proxy.core.entity.dto.response.BookedProxyResponse;
import org.dbs24.proxy.core.entity.dto.response.ProxyListResponse;
import org.dbs24.rest.api.action.SimpleActionInfo;
import org.dbs24.stmt.StmtProcessor;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import reactor.core.publisher.Mono;

import java.util.List;

import static org.dbs24.consts.SysConst.ALL_PACKAGES;
import static org.dbs24.proxy.core.rest.api.consts.ProxyHttpConsts.RestQueryParams.QP_PROXY_ID;
import static org.dbs24.proxy.core.rest.api.consts.ProxyHttpConsts.RestQueryParams.QP_BOOKING_TIME_MILLIS;
import static org.dbs24.proxy.core.rest.api.consts.ProxyHttpConsts.RestQueryParams.QP_APPLICATION_NAME;
import static org.dbs24.rest.api.action.RestAction.CREATE_ENTITY;
import static org.dbs24.rest.api.action.RestAction.MODIFY_ENTITY;
import static org.springframework.http.MediaType.APPLICATION_JSON;

@Log4j2
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = {ProxyCoreApplication.class})
@Import({ProxyCoreConfig.class})
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@EntityScan(basePackages = {ALL_PACKAGES})
public class BookProxiesTests extends AbstractProxyTest {

    @Order(100)
    @Test
    //@RepeatedTest(5)
    public void bookProxiesList() {

        log.info("START");
        runTest(() -> {

            List<Proxy> oldBookedProxyList = bookMultipleProxiesFacebookNetwork();

            //check proxies from response to match required data with provided

            oldBookedProxyList.forEach(
                    proxy -> {
                        log.info("OLD BOOKED PROXY ID: {}", proxy.getProxyId());

                        Assertions.assertEquals(proxy.getProxyStatus().getProxyStatusName(), "PS_ACTUAL");
                        Assertions.assertEquals(proxy.getProxyProvider().getProviderName(), "ZetaSol");
                    });

            //book once again (while previous proxies are still in usage and check that fresh booked list does not contain old booked list elements)

            List<Proxy> freshBookedProxyList = bookMultipleProxiesFacebookNetwork();


            Assertions.assertFalse(Collections.containsAny(oldBookedProxyList, freshBookedProxyList));

            //and get fresh proxies by other network and check ability to use same proxies

            List<Proxy> freshTiktokBookedProxyList = bookMultipleProxiesTikTokNetwork();


            //assertions and ids console output
            freshBookedProxyList.forEach(
                    proxy -> {
                        log.info("[IG]FRESH BOOKED PROXY ID: {}", proxy.getProxyId());

                        Assertions.assertEquals(proxy.getProxyStatus().getProxyStatusName(), "PS_ACTUAL");
                        Assertions.assertEquals(proxy.getProxyProvider().getProviderName(), "ZetaSol");
                    });
            freshTiktokBookedProxyList.forEach(
                    proxy -> {
                        log.info("[TIKTOK]FRESH BOOKED PROXY ID: {}", proxy.getProxyId());

                        Assertions.assertEquals(proxy.getProxyStatus().getProxyStatusName(), "PS_ACTUAL");
                        Assertions.assertEquals(proxy.getProxyProvider().getProviderName(), "ZetaSol");
                    });



            // also, can be false if there are so many active and not busy proxies in db
            Assertions.assertTrue(Collections.containsAny(freshTiktokBookedProxyList, freshBookedProxyList));
        });
    }

    private List<Proxy> bookMultipleProxiesFacebookNetwork() {
        final BookProxiesDto requirementsDto = StmtProcessor.create(BookProxiesDto.class, proxyRequestDto -> {
            proxyRequestDto.setAmount(5);
            proxyRequestDto.setProxyTypeName("PT.OWN_MOBILE");
            proxyRequestDto.setProviderName("ZetaSol");
            proxyRequestDto.setCountryName("any");
            proxyRequestDto.setApplicationName("instagram");
            proxyRequestDto.setBookingTimeMillis(1000000);
        });

        final Mono<BookProxiesRequest> mono = Mono.just(StmtProcessor.create(BookProxiesRequest.class, bookProxiesRequest -> {

            bookProxiesRequest.setEntityInfo(requirementsDto);

            bookProxiesRequest.setEntityAction(StmtProcessor.create(SimpleActionInfo.class,
                    sai -> sai.setActionCode(MODIFY_ENTITY)
            ));
        }));

        ProxyListResponse responseBody = webTestClient
                .post()
                .uri(uriBuilder
                        -> uriBuilder
                        .path(ProxyCoreRestConfig.URI_BOOK)
                        .build())
                .accept(APPLICATION_JSON)
                .body(mono, BookProxiesRequest.class)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(ProxyListResponse.class)
                .returnResult()
                .getResponseBody();

        List<Proxy> bookedProxyList = responseBody.getCreatedEntity().getBookedProxyList();
        String responseBodyMessage = responseBody.getMessage();

        log.info("RESPONSE BODY MESSAGE : {}", responseBodyMessage);
        bookedProxyList.forEach(proxy -> log.info("GOT BOOKED PROXY FROM RESPONSE: {}", proxy));

        return bookedProxyList;
    }

    private List<Proxy> bookMultipleProxiesTikTokNetwork() {
        final BookProxiesDto requirementsDto = StmtProcessor.create(BookProxiesDto.class, proxyRequestDto -> {
            proxyRequestDto.setAmount(5);
            proxyRequestDto.setProxyTypeName("PT.OWN_MOBILE");
            proxyRequestDto.setProviderName("ZetaSol");
            proxyRequestDto.setCountryName("any");
            proxyRequestDto.setApplicationName("tiktok");
            proxyRequestDto.setBookingTimeMillis(1000000);
        });

        final Mono<BookProxiesRequest> mono = Mono.just(StmtProcessor.create(BookProxiesRequest.class, bookProxiesRequest -> {

            bookProxiesRequest.setEntityInfo(requirementsDto);

            bookProxiesRequest.setEntityAction(StmtProcessor.create(SimpleActionInfo.class,
                    sai -> sai.setActionCode(MODIFY_ENTITY)
            ));
        }));

        ProxyListResponse responseBody = webTestClient
                .post()
                .uri(uriBuilder
                        -> uriBuilder
                        .path(ProxyCoreRestConfig.URI_BOOK)
                        .build())
                .accept(APPLICATION_JSON)
                .body(mono, BookProxiesRequest.class)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(ProxyListResponse.class)
                .returnResult()
                .getResponseBody();

        List<Proxy> bookedProxyList = responseBody.getCreatedEntity().getBookedProxyList();
        String responseBodyMessage = responseBody.getMessage();

        log.info("RESPONSE BODY MESSAGE : {}", responseBodyMessage);
        bookedProxyList.forEach(proxy -> log.info("GOT BOOKED PROXY FROM RESPONSE: {}", proxy));

        return bookedProxyList;
    }


    @Order(200)
    //@Test
    //@RepeatedTest(5)
    public void bookSingleProxyById() {
        final BookProxyByIdDto requirementsDto = StmtProcessor.create(BookProxyByIdDto.class, bookProxyByIdDto -> {
            bookProxyByIdDto.setProxyId(100);
            bookProxyByIdDto.setBookingTimeMillis(10000);
            bookProxyByIdDto.setApplicationName("tiktok");
        });

        final Mono<BookProxyRequest> mono = Mono.just(StmtProcessor.create(BookProxyRequest.class, bookProxyRequest -> {

            bookProxyRequest.setEntityInfo(requirementsDto);

            bookProxyRequest.setEntityAction(StmtProcessor.create(SimpleActionInfo.class,
                    sai -> sai.setActionCode(CREATE_ENTITY)
            ));
        }));

        log.info("START");
        runTest(() -> {

            BookedProxyResponse responseBody = webTestClient
                    .post()
                    .uri(uriBuilder
                            -> uriBuilder
                            .path(ProxyCoreRestConfig.URI_BOOK_BY_PROXY_ID)
                            .build())
                    .accept(APPLICATION_JSON)
                    .body(mono, BookProxyRequest.class)
                    .exchange()
                    .expectStatus()
                    .isOk()
                    .expectBody(BookedProxyResponse.class)
                    .returnResult()
                    .getResponseBody();

            log.info("RESPONSE BODY MESSAGE : {}", responseBody.getMessage());
            log.info("RESPONSE BODY BOOKED PROXY : {}", responseBody.getCreatedEntity().getBookedProxy());
        });
    }
}
