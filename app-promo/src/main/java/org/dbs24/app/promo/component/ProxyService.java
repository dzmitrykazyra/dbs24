package org.dbs24.app.promo.component;

import org.dbs24.app.promo.entity.Bot;
import org.dbs24.app.promo.rest.dto.proxy.refs.BookProxiesDto;
import org.dbs24.app.promo.rest.dto.proxy.refs.Proxy;
import org.dbs24.app.promo.rest.dto.proxy.request.BookProxiesRequest;
import org.dbs24.app.promo.rest.dto.proxy.response.BookedProxyResponse;
import org.dbs24.app.promo.rest.dto.proxy.response.GetApplicationsResponse;
import org.dbs24.app.promo.rest.dto.proxy.response.ProxyListResponse;
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
public class ProxyService {
    private WebClient proxyCoreWebClient;

    @Value("${proxy.core.uri}")
    private String proxyCoreUrl;

    @Value("${proxy.core.api.book}")
    private String apiBookProxy;

    @Value("${proxy.core.api.book-by-id}")
    private String apiBookProxyById;

    @Value("${proxy.core.api.applications}")
    private String apiGetApplications;

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

    public List<Proxy> bookProxies(Integer proxiesAmount) {

        Mono<BookProxiesRequest> proxyRequest = Mono.just(
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
                .body(proxyRequest, BookProxiesRequest.class)
                .retrieve()
                .bodyToMono(ProxyListResponse.class)
                .toProcessor()
                .block()
                .getCreatedEntity()
                .getBookedProxyList();
    }

    public Proxy bookProxyById(Integer proxyId) {

        Mono<BookProxiesRequest> proxyRequest = Mono.just(
                StmtProcessor.create(
                        BookProxiesRequest.class,
                        bookProxiesRequest -> {
                            bookProxiesRequest.setEntityInfo(
                                    StmtProcessor.create(
                                            BookProxiesDto.class,
                                            bookProxiesDto -> {
                                                bookProxiesDto.setProxyId(proxyId);
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
                        .path(apiBookProxyById)
                        .build())
                .contentType(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .body(proxyRequest, BookProxiesRequest.class)
                .retrieve()
                .bodyToMono(BookedProxyResponse.class)
                .toProcessor()
                .block()
                .getCreatedEntity()
                .getBookedProxy();
    }

    public void unbookAllProxies(Proxy proxy) {

        Integer applicationId = retrieveApplicationId();

        proxyCoreWebClient
                .get()
                .uri(uriBuilder -> uriBuilder
                        .path(apiUnbookProxy)
                        .queryParam("applicationId", applicationId)
                        .build())
                .accept(APPLICATION_JSON)
                .retrieve();
    }

    public Proxy retrieveProxyByBot(Bot bot) {
        Proxy proxy;

        if (bot.getProxyId() == null) {
            proxy = bookProxies(5).get(0);
            bot.setProxyId(proxy.getProxyId());
        } else {
            proxy = bookProxyById(bot.getProxyId());
        }

        return proxy;
    }

    private Integer retrieveApplicationId() {

        //todo: remove hardcode params

        return proxyCoreWebClient
                .get()
                .uri(uriBuilder -> uriBuilder
                        .path(apiGetApplications)
                        .queryParam("applicationNetworkName", "Google")
                        .build())
                .accept(APPLICATION_JSON)
                .retrieve()
                .bodyToMono(GetApplicationsResponse.class)
                .toProcessor()
                .block()
                .getCreatedEntity()
                .getApplicationDtoList()
                .stream()
                .filter(app -> app.getName().equals("GoogleTasker"))
                .findFirst()
                .get()
                .getApplicationId();
    }
}