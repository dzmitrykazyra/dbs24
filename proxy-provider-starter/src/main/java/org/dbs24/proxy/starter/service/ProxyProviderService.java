/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.proxy.starter.service;

import java.time.LocalDateTime;
import javax.annotation.PostConstruct;
import lombok.extern.log4j.Log4j2;
import org.dbs24.application.core.locale.NLS;
import static org.dbs24.proxy.core.rest.api.consts.ProxyHttpConsts.RestQueryParams.QP_PROXY_ID;
import org.dbs24.proxy.core.rest.api.request.*;
import org.dbs24.proxy.core.rest.api.usage.*;
import org.dbs24.spring.core.api.AbstractApplicationService;
import org.dbs24.stmt.StmtProcessor;
import org.springframework.beans.factory.annotation.Value;
import static org.dbs24.proxy.core.rest.api.consts.ProxyHttpConsts.UriConsts.*;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingClass;
import org.dbs24.proxy.core.rest.api.proxy.ProxyInfo;
import org.dbs24.proxy.core.rest.api.proxy.ProxyInfoResponse;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import okhttp3.OkHttpClient;
import java.net.InetSocketAddress;
import java.net.PasswordAuthentication;
import java.net.Proxy;
import java.util.concurrent.TimeUnit;
import java.util.ArrayList;
import okhttp3.CookieJar;
import okhttp3.Cookie;
import okhttp3.HttpUrl;

@Log4j2
@Service
@ConditionalOnMissingClass("org.dbs24.proxy.core.component.ProxiesService")
public class ProxyProviderService extends AbstractApplicationService {

    private WebClient webClient;

    @Value("${config.proxy-provider.address:127.0.0.1:8000}")
    private String uriBase;

    //==========================================================================
    public void doTask(Integer countryId, Integer providerId, Integer algId, String applicationCode, TaskRunner taskRunner) {

        // request 4 proxy
        final CreatedProxyRequest createdProxyRequest
                = createProxyRequest(countryId, providerId, algId, applicationCode);

        log.debug("createdProxyRequest = {} ", createdProxyRequest);

        StmtProcessor.ifNotNull(createdProxyRequest.getProxyId(), proxyId -> {

            // proxy details
            final ProxyInfo proxyInfo = getProxy(createdProxyRequest.getProxyId());

            log.debug("receive proxy info = {}", proxyInfo);

            // start usage
            final CreatedUsage startUsage = startProxyUsage(createdProxyRequest.getProxyRequestId(), createdProxyRequest.getProxyId(), proxyInfo.getUrl());

            log.debug("startUsage = {}", startUsage);

            taskRunner.runTask(proxyInfo.getUrl(), proxyInfo.getUrlIpChange(), proxyInfo.getLogin(), proxyInfo.getPass());

            // finish usage
            final CreatedUsage finishUsage = finishProxyUsage(startUsage.getUsageId(), Boolean.TRUE, null);

            log.debug("finishUsage = {}", finishUsage);

        });
    }

