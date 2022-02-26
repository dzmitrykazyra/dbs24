package org.dbs24.google.service.impl;

import org.dbs24.google.entity.dto.proxy.BookProxiesDto;
import org.dbs24.google.entity.dto.proxy.BookProxiesRequest;
import org.dbs24.google.entity.dto.proxy.Proxy;
import org.dbs24.google.entity.dto.proxy.ProxyListResponse;
import org.dbs24.google.service.ProxyService;
import org.dbs24.stmt.StmtProcessor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import javax.annotation.PostConstruct;
import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON;

@Service
public class ProxyServiceImpl implements ProxyService {
    private WebClient proxyCoreWebClient;

    @Value("${proxy.core.uri}")
    private String proxyCoreUrl;

    @Value("${proxy.core.api.book}")
    private String apiBookProxy;

    @Value("${proxy.core.api.unbook}")
    private String apiUnbookProxy;

    @Value("${proxy.core.application-name}")
    private String applicationName;

    @Value("${proxy.core.required-use-time}")
    private Integer requiredUseTime;

    @PostConstruct
    public void initWebClient() {

        proxyCoreWebClient = WebClient.builder()
                .baseUrl(proxyCoreUrl)
                .exchangeStrategies(ExchangeStrategies.builder()
                        .codecs(configurer -> configurer
                                .defaultCodecs()
                                .maxInMemorySize(16 * 1024 * 1024))
                        .build())
                .build();
    }

    @Override
    public List<Proxy> bookProxies(Integer proxiesAmount) {

        Mono<BookProxiesRequest> searchBySecUserIdMono = Mono.just(
                StmtProcessor.create(
                        BookProxiesRequest.class,
                        bookProxiesRequest -> {
                            bookProxiesRequest.setEntityInfo(
                                    StmtProcessor.create(
                                            BookProxiesDto.class,
                                            bookProxiesDto -> {
                                                bookProxiesDto.setAmount(proxiesAmount);
                                                bookProxiesDto.setApplicationName(applicationName);
                                                bookProxiesDto.setBookingTimeMillis(requiredUseTime);
                                            }
                                    )
                            );
                        }
                )
        );

        return proxyCoreWebClient
                .post()
                .uri(uriBuilder -> uriBuilder
                        .path(apiBookProxy)
                        .build())
                .contentType(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .body(searchBySecUserIdMono, BookProxiesRequest.class)
                .retrieve()
                .bodyToMono(ProxyListResponse.class)
                .toProcessor()
                .block()
                .getCreatedEntity()
                .getBookedProxyList();
    }

    @Override
    public void unbookProxy(Proxy proxy) {

    }
}