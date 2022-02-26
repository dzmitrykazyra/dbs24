package org.dbs24.refbook;

import lombok.Data;
import lombok.extern.log4j.Log4j2;
import org.dbs24.refbook.config.RefBookRestConfig;
import org.dbs24.refbook.entity.dto.*;
import org.dbs24.stmt.StmtProcessor;
import org.dbs24.test.core.AbstractWebTest;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import java.util.List;

import static org.dbs24.consts.SysConst.ALL_PACKAGES;
import static org.springframework.http.MediaType.APPLICATION_JSON;


@Data
@Log4j2
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = {RefBookApplication.class})
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@EntityScan(basePackages = {ALL_PACKAGES})
public class RefBookApplicationTests extends AbstractWebTest {

    private Integer createdUserId;
    private String createdUserName;

    @Order(100)
    @Test
    @RepeatedTest(1)
    @Transactional(readOnly = true)
    public void getAllCountries() {

        runTest(() -> {

            CountryListDto ua = webTestClient
                    .get()
                    .uri(uriBuilder
                            -> uriBuilder
                            .path(RefBookRestConfig.URI_GET_COUNTRIES)
                            .build())
                    .accept(APPLICATION_JSON)
                    .exchange()
                    .expectStatus()
                    .isOk()
                    .expectBody(CountryListDto.class)
                    .returnResult()
                    .getResponseBody();

            log.info("");
            ua.getCountryDtoList().forEach(
                    countryDto -> log.info("COUNTRY -> {}",countryDto)
            );
        });

    }

    @Order(200)
    @Test
    @RepeatedTest(1)
    @Transactional(readOnly = true)
    public void createCountry() {

        runTest(() -> {

            final Mono<CountryListDto> countryDtoMono = Mono.just(
                    StmtProcessor.create(
                            CountryListDto.class,
                            countryListDto -> countryListDto.setCountryDtoList(List.of(
                                    StmtProcessor.create(
                                    CountryDto.class,
                                    countryDto -> {
                                        countryDto.setCountryId(112);
                                        countryDto.setCountryIso("BY");
                                        countryDto.setCountryName("Belarus");
                                    }),
                                    StmtProcessor.create(
                                            CountryDto.class,
                                            countryDto -> {
                                                countryDto.setCountryId(643);
                                                countryDto.setCountryIso("KZ");
                                                countryDto.setCountryName("Kazakhstan");
                                            })
                                    )))
            );

            CreatedCountryListDto responseBody = webTestClient
                    .post()
                    .uri(uriBuilder
                            -> uriBuilder
                            .path(RefBookRestConfig.URI_CREATE_COUNTRIES)
                            .build())
                    .body(countryDtoMono, CountryListDto.class)
                    .accept(APPLICATION_JSON)
                    .exchange()
                    .expectStatus()
                    .isOk()
                    .expectBody(CreatedCountryListDto.class)
                    .returnResult()
                    .getResponseBody();

            log.info("--> {}", responseBody);
        });
    }

    @Order(300)
    @RepeatedTest(1)
    @Transactional(readOnly = true)
    public void getCountriesByHost() {

        runTest(() -> {

            final Mono<HostListDto> countryDtoMono = Mono.just(
                    StmtProcessor.create(
                            HostListDto.class,
                            hostListDto -> hostListDto.setHostList(List.of(
                                    "178.243.119.151",
                                    "106.210.151.168",
                                    "77.138.5.137",
                                    "207.248.3.251",
                                    "157.47.60.181",
                                    "49.34.93.106",
                                    "45.225.225.195",
                                    "89.15.239.98",
                                    "37.131.123.22",
                                    "178.86.37.104"
                            )))
            );

            HostToIsoMap responseBody = webTestClient
                    .post()
                    .uri(uriBuilder
                            -> uriBuilder
                            .path(RefBookRestConfig.URI_GET_HOST_TO_COUNTRY_MAP)
                            .build())
                    .body(countryDtoMono, HostListDto.class)
                    .accept(APPLICATION_JSON)
                    .exchange()
                    .expectStatus()
                    .isOk()
                    .expectBody(HostToIsoMap.class)
                    .returnResult()
                    .getResponseBody();

            log.info("--> {}", responseBody);
        });
    }
}