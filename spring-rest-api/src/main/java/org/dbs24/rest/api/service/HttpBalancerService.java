package org.dbs24.rest.api.service;

import lombok.EqualsAndHashCode;
import lombok.extern.log4j.Log4j2;
import org.dbs24.consts.SysConst;
import org.dbs24.spring.core.api.AbstractApplicationService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.client.loadbalancer.reactive.ReactorLoadBalancerExchangeFilterFunction;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.server.ServerRequest;
import reactor.core.publisher.Mono;

import java.util.function.Consumer;

import static java.lang.String.format;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static reactor.core.publisher.Mono.justOrEmpty;

@Log4j2
@Configuration
@EqualsAndHashCode(callSuper = true)
@ConditionalOnProperty(name = "eureka.balancer.enabled", havingValue = "true", matchIfMissing = false)
public class HttpBalancerService extends AbstractApplicationService {

    @Value("${eureka.service-name:not-defined}")
    private String serviceName;

    final WebClient.Builder loadBalancedWebClientBuilder;
    final ReactorLoadBalancerExchangeFilterFunction lbFunction;

    public HttpBalancerService(WebClient.Builder webClientBuilder,
                               ReactorLoadBalancerExchangeFilterFunction lbFunction) {
        this.loadBalancedWebClientBuilder = webClientBuilder;
        this.lbFunction = lbFunction;
    }

    private WebClient createWebClient(String uri) {
        return setupSSL(loadBalancedWebClientBuilder, uri)
                .build();
    }

    public <T, V> Mono<V> getCloudEntity(ServerRequest serverRequest, T entity, Class<V> classV) {

        log.debug("serverRequest.uri: {}", serverRequest.uri().toString());
        final var uri = format("%s", serverRequest.uri().toString().replaceAll(format(":%d", serverRequest.uri().getPort()), SysConst.EMPTY_STRING));

        log.debug("uri: {}; attached entity: [{}], return class: {}", uri, entity, classV);

        final Consumer<HttpHeaders> headers = hs -> hs.addAll(serverRequest.headers().asHttpHeaders());

        return serverRequest.method().compareTo(GET) == 0 ?
                createWebClient(uri)
                        .get()
                        .uri(uri)
                        .headers(headers)
                        .accept(APPLICATION_JSON)
                        .retrieve()
                        .bodyToMono(classV) :
                createWebClient(uri)
                        .post()
                        .uri(uri)
                        .headers(headers)
                        .accept(APPLICATION_JSON)
                        .body(justOrEmpty(entity), entity.getClass())
                        .retrieve()
                        .bodyToMono(classV);
    }
}