    //==========================================================================
    public void doTask(Integer countryId, Integer providerId, Integer algId, String applicationCode, OkHttpClient.Builder builder, TaskProxyRunner taskProxyRunner) {

        // request 4 proxy
        final CreatedProxyRequest createdProxyRequest
                = createProxyRequest(countryId, providerId, algId, applicationCode);

        log.debug("createdProxyRequest = {} ", createdProxyRequest);

        StmtProcessor.ifNotNull(createdProxyRequest.getProxyId(), proxyId -> {

            // proxy details
            final ProxyInfo proxyInfo = getProxy(createdProxyRequest.getProxyId());

            log.debug("receive proxy info = {}", proxyInfo);

            // start usage
            final CreatedUsage startUsage = startProxyUsage(createdProxyRequest.getProxyRequestId(), createdProxyRequest.getProxyId(), proxyInfo.getUrl());

            log.debug("startUsage = {}", startUsage);

            try {

                final Proxy proxy = new Proxy(Proxy.Type.SOCKS, new InetSocketAddress(proxyInfo.getUrl(), proxyInfo.getPort()));

                java.net.Authenticator.setDefault(new java.net.Authenticator() {
                    private PasswordAuthentication authentication = new PasswordAuthentication(proxyInfo.getLogin(), proxyInfo.getPass().toCharArray());

                    @Override
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return authentication;
                    }
                });
//

//                final ArrayList<Cookie> cookies = new ArrayList<>();
//                cookies.add(new Cookie.Builder().name("sessionid").value("").domain("i.instagram.com").build());
//
//                CookieJar.NO_COOKIES.saveFromResponse(HttpUrl.get(cookieUri), cookies);
                final OkHttpClient okHttpClient = builder.proxy(proxy)
                        ./*proxyAuthenticator(proxyAuthenticator).*/addInterceptor(new UnzippingInterceptor())
                        .connectTimeout(120, TimeUnit.SECONDS)
                        .readTimeout(120, TimeUnit.SECONDS)
                        //                        .cookieJar(CookieJar.NO_COOKIES)
                        .build();

                //okHttpClient.cookieJar().saveFromResponse(hu, list);
                // okHttpClient.cookieJar(new JavaNetCookieJar(new CookieManager()));
                //okHttpClient.cookieJar();
                taskProxyRunner.runTask(okHttpClient);

                log.debug("okHttpClient.connectionCount = {}", okHttpClient.connectionPool().connectionCount());

                // finish usage
                final CreatedUsage finishUsage = finishProxyUsage(startUsage.getUsageId(), Boolean.TRUE, null);

                log.debug("finishUsage = {}", finishUsage);

            } catch (Throwable throwable) {

                final CreatedUsage finishUsage = finishProxyUsage(startUsage.getUsageId(), Boolean.FALSE, throwable.getMessage());

                log.error("finishUsage = {}/ throwable = {}", finishUsage, throwable.getMessage());

                throwable.printStackTrace();

            }
        });
    }

    //==========================================================================
    private CreatedProxyRequest createProxyRequest(Integer countryId, Integer providerId, Integer algId, String applicationCode) {

        final Mono<CreateProxyRequestRequest> mono = Mono.just(StmtProcessor.create(CreateProxyRequestRequest.class, createProxyRequestRequest -> {

            createProxyRequestRequest.setEntityInfo(StmtProcessor.create(ProxyRequestInfo.class, proxyRequestInfo -> {

                proxyRequestInfo.setCountryId(countryId);
                proxyRequestInfo.setProxyProviderId(providerId);
                proxyRequestInfo.setAlgId(algId);
                proxyRequestInfo.setRequestDate(NLS.localDateTime2long(LocalDateTime.now()));
                proxyRequestInfo.setApplication(applicationCode);

            }));

            log.debug("registry proxy request: {}", createProxyRequestRequest);

        }));

        final CreatedProxyRequest createdProxyRequest
                = webClient
                        .post()
                        .uri(uriBuilder
                                -> uriBuilder
                                .path(URI_CREATE_OR_UPDATE_PROXY_REQUEST)
                                .build())
                        .contentType(APPLICATION_JSON)
                        .accept(APPLICATION_JSON)
                        .body(mono, CreateProxyRequestRequest.class)
                        .retrieve()
                        .bodyToMono(CreatedProxyRequestResponse.class)
                        .blockOptional()
                        .orElseThrow()
                        .getCreatedEntity();

        log.debug("return available proxy: {}", createdProxyRequest);

        return createdProxyRequest;

    }

    //==========================================================================
    private CreatedUsage startProxyUsage(Integer proxyRequestId, Integer proxyId, String usedIp) {

        final Mono<CreatedUsageRequest> mono = Mono.just(StmtProcessor.create(CreatedUsageRequest.class, createdUsageRequest -> {

            createdUsageRequest.setEntityInfo(StmtProcessor.create(ProxyUsageInfo.class, proxyUsageInfo -> {

                proxyUsageInfo.setProxyRequestId(proxyRequestId);
                proxyUsageInfo.setProxyId(proxyId);
                proxyUsageInfo.setSessionStart(NLS.localDateTime2long(LocalDateTime.now()));
                proxyUsageInfo.setSessionFinish(null);
                proxyUsageInfo.setIsSuccess(null);
                proxyUsageInfo.setError(null);
                proxyUsageInfo.setUsedIp(usedIp);
                proxyUsageInfo.setUsageId(null);

            }));

            log.debug("registry proxy usage: {}", createdUsageRequest);

        }));

        final CreatedUsage createdUsage
                = webClient
                        .post()
                        .uri(uriBuilder
                                -> uriBuilder
                                .path(URI_CREATE_OR_UPDATE_PROXY_USAGE)
                                .build())
                        .contentType(APPLICATION_JSON)
                        .accept(APPLICATION_JSON)
                        .body(mono, CreatedUsageRequest.class)
                        .retrieve()
                        .bodyToMono(CreatedUsageResponse.class)
                        .blockOptional()
                        .orElseThrow()
                        .getCreatedEntity();

        log.debug("registry proxy usage: {}", createdUsage);

        return createdUsage;

    }

    //==========================================================================
    private CreatedUsage finishProxyUsage(Integer usageId, Boolean isSuccess, String errMsg) {

        final Mono<CreatedUsageRequest> mono = Mono.just(StmtProcessor.create(CreatedUsageRequest.class, createdUsageRequest -> {

            createdUsageRequest.setEntityInfo(StmtProcessor.create(ProxyUsageInfo.class, proxyUsageInfo -> {

                proxyUsageInfo.setUsageId(usageId);
                proxyUsageInfo.setProxyRequestId(null);
                proxyUsageInfo.setProxyId(null);
                proxyUsageInfo.setSessionStart(null);
                proxyUsageInfo.setSessionFinish(NLS.localDateTime2long(LocalDateTime.now()));
                proxyUsageInfo.setIsSuccess(isSuccess);
                proxyUsageInfo.setError(errMsg);
                proxyUsageInfo.setUsedIp(null);

            }));

            log.debug("registry finish proxy usage : {}", createdUsageRequest);

        }));

        final CreatedUsage createdUsage
                = webClient
                        .post()
                        .uri(uriBuilder
                                -> uriBuilder
                                .path(URI_CREATE_OR_UPDATE_PROXY_USAGE)
                                .build())
                        .contentType(APPLICATION_JSON)
                        .accept(APPLICATION_JSON)
                        .body(mono, CreatedUsageRequest.class)
                        .retrieve()
                        .bodyToMono(CreatedUsageResponse.class)
                        .blockOptional()
                        .orElseThrow()
                        .getCreatedEntity();

        log.debug("registry finish proxy usage : {}", createdUsage);

        return createdUsage;

    }

    //==========================================================================
    private ProxyInfo getProxy(Integer proxyId) {

        final ProxyInfo proxyInfo = webClient
                .get()
                .uri(uriBuilder
                        -> uriBuilder
                        .path(URI_GET_PROXY)
                        .queryParam(QP_PROXY_ID, proxyId)
                        .build())
                .accept(APPLICATION_JSON)
                .retrieve()
                .bodyToMono(ProxyInfoResponse.class)
                .blockOptional()
                .orElseThrow()
                .getCreatedEntity();

        log.debug("return proxy info : {}", proxyId);

        return proxyInfo;
    }

    //==========================================================================
    @PostConstruct

    public void initializeService() {

        StmtProcessor.assertNotNull(String.class, uriBase, "parameter ${config.proxy-provider.address}'");
        webClient = WebClient.builder()
                .baseUrl(uriBase)
                .exchangeStrategies(ExchangeStrategies.builder()
                        .codecs(configurer -> configurer
                        .defaultCodecs()
                        .maxInMemorySize(16 * 1024 * 1024))
                        .build())
                .build();
        log.debug("webClient: succefull created ({})", uriBase);

    }
}
